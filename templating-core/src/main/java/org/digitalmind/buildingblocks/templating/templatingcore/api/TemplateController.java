package org.digitalmind.buildingblocks.templating.templatingcore.api;


import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateSearchOperator;
import org.digitalmind.buildingblocks.templating.templatingcore.entity.Template;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

import static org.digitalmind.buildingblocks.templating.templatingcore.config.TemplateModuleConfig.API_ENABLED;
import static org.digitalmind.buildingblocks.templating.templatingcore.config.TemplateModuleConfig.PREFIX;


@Slf4j
@RestController
@ConditionalOnProperty(name = API_ENABLED, havingValue = "true")
@RequestMapping("${" + PREFIX + ".api.docket.base-path}/template")
@Api(value = "Template", description = "This resource is exposing the services for template support", tags = {"Template"})
public class TemplateController {
    private final TemplateService templateService;
    private final TemplateDBService templateDBService;

    @Autowired
    public TemplateController(
            TemplateService templateService,
            TemplateDBService templateDBService
    ) {
        this.templateService = templateService;
        this.templateDBService = templateDBService;
    }

    //GET SUPPORTED ENGINES
    @ApiOperation(
            value = "Retrieve engine list",
            notes = "This API is used for retrieving engine list.",
            response = String.class,
            responseContainer = "Set"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/engine", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<String>> getSupportedEngines() {
        Set<String> supportedEngines = templateService.getSupportedEngines();
        return ResponseEntity.ok(supportedEngines);
    }

    //LIST SUPPORTED CONTENT TYPES
    @ApiOperation(
            value = "Retrieve supported content type list",
            notes = "This API is used for retrieving supported content type for a template engine.",
            response = String.class,
            responseContainer = "Set"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/engine/{engine-identifier}/content-type", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<String>> getContentTypes(
            @ApiParam(name = "engine-identifier", value = "The engine identifier", required = true, allowMultiple = false) @PathVariable(name = "engine-identifier", required = true) String engineIdentifier
    ) {
        Set<String> modes = templateService.getContentTypes(engineIdentifier);
        return ResponseEntity.ok(modes);
    }

    //LIST SUPPORTED RESULT MIME TYPE FOR A CONTENT TYPE
    @ApiOperation(
            value = "Retrieve supported result mime type list for a specified content type",
            notes = "This API is used for retrieving supported tesult mime type list for a template engine content type.",
            response = String.class,
            responseContainer = "Set"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/engine/{engine-identifier}/content-type/{content-type-identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<String>> getModes(
            @ApiParam(name = "engine-identifier", value = "The engine identifier", required = true, allowMultiple = false) @PathVariable(name = "engine-identifier", required = true) String engineIdentifier,
            @ApiParam(name = "content-type-identifier", value = "The content type identifier", required = true, allowMultiple = false) @PathVariable(name = "content-type-identifier", required = true) String contentTypeIdentifier
    ) {
        Set<String> modes = templateService.getResultMimeTypes(engineIdentifier, contentTypeIdentifier);
        return ResponseEntity.ok(modes);
    }

    //CREATE TEMPLATE
    @ApiOperation(
            value = "Create template",
            notes = "This API is used for creating a new template entry.",
            response = Template.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Error encountered when processing request")
    })
    @PostMapping(path = "/", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Template> createTemplate(
            @ApiParam(name = "template", value = "The template", required = true, allowMultiple = false) @Valid @RequestBody Template template) {

        Template result = templateDBService.save(template);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    //GET TEMPLATE
    @ApiOperation(
            value = "Retrieve template",
            notes = "This API is used for retrieving template.",
            response = Template.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Template> retrieveTemplate(
            @ApiParam(name = "identifier", value = "The identifier used to identify a template.", required = true) @PathVariable(value = "identifier", required = true) String identifier
    ) {
        TemplateIdentifier templateIdentifier = TemplateIdentifier.builder().identifier(identifier).build();
        Template result = templateDBService.getTemplate(templateIdentifier);
        return ResponseEntity.ok(result);
    }

    //LIST TEMPLATE
    @ApiOperation(
            value = "Retrieve template list",
            notes = "This API is used for retrieving template lists.",
            response = Page.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })
    @GetMapping(path = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Template>> listTemplate(
            @ApiParam(name = "engine", value = "The engine", required = true, allowMultiple = true) @Valid @RequestParam(name = "engine", required = true) List<String> engineList,
            @ApiParam(name = "namespace", value = "The namespace", required = false, allowMultiple = false) @Valid @RequestParam(name = "namespace", required = false) String namespace,
            @ApiParam(name = "name", value = "The name", required = false, allowMultiple = false) @Valid @RequestParam(name = "name", required = false) String name,
            @ApiParam(name = "operator", value = "The operator", required = false, allowMultiple = false, allowableValues = "EQUALS,CONTAINS,START_WITH") @Valid @RequestParam(name = "operator", required = false) TemplateSearchOperator operator,
            @ApiParam(name = "pageable", value = "Pageable parameters.", required = false) Pageable pageable
    ) {
        Page<Template> result = templateDBService.findBy(engineList, namespace, name, operator, pageable);
        return ResponseEntity.ok(result);
    }

    //UPDATE BY ID
    @ApiOperation(
            value = "Update template info",
            notes = "This API is used for updating a template.",
            response = Void.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 404, message = "Process does not exists"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })

    @PutMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateTemplate(
            @ApiParam(name = "identifier", value = "The identifier used to identify a template.", required = true) @PathVariable(value = "identifier", required = true) Long identifier,
            @ApiParam(name = "template", value = "The template details", required = true) @Valid @RequestBody Template template
    ) {
        Template result = templateDBService.getById(identifier);
        result.setEngine(template.getEngine());
        result.setNamespace(template.getNamespace());
        result.setName(template.getName());
        result.setContent(template.getContent());
        result.setDescription(template.getDescription());
        result.setContentType(template.getContentType());
        result.setResultMimeTypes(template.getResultMimeTypes());
        templateDBService.save(result);
        return ResponseEntity.ok().build();
    }

    //DELETE BY ID
    @ApiOperation(
            value = "Delete template info",
            notes = "This API is used for deleting a template.",
            response = Void.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request executed with success"),
            @ApiResponse(code = 401, message = "Request not authorized"),
            @ApiResponse(code = 404, message = "Process does not exists"),
            @ApiResponse(code = 500, message = "Error encountered when executing request")
    })

    @DeleteMapping(path = "/{identifier}")
    public ResponseEntity<Void> deleteTemplate(
            @ApiParam(name = "identifier", value = "The identifier used to identify a template.", required = true) @PathVariable(value = "identifier", required = true) Long identifier
    ) {
        Template template = templateDBService.getById(identifier);
        templateDBService.delete(template);
        return ResponseEntity.ok().build();
    }

}

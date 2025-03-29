package org.digitalmind.buildingblocks.templating.core.template.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateSearchOperator;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static org.digitalmind.buildingblocks.templating.core.template.config.TemplateModuleConfig.API_ENABLED;
import static org.digitalmind.buildingblocks.templating.core.template.config.TemplateModuleConfig.PREFIX;


@Slf4j
@RestController
@ConditionalOnProperty(name = API_ENABLED, havingValue = "true")
@RequestMapping("${" + PREFIX + ".api.docket.base-path}/template")
@Tag(name = "Template", description = "This resource is exposing the services for template support")
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
    @Operation(
            summary = "Retrieve engine list",
            description = "This API is used for retrieving engine list."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/engine", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<String>> getSupportedEngines() {
        Set<String> supportedEngines = templateService.getSupportedEngines();
        return ResponseEntity.ok(supportedEngines);
    }

    //LIST SUPPORTED CONTENT TYPES
    @Operation(
            summary = "Retrieve supported content type list",
            description = "This API is used for retrieving supported content type for a template engine."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/engine/{engine-identifier}/content-type", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<String>> getContentTypes(
            @Parameter(name = "engine-identifier", description = "The engine identifier", required = true) @PathVariable(name = "engine-identifier", required = true) String engineIdentifier
    ) {
        Set<String> modes = templateService.getContentTypes(engineIdentifier);
        return ResponseEntity.ok(modes);
    }

    //LIST SUPPORTED RESULT MIME TYPE FOR A CONTENT TYPE
    @Operation(
            summary = "Retrieve supported result mime type list for a specified content type",
            description = "This API is used for retrieving supported tesult mime type list for a template engine content type."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/engine/{engine-identifier}/content-type/{content-type-identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<String>> getModes(
            @Parameter(name = "engine-identifier", description = "The engine identifier", required = true) @PathVariable(name = "engine-identifier", required = true) String engineIdentifier,
            @Parameter(name = "content-type-identifier", description = "The content type identifier", required = true) @PathVariable(name = "content-type-identifier", required = true) String contentTypeIdentifier
    ) {
        Set<String> modes = templateService.getResultMimeTypes(engineIdentifier, contentTypeIdentifier);
        return ResponseEntity.ok(modes);
    }

    //CREATE TEMPLATE
    @Operation(
            summary = "Create template",
            description = "This API is used for creating a new template entry."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when processing request")
    })
    @PostMapping(path = "/", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Template> createTemplate(
            @Parameter(name = "template", description = "The template", required = true) @Valid @RequestBody Template template) {

        Template result = templateDBService.save(template);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    //GET TEMPLATE
    @Operation(
            summary = "Retrieve template",
            description = "This API is used for retrieving template."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Template> retrieveTemplate(
            @Parameter(name = "identifier", description = "The identifier used to identify a template.", required = true) @PathVariable(value = "identifier", required = true) String identifier
    ) {
        TemplateIdentifier templateIdentifier = TemplateIdentifier.builder().identifier(identifier).build();
        Template result = templateDBService.getTemplate(templateIdentifier);
        return ResponseEntity.ok(result);
    }

    //LIST TEMPLATE
    @Operation(
            summary = "Retrieve template list",
            description = "This API is used for retrieving template lists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })
    @GetMapping(path = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Template>> listTemplate(
            @Parameter(name = "engine", description = "The engine", required = true) @Valid @RequestParam(name = "engine", required = true) List<String> engineList,
            @Parameter(name = "namespace", description = "The namespace", required = false) @Valid @RequestParam(name = "namespace", required = false) String namespace,
            @Parameter(name = "name", description = "The name", required = false) @Valid @RequestParam(name = "name", required = false) String name,
            @Parameter(name = "operator", description = "The operator", required = false) @Valid @RequestParam(name = "operator", required = false) TemplateSearchOperator operator,
            @Parameter(name = "pageable", description = "Pageable parameters.", required = false) Pageable pageable
    ) {
        Page<Template> result = templateDBService.findBy(engineList, namespace, name, operator, pageable);
        return ResponseEntity.ok(result);
    }

    //UPDATE BY ID
    @Operation(
            summary = "Update template info",
            description = "This API is used for updating a template."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "404", description = "Process does not exists"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })

    @PutMapping(path = "/{identifier}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateTemplate(
            @Parameter(name = "identifier", description = "The identifier used to identify a template.", required = true) @PathVariable(value = "identifier", required = true) Long identifier,
            @Parameter(name = "template", description = "The template details", required = true) @Valid @RequestBody Template template
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
    @Operation(
            summary = "Delete template info",
            description = "This API is used for deleting a template."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request executed with success"),
            @ApiResponse(responseCode = "401", description = "Request not authorized"),
            @ApiResponse(responseCode = "404", description = "Process does not exists"),
            @ApiResponse(responseCode = "500", description = "Error encountered when executing request")
    })

    @DeleteMapping(path = "/{identifier}")
    public ResponseEntity<Void> deleteTemplate(
            @Parameter(name = "identifier", description = "The identifier used to identify a template.", required = true) @PathVariable(value = "identifier", required = true) Long identifier
    ) {
        Template template = templateDBService.getById(identifier);
        templateDBService.delete(template);
        return ResponseEntity.ok().build();
    }

}

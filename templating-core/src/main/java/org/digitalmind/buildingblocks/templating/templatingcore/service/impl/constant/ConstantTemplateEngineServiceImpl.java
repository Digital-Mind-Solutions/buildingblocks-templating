package org.digitalmind.buildingblocks.templating.templatingcore.service.impl.constant;


import lombok.Getter;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateEngineProperties;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateResult;
import org.digitalmind.buildingblocks.templating.templatingcore.entity.Template;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateEngineService;
import org.digitalmind.buildingblocks.templating.templatingcore.util.TemplateNamespaceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.activation.MimeType;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ConstantTemplateEngineServiceImpl implements TemplateEngineService {
    private final TemplateDBService templateDBService;
    private final String engine;
    public static final Map<String, String> TEMPLATE_CONTENT_TYPE_MAP = new HashMap();
    public static final MultiValueMap<String, String> TEMPLATE_MIME_TYPE_MAP = new LinkedMultiValueMap();
    private final ApplicationContext applicationContext;

    static {
        TEMPLATE_CONTENT_TYPE_MAP.put("RAW".toUpperCase(), "application/octet-stream");

        for (Map.Entry<String, String> e : TEMPLATE_CONTENT_TYPE_MAP.entrySet()) {
            TEMPLATE_MIME_TYPE_MAP.put(e.getKey(), Collections.singletonList(e.getValue()));
        }
    }


    public ConstantTemplateEngineServiceImpl(
            TemplateEngineProperties templateEngineProperties,
            TemplateDBService templateDBService,
            String engine,
            MessageSource messageSource,
            ApplicationContext applicationContext
    ) {
        this.templateDBService = templateDBService;
        this.engine = engine;
        this.applicationContext = applicationContext;
    }

    @Override
    public Set<String> getContentTypes() {
        return TEMPLATE_CONTENT_TYPE_MAP.keySet();
    }

    @Override
    public Set<String> getResultMimeTypes(String contentType) {
        return TEMPLATE_MIME_TYPE_MAP.get(contentType.toUpperCase()).stream().collect(Collectors.toSet());
    }

    @Override
    public TemplateResult execute(TemplateIdentifier identifier, Map<String, Object> variables, Locale locale, MimeType mimeType) {
        Template template = this.templateDBService.getTemplate(identifier);
        return execute(template, variables, locale, mimeType);
    }

    @Override
    public TemplateResult execute(Template template, Map<String, Object> variables, Locale locale, MimeType mimeType) {
        validate(template, variables, locale, mimeType);

        String fullName = TemplateNamespaceUtil.getAbsolute(template.getNamespace(), template.getName());
        String content = template.getContent();
        Resource resource = new ByteArrayResource(content.getBytes(), fullName);
        TemplateResult result = TemplateResult.builder().mimeType(mimeType).resource(resource).build();
        return result;
    }
}

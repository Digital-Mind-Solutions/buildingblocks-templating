package org.digitalmind.buildingblocks.templating.templatingcore.service.impl.thymeleaf.mapper;

import org.digitalmind.buildingblocks.templating.templatingcore.exception.TemplateExecutionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ThymeleafTemplateModeMapper {

    public static final Map<String, String> TEMPLATE_CONTENT_TYPE_MAP = new HashMap();
    public static final MultiValueMap<String, String> TEMPLATE_MIME_TYPE_MAP = new LinkedMultiValueMap();

    static {
        TEMPLATE_CONTENT_TYPE_MAP.put("HTML".toUpperCase(), "text/html");
        TEMPLATE_CONTENT_TYPE_MAP.put("XML".toUpperCase(), "application/xml");
        TEMPLATE_CONTENT_TYPE_MAP.put("TEXT".toUpperCase(), "text/plain");
        TEMPLATE_CONTENT_TYPE_MAP.put("JAVASCRIPT".toUpperCase(), "text/javascript");
        TEMPLATE_CONTENT_TYPE_MAP.put("CSS".toUpperCase(), "text/css");
        TEMPLATE_CONTENT_TYPE_MAP.put("RAW".toUpperCase(), "application/octet-stream");

        for (Map.Entry<String, String> e : TEMPLATE_CONTENT_TYPE_MAP.entrySet()) {
            TEMPLATE_MIME_TYPE_MAP.put(e.getKey(), Collections.singletonList(e.getValue()));
        }
    }

    public static Set<String> getTemplateContentTypes() {
        return TEMPLATE_CONTENT_TYPE_MAP.keySet();
    }

    public static Set<String> getResultMimeTypes(String contentType) {
        return TEMPLATE_MIME_TYPE_MAP.get(contentType.toUpperCase()).stream().collect(Collectors.toSet());
    }

    public static String toMimeType(String contentType) {
        String mimeType = TEMPLATE_CONTENT_TYPE_MAP.get(contentType.toUpperCase());
        if (mimeType == null) {
            throw new TemplateExecutionException("Unable to map template mode <" + contentType + "> to a MimeType");
        }
        return mimeType;
    }

}

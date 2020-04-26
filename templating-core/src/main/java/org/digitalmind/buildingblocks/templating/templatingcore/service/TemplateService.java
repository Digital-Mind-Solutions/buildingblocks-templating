package org.digitalmind.buildingblocks.templating.templatingcore.service;


import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateResult;

import javax.activation.MimeType;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface TemplateService {

    public TemplateResult execute(TemplateIdentifier identifier, Map<String, Object> variables, Locale locale, MimeType mimeType);

    public Set<String> getSupportedEngines();

    public Set<String> getContentTypes(String engine);

    public Set<String> getResultMimeTypes(String engine, String contentType);

}

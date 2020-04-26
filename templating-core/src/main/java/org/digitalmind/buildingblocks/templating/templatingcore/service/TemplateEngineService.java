package org.digitalmind.buildingblocks.templating.templatingcore.service;

import lombok.SneakyThrows;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateResult;
import org.digitalmind.buildingblocks.templating.templatingcore.entity.Template;
import org.digitalmind.buildingblocks.templating.templatingcore.exception.TemplateExecutionException;

import javax.activation.MimeType;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface TemplateEngineService {

    String getEngine();

    Set<String> getContentTypes();

    Set<String> getResultMimeTypes(String contentType);

    TemplateResult execute(TemplateIdentifier identifier, Map<String, Object> variables, Locale locale, MimeType mimeType);

    TemplateResult execute(Template template, Map<String, Object> variables, Locale locale, MimeType mimeType);

    @SneakyThrows
    default void validate(Template template, Map<String, Object> variables, Locale locale, MimeType mimeType) {
        if (mimeType == null) {
            if (getResultMimeTypes(template.getContentType()).size() != 1) {
                throw new TemplateExecutionException("The engine " + this.getEngine() + " require explicit mimetype for content type " + template.getContentType());
            }
            mimeType = new MimeType(getResultMimeTypes(template.getContentType()).iterator().next());
        } else {
            if (!getResultMimeTypes(template.getContentType()).contains(mimeType.getBaseType())) {
                throw new TemplateExecutionException("The engine " + this.getEngine() + " does not support mimetype " + mimeType.getBaseType() + " for content type " + template.getContentType());
            }
        }
    }

}

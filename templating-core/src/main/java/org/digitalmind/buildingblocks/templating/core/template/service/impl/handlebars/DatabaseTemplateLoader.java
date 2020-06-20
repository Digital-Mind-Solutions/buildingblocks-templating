package org.digitalmind.buildingblocks.templating.core.template.service.impl.handlebars;

import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.digitalmind.buildingblocks.templating.core.template.exception.TemplateExecutionException;
import org.digitalmind.buildingblocks.templating.core.template.util.TemplateNamespaceUtil;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class DatabaseTemplateLoader implements TemplateLoader {

    @Getter
    @Setter
    private String prefix;

    @Getter
    @Setter
    private String suffix;

    @Getter
    @Setter
    private Charset charset;

    private HandlebarsTemplateEngineServiceImpl handlebarsTemplateEngineService;

    public DatabaseTemplateLoader(String prefix, String suffix, HandlebarsTemplateEngineServiceImpl handlebarsTemplateEngineService) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.charset = null;
        this.handlebarsTemplateEngineService = handlebarsTemplateEngineService;
    }

    @Override
    public TemplateSource sourceAt(String location) throws IOException {
        DatabaseTemplateSource databaseTemplateSource = null;
        TemplateIdentifier templateIdentifier;
        try {
            Long id = Long.parseLong(location);
            templateIdentifier = new TemplateIdentifier(id);
        } catch (Exception e) {
            String namespace = TemplateNamespaceUtil.getNamespace(location, false);
            String name = TemplateNamespaceUtil.getName(location);
            templateIdentifier = new TemplateIdentifier(namespace, name);
        }
        Template template = handlebarsTemplateEngineService.getTemplateDBService().getTemplate(templateIdentifier);
        if (template != null) {
            if (!handlebarsTemplateEngineService.getEngine().equalsIgnoreCase(template.getEngine())) {
                throw new TemplateExecutionException("The template engine found for template <" + template.toString() + "> is " + template.getEngine() + " and must be " + handlebarsTemplateEngineService.getEngine());
            }
            databaseTemplateSource = new DatabaseTemplateSource(template.getContent(), TemplateNamespaceUtil.getAbsolute(template.getNamespace(), template.getName()), template.getUpdatedAt() != null ? template.getUpdatedAt().getTime() : 0);
        }
        return databaseTemplateSource;
    }

    @Override
    public String resolve(String name) {
        return prefix + name + suffix;
    }

}

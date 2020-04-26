package org.digitalmind.buildingblocks.templating.core.template.service.impl.thymeleaf.resolver;

import lombok.Getter;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.digitalmind.buildingblocks.templating.core.template.exception.TemplateExecutionException;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.core.template.util.TemplateNamespaceUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

@Getter
public class ThymeleafDBResourceResolver extends StringTemplateResolver implements ApplicationContextAware {
    // SpringResourceTemplateResolver
    private final TemplateDBService templateDBService;
    private final String engine;
    private ApplicationContext applicationContext;

    public ThymeleafDBResourceResolver(TemplateDBService templateDBService, String engine, ApplicationContext applicationContext) {
        this.setTemplateMode(TemplateMode.HTML);
        this.templateDBService = templateDBService;
        this.engine = engine;
        this.applicationContext = applicationContext;
    }

    private Template getTemplate(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        String namespace = TemplateNamespaceUtil.getNamespace(template, false);
        String name = TemplateNamespaceUtil.getName(template);
        Template thymeleafTemplate = templateDBService.getByNamespaceAndName(namespace, name);
        if (!thymeleafTemplate.getEngine().equalsIgnoreCase(this.engine)) {
            throw new TemplateExecutionException("The template engine found for template namespace <" + namespace + "> and name <" + name + "> is " + thymeleafTemplate.getEngine() + " and must be " + this.engine);
        }
        return thymeleafTemplate;
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        Template thymeleafTemplate = getTemplate(configuration, ownerTemplate, template, templateResolutionAttributes);
        if (thymeleafTemplate != null) {
            return super.computeTemplateResource(configuration, ownerTemplate, thymeleafTemplate.getContent(), templateResolutionAttributes);
        }
        return null;
    }

    @Override
    protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        Template thymeleafTemplate = getTemplate(configuration, ownerTemplate, template, templateResolutionAttributes);
        if (thymeleafTemplate != null) {
            return TemplateMode.parse(thymeleafTemplate.getContentType());
        }
        return super.computeTemplateMode(configuration, ownerTemplate, template, templateResolutionAttributes);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

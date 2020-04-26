package org.digitalmind.buildingblocks.templating.templatingcore.service.impl.thymeleaf;


import lombok.Getter;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateEngineProperties;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateResult;
import org.digitalmind.buildingblocks.templating.templatingcore.entity.Template;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateEngineService;
import org.digitalmind.buildingblocks.templating.templatingcore.service.impl.thymeleaf.mapper.ThymeleafTemplateModeMapper;
import org.digitalmind.buildingblocks.templating.templatingcore.service.impl.thymeleaf.resolver.ThymeleafDBResourceResolver;
import org.digitalmind.buildingblocks.templating.templatingcore.util.TemplateNamespaceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;

import javax.activation.MimeType;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Getter
public class ThymeleafTemplateEngineServiceImpl implements TemplateEngineService {

    private final TemplateDBService templateDBService;
    private final TemplateEngineProperties templateEngineProperties;
    private final String engine;
    private final SpringTemplateEngine templateEngine;
    private final ApplicationContext applicationContext;
    private final ThymeleafEvaluationContext thymeleafEvaluationContext;

    public ThymeleafTemplateEngineServiceImpl(
            TemplateEngineProperties templateEngineProperties,
            TemplateDBService templateDBService,
            String engine,
            MessageSource messageSource,
            ApplicationContext applicationContext
    ) {
        this.templateDBService = templateDBService;
        this.templateEngineProperties = templateEngineProperties;
        this.engine = engine;
        this.applicationContext = applicationContext;
        this.templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(new ThymeleafDBResourceResolver(templateDBService, this.engine, this.applicationContext));
        //templateEngine.setTemplateEngineMessageSource(messageSource);
        //templateEngine.addDialect(new SpringStandardDialect());
        templateEngine.setEnableSpringELCompiler(true);

        this.thymeleafEvaluationContext = new ThymeleafEvaluationContext(this.applicationContext, null);
    }

    @Override
    public Set<String> getContentTypes() {
        return ThymeleafTemplateModeMapper.getTemplateContentTypes();
    }

    @Override
    public Set<String> getResultMimeTypes(String contentType) {
        return ThymeleafTemplateModeMapper.getResultMimeTypes(contentType);
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
        Context context = new Context(locale, variables);
        // Set the Thymeleaf evaluation context to allow access to Spring beans with @beanName in SpEL expressions
        context.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, thymeleafEvaluationContext);
        String content = templateEngine.process(fullName, context);
        Resource resource = new ByteArrayResource(content.getBytes(), fullName);
        TemplateResult result = TemplateResult.builder().mimeType(mimeType).resource(resource).build();
        return result;
    }

}

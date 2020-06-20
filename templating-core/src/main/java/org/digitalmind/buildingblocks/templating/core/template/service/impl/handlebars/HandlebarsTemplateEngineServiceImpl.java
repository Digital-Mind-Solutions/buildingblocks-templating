package org.digitalmind.buildingblocks.templating.core.template.service.impl.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateEngineProperties;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateResult;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateEngineService;
import org.digitalmind.buildingblocks.templating.core.template.service.impl.handlebars.mapper.HandlebarsTemplateModeMapper;
import org.digitalmind.buildingblocks.templating.core.template.util.TemplateNamespaceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.activation.MimeType;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
public class HandlebarsTemplateEngineServiceImpl implements TemplateEngineService {

    private final TemplateDBService templateDBService;
    private final TemplateEngineProperties templateEngineProperties;
    private final String engine;
    private final ApplicationContext applicationContext;

    private final Handlebars handlebars;
    private final TemplateLoader templateLoader;

    public HandlebarsTemplateEngineServiceImpl(
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

        final Cache<TemplateSource, com.github.jknack.handlebars.Template> templateCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000).build();
        this.templateLoader = new DatabaseTemplateLoader("", "", this);
        this.handlebars = new Handlebars(templateLoader).with((new GuavaTemplateCache(templateCache)));

        //load handlebars helpers
        //class
        Map<String, Object> properties = this.templateEngineProperties.getProperties();
        List<String> helpers = null;
        if (properties.get("helper-class") != null && properties.get("helper-class") instanceof List) {
            helpers = (List<String>) properties.get("helper-class");
        }
        if (helpers != null) {
            for (int i = 0; i < helpers.size(); i++) {
                String className = String.valueOf(helpers.get(i));
                Class clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    log.error("Unable to load class {}", className, e);
                }
                try {
                    Method method = clazz.getMethod("register", Handlebars.class);
                    method.invoke(null, handlebars);
                } catch (NoSuchMethodException ex) {
                    //nothing to register
                } catch (IllegalAccessException e) {
                    log.error("IllegalAccessException on registering helper {}", className, e);
                } catch (InvocationTargetException e) {
                    log.error("InvocationTargetException on registering helper {}", className, e);
                }

                try {
                    Method method = clazz.getMethod("setApplicationContext", ApplicationContext.class);
                    method.invoke(null, this.applicationContext);
                } catch (NoSuchMethodException ex) {
                    //nothing to register
                } catch (IllegalAccessException e) {
                    log.error("IllegalAccessException on registering helper {}", className, e);
                } catch (InvocationTargetException e) {
                    log.error("InvocationTargetException on registering helper {}", className, e);
                }

                handlebars.registerHelpers(clazz);
            }

        }

    }

    @Override
    public Set<String> getContentTypes() {
        return HandlebarsTemplateModeMapper.getTemplateContentTypes();
    }

    @Override
    public Set<String> getResultMimeTypes(String contentType) {
        return HandlebarsTemplateModeMapper.getResultMimeTypes(contentType);
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
        com.github.jknack.handlebars.Template hTemplate = null;
        String result = null;
        try {
            hTemplate = handlebars.compile(fullName);
            com.github.jknack.handlebars.Context context = com.github.jknack.handlebars.Context
                    .newBuilder(variables)
                    .resolver(JsonNodeValueResolver.INSTANCE,
                            JavaBeanValueResolver.INSTANCE,
                            FieldValueResolver.INSTANCE,
                            MapValueResolver.INSTANCE,
                            MethodValueResolver.INSTANCE)
                    .build();
            result = hTemplate.apply(context);
        } catch (IOException e) {
            e.printStackTrace();
        };
        Resource resource = new ByteArrayResource(result.getBytes(), fullName);
        TemplateResult templateResult = TemplateResult.builder().mimeType(mimeType).resource(resource).build();
        return templateResult;
    }

}

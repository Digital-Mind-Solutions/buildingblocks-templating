package org.digitalmind.buildingblocks.templating.core.template.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.templating.core.template.config.TemplateConfig;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.core.template.exception.TemplateEngineInitializeException;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateService;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateEngineProperties;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateResult;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.digitalmind.buildingblocks.templating.core.template.exception.TemplateEngineNotInitializedException;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateDBService;
import org.digitalmind.buildingblocks.templating.core.template.service.TemplateEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.activation.MimeType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.digitalmind.buildingblocks.templating.core.template.config.TemplateModuleConfig.ENABLED;

@Service
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    private final TemplateDBService templateDBService;

    private final TemplateConfig config;

    private final Map<String, TemplateEngineService> templateEngineServiceMap;

    private final MessageSource messageSource;

    private final ApplicationContext applicationContext;

    @Autowired
    public TemplateServiceImpl(
            TemplateDBService templateDBService,
            TemplateConfig config,
            MessageSource messageSource,
            ApplicationContext applicationContext
    ) {
        this.templateDBService = templateDBService;
        this.config = config;
        this.messageSource = messageSource;
        this.applicationContext = applicationContext;
        this.templateEngineServiceMap = initServiceMap();
    }

    private Map<String, TemplateEngineService> initServiceMap() {
        Map<String, TemplateEngineService> templateEngineServiceMap = new HashMap<>();
        log.info("Initializing template engine services");
        log.info("-----------------------------");
        for (Map.Entry<String, TemplateEngineProperties> entry : this.config.getEngines().entrySet()) {
            String engine = entry.getKey();
            TemplateEngineProperties templateEngineProperties = entry.getValue();
            if (templateEngineProperties.isEnabled()) {
                log.info("Initializing template engine service " + engine + " using class " + templateEngineProperties.getClassName());
                Class<? extends TemplateEngineService> templateEngineServiceClass = null;
                try {
                    templateEngineServiceClass = (Class<TemplateEngineService>) Class.forName(templateEngineProperties.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new TemplateEngineInitializeException("Could not find template engine class " + templateEngineProperties.getClassName());
                }
                Constructor constructor = null;
                try {
                    constructor = templateEngineServiceClass.getConstructor(
                            new Class[]{
                                    TemplateEngineProperties.class,
                                    TemplateDBService.class,
                                    String.class,
                                    MessageSource.class,
                                    ApplicationContext.class
                            }
                    );
                } catch (NoSuchMethodException e) {
                    throw new TemplateEngineInitializeException(e.getMessage(), e);
                }
                TemplateEngineService templateEngineService = null;
                try {
                    templateEngineService = (TemplateEngineService)
                            constructor.newInstance(
                                    new Object[]{
                                            templateEngineProperties,
                                            templateDBService,
                                            engine,
                                            messageSource,
                                            applicationContext
                                    }
                            );
                } catch (InstantiationException e) {
                    throw new TemplateEngineInitializeException(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new TemplateEngineInitializeException(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    throw new TemplateEngineInitializeException(e.getMessage(), e);
                }
                templateEngineServiceMap.put(engine, templateEngineService);
                log.info("Initialized template engine service " + engine + " class " + templateEngineProperties.getClassName());
            }
        }
        log.info("-----------------------------");
        return templateEngineServiceMap;
    }

    @Override
    public TemplateResult execute(TemplateIdentifier identifier, Map<String, Object> variables, Locale locale, MimeType mimeType) {
        Template template = templateDBService.getTemplate(identifier);
        TemplateEngineService templateEngineService = getTemplateEngineService(template.getEngine());
        return templateEngineService.execute(template, variables, locale, mimeType);
    }

    @Override
    public Set<String> getSupportedEngines() {
        return config.getEngines().keySet();
    }

    @Override
    public Set<String> getContentTypes(String engine) {
        return getTemplateEngineService(engine).getContentTypes();
    }

    @Override
    public Set<String> getResultMimeTypes(String engine, String contentType) {
        return getTemplateEngineService(engine).getResultMimeTypes(contentType);
    }

    private TemplateEngineService getTemplateEngineService(String engine) {
        if (!this.templateEngineServiceMap.containsKey(engine)) {
            throw new TemplateEngineNotInitializedException("Template engine service for engine " + engine + " is not initialized.");
        }
        return this.templateEngineServiceMap.get(engine);
    }

}

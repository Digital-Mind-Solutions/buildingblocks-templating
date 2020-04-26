package org.digitalmind.buildingblocks.templating.core.template.config;


import lombok.Getter;
import lombok.Setter;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateEngineProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.digitalmind.buildingblocks.templating.core.template.config.TemplateModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.templating.core.template.config.TemplateModuleConfig.PREFIX;

@Configuration
@ConfigurationProperties(prefix = PREFIX)
@EnableConfigurationProperties
@Getter
@Setter
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
public class TemplateConfig {
    private boolean enabled;
    private Map<String, TemplateEngineProperties> engines = new HashMap<>();
}

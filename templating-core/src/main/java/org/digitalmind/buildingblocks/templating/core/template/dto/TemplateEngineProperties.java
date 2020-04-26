package org.digitalmind.buildingblocks.templating.core.template.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TemplateEngineProperties {
    protected String className;
    protected boolean enabled;
    protected Map<String, Object> properties = new HashMap<>();
}

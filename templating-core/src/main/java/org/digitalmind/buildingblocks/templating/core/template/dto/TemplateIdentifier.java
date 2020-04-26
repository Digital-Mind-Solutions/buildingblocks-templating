package org.digitalmind.buildingblocks.templating.core.template.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.templating.core.template.exception.TemplateBadRequestException;

@SuperBuilder
@Getter
@EqualsAndHashCode
@ToString
public class TemplateIdentifier {
    private final Long id;
    private final String namespace;
    private final String name;

    public TemplateIdentifier(Long id) {
        this.id = id;
        this.namespace = null;
        this.name = null;
    }

    public TemplateIdentifier(String namespace, String name) {
        this.id = null;
        this.namespace = namespace;
        this.name = name;
    }


    public abstract static class TemplateIdentifierBuilder<C extends TemplateIdentifier, B extends TemplateIdentifierBuilder<C, B>> {

        public final TemplateIdentifierBuilder identifier(String identifier) {
            if (identifier != null) {
                int pos = identifier.indexOf(":");
                if (pos > 0) {
                    this.namespace(identifier.substring(0, pos)).build();
                    this.name(identifier.substring(pos + 1));
                    return this;
                } else {
                    this.id(Long.valueOf(identifier));
                    return this;
                }
            }
            throw new TemplateBadRequestException("The provided identifier value is missing or does not have a valid value");
        }

    }

}

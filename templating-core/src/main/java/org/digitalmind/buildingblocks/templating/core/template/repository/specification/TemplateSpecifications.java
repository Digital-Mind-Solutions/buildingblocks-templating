package org.digitalmind.buildingblocks.templating.core.template.repository.specification;


import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateSearchOperator;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class TemplateSpecifications {

    public static Specification<Template> matchEngineList(final List<String> engineList) {
        return new Specification<Template>() {
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Path<Template> enginePath = root.<Template>get("engine");
                return enginePath.in(engineList);
            }
        };
    }

    public static Specification<Template> matchLocale(final String locale) {
        return new Specification<Template>() {
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Path<String> localePath = root.<String>get("locale");
                return builder.equal(localePath, locale);
            }
        };
    }

    public static Specification<Template> matchNamespace(final String namespace) {
        return new Specification<Template>() {
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Path<String> namespacePath = root.<String>get("namespace");
                return builder.equal(namespacePath, namespace);
            }
        };
    }

    public static Specification<Template> matchName(final String name, TemplateSearchOperator operator) {
        return new Specification<Template>() {
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                final Path<String> namePath = root.<String>get("name");
                switch (operator) {
                    case CONTAINS:
                        return builder.like(namePath, "%" + name + "%");
                    case START_WITH:
                        return builder.like(namePath, name + "%");
                    case EQUALS:
                        return builder.equal(namePath, name);
                }
                return null;
            }
        };
    }

}

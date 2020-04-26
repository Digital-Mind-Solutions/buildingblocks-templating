package org.digitalmind.buildingblocks.templating.core.template.service;


import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.core.template.entity.Template;
import org.digitalmind.buildingblocks.templating.core.template.dto.TemplateSearchOperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TemplateDBService {

    Template getTemplate(TemplateIdentifier identifier);

    Template getById(Long id);

    Template getByNamespaceAndName(String namespace, String name);

    List<String> findNamespace(String namespace);

    Page<Template> findBy(List<String> engineList, String namespace, String name, TemplateSearchOperator operator, Pageable pageable);

    Template save(Template template);

    void delete(Template template);

}

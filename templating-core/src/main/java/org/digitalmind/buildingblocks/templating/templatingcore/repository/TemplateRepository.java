package org.digitalmind.buildingblocks.templating.templatingcore.repository;

import org.digitalmind.buildingblocks.templating.templatingcore.entity.Template;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.digitalmind.buildingblocks.templating.templatingcore.config.TemplateModuleConfig.ENABLED;


@Repository
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
public interface TemplateRepository extends JpaRepository<Template, Long>, JpaSpecificationExecutor {

    Template getById(Long id);

    Template getByNamespaceAndName(String namespace, String name);

    @Query("SELECT DISTINCT U.namespace FROM Template U WHERE U.namespace LIKE %?1%")
    List<String> findNamespace(String namespace);

}

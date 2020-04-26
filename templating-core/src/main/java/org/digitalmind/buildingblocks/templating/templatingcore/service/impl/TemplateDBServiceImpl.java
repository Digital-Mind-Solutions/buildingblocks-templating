package org.digitalmind.buildingblocks.templating.templatingcore.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateIdentifier;
import org.digitalmind.buildingblocks.templating.templatingcore.dto.TemplateSearchOperator;
import org.digitalmind.buildingblocks.templating.templatingcore.entity.Template;
import org.digitalmind.buildingblocks.templating.templatingcore.repository.TemplateRepository;
import org.digitalmind.buildingblocks.templating.templatingcore.repository.specification.TemplateSpecifications;
import org.digitalmind.buildingblocks.templating.templatingcore.service.TemplateDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.digitalmind.buildingblocks.templating.templatingcore.config.TemplateModuleConfig.CACHE_NAME;
import static org.digitalmind.buildingblocks.templating.templatingcore.config.TemplateModuleConfig.ENABLED;


@Service
@Transactional
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
public class TemplateDBServiceImpl implements TemplateDBService {

    private final TemplateRepository templateRepository;
    //private final TemplateDBConfig config;
    //private final DynamicCacheResolver cacheResolver;


    @Autowired
    public TemplateDBServiceImpl(
            TemplateRepository templateRepository
            //,
            //@Qualifier(DYNAMIC_CACHE_RESOLVER) DynamicCacheResolver cacheResolver,
            //TemplateDBConfig config
    ) {
        this.templateRepository = templateRepository;
        //this.cacheResolver = cacheResolver;
        //this.config = config;

        //        if (config.getDatabaseCache() != null) {
        //            try {
        //                BasicOperation operation;
        //                CacheableOperation.Builder cacheableBuilder;
        //                CachePutOperation.Builder cachePutBuilder;
        //                CacheEvictOperation.Builder cacheEvictBuilder;
        //
        //
        //                cacheableBuilder = new CacheableOperation.Builder();
        //                cacheableBuilder.setUnless("#result == null");
        //                cacheableBuilder.setKey("#identifier.id != null ? #identifier.id : #identifier.namespace + ':' + #identifier.name");
        //                operation = cacheableBuilder.build();
        //                this.cacheResolver.registerCacheDefinition(
        //                        DynamicCacheDefinition.builder()
        //                                .method(TemplateDBServiceImpl.class.getMethod(
        //                                        "getTemplate",
        //                                        new Class[]{TemplateIdentifier.class}
        //                                        )
        //                                )
        //                                .operation(operation)
        //                                .cach(
        //                                        DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                                .cacheManager(config.getDatabaseCache().getCacheManager())
        //                                                .cacheNames(config.getDatabaseCache().getCacheNames())
        //                                                .build()
        //                                )
        //                                .build()
        //                );
        //
        //                for (String key : new String[]{"#result.namespace + ':' + #result.name", "#result.id"}) {
        //                    cachePutBuilder = new CachePutOperation.Builder();
        //                    cachePutBuilder.setCondition("#result != null");
        //                    cachePutBuilder.setKey(key);
        //                    operation = cachePutBuilder.build();
        //                    this.cacheResolver.registerCacheDefinition(
        //                            DynamicCacheDefinition.builder()
        //                                    .method(TemplateDBServiceImpl.class.getMethod(
        //                                            "findById",
        //                                            new Class[]{Long.class}
        //                                            )
        //                                    )
        //                                    .operation(operation)
        //                                    .cach(
        //                                            DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                                    .cacheManager(config.getDatabaseCache().getCacheManager())
        //                                                    .cacheNames(config.getDatabaseCache().getCacheNames())
        //                                                    .build()
        //                                    )
        //                                    .build()
        //                    );
        //
        //                    this.cacheResolver.registerCacheDefinition(
        //                            DynamicCacheDefinition.builder()
        //                                    .method(TemplateDBServiceImpl.class.getMethod(
        //                                            "findByNamespaceAndName",
        //                                            new Class[]{String.class, String.class}))
        //                                    .operation(operation)
        //                                    .cach(
        //                                            DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                                    .cacheManager(config.getDatabaseCache().getCacheManager())
        //                                                    .cacheNames(config.getDatabaseCache().getCacheNames())
        //                                                    .build()
        //                                    )
        //                                    .build()
        //                    );
        //
        //                    this.cacheResolver.registerCacheDefinition(
        //                            DynamicCacheDefinition.builder()
        //                                    .method(TemplateDBServiceImpl.class.getMethod("save", new Class[]{Template.class}))
        //                                    .operation(operation)
        //                                    .cach(
        //                                            DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                                    .cacheManager(config.getDatabaseCache().getCacheManager())
        //                                                    .cacheNames(config.getDatabaseCache().getCacheNames())
        //                                                    .build()
        //                                    )
        //                                    .build()
        //                    );
        //                }
        //
        //                cacheEvictBuilder = new CacheEvictOperation.Builder();
        //                cacheEvictBuilder.setKey("#id");
        //                operation = cacheEvictBuilder.build();
        //                this.cacheResolver.registerCacheDefinition(
        //                        DynamicCacheDefinition.builder()
        //                                .method(TemplateDBServiceImpl.class.getMethod("deleteById", new Class[]{Long.class}))
        //                                .operation(operation)
        //                                .cach(
        //                                        DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                                .cacheManager(config.getDatabaseCache().getCacheManager())
        //                                                .cacheNames(config.getDatabaseCache().getCacheNames())
        //                                                .build()
        //                                )
        //                                .build()
        //                );
        //
        //                cacheEvictBuilder = new CacheEvictOperation.Builder();
        //                cacheEvictBuilder.setKey("#template.id");
        //                cacheEvictBuilder.setCondition("#template != null");
        //                operation = cacheEvictBuilder.build();
        //                this.cacheResolver.registerCacheDefinition(
        //                        DynamicCacheDefinition.builder()
        //                                .method(TemplateDBServiceImpl.class.getMethod("delete", new Class[]{Template.class}))
        //                                .operation(operation)
        //                                .cach(
        //                                        DynamicCacheDefinition.DynamicCacheProperties.builder()
        //                                                .cacheManager(config.getDatabaseCache().getCacheManager())
        //                                                .cacheNames(config.getDatabaseCache().getCacheNames())
        //                                                .build()
        //                                )
        //                                .build()
        //                );
        //
        //            } catch (NoSuchMethodException e) {
        //                throw new TemplateDBInitializeException(e);
        //            }
        //        }
    }

    //@Cacheable(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#identifier.id != null ? #identifier.id : #identifier.namespace + ':' + #identifier.name", unless = "#result == null")
    @Cacheable(cacheNames = CACHE_NAME, key = "#identifier.id != null ? #identifier.id : #identifier.namespace + ':' + #identifier.name", unless = "#result==null")
    @Override
    @Transactional(noRollbackFor = {EntityNotFoundException.class})
    public Template getTemplate(TemplateIdentifier identifier) {
        if (identifier.getId() != null) {
            return getById(identifier.getId());
        } else {
            return getByNamespaceAndName(identifier.getNamespace(), identifier.getName());
        }
    }

    //    @Caching(
    //            put = {
    //                    @CachePut(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#result.namespace + ':' + #result.name", condition = "#result != null"),
    //                    @CachePut(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#result.id", condition = "#result != null")
    //            }
    //    )
    @Caching(
            put = {
                    @CachePut(cacheNames = {CACHE_NAME}, key = "#result.namespace + ':' + #result.name", condition = "#result != null"),
                    @CachePut(cacheNames = {CACHE_NAME}, key = "#result.id", condition = "#result != null")
            }
    )
    @Override
    public Template getById(Long id) {
        return this.templateRepository.findById(id).orElseGet(() -> {
                    throw new EntityNotFoundException();
                }
        );
    }


    //    @Caching(
    //            put = {
    //                    @CachePut(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#result.namespace + ':' + #result.name", condition = "#result != null"),
    //                    @CachePut(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#result.id", condition = "#result != null")
    //            }
    //    )
    @Caching(
            put = {
                    @CachePut(cacheNames = CACHE_NAME, key = "#result.namespace + ':' + #result.name", condition = "#result != null"),
                    @CachePut(cacheNames = CACHE_NAME, key = "#result.id", condition = "#result != null")
            }
    )
    @Override
    public Template getByNamespaceAndName(String namespace, String name) {
        String namespaceNormalised = FilenameUtils.normalize(namespace, true);
        Template template = this.templateRepository.getByNamespaceAndName(namespace, name);
        if (template == null) {
            throw new EntityNotFoundException();
        }
        return template;
    }


    @Override
    public List<String> findNamespace(String namespace) {
        return this.templateRepository.findNamespace(namespace);
    }

    @Override
    public Page<Template> findBy(List<String> engineList, String namespace, String name, TemplateSearchOperator operator, Pageable pageable) {
        Specification<Template> spec = new Specification<Template>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (engineList != null && engineList.size() > 0) {
                    predicates.add(TemplateSpecifications.matchEngineList(engineList).toPredicate(root, query, criteriaBuilder));
                }
                if (namespace != null) {
                    predicates.add(TemplateSpecifications.matchNamespace(namespace).toPredicate(root, query, criteriaBuilder));
                }
                if (name != null && operator != null) {
                    predicates.add(TemplateSpecifications.matchName(name, operator).toPredicate(root, query, criteriaBuilder));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return this.templateRepository.findAll(spec, pageable);
    }

    //    @Caching(
    //            put = {
    //                    @CachePut(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#result.namespace + ':' + #result.name", condition = "#result != null"),
    //                    @CachePut(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#result.id", condition = "#result != null")
    //            }
    //    )
    @Caching(
            put = {
                    @CachePut(cacheNames = CACHE_NAME, key = "#result.namespace + ':' + #result.name", condition = "#result != null"),
                    @CachePut(cacheNames = CACHE_NAME, key = "#result.id", condition = "#result != null")
            }
    )
    @Override
    public Template save(Template template) {
        return this.templateRepository.save(template);
    }


    //    @CacheEvict(cacheResolver = DYNAMIC_CACHE_RESOLVER, key = "#template.id", condition = "#template != null")
    //    @CacheEvict(cacheNames = CACHE_NAME, key = "#template.id", condition = "#template != null")
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = CACHE_NAME, key = "#template.namespace + ':' + #template.name"),
                    @CacheEvict(cacheNames = CACHE_NAME, key = "#template.id")
            }
    )
    @Override
    public void delete(Template template) {
        this.templateRepository.delete(template);
    }

}

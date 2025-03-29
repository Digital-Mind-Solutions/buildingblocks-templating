package org.digitalmind.buildingblocks.templating.core.template.config;


import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.templating.core.template.config.TemplateModuleConfig.*;

@Configuration
@ConditionalOnProperty(name = API_ENABLED, havingValue = "true")
public class TemplateSwaggerConfig {

    private final ServletContext servletContext;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ApiSwaggerProperties {
        private ApiSwaggerDocketProperties docket;
        private ApiSwaggerInfoProperties info;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class ApiSwaggerDocketProperties {
            private String host;
            private String basePath;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class ApiSwaggerInfoProperties {
            private String groupName;
            private String title;
            private String description;
            private String version;
            private ApiSwaggerContactProperties contact;
            private String license;
            private String licenseUrl;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @ToString
            public static class ApiSwaggerContactProperties {
                private String name;
                private String url;
                private String email;
            }
        }
    }

    @Autowired
    public TemplateSwaggerConfig(
            ServletContext servletContext
    ) {
        this.servletContext = servletContext;
    }

    @Bean(PREFIX + "ApiSwaggerProperties")
    @ConfigurationProperties(prefix = PREFIX + ".api")
    public ApiSwaggerProperties apiProperties() {
        return new ApiSwaggerProperties();
    }


    //TODO check new output
    @Bean(PREFIX + "Docket")
    public GroupedOpenApi sopranoInboundApi() {
        return GroupedOpenApi.builder()
                .group(apiProperties().getInfo().getGroupName())
                .packagesToScan(API_PACKAGE)
                .addOpenApiCustomizer(openApi -> {
                    SecurityScheme apiKeyScheme = new SecurityScheme()
                            .name("ApiKey")
                            .type(SecurityScheme.Type.APIKEY)
                            .in(SecurityScheme.In.HEADER)
                            .name("x-api-key");
                    SecurityRequirement securityRequirement = new SecurityRequirement()
                            .addList("ApiKey");
                    openApi.info(apiInfo(apiProperties()));
                    openApi.addSecurityItem(securityRequirement);
                    openApi.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("ApiKey", apiKeyScheme));
                })
                .build();
    }

    private Info apiInfo(ApiSwaggerProperties apiSwaggerProperties) {
        Contact apiContact = new Contact();
        apiContact.setName(apiSwaggerProperties.getInfo().getContact().getName());
        apiContact.setEmail(apiSwaggerProperties.getInfo().getContact().getEmail());
        apiContact.setUrl(apiSwaggerProperties.getInfo().getContact().getUrl());

        License apiLicense = new License();
        apiLicense.setName(apiSwaggerProperties.getInfo().getLicense());
        apiLicense.setUrl(apiSwaggerProperties.getInfo().getLicenseUrl());

        Info apiInformation = new Info();
        apiInformation.setTitle(apiSwaggerProperties.getInfo().getTitle());
        apiInformation.setDescription(apiSwaggerProperties.getInfo().getDescription());
        apiInformation.setVersion(apiSwaggerProperties.getInfo().getVersion());
        apiInformation.setContact(apiContact);
        apiInformation.setLicense(apiLicense);

        return apiInformation;
    }
}

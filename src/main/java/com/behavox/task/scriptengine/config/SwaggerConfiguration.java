package com.behavox.task.scriptengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Script Execution Service",
                "Service can execute script-function in Javascript or Groovy \n" +
                        "Set flag for language:\n engine.groovy=true\n" +
                        "engine.javascript=false \n" +
                        "All results persisted in DB, in case of same execution it will get result from db",
                "API v1",
                "DB console http://localhost:8080/h2-console/",
                new Contact("Artem Karpov", "www.example.com", "artem.karpov@yahoo.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
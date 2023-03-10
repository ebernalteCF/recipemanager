package ebernaltemestre.recipemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
    
    @Bean
    public Docket api() {
       return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(getInfo())
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("ebernaltemestre.recipemanager.controller"))
        .build();
    }

    private ApiInfo getInfo() {
      return new ApiInfoBuilder().title("Recipe Manager").description("An api made to manage a user's recipes").build();
    }
}
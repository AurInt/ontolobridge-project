package edu.miami.schurer.ontolobridge;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;

import java.util.Collections;

// TODO Switch to springdoc-openapi, as Swagger deprecated
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport{

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("prod")
                .pathsToMatch("/api")
                .build();
    }
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("server.servlet.context-path")
                .pathsToMatch("/")
               // .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Admin.class))
                .build();
    }
//
//    @Bean
//    public Docket api(ServletContext servletContext)
//
//    {
//        ApiSelectorBuilder dock = new Docket(DocumentationType.SWAGGER_2)
//                .pathProvider(new RelativePathProvider(servletContext) {
//
//                    public String getApplicationBasePath() {
//                        String context = System.getProperty("server.servlet.context-path", "/");
//                        if(context.equals("/"))
//                            return "/";
//                        if(activeProfile.equals("prod"))
//                            return "/api";
//                        else
//                            return "/api-test";
//
//                    }
//                })
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("edu.miami.schurer.ontolobridge"))
//                .paths(PathSelectors.any())
//                ;
//        if(!activeProfile.equals("dev")){
//            dock = dock.paths(Predicates.not(Predicates.or(PathSelectors.ant("/frontend/*"),PathSelectors.ant("/"),PathSelectors.ant("/csrf"))));
//        }else{
//            dock = dock.paths(Predicates.not(Predicates.or(PathSelectors.ant("/"),PathSelectors.ant("/csrf"))));
//        }
//        return dock.build()
//                .apiInfo(apiInfo())
//                .securitySchemes(Lists.newArrayList(apiToken(),jwtToken()))
//                .produces(new HashSet<String>(Arrays.asList("application/json")));
//    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Ontolobridge API",
                "“OntoloBridge” is the bridge between the user gap and controlled scientific vocabularies. Ontologies are sets of concepts and categories in a subject area or domain that show their properties and the relations between them. The OntoloBridge system will provide a semi- automated, convenient, rigorous process for researchers around the world to update and extend the BioAssay Ontology.",
                "0.1",
                "https://github.com/OntoloBridge/ontolobridge-project/blob/master/README.md",
                new Contact("Ontolobridge", "http://ontolobridge.ccs.miami.edu/", "info@ontolobridge.org"),
                "License of API", "API license URL", Collections.emptyList());
    }

    private ApiKey jwtToken() {
        return new ApiKey("jwtToken", "Authorization", "header");
    }

    private ApiKey apiToken() {
        return new ApiKey("token", "Authorization", "header");
    }

    
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}

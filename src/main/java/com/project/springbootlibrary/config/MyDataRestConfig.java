package com.project.springbootlibrary.config;

import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private String theAllowedOrigins = "http://localhost:3000";
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config , CorsRegistry corsRegistry){
        HttpMethod[] theUnsupportedActions = {HttpMethod.POST , HttpMethod.PUT ,HttpMethod.DELETE,HttpMethod.PATCH};
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
        disableHttpMethos(Book.class , config , theUnsupportedActions);
        disableHttpMethos(Review.class , config , theUnsupportedActions);
        /*Configure CORS Mapping*/
        corsRegistry.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
    }
    private void disableHttpMethos(Class theClass , RepositoryRestConfiguration configuration , HttpMethod[] unsupportedActions){
        configuration.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));

    }
}

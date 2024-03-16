package edu.miami.schurer.ontolobridge;

import edu.miami.schurer.ontolobridge.utilities.JWTRefreshInterceptor;
import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

@SpringBootApplication
@EnableScheduling
@EnableEmailTools
@EnableCaching
public class OntolobridgeApplication {


    JWTRefreshInterceptor interceptor;
    public static void main(String[] args) {
        SpringApplication.run(OntolobridgeApplication.class, args);
    }

    @Bean
    public MappedInterceptor myInterceptor()
    {
        return new MappedInterceptor(null, (HandlerInterceptor) interceptor);
    }
}

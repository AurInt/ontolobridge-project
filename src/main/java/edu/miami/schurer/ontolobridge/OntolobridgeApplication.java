package edu.miami.schurer.ontolobridge;

import edu.miami.schurer.ontolobridge.utilities.JWTRefreshInterceptor;
import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

@SpringBootApplication
@EnableScheduling
@EnableEmailTools
@EnableCaching
public class OntolobridgeApplication extends WebSecurityConfigurerAdapter {

    JWTRefreshInterceptor interceptor;
    public static void main(String[] args) {

        SpringApplication.run(OntolobridgeApplication.class, args);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests(a -> a
                        .antMatchers("/", "/error", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login();
        // @formatter:on
    }
    @Bean
    public MappedInterceptor myInterceptor()
    {
        return new MappedInterceptor(null, (HandlerInterceptor) interceptor);
    }
}

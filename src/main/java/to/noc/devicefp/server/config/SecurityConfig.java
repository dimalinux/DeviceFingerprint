/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import to.noc.devicefp.server.auth.AutoRegisteringOpenIdUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final int  oneHundredDaysInSeconds = 100 * 24 * 60 * 60;

    @Autowired private DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/deviceFingerprint/**",
                    "/*.swf",       // flash
                    "/*.xap",       // silverlight
                    "/*.class",     // java applet (if we ever enable it again)
                    "/sitemap.xml",
                    "/favicon.ico"
                );

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .openidLogin()
                .failureUrl("/?login_error=t")
                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .authenticationUserDetailsService(userDetailsService())
                .attributeExchange("https://www\\.google\\.com/.*")
                    .attribute("email")
                        .type("http://axschema.org/contact/email")
                        .required(true)
                        .count(1)
                        .and()
                    .attribute("firstname")
                        .type("http://axschema.org/namePerson/first")
                        .required(true)
                        .and()
                    .attribute("lastname")
                        .type("http://axschema.org/namePerson/last")
                        .required(true)
                        .and()
                    .and()
                .attributeExchange(".*\\.yahoo\\.com/.*")
                    .attribute("email")
                        .type("http://axschema.org/contact/email")
                        .required(true)
                        .and()
                    .attribute("fullname")
                        .type("http://axschema.org/namePerson")
                        .required(true)
                        .and()
                    .and()
                .and()
            .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
            .rememberMe()
                .userDetailsService(userDetailsService())
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(oneHundredDaysInSeconds)
                .authenticationSuccessHandler(authenticationSuccessHandler())
                .and()
            .logout()
                .logoutSuccessUrl("/");
    }


    @Override
    @Bean
    public AutoRegisteringOpenIdUserDetailsService userDetailsService() {
        return new AutoRegisteringOpenIdUserDetailsService();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }

}

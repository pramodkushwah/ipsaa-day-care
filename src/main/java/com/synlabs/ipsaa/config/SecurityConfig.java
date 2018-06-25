package com.synlabs.ipsaa.config;

import com.synlabs.ipsaa.auth.HttpHeaderFilter;
import com.synlabs.ipsaa.auth.JwtFilter;
import com.synlabs.ipsaa.auth.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Bean
    public JwtFilter authFilter() {
        return new JwtFilter();
    }

    @Bean
    public HttpHeaderFilter cacheFilter()
    {
        return new HttpHeaderFilter();
    }

    @Bean
    public RateLimitFilter rateLimitFilter() {
        return new RateLimitFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .httpBasic().disable()
            .authorizeRequests().anyRequest().permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .antMatcher("/api/**")
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter(), JwtFilter.class);

        http.headers().frameOptions().disable()
                .cacheControl().and()
                .contentTypeOptions().and()
                .xssProtection().and()
                .httpStrictTransportSecurity();
    }
}

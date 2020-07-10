package com.modiejun.cloudfiles.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.modiejun.cloudfiles.Security.ApplicationUserRoles.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;

    @Autowired
    public SecurityConfig(PasswordEncoder encoder) {
        this.encoder = encoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //development settings
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/console/**","/h2-console/**").permitAll()// allow h2-database
                .antMatchers("/index","/js/**","/css**","/templates").permitAll()
                .antMatchers("/").authenticated()
                .anyRequest().permitAll()
            .and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
            .and()
                .logout().logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                //delete cookies
//                .deleteCookies("JSESSIONID","remember-me")
                .logoutSuccessUrl("/login");

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin= User.builder().username("admin").password(encoder.encode("admin"))
                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthoritySet()).build();

        UserDetails normal_user= User.builder().username("normal").password(encoder.encode("normal"))
                .roles(NORMAL_USER.name())
                .authorities(NORMAL_USER.getGrantedAuthoritySet()).build();

        UserDetails visitor= User.builder().username("visitor").password(encoder.encode("visitor"))
                .roles(VISITOR.name())
                .authorities(VISITOR.getGrantedAuthoritySet()).build();


        return new InMemoryUserDetailsManager(admin,normal_user,visitor);
    }


}

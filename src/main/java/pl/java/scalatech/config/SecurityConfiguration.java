package pl.java.scalatech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pl.java.scalatech.security.ApiAuthenticationEntryPoint;
import pl.java.scalatech.security.AuthenticationTokenFilter;


@Configuration
@ComponentScan(basePackages="pl.java.scalatech.security")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApiAuthenticationEntryPoint authenticationEntryPoint;

    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }
    
    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
      AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
      authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
      return authenticationTokenFilter;
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        // @formatter:off
        web.ignoring().
        antMatchers("/assets/**")
        .antMatchers("/css/**")
        .antMatchers("/js/**")
        .antMatchers("/images/**")
        .antMatchers("/favicon.ico")
        .antMatchers("/webjars/**");
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic().and().requiresChannel().anyRequest().requiresSecure();
        http
        .authorizeRequests().antMatchers("/welcome", "/api/ping","/api/cookie","/currentUser","/console","/","/login").permitAll();
        http.authorizeRequests().antMatchers("/api/**").authenticated();
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.httpBasic().and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http
        .addFilterBefore( authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
  
          // @formatter:on
    }

   @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
     // @formatter:off

        auth.inMemoryAuthentication().withUser("przodownik").password("slawek").roles("USER").and()
                                     .withUser("admin").password("slawek").roles( "ADMIN");
     // @formatter:on
    }
}

package configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity

public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	
	 @Autowired
	    private RESTAuthenticationEntryPoint authenticationEntryPoint;
	    @Autowired
	    private RESTAuthenticationFailureHandler authenticationFailureHandler;
	    @Autowired
	    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;
	 
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth ) throws Exception{
		auth.inMemoryAuthentication()
		.withUser("user").password("password").roles("rola");
	}
	
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests().antMatchers("/**").authenticated();
	        http.csrf().disable();
	       // http.authenticationProvider(new BasicAuthen)
	        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
	      //http.formLogin().successHandler(authenticationSuccessHandler);
	      //  http.formLogin().failureHandler(authenticationFailureHandler);
	    }
	

	

}

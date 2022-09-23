package net.javaguides.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import net.javaguides.springboot.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private UserService userService;

//	@Autowired
//	  private UserDetailsService secUserDetailsService;
//
//	  @Autowired
//	  private PasswordEncoder secPasswordEncoder;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
//	}

//	@Autowired
//	  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//	    auth.userDetailsService(secUserDetailsService).passwordEncoder(secPasswordEncoder);
//	  }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated()).httpBasic();
		http.authorizeRequests().antMatchers("/registration**", "/js/**", "/css/**", "/img/**").permitAll().anyRequest()
				.authenticated().and().formLogin().loginPage("/login").permitAll().and().logout()
				.invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.permitAll();
		return http.build();
	}

}

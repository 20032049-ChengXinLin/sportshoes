/**
 * 
 * I declare that this code was written by me, 20032049. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Cheng Xin Lin
 * Student ID: 20032049
 * Class: C372-4D-E62F-A
 * Date created: 2022-Jan-13 3:33:53 pm 
 * 
 */
package e62f.xinlin.gradedassignment;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author 20032049
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Bean
	public ShoesMemberDetailsService ShoesMemberDetailsService() {
		return new ShoesMemberDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(ShoesMemberDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
		tokenRepo.setDataSource(dataSource);
		return tokenRepo;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/merchant", "/products", "/products/add", "/products/edit/*", "/products/save",
						"/products/delete/*", "/products/page/*", "/shoesbrand", "/shoesbrand/add",
						"/shoesbrand/edit/*", "/shoesbrand/save", "/shoesbrand/delete/*", "/shoestype",
						"/shoestype/add", "/shoestype/edit/*", "/shoestype/save", "/shoestype/delete/*",
						"/availablesize", "/availablesize/add", "/availablesize/edit/*", "/availablesize/save",
						"/availablesize/delete/*", "/customerorders", "/member", "/performance",
						"/customerorders/member/*", "/customerorders/page/*")
				.hasRole("ADMIN")
				.antMatchers("/shoppingcart", "/shoppingcart/add/*", "/shoppingcart/edit/*", "/shoppingcart/delete/*",
						"/shoppingcart/process_order","/member/edit/*", "/member/delete/*", "/member/save",
						"/purchasehistory", "/purchasehistory/page/*")
				.hasRole("CUSTOMER")
				.antMatchers("/", "/account", "/pagetwo/*", "/sizechart", "/contactus", "/register", "/register/save",
						"/forgot_password", "/reset_password")
				.permitAll().antMatchers("/bootstrap/*/*").permitAll().antMatchers("/images/*").permitAll()
				.antMatchers("/font-awesome/*/*").permitAll().antMatchers("/jquery/*").permitAll()
				.antMatchers("/uploads/products/*/*").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().and().logout().logoutUrl("/logout").permitAll().and().rememberMe()
				.tokenRepository(persistentTokenRepository()).userDetailsService(ShoesMemberDetailsService()).and()
				.exceptionHandling().accessDeniedPage("/403");

	}

}

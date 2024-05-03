package com.cafe.cafe_management.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.cafe.cafe_management.Filter.JwtFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import com.cafe.cafe_management.JWT.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration{          //WebSecurityConfigurerAdapter ru change heichi current name


   @Autowired
   private WebSecurityConfiguration webSecurityConfiguration; // abhi add kiya hai


    @Autowired
    //  private JwtUtil  jwtUtil;
    JwtFilter  jwtFilter ;

    @Autowired
    CostumUserDetailsService costumUserDetailsService;


   public ObjectPostProcessor<Object> objectObjectPostProcessor;

//     @Bean
//     public HttpSecurity httpSecurity() {                                //Abhi add kiya hai
//         // Use webSecurityConfiguration to configure HttpSecurity
//         return webSecurityConfiguration.httpSecurity();
//     }

//     @Configuration
// public class WebSecurityConfiguration {

//     @Bean
//     public HttpSecurity httpSecurity() {                             //abhi add kiya hai
//         // Configure and return HttpSecurity
//         return new HttpSecurity(objectObjectPostProcessor, null, null);
//     }
// }

   //   @Override             Error asuchi override lekhilr
     protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(costumUserDetailsService);
     }
     @Bean
     public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
     }
     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
     


   //  public SecurityFilterChain filterchain(HttpSecurity http)throws Exception{
	// 	http.csrf(csrf->csrf.disable())
	// 	.authorizeHttpRequests(auth->auth
	// 			.requestMatchers("/auth/signup","/auth/login","/api/**").permitAll())
	// 			// .requestMatchers("/api/**").authenticated()
	// 	.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   //      .authenticationProvider(authenticationProvider())
   //      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	// 	return http.build();
	// }


   @Bean
   public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
      http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
      .and()
      .csrf().disable()
      .authorizeHttpRequests(auth -> auth
      .requestMatchers("/user/login","/user/signup","user/forgotPassword").permitAll())
      .authorizeRequests()
      .anyRequest()
      .authenticated()
      .and()
      .exceptionHandling()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


      http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
      
      return null;
   }
   
  

   @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetail());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    
	@Bean
	public UserDetailsService userDetail() {
//		UserDetails user1 = User.withUsername("Rama").password(encoder.encode("12345")).build();
//		UserDetails user2 = User.withUsername("Mama").password(encoder.encode("6789")).build();
		return new CostumUserDetailsService();
	}

}







// @Configuration
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfiguration {

//     @Autowired
//     private JwtFilter jwtFilter;

//     @Autowired
//     private CostumUserDetailsService customUserDetailsService;

//     @Override
//     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//         auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//     }

//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//             .and()
//             .csrf().disable()
//             .authorizeRequests()
//                 .antMatchers("/user/login", "/user/signup", "/user/forgotPassword").permitAll()
//                 .anyRequest().authenticated()
//             .and()
//             .exceptionHandling()
//             .and()
//             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//         http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//         authenticationProvider.setUserDetailsService(customUserDetailsService);
//         authenticationProvider.setPasswordEncoder(passwordEncoder());
//         return authenticationProvider;
//     }

//     @Bean
//     public UserDetailsService userDetail() {
//         return customUserDetailsService;
//     }
// }

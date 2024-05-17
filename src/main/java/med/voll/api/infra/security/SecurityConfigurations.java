package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
	
	@Autowired
	SecurityFilter securityFilter; 
	
	/*Este método desabilita a proteção de ataques csrf,
	estabelece a autenticação como STATELESS. 
	A Anotação @Bean indica que eu posso injetar esse método em alguma outra classe*/
	 @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         return http.csrf().disable()
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and().authorizeRequests()
         .antMatchers(HttpMethod.POST,"/login").permitAll()
         .anyRequest().authenticated()
         .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
         .build();
     }

	 
	//Esse método getAuthenticationManager() mostra ao Spring como criar o Objeto AuthenticationManager 
	//que está sendo utilizado na classe AutenticacaoController
	@Bean 
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
	     return configuration.getAuthenticationManager();
	}
	
	// Neste método ensinamos o Spring que estamos usando BCrypt como formato Hash para a senha do login
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
 
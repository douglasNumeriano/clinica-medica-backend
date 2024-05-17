package med.voll.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;


//ESSA CLASSE VAI SER CHAMADA ANTES DE CAIR EM QUALQUER CONTROLLER E CHAMAR QUALQUER REQUISIÇÃO 
//PELA PRIMEIRA VEZ, POIS VAI SER ELA QUE VAI VALIDAR O TOKEN 

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	private UsuarioRepository repository;

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var tokenJWT = recuperarToken(request); //Recupera o Token
		
		if(tokenJWT != null) {
			var subject = tokenService.getSubject(tokenJWT.replace("Bearer","").trim()); //Recupera e-mail do usuário logado
			var usuario = repository.findByLogin(subject); // Carrega usuário logado
	        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); // Cria um DTO que representa um usuário

	        SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response); // segue o fluxo para outras requisições
	}

	private String recuperarToken(HttpServletRequest request) {
		var authorization = request.getHeader("Authorization");
		if(authorization != null) {
			return authorization.replace("Bearer", "").trim();
		}
		
		return null;
	}

	
}

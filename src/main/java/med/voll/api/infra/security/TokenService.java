package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.api.domain.usuario.Usuario;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
//	- Algorithm.HMAC256: algoritimo de coficação da senha secreta
//	- withIssuer: nome da aplicação
//	- withSubject: nome do usuário que está logando
//	- withExpiresAt: data de expiração
	
	 public String gerarToken(Usuario usuario) { 
		 
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("API Voll.med")
                .withSubject(usuario.getLogin())
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo);
        } catch (JWTCreationException excepion){
            throw new RuntimeException("Erro ao gerrar token jwt", excepion);
        }        
     }
	 
	 public String getSubject(String tokenJWT) {
		
		 try {
			var algoritmo = Algorithm.HMAC256(secret);
		    return JWT.require(algoritmo)
		    	   .withIssuer("API Voll.med")
		           .build()
		           .verify(tokenJWT)
		           .getSubject();
		        
			} catch (JWTVerificationException exception){
			    throw new RuntimeException("Token JWT inválido ou expirado");
			}
	 }

	 private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	 }
}

package med.voll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//ESSA APLICAÇÃO É FOCADA NA AUTORIZAÇÃO E AUTENTICAÇÃO DE USUÁRIO, USANDO O SPRING SECURITY 
//E GERAÇÃO DE TOKEN TRAVÉS DO JWT


@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}

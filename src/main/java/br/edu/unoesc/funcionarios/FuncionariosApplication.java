package br.edu.unoesc.funcionarios;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.unoesc.funcionarios.service.FuncionarioService;

@SpringBootApplication
public class FuncionariosApplication {
	
	@Value("${mensagem}")
	private String mensagem;
	
	@Value("${ambiente}")
	
	private String ambiente;
	
	private static final String USUARIO = "postgres";
	private static final String SENHA = "jo854756";
	private static final String URL = "jdbc:postgresql://localhost:5432/empresa";
	
	public static Connection conectar() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        return conexao;
}

	
	public static void main(String[] args) {
		SpringApplication.run(FuncionariosApplication.class, args);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(FuncionarioService servico) {
		return args -> {
			System.out.println(mensagem + " " + ambiente);
		
			// Verifica se a comunicação com o banco de dados está ok
	        try {
	            Connection conexao = FuncionariosApplication.conectar();
	            System.out.println("conexao com banco de dados ok");
	        }catch(SQLException ex) {
	            System.out.println("falha na conexão com banco de dados");
	        }
	        
			servico.popularTabelaInicial();

		};
	}
}


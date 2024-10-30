package kz.bitlab.MainServiceProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MainServiceProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainServiceProjectApplication.class, args);
	}

}

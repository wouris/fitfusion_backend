package sk.kasv.mrazik.fitfusion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import sk.kasv.mrazik.fitfusion.controllers.AuthController;

@EnableMongoRepositories
@SpringBootApplication
public class FitfusionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitfusionApplication.class, args);
	}

}

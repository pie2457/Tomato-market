package pie.tomato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TomatoMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TomatoMarketApplication.class, args);
	}

}

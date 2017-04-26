package pl.lodz.p.edu.grs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableMBeanExport;

@EnableCaching
@EnableMBeanExport
@SpringBootApplication
public class GameRentingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameRentingSystemApplication.class, args);
	}
}

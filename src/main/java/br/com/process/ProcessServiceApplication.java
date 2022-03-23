package br.com.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "br.com.process")
public class ProcessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessServiceApplication.class, args);
	}


}

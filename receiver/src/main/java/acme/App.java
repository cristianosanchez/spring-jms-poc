package acme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		// Launch the application
		SpringApplication.run(App.class, args);
	}
}

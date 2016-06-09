package acme;

import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest(randomPort = true)
public class JmsdemoApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(JmsdemoApplicationTests.class);

	@Value("${local.server.port}")
	private int port;

	private final String HOST = "http://localhost";

	private String resource(String resource) {
		String url = String.format("%s:%d/%s", this.HOST, this.port, resource);
		logger.debug(String.format("URL para serviço: %s", url));
		return url;
	}

	// REST-Assured

	/**
	 * Como o retorno do método uma string json, é possível de-serializar a mesma através do ObjectMapper do Jackson.
	 * Note que o new TypeReference é uma instancia que recebe como parametro genérico a List<Associado>,
	 * que é como deve ser feito sempre que de-serializamos uma collection com generic.
	 * O mesmo pode ser feito usando TypeFactory conforme comentado abaixo.
	 */
	@Test
	public void send_message() {
		given().
				formParam("msg", "JMS Demo Application").
				expect().
				statusCode(204).
				when().
				post(resource("message/"));
	}
}

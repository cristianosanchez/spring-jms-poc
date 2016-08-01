package acme.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * Created by cristianosanchez on 16/06/16.
 */
@Configuration
@EnableJms // triggers the discovery of methods annotated with @JmsListener, creating the message listener container under the covers.
public class JMSConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JMSConfiguration.class);
}

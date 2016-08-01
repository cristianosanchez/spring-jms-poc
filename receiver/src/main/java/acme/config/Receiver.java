package acme.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

	private final String DESTINATION_NAME = "DemoQueue";
	
	 @JmsListener(destination=DESTINATION_NAME /*, containerFactory = "myJmsContainerFactory"*/)
	 public void receiveMessage(String message) {
	 	logger.info(String.format("\n\tReceived message [%s]", message));
	 }
}

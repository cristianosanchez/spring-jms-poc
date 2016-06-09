package acme.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
	
	 @JmsListener(destination="DemoQueue" /*, containerFactory = "myJmsContainerFactory"*/)
	 public void receiveMessage(String message) {
	 	logger.info(String.format("\n\tReceived message [%s]", message));
	 }
}

package acme.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

    private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);

	private final String DESTINATION_NAME = "DemoQueue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @POST
	@Consumes("text/plain")
    public void sendMessage(final String msg) {
        logger.info(String.format("Posting message [%s] to destination %s", msg, DESTINATION_NAME));
        jmsTemplate.send(DESTINATION_NAME, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
        logger.info(String.format("Message [%s] sent successfully to destination %s", msg, DESTINATION_NAME));
    }
}

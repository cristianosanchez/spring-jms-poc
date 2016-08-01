package acme.config;

import acme.ws.HelloResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfiguration extends ResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyConfiguration.class);

    private final static Class[] resources = new Class [] {
            HelloResource.class
    };

    public JerseyConfiguration() {
        StringBuilder logMsg = new StringBuilder("Configurando endpoints REST com Jersey API:\n");
        for (Class resourceClass : resources) {
            logMsg.append(String.format("\t%s\n", resourceClass.getCanonicalName()));
            register(resourceClass);
        }
        logger.info(logMsg.toString());
    }
}
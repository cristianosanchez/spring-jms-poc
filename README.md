## Execução

Esta POC foi criada para testar producers/consumers de filas (queues) no HornetQ. Os artefatos gerados estão no formato WAR para que possam ser deployed em containers Java EE.

## Producer

O producer é um módulo war que permite postar mensagem através de um POST para o recurso /message. Este deployment contém um web.xml e jboss-web.xml indicando o context-root da aplicação.

## Receiver

O receiver é um módulo war que permite consumir mensagens da fila indicada, através da configuração de um JMS Listener. Este deployment não contém um web.xml nem jboss-web.xml pois não expõe nenhum contexto HTTP. Apesar disto, é necessário indicar na classe App a configuração de servlet, que servirá de startpoint para o Spring-Boot.

	@SpringBootApplication
	public class App extends SpringBootServletInitializer {
		...
	}

### Deploy via Maven Wildfly Plugin

Note que no arquivo pom.xml da raiz do projeto, o plugin do Wildfly permite automatizar a tarefa de deployment no container indicado. 

Para fazer deployment/undeployment através da CLI, configure em seu arquivo ~.m2/settings.xml as seguintes propriedades do Wildfly: 

    wildfly.hostname
    wildfly.port
    wildfly.username
    wildfly.password

E execute o seguinte comando:

    mvn -pl producer clean wildfly:deploy

    mvn -pl receiver clean wildfly:deploy

## Configurações

### Remote HornetQ

Configure no application.properties o endereço da fila remota e outras propriedades pertinentes:

	spring.hornetq.mode=native

	spring.hornetq.host=192.168.10.17
	spring.hornetq.port=5445

#### Install HornetQ

**NOTA** instruções de como instalar um servidor HornetQ standalone, ou seja, sem o uso da fila gerenciada pelo Wildfly através do profile standalone-full ou domain-full.

1. HornetQ instalado em /opt/hornetq

2. Criar o diretório /hornetq/data o qual irá armazenar os arquivos de dados (mensagens).

3. Para subir o HornetQ em modo single-node, os arquivos de configuração utilizados serão:
 
	<hornetq_home>/config/stand-alone/non-clustered/hornetq-beans.xml
	<hornetq_home>/config/stand-alone/non-clustered/hornetq-configuration.xml
	<hornetq_home>/config/stand-alone/non-clustered/hornetq-jms.xml
	<hornetq_home>/config/stand-alone/non-clustered/hornetq-users.xml
	<hornetq_home>/config/stand-alone/non-clustered/jndi.properties
	<hornetq_home>/config/stand-alone/non-clustered/logging.properties

#### hornetq-configuration.xml

1. Altere os arquivos de dados para um local seguro, por exemplo, /hornetq/data:
 
	<paging-directory>/hornetq/data/paging</paging-directory>
	<bindings-directory>/hornetq/databindings</bindings-directory>	
	<journal-directory>/hornetq/data/journal</journal-directory>
	<large-messages-directory>/hornetq/data/large-messages</large-messages-directory>

2. Alterar o bind permitindo acesso/conexão de qualquer IP (default apenas localhost):
 
	<connectors>
		<connector name="netty">
			<param key="host"  value="0.0.0.0"/>
			...
		</connector>
		
		<connector name="netty-throughput">
			<param key="host"  value="0.0.0.0"/>
			...
		</connector>
	</connectors>

	<acceptors>
		<acceptor name="netty">
			<param key="host"  value="0.0.0.0"/>
			...
		</acceptor>
		
		<acceptor name="netty-throughput">
			<param key="host"  value="0.0.0.0"/>
			...
		</acceptor>
	</acceptors>

#### hornetq-jms.xml

1. Criar as Queue e Topic necessários:
 
	<queue name="LogAcessoQueue">
		<entry name="/queue/LogAcessoQueue"/>
	</queue>

### Wildfly Remote HornetQ

**NOTA** instruções de como configurar a fila gerenciada pelo Wildfly, ou seja, quando utiliza o profile standalone-full ou domain-full.

1. Para desabilitar a segurança e não precisar autenticar, configure o sybsystem messaging com o seguinte parametro:
 
	<subsystem xmlns="urn:jboss:domain:messaging:2.0">
	    <hornetq-server>
	        ...
	        <security-enabled>false</security-enabled>

2. O nome do destination configurado na POC foi DemoQueue. Por algum motivo não encontrou a queue usando o full JNDI name java:/jms/queue/DemoQueue, encontrou quando o valor passado para o JmsListener foi apenas DemoQueue.
 
	@JmsListener(destination="DemoQueue")
	...

## Testing

Após o deployment do producer e do receiver, teste o envio de mensagem através do comando abaixo:

	curl -X POST -d 'cristiano is waiting' -H "Content-Type: text/plain" http://localhost:8080/spring-jms-poc/message

Verifique no log do Wildfly as saídas de entrada de mensagem e consumo da mesma pelo receiver.

**NOTA** certifique-se que o log está habilitado com nível DEBUG para o namespace acme.

## Docker

Docker com Java para subir a fila manualmente, considere ter o binário do HornetQ no diretório do volume.

	docker run -it -v `PWD`:/hornetq/data -v `PWD`:/home/hornetq --rm --name=hornetq -p 5455:5455 -p 5445:5445 java /bin/bash


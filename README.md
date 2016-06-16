## Execução

Com o profile default, o sistema executa com container embedded.

    mvn clean spring-boot:run
    
### profile 'wildfly' - WAR Deployment no Wildfly

Com o profile wildfly, o sistema executa a partir de um container externo e user domain do container.

Para fazer deployment/undeployment através da CLI, configure em seu arquivo ~.m2/settings.xml as seguintes propriedades do Wildfly: 

    wildfly.hostname
    wildfly.port
    wildfly.username
    wildfly.password

E execute o seguinte comando:

    mvn -Pwildfly clean wildfly:deploy

*NOTA* O profile do Spring está sendo atribuído através de uma variável JNDI definida no web.xml.

## Configurações

### Embedded HornetQ

	spring.hornetq.mode=embedded
	spring.hornetq.embedded.queues=DemoQueue
	spring.hornetq.embedded.persistent=true
	spring.hornetq.embedded.data-directory=data

### Remote HornetQ

Configure no application.properties o endereço da fila remota e outras propriedades pertinentes:

	spring.hornetq.mode=native

	spring.hornetq.host=192.168.10.17
	spring.hornetq.port=5445

#### Install HornetQ

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

### Startup

Por padrão o comando run.sh executa seguindo os arquivos de configuração de config/stand-alone/non-clustered. Para executar outra configuração, indicar o caminho como parâmetro do comando run:

	./run.sh ../config/stand-alone/clustered

## Testing

	curl -X POST -d 'cristiano is waiting' -H "Content-Type: text/plain" http://localhost:8001/message

## Docker

Docker com Java para subir a fila manualmente, considere ter o binário do HornetQ no diretório do volume.

	docker run -it -v `PWD`:/hornetq/data -v `PWD`:/home/hornetq --rm --name=hornetq -p 5455:5455 -p 5445:5445 java /bin/bash



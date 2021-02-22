# Desafio Backend Java - Comentários

Primeiramente, gostaria de agradecer a oportunidade de desenvolver este teste/desafio. Foi mais um passo no processo de aprendizagem contínua. Tenho certeza que tentei fazer o melhor possível dentro do prazo acordado.

### Autor

Elisson Francisco da Silva
- email: elissonfs@gmail.com
- cel: (12) 997477873
- linkedIn: https://www.linkedin.com/in/elissonfs/
- github: https://github.com/elissonfs13

## Execução

Os seguintes passos deverão ser executados para uma execução automatizada do desafio proposto: 

- cd globo-efs-script
- docker-compose up -d
- cd ../globo-efs-api
- ./mvnw clean install
- ./mvnw spring-boot:run
- cd ../globo-efs-script
- python3 sendNotifications.py

Observações:
- O desafio foi desenvolvido utilizando a versão 11 do Java e a versão 3 do Python. O ambiente local deverá estar configurado com essas versões para o correto funcionamento do desafio.
- Novas janelas do terminal podem ter que ser abertas durante a execução dos passos descritos acima. Em meu ambiente local, precisei abrir um novo terminal após a execução da API ('./mvnw spring-boot:run').
- Foi implementada a exibição de logs no terminal que está executando a API ('./mvnw spring-boot:run') para a verificação de cada passo do desafio. 
- Os dados salvos no banco de dados podem ser visualizados em tempo de execução ao acessar 'http://localhost:8080/h2-console' em um navegador e acessar as tabelas desejadas. Para acessar: Username: 'sa'; Password: 'password'.
- A fila do RabbitMQ também pode ser visualizada em tempo de execução ao acessar 'http://localhost:15672/' em um navegador. Para acessar: Username: 'rabbitmq'; Password: 'rabbitmq'.

## Tecnologias Utilizadas

Para o desenvolvimento e testes desse desafio foram utilizadas as seguintes tecnologias:
- Java versão 11 (11.0.2-open)
- SpringBoot versão 2.3.8
- Maven versão 3.8.1
- RabbitMQ versão 3.8.9
- Python versão 3 (3.8.5)
- Ubuntu versão 20.04
- Spring Tool Suite versão 4
- Docker versão 19.03.12
- Docker compose versão 1.27.4
- Postman versão 5.5.5

## Comentários sobre desenvolvimento e entregas

#### 1. Configurações inciais e H2 database (12/01/2021):

- Inicialmente foi configurado o projeto como um microsserviço utilizando SpringBoot versão 2.3.8.
- Optou-se pela utilização do banco de dados relacional H2 por sua simplicidade de configuração e execução em memória, tornando-se capaz de cumprir com perfeição os objetivos desse desafio. 
- Nesta etapa também foram criadas as entidades e seus relacionamentos, bem como criadas automaticamente as tabelas no banco de dados seguindo o modelo proposto. 
- Adicionado arquivo 'data.sql' para sempre inserir os dados necessários para correta execução do desafio no banco de dados ao iniciar o sistema.
- Foram adicionadas as anotações '@CreatedDate' e '@LastModifiedDate' nas entidades que possuem datas para que essas sejam preenchidas automaticamente ao salvar a entidade no banco de dados.
- Utilização do Project Lombok para automação do desenvolvimento das entidades.

#### 2. Rest API e atualização de assinaturas (13/01/2021):

- Nesta etapa foi criado a API REST proposta para esse desafio. Criada a RestController POST '/notification' para receber cada uma das notificações individualmente.
- Criadas interfaces com Spring Data JPA para manipulação de dados no banco de dados.
- Criação de serviço para atender as regras de negócio propostas, criando, inserindo e alterando dados no banco conforme chegada das notificações.
- Neste primeiro momento não foi abordado nenhuma conexão com RabbitMQ, foi criado uma conexão entre a controller e o serviço para o desenvolvimento e testes das regras de negócio e a garantia do correto funcionamento desta parte do desafio.
- Foram realizados testes utilizando o Postman, que serão descritos posteriormente, para a garantia do correto funcionamento do desafio. 

#### 3. Docker RabbitMQ e conexão com API (14/01/2021):

- Neste ponto foi criado um docker-compose para download e execução de uma imagem docker do RabbitMQ, bem como a correta configuração deste no projeto em desenvolvimento. A imagem escolhida foi a 'rabbitmq:3-management'. 
- Foram criados o produtor e o consumidor para a fila configurada no RabbitMQ. A conexão entre a controller e o serviço foi desfeita e a partir desse momento, a controller encaminha a notificação recebida diretamente para o produtor, que enfileira a notificação na fila do RabbitMQ. Por sua vez, o consumidor ao ler uma notificação da fila do RabbitMQ, a encaminha ao serviço desenvolvido anteriormente para ser persistida no banco de dados conforme regra proposta. 
- Os mesmos testes com o Postman realizados na etapa anterior (sem o RabbitMQ) foram executados para garantir o mesmo correto funcionamento utilizando o RabbitMQ.

#### 4. Automatizando chamadas para a API (15/01/2021):

- Para a automação do envio das notificação encontradas no arquivo 'notificacoes.txt' foi eschida a linguagem Python, por esta facilitar a implementação e execução deste tipo de aplicação/script. 
- O script desenvolvido lê cada linha do arquivo 'notificacoes.txt' que consiste em uma notificação completa, transforma o dado lido para o tipo JSON e o envia em uma requisição POST para a url 'http://localhost:8080/notification', criada anteriormente na API. 
- De acordo com o retorno da requição (status=200 para sucesso) o script exibe a informação de sucesso ou erro na requisição enviada para a API. 

#### 5. Testes unitários e de API (16/01/2021):

- Foi criado nesta etapa o teste unitário para o serviço que implementa as regras de negócio desenvolvido anteriormente. Para esse teste foram utilizados o JUnit e o Mockito, tecnologias amplamente utilizadas para este tipo de teste. 
- Também foi criado o teste de integração, ou teste de API, para a garantia da correta execução do desafio. Para este teste foi escolhido o MockMvc para sua implementação e execução. 
- O mesmo teste executado nas etapas 2 e 3 foi executado de forma automatizada pelo teste de API, garantindo o mesmo resultado obtido anteriormente. 
- Foram criados novos endpoints REST para auxiliar a visualização dos resultados dos testes: GET '/subscription', GET '/subscription/{subscriptionId}', GET '/event-history' e GET '/event-history/{subscriptionId}'. Também foram criadas interfaces com Spring JPA para a busca dessas informações no banco de dados.
- Para o teste de API, foi adicionado 'Thread.sleep(1000L)' na execução para garantir que a fila do RabbitMQ processe todas as informações antes das consultas de verificação criadas e descritas anteriormente.
- Foi tentado inicialmente a utilização da biblioteca 'org.hamcrest-library' para comparação dos resultados após a realização das requisições com o MockMvc no teste de API. No entanto, após alguns erros não solucionados no uso da biblioteca, optou-se por adotar a utilização do 'ObjectMapper' para mapear os resultados das requisições para as entidades em questão e posteriores comparações dos resultados.

#### 6. Últimas atualizações e comentários (17/01/2021):

- Nesta última etapa foram realizados os últimos ajustes para execução do desafio e a escrita desses comentários finais. 
- Foi acrescentado também algumas imagens dos resultados dos testes realizados em ambiente local. 

## Testes realizados

#### Teste por amostragem

Esse foi o teste descrito nos itens 2 e 3 do tópico anterior. Com a API e o docker RabbitMQ em execução, foram enviadas 6 notificações: 
- {'notification_type' : 'SUBSCRIPTION_PURCHASED', 'subscription': '5793cf6b3fd833521db8c420955e6f01'}
- {'notification_type' : 'SUBSCRIPTION_PURCHASED', 'subscription': '5793cf6b3fd833521db8c420955e6f02'}
- {'notification_type' : 'SUBSCRIPTION_PURCHASED', 'subscription': '5793cf6b3fd833521db8c420955e6f03'}
- {'notification_type' : 'SUBSCRIPTION_CANCELED', 'subscription': '5793cf6b3fd833521db8c420955e6f03'}
- {'notification_type' : 'SUBSCRIPTION_CANCELED', 'subscription': '5793cf6b3fd833521db8c420955e6f02'}
- {'notification_type' : 'SUBSCRIPTION_RESTARTED', 'subscription': '5793cf6b3fd833521db8c420955e6f02'}

Para essas requisições HTTP foi utilizado o Postman e um exemplo dessas requisições pode ser visto na imagem 'postman.png'.

Os logs da API que mostram a execução de cada passo da aplicação pode ser visto na imagem 'logs_api.png'. 

Obteve-se o resultado conforme esperado, em que as assinaturas com ID igual a '5793cf6b3fd833521db8c420955e6f01' e '5793cf6b3fd833521db8c420955e6f02' ficaram com status 'ATIVA' e a assinatura com ID igual a '5793cf6b3fd833521db8c420955e6f03' ficou com status 'CANCELADA'.

As tabelas no banco de dados foram atualizadas corretamente, inclusive com as datas de criação e modificação preenchidas corretamente. Esse resultado pode ser visualizado nas imagens 'dbh2_1.png', 'dbh2_2.png' e 'dbh2_3.png'.

As imagens descritas nesse tópico estão na pasta 'globo-efs-images/teste_amostragem'.

#### Teste final

Esse teste foi realizado após a finalização de todas as etapas desse desafio e consiste na execução de todo o sistema conforme se espera seu correto funcionamento. Segundo a proposta do desafio, deverá ser possível reproduzir esse teste em um Mac OS X OU no Ubuntu. Conforme descrito anteriormente, esse desafio foi desenvolvido no Ubuntu 20.04 e recomenda-se a reprodução desse teste no mesmo ambiente, pois não foi testado em um Mac. 

Com a API e o docker RabbitMQ em execução, foram enviadas todas as requisições encontradas no arquivo 'notificacoes.txt' por meio do script 'sendNotifications.py', conforme log pode ser visto na imagem 'log_python.png'.

Os logs da API que mostram a execução de cada passo da aplicação pode ser visto nas imagens 'logs_api_1.png' e 'logs_api_2.png'. 

Obteve-se o resultado conforme esperado, em que as assinaturas com ID igual a '5793cf6b3fd833521db8c420955e6f03' e '5793cf6b3fd833521db8c420955e6f08' ficaram com status 'CANCELADA' e as demais assinaturas ficaram com status 'ATIVA'.

As tabelas no banco de dados foram atualizadas corretamente, inclusive com as datas de criação e modificação preenchidas corretamente. Esse resultado pode ser visualizado nas imagens 'dbh2_1.png', 'dbh2_2.png' e 'dbh2_3.png'.

As imagens descritas nesse tópico estão na pasta 'globo-efs-images/teste_final'.

## Próximos passos

Tendo em vista a proposta do desafio, creio que a presente implementação atendeu bem a todos os requisitos descritos. No entanto, alguns itens poderiam ser acrescentados para melhorar a automatização, segurança e confiabilidade das regras de negócio, tais como:
- Criação de um novo container docker com a API desenvolvida e colocá-la dentro do compose, para que com um único comando 'docker-compose' podermos subir o container do RAbbitMQ e o da API. 
- Incluir validações na API desenvolvida com BeanValidation para evitar dados nulos e/ou inconsistentes.
- Implementar validações e tratamentos de exceções no processamento das regras de negócios.

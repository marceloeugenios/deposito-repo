### Depósito de Bebidas

O projeto tem como objetivo principal controlar o estoque de um depósito de bebidas em que entradas e saídas serão feitas com base nos locais de armazenamento (seções) disponíveis e os tipos de bebidas. Neste ínicio o projeto suporta apenas bebidas Alcóolicas e Não alcóolicas além de ter um limite máximo para cada tipo que poderá ser alterado futuramente e não poderem serem armanezadas juntas.

- Alcóolica: 500 Litros
- Não Alcóolica: 400 Litros

#### Principais desafios encontrados:

- Cobertura de testes foi bem alta como pode ser vista [aqui](https://gitlab.com/marceloeugenios/deposito-service/-/blob/main/test-coverage.png) onde os principais casos de uso foram incluídos nos cenários de testes
- Não foi adicionado indices nas tabelas já que não foi realizado testes de stress ou performance do banco de dados. O ideal seria antes de subir para algum ambiente de desenvolvimento popularmos uma base com um volume considerável de dados e analisar os planos de execução de cada query enquanto o sistema é utilizado
- Algumas regras não estavam tão claramente explicadas, talvez um simples bate-papo com o PO poderia resolver, porém optei por seguir meu entendimento e realizar a implementação
- Queries dinâmicas como o proposto no projeto poderia requerer a utilização de bibliotecas mais completas e complexas, tais como Specification API com a utilização de reflections, assim poderiamos definir por Annotations quais campos poderiam ser filtrados e orenados. Para este projeto foi utilizado a maneira forçada mesmo, tratando com if-else (o que é longe do ideal).

#### Subindo a infraestrutura completa

Para rodar o projeto bastar realizar o download ou copiar o conteúdo do [docker-compose](https://gitlab.com/marceloeugenios/deposito-service/-/blob/main/deposito-service/docker-compose.yml). Certifique-se de que as portas `8080` (backend) e `5432` (postgresql) estejam disponíveis.
Dentro do docker-compose está a imagem da aplicação backend além de uma versão "stateless" do PostgreSQL para validar a solução. Para autenticação e autorização a melhor opção foi subir o KeyCloak em um cloud provider para evitar ter que ficar configurando no startup da infraestrutura.

```sh
 docker-compose up
```

#### Subindo a infraestrutura em desenvolvimento

Para rodar o projeto em desenvolvimento é utilizado o [docker-compose-dev](https://gitlab.com/marceloeugenios/deposito-service/-/blob/main/deposito-service/docker-compose-dev.yml) para subir apenas o banco de dados.

```sh
 docker-compose -f docker-compose-dev up
```

O Projeto backend pode ser iniciado pela IDE configurando o profile "dev" em variáveis de ambiente. Com o docker-compose-dev e o projeto iniciados, basta acessar a URL do [swagger](http://localhost:8080/swagger-ui/index.html) para ter acesso aos endpoints.

```sh
spring.profiles.active=dev
```

#### Dados utilizados para popular a base de dados

No startup do projeto foi adicionado [migrations](https://gitlab.com/marceloeugenios/deposito-service/-/blob/main/deposito-service/src/main/resources/db/migration/V1_0002__popular_tabelas.sql) para popular o banco de dados e estes ids deverão ser os que serão utilizados nos endpoints, caso o usuário não queira adicionar novas Marcas, Bebidas e Seções para realizar as movimentações, o que é livre já que os endpoints para essas gestões também foram disponibilizados.

#### Acessos:

- APIs backend: http://localhost:8080/swagger-ui/index.html
- KeyCloak: http://188.166.126.100:8080/auth/

### Autenticação

Utilize os dados de acesso baixo no endpoint de [autenticação](http://localhost:8080/swagger-ui/index.html#/autenticacao-controller/login).

| Usuário  | Senha |
| -------- | ----- |
| deposito | 12345 |

Como resposta você deverá receber o access_token para realizar as operações, basta cola-lo no swagger.

#### Requisitos do desafio

- Cadastro de histórico de entrada e saída de bebidas em caso de venda e recebimento:
  http://localhost:8080/swagger-ui/index.html#/movimentacao-controller/gerarMovimentacao

- Cadastro e consulta das bebidas armazenadas em cada seção com suas respectivas queries:
  http://localhost:8080/swagger-ui/index.html#/movimentacao-controller/extratoBebidaSecao

- Consulta do volume total no estoque por cada tipo de bebida:
  http://localhost:8080/swagger-ui/index.html#/movimentacao-controller/extratoPorTipoBebida

- Consulta dos locais (seções?) disponíveis de armazenamento de um determinado volume de bebida. (calcular via algoritmo). Vazios ou não cheios por tipo de bebida:
  http://localhost:8080/swagger-ui/index.html#/movimentacao-controller/secaoDisponivelBebidaVolume

- Consulta das seções disponíveis para venda de determinado tipo de bebida (calcular via algoritmo): Quais seções tem álcool e não álcool?
  http://localhost:8080/swagger-ui/index.html#/movimentacao-controller/secaoDisponivelTipoBebida

- Consulta do histórico de entradas e saídas por tipo de bebida e seção:
  http://localhost:8080/swagger-ui/index.html#/movimentacao-controller/extratoMovimentacao

> IMPORTANTE: Após executar o projeto não esqueça de parar os containers e limpar as imagens locais criadas.

#### Imagens:

- Backend: https://hub.docker.com/repository/docker/marceloeugenios/deposito-app - A publicação da imagem do backend no dockerhub é feito por meio de uma task do gradle
- PostgreSQL: https://hub.docker.com/repository/docker/marceloeugenios/deposito-db

## Ferramentas / Tecnologias utilizadas

##### Geral

- Readme - https://dillinger.io/
- Docker - Docker Compose
- DockerHub
- DigitalOcean (KeyCloak)
- Intellij
- Postman

##### Backend

- Java 11
- Spring Boot
- Gradle + Dockerhub integration
- Jacoco
- Lombok
- PostgreSQL
- Swagger - OpenAPI 3
- KeyCloak
- Flyway

##### Observações:

- Arquivo `docker-compose` foi deixado dentro do projeto backend pra ficar mais fácil sua localização
- Dockerfile utilizado para gerar a imagem do banco de dados https://gitlab.com/marceloeugenios/deposito-service/-/tree/main/db e script de inicialização.

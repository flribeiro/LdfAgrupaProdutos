# API para agrupamento de produtos para Marketplace
Desafio realizado por **Fabrício L. Ribeiro** como teste para seleção de desenvolvedor para o Luizalabs. 
Data de recebimento do desafio: 14/07/2018
Data de conclusão: 21/04/2018
 
## Fazendo o setup da aplicação
Para fazer o setup da aplicação, primeiramente certifique-se de ter o Java na versão 8 em seu ambiente:
      
      # java -version
      java version "1.8.0_172"
	  Java(TM) SE Runtime Environment (build 1.8.0_172-b11)
	  Java HotSpot(TM) 64-Bit Server VM (build 25.172-b11, mixed mode)
      
Depois disso, faça um clone do repositório git:

      # mkdir ldf
      # cd ldf
      # git clone https://github.com/flribeiro/LdfAgrupaProdutos.git

Já será possível executar a aplicação via linha de comando a partir do maven:

	  # mvnw spring-boot:run

Como alternativa, é possível executar a aplicação diretamente através do JAR file disponível:

	  # java -jar luizalabs-desafio-fabricio-0.0.1-SNAPSHOT.jar

A aplicação estará respondendo através do endereço `http://localhost:8080/ldf/grouping`.

## Implementação
API desenvolvida em Java com Spring Boot, utilizando como IDE o IntelliJ IDEA da JetBrains. Foi utilizado o Maven para o gerenciamento do projeto. 

## Arquitetura
O projeto foi implementado no modelo MVC, onde um controller (a classe `LdfController`) recebe as requisições HTTP e submete o processamento dos dados recebidos para uma camada service. Dado o fato de a API ser um serviço auxiliar, não houve necessidade de persistir os dados, portanto não há uma camada de persistência.

## Decisões de implementação:
* Para evitar inconsistências no resultado esperado, eu considerei que, ao receber produtos com EANs iguais e marcas diferentes, meu algoritmo consideraria como sendo de mais provável procedência o EAN cuja marca contivesse mais produtos na lista recebida;
* Também para evitar inconsistências, o método do service que faz validação dos produtos recebidos desconsidera aqueles produtos que porventura trazem na descrição uma marca diferente do atributo brand;
* Os testes de toda a classe do service (`ProductGroupingSvc`) estão implementados e resultando em OK.

## Possíveis melhorias:
Muito poderia ser melhorado se eu tivesse mais tempo. Exemplo:
* Eu pretendia implementar mensagens de erro a serem retornadas no atributo `errors` da classe Response, mas gastei muito tempo adequando a lógica para os algoritmos de ordenação e agrupamento, e decidi priorizar a responsividade;
* Também seria interessante implementar testes não só para a lógica da aplicação, mas também para o controller. Mas pra isso eu gostaria de ter implementado as respostas de erro a serem devolvidas pela API.

## Considerações finais:
Agradeço imensamente a oportunidade de participar do processo de seleção, e realizar este desafio. Reconheço minhas limitações técnicas por ser, como disse na entrevista, iniciante em desenvolvimento. Mas tenho muita sede de aprender. Realizar este pequeno projeto foi um desafio muito benéfico, pois a necessidade de pesquisa e uma boa demanda são os principais combustíveis para o meu aprendizado. Espero continuar crescendo aí, junto com vocês.
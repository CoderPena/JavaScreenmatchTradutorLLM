# Screenmatch – Tradução Automática de Sinopses de Séries

Este projeto é uma aplicação Java desenvolvida com Spring Boot para consumo de dados de séries a partir de uma API externa, com foco na **tradução automática de sinopses do inglês (EN) para o português brasileiro (PT-BR)**.

A aplicação processa informações como título, gênero, avaliação, número de temporadas, elenco e sinopse. Durante a criação do objeto de domínio, a sinopse original em inglês é traduzida automaticamente utilizando a **MyMemory Translation API**, uma solução gratuita baseada em requisições HTTP.

O projeto tem caráter educacional e foi desenvolvido para praticar conceitos como consumo de APIs REST, manipulação de JSON, uso de streams, organização em camadas e integração de serviços externos em aplicações Java com Spring Boot.

## Tecnologias utilizadas

- Java 17+
- Spring Boot 3.x
- JPA / Hibernate
- Jackson
- HttpClient (Java 11+)
- MyMemory Translation API

## Execução

1. Clonar o repositório
2. Abrir o projeto em uma IDE Java (ex.: IntelliJ IDEA)
3. Garantir Java 17+ configurado
4. Executar a classe `ScreenmatchApplication`

## Observações

- A tradução depende da disponibilidade da API MyMemory.
- Não é necessário token ou chave de API.
- Projeto voltado para fins de estudo.

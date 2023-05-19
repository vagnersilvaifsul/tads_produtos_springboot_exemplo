# Projeto Exemplo sobre API REST com Spring Boot

Este projeto demonstra a construção de API REST com a utilização do Spring Boot.

Lista de artefatos utilizados:
1. Segurança com jsonwebtoken: JWT (Javascript Web Token);
2. Lombok para Getters, Setters, toString, construtores;
3. Model Mapper para o mapeamento entre objetos das classes de modelo para DTO (Data Transfer Object);
4. lang3 para manipulação de strings; 
5. JPA para ORM;
6. MySQL para persitência em ambiente de produção;
7. MariaBD e H2 para desenvolvimento e testes;
8. Firebase Storage para armazenamento de imagens/arquivos;
9. JUnit para testes unitários;
10. Heroku para deploy do ambiente de produção.

Para executar o projeto no Heroku Local (como parte de um teste de integração), siga esses passos:
1. Baixe e instale o Heroku CLI, link: https://devcenter.heroku.com/articles/heroku-cli#install-the-heroku-cli
2. Abra um terminal na pasta raiz do projeto;
3. Execute o seguinte comando: heroku local -p 8080 -f ./proc_local/Procfile (-p é a porta do servidor e -f o local do Procfile que você utilizará para essa execução)
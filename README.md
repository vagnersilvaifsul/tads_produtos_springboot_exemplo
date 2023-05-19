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
3. Execute o seguinte comando: heroku local -p 8080 -f ./proc_local/Procfile (-p é a porta do servidor e -f o local do Procfile que você utilizará para essa execução).
4. Ou execute esse comando: heroku local -p 8080

O comando da linha 3 roda no Heroku Local com o Bando de Dados Local (o MariaDB) do Profile ativo no ./proc_local/Procfile, o dev. Ou seja, tudo localhost.
O comando da linha 4 roda no Heroku Local com o Banco de Dados Remoto (o MySQL) do Profile ativo no Procfile, o prod.

Observação:
Se a execução nesse ambiente estiver "ok" significa que a integração do Heroku com o MySQL está "ok". Logo, ao fazer o deploy do app no Heroku (na nuvem) estará garantida essa integração.
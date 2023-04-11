insert into role(id,nome) values (1, 'ROLE_USER');
insert into role(id,nome) values (2, 'ROLE_ADMIN');

insert into user(id,nome,email,login,senha) values (1,'User','user@gmail.com','user','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');
insert into user(id,nome,email,login,senha) values (2,'Admin','admin@gmail.com','admin','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');

insert into user_roles(user_id,role_id) values(1, 1);
insert into user_roles(user_id,role_id) values(2, 2);

INSERT INTO produto (id, nome, valor, descricao, situacao, estoque) VALUES
(1, 'Café', 12.90, 'Café em pó tradicional Igaçu lata 400g', 1, 100),
(2, 'Erva Mate', 13.90, 'Erva Mate Pura Folha 1kg', 1, 100),
(3, 'Chá Preto', 3.90, 'Prenda', 1, 100),
(4, 'Arroz', 16.90, 'Arroz Camil branco polido tipo 1 pacote 5kg', 1, 100),
(5, 'Feijão', 6.90, 'Feijão Tordilho pacote 1kg', 0, 100);
insert into role(id,nome) values (1, 'ROLE_USER');
insert into role(id,nome) values (2, 'ROLE_ADMIN');

insert into user(id,nome,email,login,senha) values (1,'User','user@gmail.com','user','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');
insert into user(id,nome,email,login,senha) values (2,'Admin','admin@gmail.com','admin','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');

insert into user_roles(user_id,role_id) values(1, 1);
insert into user_roles(user_id,role_id) values(2, 2);

INSERT INTO produtos (id, nome, descricao, estoque, valor_de_compra, valor_de_venda, situacao) VALUES
(1, 'Café', 'Café em pó tradicional Igaçu lata 400g', 100, 5.00, 10.00, true),
(2, 'Erva Mate', 'Erva Mate Pura Folha 1kg', 100, 5.00, 10.00, true),
(3, 'Chá Preto', 'Prenda', 100, 5.00, 10.00, true),
(4, 'Arroz', 'Arroz Camil branco polido tipo 1 pacote 5kg', 100, 5.00, 10.00, true),
(5, 'Feijão', 'Feijão Tordilho pacote 1kg', 100, 5.00, 10.00, true);
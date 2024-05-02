insert into perfis(id,nome) values (1, 'ROLE_ADMIN');
insert into perfis(id,nome) values (2, 'ROLE_USER');

insert into usuarios(id,nome,sobrenome,email,senha, is_confirmado) values (1,'Admin','do Sistema','admin@email.com','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe', true);
insert into usuarios(id,nome,sobrenome,email,senha, is_confirmado) values (2,'Usuario','do Sistema','user@email.com','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe', true);

insert into usuarios_perfis(usuarios_id,perfis_id) values(1, 1);
insert into usuarios_perfis(usuarios_id,perfis_id) values(2, 2);

insert into token_confirmacao_email(id, data_de_criacao, token, usuario_id) value (1,'2024-05-02 10:00','aecc73b7-dae5-4011-925d-e07633d9993f',1);

INSERT INTO produtos (id, nome, descricao, estoque, valor_de_compra, valor_de_venda, situacao) VALUES
(1, 'Café', 'Café em pó tradicional Igaçu lata 400g', 100, 5.00, 10.00, true),
(2, 'Erva Mate', 'Erva Mate Pura Folha 1kg', 100, 5.00, 10.00, true),
(3, 'Chá Preto', 'Prenda', 100, 5.00, 10.00, true),
(4, 'Arroz', 'Arroz Camil branco polido tipo 1 pacote 5kg', 100, 5.00, 10.00, true),
(5, 'Feijão', 'Feijão Tordilho pacote 1kg', 100, 5.00, 10.00, true);
INSERT INTO MARCA (id, nome, situacao) values (1, 'Marca Cerveja 1', 'ATIVO');
INSERT INTO MARCA (id, nome, situacao) values (2, 'Marca Cerveja 2', 'ATIVO');
INSERT INTO MARCA (id, nome, situacao) values (3, 'Marca Cerveja 3', 'ATIVO');

INSERT INTO MARCA (id, nome, situacao) values (4, 'Fanta', 'ATIVO');
INSERT INTO MARCA (id, nome, situacao) values (5, 'Aguas de Lindoia', 'ATIVO');
INSERT INTO MARCA (id, nome, situacao) values (6, 'Sucos do Vale', 'ATIVO');

INSERT INTO SECAO (id, nome, situacao, version) values (1, 'Secao 1', 'ATIVO', 0);
INSERT INTO SECAO (id, nome, situacao, version) values (2, 'Secao 2', 'ATIVO', 0);
INSERT INTO SECAO (id, nome, situacao, version) values (3, 'Secao 3', 'ATIVO', 0);

INSERT INTO SECAO (id, nome, situacao, version) values (4, 'Secao 4', 'ATIVO', 0);
INSERT INTO SECAO (id, nome, situacao, version) values (5, 'Secao 5', 'ATIVO', 0);

INSERT INTO BEBIDA (id, nome, bebida_tipo, situacao, _marca) values (1, 'Bebida Alcool 1', 'ALCOOLICA', 'ATIVO', 1);
INSERT INTO BEBIDA (id, nome, bebida_tipo, situacao, _marca) values (2, 'Bebida Alcool 2', 'ALCOOLICA', 'ATIVO', 2);
INSERT INTO BEBIDA (id, nome, bebida_tipo, situacao, _marca) values (3, 'Bebida Alcool 3', 'ALCOOLICA', 'ATIVO', 3);

INSERT INTO BEBIDA (id, nome, bebida_tipo, situacao, _marca) values (4, 'Bebida Nao Alcool 1', 'NAO_ALCOOLICA', 'ATIVO', 4);
INSERT INTO BEBIDA (id, nome, bebida_tipo, situacao, _marca) values (5, 'Bebida Nao Alcool 2', 'NAO_ALCOOLICA', 'ATIVO', 5);
INSERT INTO BEBIDA (id, nome, bebida_tipo, situacao, _marca) values (6, 'Bebida Nao Alcool 3', 'NAO_ALCOOLICA', 'ATIVO', 6);
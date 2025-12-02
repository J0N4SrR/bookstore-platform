-- ==================================================================================
-- 0. FUNCIONARIO - exigencia do CSU01
-- ==================================================================================
INSERT INTO funcionarios (id, nome, matricula) VALUES (UUID(), 'Administrador Livraria', 'FUNC-001');
-- ==================================================================================
-- 1. CLIENTES
-- ==================================================================================
INSERT INTO clientes (id, nome_completo, cpf, email, data_nascimento, telefone, senha) VALUES
('cli-001', 'Bruce Wayne', '444.555.666-77', 'bruce@wayne.com', '1972-02-19', '11999990004', 'batman'),
('cli-002', 'Diana Prince', '555.666.777-88', 'diana@themyscira.com', '1900-03-22', '11999990005', 'wonder'),
('cli-003', 'Clark Kent', '666.777.888-99', 'clark@dailyplanet.com', '1985-06-18', '11999990006', 'krypton'),
(UUID(), 'Natasha Romanoff', '777.888.999-00', 'natasha@shield.gov', '1984-11-22', '11999990007', 'widow'),
(UUID(), 'Peter Parker', '888.999.000-11', 'peter@midtown.edu', '2001-08-10', '11999990008', 'spidey'),
(UUID(), 'Wanda Maximoff', '999.000.111-22', 'wanda@vision.com', '1989-02-10', '11999990009', 'chaos'),
(UUID(), 'Stephen Strange', '000.111.222-33', 'stephen@sanctum.com', '1976-11-18', '11999990010', 'doctor'),
(UUID(), 'T Challa', '123.456.789-00', 'tchalla@wakanda.gov', '1980-11-29', '11999990011', 'panther'),
(UUID(), 'Carol Danvers', '234.567.890-11', 'carol@force.mil', '1965-04-24', '11999990012', 'marvel'),
(UUID(), 'Scott Lang', '345.678.901-22', 'scott@pym.tech', '1969-04-06', '11999990013', 'antman');

-- ==================================================================================
-- 1.1 ENDEREÇOS (Vinculados aos Clientes)
-- ==================================================================================

INSERT INTO enderecos (id, rua, numero, complemento, bairro, cidade, estado, cep, cliente_id) VALUES
('end-001', 'Rua da Caverna', '100', 'Subsolo', 'Mansão Wayne', 'São Paulo', 'SP', '01001-000', 'cli-001');

INSERT INTO enderecos (id, rua, numero, complemento, bairro, cidade, estado, cep, cliente_id) VALUES
('end-002', 'Av. das Amazonas', '500', 'Ilha', 'Paraíso', 'Rio de Janeiro', 'RJ', '20000-000', 'cli-002');

INSERT INTO enderecos (id, rua, numero, complemento, bairro, cidade, estado, cep, cliente_id) VALUES
('end-003', 'Rua do Planeta', '1', 'Redação', 'Centro', 'Porto Alegre', 'RS', '90000-000', 'cli-003');

-- ==================================================================================
-- 2. EDITORAS
-- IDs fixos para facilitar o vínculo
-- ==================================================================================
INSERT INTO editoras (id, nome, cnpj, telefone, email) VALUES
('edit-001', 'Companhia das Letras', '11.111.111/0001-11', '1133330001', 'contato@cialetras.com.br'),
('edit-002', 'HarperCollins', '22.222.222/0001-22', '1133330002', 'sac@harpercollins.com.br'),
('edit-003', 'Penguin Classics', '33.333.333/0001-33', '1133330003', 'info@penguin.com.br'),
('edit-004', 'Editora Rocco', '44.444.444/0001-44', '1133330004', 'faleconosco@rocco.com.br');

-- ==================================================================================
-- 3. AUTORES
-- ==================================================================================
INSERT INTO autores (id, nome, nacionalidade, data_nascimento) VALUES
('auth-001', 'Miguel de Cervantes', 'Espanhol', '1547-09-29'),
('auth-002', 'George Orwell', 'Britânico', '1903-06-25'),
('auth-003', 'J.R.R. Tolkien', 'Britânico', '1892-01-03'),
('auth-004', 'Franz Kafka', 'Tcheco', '1883-07-03'),
('auth-005', 'Jane Austen', 'Britânica', '1775-12-16'),
('auth-006', 'Antoine de Saint-Exupéry', 'Francês', '1900-06-29'),
('auth-007', 'Machado de Assis', 'Brasileiro', '1839-06-21'),
('auth-008', 'Fiódor Dostoiévski', 'Russo', '1821-11-11'),
('auth-009', 'Dante Alighieri', 'Italiano', '1265-05-21'),
('auth-010', 'Herman Melville', 'Americano', '1819-08-01'),
('auth-011', 'F. Scott Fitzgerald', 'Americano', '1896-09-24'),
('auth-012', 'Gabriel García Márquez', 'Colombiano', '1927-03-06'),
('auth-013', 'Alexandre Dumas', 'Francês', '1802-07-24'),
('auth-014', 'Mary Shelley', 'Britânica', '1797-08-30'),
('auth-015', 'Bram Stoker', 'Irlandês', '1847-11-08'),
('auth-016', 'Victor Hugo', 'Francês', '1802-02-26'),
('auth-017', 'Homero', 'Grego', '0800-01-01'), -- Data estimada AC
('auth-018', 'William Shakespeare', 'Britânico', '1564-04-23'),
('auth-019', 'Liev Tolstói', 'Russo', '1828-09-09');

-- ==================================================================================
-- 4. CATEGORIAS
-- ==================================================================================
INSERT INTO categorias (id, nome) VALUES
('cat-001', 'Romance'),
('cat-002', 'Ficção Científica'),
('cat-003', 'Fantasia'),
('cat-004', 'Clássico'),
('cat-005', 'Terror'),
('cat-006', 'Distopia'),
('cat-007', 'Aventura'),
('cat-008', 'Drama');

-- ==================================================================================
-- 5. LIVROS
-- Vinculados a Editoras
-- ==================================================================================

-- 1. Dom Quixote (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-001', 'CAPA_DURA', 'Dom Quixote', '978-001', 1032, 1605, 'O fidalgo que enlouqueceu lendo livros de cavalaria.', 15, 120.00, 'DISPONIVEL', 'edit-003');

-- 2. 1984 (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-002', 'BROCHURA', '1984', '978-002', 416, 1949, 'O Grande Irmão está de olho em você.', 50, 45.00, 'INDISPONIVEL', 'edit-001');

-- 3. O Senhor dos Anéis (Digital)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-003', 'DIGITAL', 'O Senhor dos Anéis', '978-003', 1200, 1954, 'Uma jornada épica pela Terra Média.', 1000, 39.90, 'FORADECIRCULACAO', 'edit-002');

-- 4. A Metamorfose (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-004', 'CAPA_DURA', 'A Metamorfose', '978-004', 96, 1915, 'Gregor Samsa acorda transformado em um inseto.', 20, 55.00, 'DISPONIVEL', 'edit-001');

-- 5. Orgulho e Preconceito (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-005', 'BROCHURA', 'Orgulho e Preconceito', '978-005', 424, 1813, 'O romance entre Elizabeth Bennet e Mr. Darcy.', 30, 29.90, 'DISPONIVEL', 'edit-003');

-- 6. O Pequeno Príncipe (Digital)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-006', 'DIGITAL', 'O Pequeno Príncipe', '978-006', 96, 1943, 'O essencial é invisível aos olhos.', 500, 15.00, 'DISPONIVEL', 'edit-004');

-- 7. Dom Casmurro (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-007', 'CAPA_DURA', 'Dom Casmurro', '978-007', 200, 1899, 'Capitu traiu ou não traiu Bentinho?', 25, 60.00, 'DISPONIVEL', 'edit-001');

-- 8. Crime e Castigo (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-008', 'BROCHURA', 'Crime e Castigo', '978-008', 592, 1866, 'O drama psicológico de Raskólnikov.', 10, 75.00, 'DISPONIVEL', 'edit-003');

-- 9. A Divina Comédia (Digital)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-009', 'DIGITAL', 'A Divina Comédia', '978-009', 800, 1320, 'A jornada pelo Inferno, Purgatório e Paraíso.', 200, 25.00, 'DISPONIVEL', 'edit-003');

-- 10. Moby Dick (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-010', 'CAPA_DURA', 'Moby Dick', '978-010', 656, 1851, 'A caça obsessiva à baleia branca.', 12, 110.00, 'DISPONIVEL', 'edit-003');

-- 11. O Grande Gatsby (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-011', 'BROCHURA', 'O Grande Gatsby', '978-011', 256, 1925, 'O sonho americano e seus excessos.', 40, 35.00, 'DISPONIVEL', 'edit-002');

-- 12. Cem Anos de Solidão (Digital)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-012', 'DIGITAL', 'Cem Anos de Solidão', '978-012', 448, 1967, 'A saga da família Buendía em Macondo.', 300, 42.00, 'DISPONIVEL', 'edit-004');

-- 13. A Revolução dos Bichos (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-013', 'CAPA_DURA', 'A Revolução dos Bichos', '978-013', 152, 1945, 'Uma sátira política em uma fazenda.', 35, 49.90, 'DISPONIVEL', 'edit-001');

-- 14. O Conde de Monte Cristo (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-014', 'BROCHURA', 'O Conde de Monte Cristo', '978-014', 1300, 1844, 'Uma história épica de vingança.', 8, 89.90, 'DISPONIVEL', 'edit-003');

-- 15. Frankenstein (Digital)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-015', 'DIGITAL', 'Frankenstein', '978-015', 240, 1818, 'O monstro criado pelo Dr. Victor Frankenstein.', 150, 12.90, 'DISPONIVEL', 'edit-002');

-- 16. Drácula (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-016', 'CAPA_DURA', 'Drácula', '978-016', 480, 1897, 'O vampiro mais famoso da história.', 22, 65.00, 'DISPONIVEL', 'edit-004');

-- 17. Os Miseráveis (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-017', 'BROCHURA', 'Os Miseráveis', '978-017', 1500, 1862, 'A redenção de Jean Valjean.', 5, 99.00, 'DISPONIVEL', 'edit-003');

-- 18. A Odisséia (Digital)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-018', 'DIGITAL', 'A Odisséia', '978-018', 300, -800, 'O retorno de Ulisses para Ítaca.', 200, 19.90, 'DISPONIVEL', 'edit-003');

-- 19. Hamlet (Capa Dura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-019', 'CAPA_DURA', 'Hamlet', '978-019', 160, 1603, 'Ser ou não ser, eis a questão.', 18, 40.00, 'DISPONIVEL', 'edit-002');

-- 20. Guerra e Paz (Brochura)
INSERT INTO livros (id, tipo_livro, titulo, isbn, num_paginas, ano_publicacao, resumo, quantidade_estoque, preco_base, status, editora_id) VALUES
('book-020', 'BROCHURA', 'Guerra e Paz', '978-020', 1200, 1869, 'A Rússia durante as guerras napoleônicas.', 2, 115.00, 'DISPONIVEL', 'edit-001');

-- ==================================================================================
-- 6. RELACIONAMENTOS
-- ==================================================================================

-- LIVRO_AUTOR
INSERT INTO livro_autor (livro_id, autor_id) VALUES
('book-001', 'auth-001'), -- Quixote -> Cervantes
('book-002', 'auth-002'), -- 1984 -> Orwell
('book-003', 'auth-003'), -- Anéis -> Tolkien
('book-004', 'auth-004'), -- Metamorfose -> Kafka
('book-005', 'auth-005'), -- Orgulho -> Austen
('book-006', 'auth-006'), -- Pequeno Príncipe -> Saint-Exupéry
('book-007', 'auth-007'), -- Casmurro -> Machado
('book-008', 'auth-008'), -- Crime -> Dostoiévski
('book-009', 'auth-009'), -- Divina -> Dante
('book-010', 'auth-010'), -- Moby -> Melville
('book-011', 'auth-011'), -- Gatsby -> Fitzgerald
('book-012', 'auth-012'), -- Cem Anos -> Márquez
('book-013', 'auth-002'), -- Revolução -> Orwell (Repetido)
('book-014', 'auth-013'), -- Monte Cristo -> Dumas
('book-015', 'auth-014'), -- Frankenstein -> Shelley
('book-016', 'auth-015'), -- Drácula -> Stoker
('book-017', 'auth-016'), -- Miseráveis -> Hugo
('book-018', 'auth-017'), -- Odisséia -> Homero
('book-019', 'auth-018'), -- Hamlet -> Shakespeare
('book-020', 'auth-019'); -- Guerra -> Tolstói

-- LIVRO_CATEGORIA
INSERT INTO livro_categoria (livro_id, categoria_id) VALUES
('book-001', 'cat-004'), ('book-001', 'cat-007'), -- Quixote: Clássico, Aventura
('book-002', 'cat-006'), ('book-002', 'cat-004'), -- 1984: Distopia, Clássico
('book-003', 'cat-003'), ('book-003', 'cat-007'), -- Anéis: Fantasia, Aventura
('book-004', 'cat-004'), ('book-004', 'cat-008'), -- Metamorfose: Clássico, Drama
('book-005', 'cat-001'), ('book-005', 'cat-004'), -- Orgulho: Romance, Clássico
('book-006', 'cat-003'), ('book-006', 'cat-008'), -- Príncipe: Fantasia, Drama
('book-007', 'cat-001'), ('book-007', 'cat-004'), -- Casmurro: Romance, Clássico
('book-008', 'cat-008'), ('book-008', 'cat-004'), -- Crime: Drama, Clássico
('book-009', 'cat-004'), -- Divina: Clássico
('book-010', 'cat-007'), ('book-010', 'cat-004'), -- Moby: Aventura, Clássico
('book-011', 'cat-001'), -- Gatsby: Romance
('book-012', 'cat-001'), ('book-012', 'cat-003'), -- Cem Anos: Romance, Fantasia
('book-013', 'cat-006'), -- Revolução: Distopia
('book-014', 'cat-007'), ('book-014', 'cat-004'), -- Monte Cristo: Aventura, Clássico
('book-015', 'cat-005'), ('book-015', 'cat-002'), -- Frank: Terror, Sci-Fi
('book-016', 'cat-005'), -- Drácula: Terror
('book-017', 'cat-004'), ('book-017', 'cat-008'), -- Miseráveis: Clássico, Drama
('book-018', 'cat-004'), ('book-018', 'cat-007'), -- Odisséia: Clássico, Aventura
('book-019', 'cat-008'), ('book-019', 'cat-004'), -- Hamlet: Drama, Clássico
('book-020', 'cat-004'), ('book-020', 'cat-008'); -- Guerra: Clássico, Drama
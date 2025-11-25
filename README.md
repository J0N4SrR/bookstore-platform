# 📚 Bookstore Platform

Projeto integrado do IFSP (Campus Bragança Paulista) baseado em um estudo de caso de **[Arquitetura de Software](https://drive.google.com/file/d/1Njpl5T0ELh5H1w6wIZy2VYUexdBFbj-Q/view?usp=sharing)** para um E-commerce de Livraria.

Este repositório implementa uma solução robusta, modular e testável,
atendendo aos requisitos de três disciplinas simultâneas:

-   **BRADECO** (Componentes)
-   **BRADWBK** (Back-end)
-   **QSW** (Qualidade de Software)

------------------------------------------------------------------------
## 📘 Análise e Design de uma Aplicação Web de E-commerce de Livraria

## 📝 Especificação do Sistema de Software

Com base nos **Atores**, **Regras de Negócio** e **Casos de Uso** presentes neste documento, realiza-se a análise e o design da aplicação Web de E-commerce de Livraria.

---

## 👥 Atores

- **Cliente**: representa os usuários externos interessados na compra de livros.
- **Funcionário**: representa os usuários internos responsáveis pela manutenção dos dados dos livros, incluindo o controle de estoque.
- **Sistema de Frete**: sistema/componente externo integrado para calcular o frete.
- **Sistema de Cartão**: sistema/componente externo para pagamento via cartão de crédito.
- **Sistema de Banco**: sistema/componente externo para pagamento via Pix.
- **Outros atores** podem ser identificados, caso necessário.

---

## 📜 Regras de Negócio

- **RN01:** O livro pode ter um dos seguintes status: **disponível**, **indisponível** ou **fora de circulação**.
- **RN02:** Existem três formatos de livro:
  - físico capa dura — **sem desconto**
  - físico brochura — **5% de desconto**
  - digital — **10% de desconto**
- **RN03:** Livros em circulação devem ter **estoque mínimo de 2 exemplares**. Ao atingir esse limite, um funcionário deve ser notificado.
- **RN04:** Formas de pagamento:
  - **Cartão de crédito:** até 3x sem juros ou **3% de desconto** à vista.
  - **Pix:** pagamento à vista com **8% de desconto**.
- **RN05:** O pedido pode ter os status: **EM PROCESSAMENTO**, **PAGAMENTO PENDENTE**, **CONFIRMADO**, **EM TRANSPORTE**, **FINALIZADO**.
- Outras regras podem ser adicionadas conforme necessário.

---

## 🧩 Casos de Uso

### **CSU01 — Pesquisar Livro**
Processo no qual o cliente pesquisa livros por título, autor, editora ou categoria.

Fluxo:
- Sistema exibe lista com título, autor, editora, categoria, status e preço.
- Cliente pode:
  - visualizar detalhes do livro (resumo, formato, páginas, ano),
  - adicionar o livro ao carrinho.
- Extensões:
  - **Exibir Detalhes**
  - **Adicionar ao Carrinho**

---

### **CSU02 — Efetuar Pedido**
Processo de finalização do pedido.

Etapas:
1. Cliente confirma os itens do carrinho (ao menos um item).
2. Se não cadastrado, deve preencher seus dados pessoais.
3. Cliente informa endereço para cálculo do frete.
4. Seleciona forma de pagamento (cartão ou Pix).
5. Pedido é finalizado:
   - estoque é atualizado,
   - carrinho é esvaziado.

Inclusões:
- **Atualizar Estoque**
- **Visualizar Carrinho**
- **Calcular Frete**
- **Efetuar Pagamento**

Especializações:
- **Pagar via Cartão**
- **Pagar via Pix**

---

### **CSU03 — Manter Cliente**
Manutenção dos dados do cliente: nome, CPF, data de nascimento, e-mail, telefone e um ou mais endereços.

- Necessário para novos clientes ou quando houver alteração cadastral.
- É extensão do caso de uso **Efetuar Pedido**.

---

### **CSU04 — Visualizar Pedido**
Cliente pode consultar:
- status do pedido atual,
- histórico de compras.

Para acessar, é necessário estar autenticado.

---

### **CSU05 — Manter Livro**
Manutenção dos dados do livro: título, ISBN, páginas, ano, categoria(s), formato, status, preço, resumo, estoque e dados do autor e da editora.

Extensões:
- **Manter Autor**
- **Manter Editora**

------------------------------------------------------------------------

## 🧩 Arquitetura do Projeto (Multi-Module Maven)

O sistema foi construído seguindo os princípios de **Clean
Architecture** e **Domain-Driven Design (DDD)**, dividido em módulos
independentes (Componentes) que são integrados pela aplicação principal.

``` text
bookstore-platform/
├── book-domain/           # Componente de Gestão de Livros (Catálogo/Estoque)
├── customer-domain/       # Componente de Gestão de Clientes
├── order-domain/          # Componente de Gestão de Pedidos (Core Business)
├── common-domain/         # (Opcional) Classes utilitárias compartilhadas
├── api-rest/              # Aplicação Spring Boot (API Gateway/Controller)
└── pom.xml                # Parent POM (Gerenciamento de Dependências)
```

------------------------------------------------------------------------

## 🚀 Tecnologias Utilizadas

-   **Linguagem:** Java 17
-   **Framework:** Spring Boot 3.1.5
-   **Persistência:** Spring Data JPA / Hibernate
-   **Banco de Dados:**
    -   *Produção:* MySQL 8.0 (Driver 8.4.0)
    -   *Testes:* H2 Database (Em memória)
-   **Testes:** JUnit 5 (Jupiter), Mockito, AssertJ
-   **Build:** Maven
-   **Documentação:** SpringDoc OpenAPI (Swagger UI)

------------------------------------------------------------------------

## 📘 Detalhes dos Módulos e Padrões de Projeto

### 1. 📚 `book-domain` (Catálogo)

Responsável pelas regras de negócio dos produtos.

-   **Entidades:** `Livro` (Abstrata), `Autor`, `Editora`, `Categoria`.
-   **Padrões Aplicados:**
    -   **Polimorfismo/Template Method (RN02):** Cálculo de preço
        dinâmico nas subclasses `LivroCapaDura`, `LivroBrochura` e
        `LivroDigital`.
    -   **Rich Domain Model:** Lógica de validação de estoque
        (`decrementarEstoque`, `verificarEstoqueMinimo`) encapsulada na
        entidade.
-   **Funcionalidades:** CRUD de Livros, Baixa de Estoque.

### 2. 👤 `customer-domain` (Cliente)

Responsável pela gestão de usuários.

-   **Entidades:** `Cliente` (Aggregate Root), `Endereco`.
-   **Funcionalidades:** Cadastro com validação de unicidade
    (CPF/Email), Busca por Email.

### 3. 🛒 `order-domain` (Pedidos)

O coração do sistema, orquestrando os outros módulos.

-   **Entidades:** `Pedido`, `ItemPedido`.
-   **Padrões Aplicados:**
    -   **Strategy Pattern (RN04):** Hierarquia `Pagamento` →
        `PagamentoPix` (8% desconto) e `PagamentoCartao` (3% à vista).
    -   **Factory Pattern:** Classe `PagamentoFactory` para decidir qual
        estratégia de pagamento instanciar.
    -   **Snapshot Pattern:** `ItemPedido` congela o preço do livro no
        momento da compra.
-   **Funcionalidades:** Efetuar Pedido (Transacional), Cálculo de
    Total, Histórico.

### 4. 🌐 `api-rest` (Apresentação)

Expõe os serviços de domínio para o mundo externo via HTTP.

-   **Controllers:** `LivroController`, `ClienteController`,
    `PedidoController`, `EditoraController`.
-   **DTOs:** Uso de Java Records para transferência de dados (ex:
    `LivroRequestDTO`, `DadosPedidoDTO`).
-   **Configuração:** Conexão com MySQL e carga inicial de dados
    (`data.sql`).

------------------------------------------------------------------------

## 🚦 Status das Entregas (Por Disciplina)

### 🔶 BRAARQS -- Arquitetura de Software

-   ✅ Diagramas de Casos de Uso, Classes e Sequência.
-   ✅ Modelo de Domínio rico e arquitetura em camadas.

### 🔶 BRADECO -- Desenvolvimento de Componentes

-   ✅ Separação física em módulos `.jar`.
-   ✅ Baixo acoplamento (Módulos `book` e `customer` não se conhecem).
-   ✅ Coesão alta (Pacotes organizados por Agregados).

### 🔶 BRADWBK -- Desenvolvimento Web Back-end

-   ✅ API RESTful completa.
-   ✅ CRUDs implementados para 4 entidades principais.
-   ✅ Relacionamentos 1:N e N:N mapeados com JPA.
-   ⏳ **Pendente:** Configuração NGINX e Testes de Carga (JMeter).

### 🔶 QSW -- Qualidade de Software

-   ✅ Testes de Unidade (Regras de Negócio e Fluxo).
-   ✅ Testes de Integração (Repositórios e Queries).
-   ✅ Uso de Técnicas: Partição de Equivalência, Valor Limite e Caminho
    de Exceção.
-   ✅ Cobertura de testes automatizados (JUnit + Mockito).

------------------------------------------------------------------------

## 🏗️ Como Executar o Projeto

### Pré-requisitos

-   Java 17+
-   Maven 3.8+
-   MySQL rodando na porta 3306 (com banco `bookstore_db` criado).

### 1. Compilar e Instalar os Módulos

Na raiz do projeto (`bookstore-platform`), execute:

``` bash
mvn clean install -DskipTests
```

### 2. Configurar Variáveis de Ambiente

-   `DB_PASSWORD`: Sua senha do MySQL.

### 3. Rodar a API

``` bash

cd api-rest

mvn spring-boot:run

```

A aplicação subirá na porta **8081**.

### 4. Acessar Documentação (Swagger)

👉 http://localhost:8081/swagger-ui.html

------------------------------------------------------------------------

## 🧪 Como Rodar os Testes

``` bash

mvn test

```

Ou para um módulo específico:

``` bash

cd book-domain

mvn test

```

------------------------------------------------------------------------

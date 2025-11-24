# 📚 Bookstore Platform

Projeto integrado do IFSP (Campus Bragança Paulista) baseado em um estudo de caso de **[Arquitetura de Software](https://drive.google.com/file/d/1Njpl5T0ELh5H1w6wIZy2VYUexdBFbj-Q/view?usp=sharing)** para um E-commerce de Livraria.

Este repositório implementa uma solução robusta, modular e testável,
atendendo aos requisitos de três disciplinas simultâneas:

-   **BRADECO** (Componentes)
-   **BRADWBK** (Back-end)
-   **QSW** (Qualidade de Software)

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

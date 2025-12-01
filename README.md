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
├── common-domain/         # Componente de Infraestrutura e Serviços Compartilhados (Frete/Email)
├── api-rest/              # Aplicação Spring Boot (API Gateway/Controller)
└── pom.xml                # Parent POM (Gerenciamento de Dependências)
```

------------------------------------------------------------------------

## 🚀 Tecnologias Utilizadas

-   **Linguagem:** Java 17
-   **Framework:** Spring Boot 3.1.5
-   **Persistência:** Spring Data JPA / Hibernate
-   **Web Server / Load Balancer:** NGINX
-   **Banco de Dados:**
    -   *Produção:* MySQL 8.0 (Driver 8.4.0)
    -   *Testes:* H2 Database (Em memória)
-   **Serviços e Integrações Externas**
    - *ViaCEP:* Utilizado para consulta de endereços e cálculo lógico de frete por região. 
    - *MailHog:* Ambiente local para captura e inspeção de e-mails enviados. 
    - *Spring Mail:* Estrutura configurada para envio de notificações, como alertas de estoque.

-   **Testes:** JUnit 5 (Jupiter), Mockito, AssertJ, JMeter (Performance) 
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
    
### 5.📦 Infraestrutura (Docker Cluster)

O ambiente de produção opera em um cluster containerizado utilizando **Docker Compose**:

- **Load Balancer:** NGINX (porta **8000**) distribuindo requisições entre as instâncias da API.  
- **Aplicação:** 2 réplicas da API (`api1`, `api2`) rodando **Spring Boot**.  
- **Banco de Dados:** MySQL 8.0 com persistência de dados.  

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
-   ✅ Configuração NGINX e Testes de Carga (JMeter).

### 🔶 QSW -- Qualidade de Software

-   ✅ Testes de Unidade (Regras de Negócio e Fluxo).
-   ✅ Testes de Integração (Repositórios e Queries).
-   ✅ Uso de Técnicas: Partição de Equivalência, Valor Limite e Caminho
    de Exceção.
-   ✅ Cobertura de testes automatizados (JUnit + Mockito).

### 🔶 BRADECO / BRADWBK / QSW
Todos os requisitos foram concluídos:
- Módulos  
- CRUDs  
- Relacionamentos  
- Testes de unidade  
- Infraestrutura com **Load Balancer NGINX** e múltiplas réplicas da API  


------------------------------------------------------------------------

## 🏗️ Como Executar o Projeto

> **Importante:** A arquitetura final exige Docker.\
> Desinstale ou pare qualquer MySQL/NGINX/Apache local para evitar
> conflitos de porta.

### ✅ Pré-requisitos

-   Java **17+**\
-   Maven **3.8+**\
-   Docker + Docker Compose

------------------------------------------------------------------------

### 1. Gerar o Executável (.jar)

Na raiz do projeto:

``` bash
mvn clean package -DskipTests
```

------------------------------------------------------------------------

### 2. Subir o Ambiente Clusterizado

Execute:

``` bash
sudo docker-compose up --build
```

Componentes iniciados: - `banco`\
- `api1`\
- `api2`\
- `nginx`

------------------------------------------------------------------------

### 3. Acessar a Aplicação

Entrada única via **NGINX (porta 8000)**:

📘 **Swagger UI:**\
👉 http://localhost:8000/swagger-ui.html

#### Exemplos de Endpoints:

  Método   Endpoint         Descrição
  -------- ---------------- ---------------
  GET      `/api/livros`    Listar livros
  POST     `/api/pedidos`   Criar pedido

------------------------------------------------------------------------

## 🧪 Testes de Carga (JMeter)

1.  Abra o **Apache JMeter**\

2.  Configure:

        Host: localhost
        Porta: 8000
        Path: /api/livros

3.  Execute o teste\

4.  Gere:

    -   *Summary Report*\
    -   *Graph Results*

------------------------------------------------------------------------

## 🛠️ Comandos Úteis

### Parar os containers:

``` bash
sudo docker-compose down
```

### Reset total (remove banco de dados):

``` bash
sudo docker-compose down -v
```

### Ver status:

``` bash
sudo docker-compose ps
```


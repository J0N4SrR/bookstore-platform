# 📚 bookstore-platform

Projeto integrado do IFSP baseado em um estudo de caso de Arquitetura de Software para um e-commerce de livraria.  
Inclui desenvolvimento baseado em componentes (BRADECO), API REST com Spring Boot e testes completos (BRADWBK e BRAQSOF).

---

## 🧩 Visão Geral

Este repositório reúne todos os artefatos, implementações e testes das disciplinas:

- **BRAARQS – Arquitetura de Software**  
  Estudo de caso + modelagem + diagramas + especificação.

- **BRADECO – Desenvolvimento de Componentes**  
  Arquitetura multimódulo, componentes reutilizáveis e `.jar`.

- **BRADWBK – Desenvolvimento Web Back-end**  
  API REST, CRUDs, relacionamentos, implantação com NGINX, testes funcionais e de performance.

- **BRAQSOF – Qualidade de Software**  
  Plano de teste completo (diagramas, casos de teste, GFC, GE, JUnit, cobertura, testes de sistema).

---

## 📦 Estrutura do Projeto

bookstore-platform/
├── dominio-comum/
├── componente-gestao-livro/
├── componente-gestao-cliente/
├── componente-gestao-pedido/
├── aplicacao-api-rest/
├── demonstrador-terminal/
└── docs/

---

## 🚀 Tecnologias Utilizadas

- **Java 17**  
- **Spring Boot**  
- **Spring Data JPA**  
- **Maven (multimódulo)**  
- **MySQL**  
- **NGINX (load balancing)**  
- **JUnit + Jacoco**  
- **Postman / Insomnia / JMeter**  
- **PlantUML / ObjectAid / Visual Paradigm**

---

## 📘 Objetivos de Cada Disciplina

### 🔶 BRAARQS – Arquitetura de Software
- Estudo de caso: E-commerce de Livraria  
- Diagramas de arquitetura  
- Modelo de domínio e casos de uso  
- Design orientado a camadas e componentes  

### 🔶 BRADECO – Desenvolvimento de Componentes
- Componentização (módulos `.jar`)  
- Separação de domínio, aplicação e infraestrutura  
- Serviços independentes por caso de uso  
- Demonstração de reuso via CLI  

### 🔶 BRADWBK – Desenvolvimento Web Back-end
- API REST com CRUDs  
- Relacionamentos 1:N e N:N  
- Implantação com NGINX  
- Testes funcionais das rotas  
- Testes de performance com JMeter  

### 🔶 BRAQSOF – Qualidade de Software
- Diagrama Arquitetural  
- Diagrama de Atividades + GFC  
- Diagrama de Estados + GE  
- Casos de teste (PE, AVL, Modelo, Caso de Uso)  
- Testes unitários (JUnit) + cobertura  
- Testes de sistema com evidências  
- Resumo final conforme Quadro 5a  

---

## 🏗️ Como Executar o Projeto

### 1. Clonar o repositório
```bash
git clone https://github.com/SEU_USUARIO/bookstore-platform.git

## ⚙️ Como Compilar

### Maven
```bash
./mvnw clean install

### 🚀 Como Rodar a API
cd aplicacao-api-rest
./mvnw spring-boot:run

### 📄 Swagger (Documentação da API)
http://localhost:8080/swagger-ui.html


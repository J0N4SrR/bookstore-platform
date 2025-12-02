
# 📘 Análise e Design de uma Aplicação Web de E-commerce de Livraria

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
5. Pedido é finalizado
   - estoque é atualizado

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

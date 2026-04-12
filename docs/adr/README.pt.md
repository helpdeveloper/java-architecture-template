# Registros de Decisões Arquiteturais (ADR)

Este diretório contém os **Registros de Decisões Arquiteturais (ADRs)** do projeto.  
Cada ADR documenta uma decisão significativa tomada durante o desenvolvimento do sistema, garantindo rastreabilidade e compreensão das escolhas arquiteturais.

📚 Leia em:
- 🇬🇧 [English](README.md)

---

## 📜 O que é um ADR?

Um **Registro de Decisão Arquitetural (ADR)** é um documento que captura uma decisão arquitetural importante, juntamente com seu contexto e consequências. Isso ajuda a manter a transparência e fornece uma referência para revisitar decisões no futuro.

---

## 🛠️ Processo para Adicionar um Novo ADR

Siga estes passos ao criar um novo ADR:

1. **Identifique a Decisão**  
   Defina claramente o problema ou desafio que levou à decisão.

2. **Crie um Novo Arquivo ADR**  
   Use a seguinte convenção de nomenclatura para o arquivo:  
   `NNNN-titulo-da-decisao.md`
    - `NNNN`: Um número sequencial com zeros à esquerda (ex.: `0001`).
    - `titulo-da-decisao`: Um título curto e descritivo em letras minúsculas, com palavras separadas por hífens.

3. **Use o Modelo**  
   Copie e personalize o modelo de ADR fornecido para estruturar o documento. Certifique-se de preencher todas as seções relevantes.

4. **Atualize a Lista de ADRs**  
   Adicione manualmente o novo ADR à lista neste arquivo `README.md` na seção **Lista de ADRs**.

## 🗂️ Lista de ADRs

| #    | Título                                                                        | Status    | Data       |
|------|------------------------------------------------------------------------------|-----------|------------|
| 0001 | [Adotar REST para Comunicação de API](0001-adopt-rest-for-api-communication.md) | Aceito    | 2024-12-31 |
| 0002 | [Usar MySQL como Banco de Dados Primário](0002-use-mysql.md)                      | Aceito    | 2024-12-31 |
| 0003 | [Usar Kafka para Streaming de Eventos](0003-use-kafka-for-event-streaming.md)  | Aceito    | 2024-12-31 |

---

## 🖋️ Modelo

Consulte o arquivo [Modelo de ADR](template.md) para criar novos ADRs.

---

## 📚 Referências

- [O que é um ADR?](https://adr.github.io/)
- [Modelos e Exemplos de ADR](https://github.com/joelparkerhenderson/architecture_decision_record)


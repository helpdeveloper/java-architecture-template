# Architectural Decision Records (ADR)

This folder contains the **Architectural Decision Records (ADRs)** for the project. 
Each ADR documents a significant decision made during the development of the system, 
ensuring traceability and understanding of architectural choices.

📖 Read this in:
- 🇧🇷 [Português](README.pt.md)

---

## 📜 What is an ADR?

An **Architectural Decision Record (ADR)** is a document that captures an important architectural decision, 
along with its context and consequences. This helps maintain transparency and provides a reference for revisiting decisions in the future.

---

## 🛠️ Process for Adding a New ADR

Follow these steps when creating a new ADR:

1. **Identify the Decision**  
   Clearly define the problem or challenge prompting the decision.

2. **Create a New ADR File**  
   Use the following naming convention for the file:  
   `NNNN-title-of-decision.md`
    - `NNNN`: A zero-padded sequential number (e.g., `0001`).
    - `title-of-decision`: A short, descriptive title in lowercase, with words separated by hyphens.

3. **Use the Template**  
   Copy and customize the provided ADR template to structure the document. Ensure all relevant sections are filled out.

4. **Update the List of ADRs**  
   Manually add the new ADR to the list in this `README.md` file under the **List of ADRs** section.

## 🗂️ List of ADRs

| #    | Title                                                                        | Status    | Date       |
|------|------------------------------------------------------------------------------|-----------|------------|
| 0001 | [Adopt REST for API Communication](0001-adopt-rest-for-api-communication.md) | Accepted  | 2024-12-31 |
| 0002 | [Use MySQL as the Primary Database](0002-use-mysql.md)                       | Accepted  | 2024-12-31 |
| 0003 | [Use Kafka for Event Streaming](0003-use-kafka-for-event-streaming.md)       | Accepted  | 2024-12-31 |

---

## 🖋️ Template

Refer to the [ADR Template](template.md) file for creating new ADRs.

---

## 📚 References

- [What is an ADR?](https://adr.github.io/)
- [ADR Templates and Examples](https://github.com/joelparkerhenderson/architecture_decision_record)

# 💼 Valor em Alta - Sistema de Gestão de Leilões

Projeto desenvolvido no âmbito da unidade curricular **Laboratório de Projeto II (LP2)** do curso de Engenharia Informática no **ISEP**, no ano letivo 2024/2025.

## 🧠 Objetivo

Este projeto tem como objetivo desenvolver uma aplicação para informatizar os processos da empresa **Valor em Alta**, especializada na realização de três tipos de leilões:

- Leilão Eletrónico
- Leilão por Carta Fechada
- Venda Direta

A aplicação utiliza o padrão arquitetural **MVC**, e ser implementada em **Java**. O sistema será baseado em **linha de comandos** e os dados são armazenados em **ficheiros CSV**.

---

## 📦 Funcionalidades

### 🧾 Gestão de Leilões
- Criar, listar, editar e remover leilões
- Tipos de leilão suportados:
  - Eletrónico (com valor mínimo e múltiplos de lance)
  - Carta Fechada (valores ocultos)
  - Venda Direta (primeiro a oferecer vence)

### 👥 Gestão de Clientes
- Registo e login de clientes
- Alteração de dados
- Visualização de leilões ativos, a terminar, e em que está inscrito
- Compra de lances (no caso de leilão eletrónico)

### 📊 Estatísticas
- Cliente com mais lances por leilão
- Tempo ativo dos leilões
- Média de idades dos clientes
- Percentagem de domínios de e-mail
- Leilões sem lances e mais participados

### 📧 Notificações (via e-mail) ### EM DESENVOLVIMENTO
- Boas-vindas ao cliente
- Informações de login, créditos e vitórias
- Relatório diário para o gestor em CSV com:
  - Leilões terminados e a iniciar
  - Clientes pendentes e ativos

---

## 🔐 Tipos de Utilizador

- **Cliente:** Acede a leilões, licita, compra créditos e edita seu perfil
- **Gestor (Sistema):** Confirma registos, regista produtos, vê estatísticas, configura templates

---

## 🛠️ Tecnologias Utilizadas

- Java (sem interface gráfica)
- Ficheiros CSV para persistência
- Padrão MVC
- Git + GitHub (Git Flow)

---

## 🔄 Organização do Repositório

- `master`: Branch principal (avaliada)
- `quality`: Código testado e estável
- `dev-nomealuno`: Desenvolvimento individual e por funcionalidade
- **Todos os commits devem ser feitos via Pull Request.**

---

## 📅 Planeamento - Sprints

- Sprint 1: 05/03 – 18/03
- Sprint 2: 19/03 – 02/04
- Sprint 3: 03/04 – 16/04 (1ª entrega)
- Sprint 4: 17/04 – 30/04
- Sprint 5: 01/05 – 13/05
- Sprint 6: 14/05 – 28/05
- Sprint 7: 29/05 – 17/06 (Entrega final)

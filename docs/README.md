<div align="center">

# RiskLens (Portfolio X-Ray)

**Enterprise-Grade Cross-Asset Portfolio Risk Analytics & Quantitative Engine**

[![License: Proprietary / Restricted](https://img.shields.io/badge/License-Proprietary%20%2F%20Non--Commercial-red.svg)](LICENSE.md)
[![Java](https://img.shields.io/badge/Backend-Java%2021%20%7C%20Spring%20Boot%203.3-007396?logo=openjdk&logoColor=white)](https://spring.io)
[![Nuxt](https://img.shields.io/badge/Frontend-Nuxt%203%20%7C%20Vue%203%20%7C%20TS-00DC82?logo=nuxt.js&logoColor=white)](https://nuxt.com)
[![Architecture](https://img.shields.io/badge/Architecture-Modular%20Monolith-blue.svg)](#--architecture--technology-stack)
[![Docker](https://img.shields.io/badge/Deployment-Docker%20Compose-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)

[Executive Summary](#-executive-summary) •
[Architecture & Tech Stack](#-architecture--technology-stack) •
[Quantitative Risk Engine](#-quantitative-risk-engine) •
[Security & Compliance](#-security--compliance) •
[Legal & Commercial Notice](#-legal--commercial-notice)

</div>

---

## 📌 Executive Summary

**RiskLens** (working title: *Portfolio X-Ray*) is an institutional-grade portfolio risk analytics platform designed for multi-asset investors holding traditional equities, ETFs, and cryptocurrencies (including self-custody on-chain wallets). Traditional portfolio trackers focus on accounting, whereas RiskLens bridges the gap by providing unified cross-asset risk metrics: portfolio-wide volatility, correlation matrices, Value-at-Risk (VaR), Monte Carlo stress simulations, and deterministic quantitative diagnostics[cite: 3, 6, 12].

> **Notice:** This repository is published strictly as a **technical portfolio demonstration** for code review, architecture evaluation, and recruitment purposes. All proprietary rights, quantitative models, and source code are fully protected[cite: 3, 5].

---

## 🛠 Architecture & Technology Stack

The system follows a strict **Modular Monolith** architecture built on Java 21 and Spring Boot 3[cite: 7]. Module boundaries are strictly enforced via ArchUnit and Spring Modulith, ensuring zero tight coupling and enabling seamless extraction into independent microservices in future scaling phases[cite: 7].

### **Backend Core (Java 21 / Spring Boot 3.3)**
* **Runtime & Concurrency:** Java 21 LTS utilizing virtual threads for high-throughput I/O with market data providers[cite: 9].
* **Architecture Pattern:** Clean N-tier layers (`api` $\rightarrow$ `application` $\rightarrow$ `domain` $\rightarrow$ `infrastructure`) implementing Ports & Adapters and Strategy patterns[cite: 7, 8].
* **Quantitative & Math Engine:** Apache Commons Math 3 for matrix operations, Cholesky decomposition, and statistical calculations[cite: 9, 12].
* **Security & Auth:** Spring Security 6, stateless JWT (access/refresh token rotation with family detection), and Sign-In with Ethereum (SIWE / EIP-4361)[cite: 9, 33].
* **Web3 Integration:** `web3j` for EVM wallet balance verification and ERC-20 token reads[cite: 6, 9].
* **Testing Suite:** JUnit 5, Mockito, Spring Boot Test, and Testcontainers backed by a real PostgreSQL 16 instance[cite: 9].

### **Frontend & Visualization (Nuxt 3 / TypeScript)**
* **Framework:** Nuxt 3, Vue 3 (Composition API), TypeScript, Pinia, TailwindCSS, and Vite[cite: 9].
* **Data Visualization:** Apache ECharts (`vue-echarts`) for rendering correlation heatmaps, portfolio allocation breakdowns, and Monte Carlo fan charts[cite: 9, 12].

### **Data & Infrastructure**
* **Database & Migrations:** PostgreSQL 16 managed via Flyway DDL versioning scripts[cite: 9, 13].
* **Caching & Rate Limiting:** Redis 7 implementing Cache-Aside patterns and Token Bucket rate limiters[cite: 9, 18, 37].
* **DevOps & CI/CD:** Multi-stage Docker builds, Docker Compose orchestration, and GitHub Actions pipelines[cite: 9].

---

## 📐 Quantitative Risk Engine

The risk computation engine guarantees deterministic reproducibility using immutable calculation snapshots (`risk_runs.input_snapshot` and fixed random seeds)[cite: 8, 12].

* **Calendar Alignment:** Merges equity trading schedules (~252 days/year) and crypto markets (365 days) via inner joins on trading dates[cite: 12].
* **Value-at-Risk (VaR):** Historical VaR (empirical quantile interpolation matching NumPy precision $\le 1\text{e-6}$) and Parametric VaR with horizon scaling[cite: 6, 12].
* **Monte Carlo Simulation:** Generates 10,000+ paths using Cholesky decomposition of the covariance matrix and Mersenne Twister random number generator[cite: 6, 12].
* **Stress Testing Scenarios:** Built-in historical shock simulations for major market crises:
  * **GFC 2008** (Global Financial Crisis)[cite: 12]
  * **COVID-19 2020** market crash[cite: 12]
  * **FTX 2022** liquidity collapse[cite: 12]

---

## 🔒 Security & Compliance

* **OWASP ASVS L1 Compliant:** Strict input validation via Bean Validation, parameterized JPA queries, secure CORS policies, and advanced HTTP security headers[cite: 7, 35].
* **Zero Custody Model:** Read-only analytics platform. No private keys, brokerage credentials, or asset custody capabilities are ever requested or stored[cite: 4, 35].
* **AI Guardrails:** LLM integrations (Claude API) operate under strict isolation rules — the AI only explains pre-calculated JSON metrics and is constrained by automated validators to prevent hallucinations and financial advice generation[cite: 4, 12, 31].

---

## ⚖️ Legal & Commercial Notice

**Copyright (c) 2026 RiskLens / Author. All Rights Reserved.**[cite: 3, 5]

### **Proprietary & Non-Commercial License**

1. **Portfolio Demonstration Scope:** Permission is granted solely to view, inspect, and evaluate the source code in this repository for personal, educational, or professional recruitment evaluation purposes.
2. **Prohibition of Commercial Exploitation:** No entity, organization, or individual is permitted to copy, modify, distribute, host, re-license, or commercialize this software (in whole or in part) as a SaaS product, revenue-generating service, or commercial offering without explicit written authorization from the copyright holder[cite: 5].
3. **Disclaimer:** RiskLens provides automated financial analytics and educational explanations only. It does not constitute investment advice, asset custody, or trade execution[cite: 3].

For licensing inquiries or enterprise partnership proposals, please contact the author directly.

---

## 📩 Contact

* **Project Specification:** RL-SRS-001 (v1.0)[cite: 3]
* **Author:** Full-Stack & Systems Engineer

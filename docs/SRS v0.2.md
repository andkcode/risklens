# Software Requirements Specification (SRS) v0.2 — AI-Powered Crypto & Stock Risk Analysis Platform

*Version 0.2 (Draft) · 23 November 2026 · Structure: IEEE Std 830 · Status: Draft for stakeholder review*

## Document Control

| Attribute | Value |
| --- | --- |
| Document title | Software Requirements Specification — AI-Powered Cryptocurrency & Stock Risk Analysis Platform (working title) |
| Version / status | 0.2 — Draft for stakeholder review (MVP baseline candidate) |
| Date | 23 November 2026 |
| Governing structure | IEEE Std 830-1998, applying requirement-quality practices from ISO/IEC/IEEE 29148 |
| Author / owner | \[To be completed by the document owner\] |
| Baseline identifiers | FR-01 to FR-25 (functional), US-01 to US-16 (user stories), NFR-01 to NFR-08 (non-functional) |
| Derived identifiers (informative) | BR (business rules), ASM (assumptions), CON (constraints), DEP (dependencies), OOS (out of scope), OI (open issues), EP (API endpoints) |

### Revision History

| Version | Date | Author | Description |
| --- | --- | --- | --- |
| 0.1 | 2026-10-28 | \[Author\] | Initial draft: FR-01 to FR-25, US-01 to US-16, NFR-01 to NFR-08, and supplementary specifications |
| 0.2 | 2026-11-23 | \[Author\] | Added the Role Model Extension (§1.3.1), Extended Glossary (§1.6.1), API Endpoint Catalog (§5.5), Roles and Permissions Matrix (§5.6), FR-to-Endpoint Traceability Matrix (§6.3), OpenAPI 3.1 skeleton (Appendix D), Delta Log (Appendix E), and register rows ASM-10 to ASM-13, CON-08 to CON-09, DEP-08, OOS-16, and OI-18 to OI-24. FR, US, and NFR wording is unchanged. All other v0.1 text is preserved, except the metadata and index edits recorded in Appendix E. |

## Document Conventions & IEEE 830 Compliance Map

**Requirement language.** “Shall” marks a mandatory, verifiable requirement; “should” a strong recommendation; “may” or “could” an optional capability. Functional requirements carry a MoSCoW priority: **Must** (mandatory for the MVP release), **Should** (important; the release is viable without it, but a workaround is needed), **Could** (desirable if capacity allows), **Won’t** (explicitly excluded from this release; see §1.5).

**Normative vs. informative.** Requirement tables (§2), user stories and scenarios (§3), and non-functional requirements (§4) are *normative*. Anything labelled *Design note*, *Proposed*, or *Derived*, and all of §5–§8, is *informative* and does not change the baseline.

**Verification methods.** **T** = Test, **I** = Inspection, **A** = Analysis, **D** = Demonstration.

**Quoted UI strings** are English reference strings; they are localized per FR-21.

**Standard note.** IEEE 830-1998 has been superseded by ISO/IEC/IEEE 29148 (2011, revised 2018). This SRS keeps the IEEE 830 structure as the project brief requires, and applies the 29148 quality attributes (unambiguous, verifiable, consistent, traceable) to every requirement.

| IEEE 830 section | Location in this SRS |
| --- | --- |
| 1.1 Purpose; 1.2 Scope | §1.1, §1.2, §1.5 |
| 1.3 Definitions, acronyms, abbreviations | §1.6, §1.6.1 |
| 1.4 References; 1.5 Overview | §1.7, §1.8 |
| 2.1 Product perspective | §1.2, §5.1 |
| 2.2 Product functions | §1.2, §2 |
| 2.3 User characteristics | §1.3, §1.3.1, §5.6 |
| 2.4 Constraints; 2.5 Assumptions and dependencies | §1.4 |
| 2.6 Apportioning of requirements | §1.5, §2.1 |
| 3.1 External interface requirements | §5.2, §5.5 |
| 3.2 Functional requirements | §2, §3 |
| 3.3 Performance requirements | §4 (NFR-01, NFR-07) |
| 3.4 Logical database requirements | §5.3 |
| 3.5 Design constraints | §1.4, §4 |
| 3.6 Software system attributes | §4 |
| 3.7 Organizing the specific requirements | §2, §3 (by module and feature), §6 |
| Appendices | §8 |

## 1. Document Skeleton & Introduction (IEEE 830 Section 1 & 2 Alignment)

### 1.1 Document Purpose & Project Vision

**Purpose.** This SRS defines the functional, non-functional, and behavioural requirements of the Minimum Viable Product (MVP) of an AI-powered cryptocurrency and stock risk analysis application (“the System”). It is the reference baseline for product management, architecture, engineering, QA, security, and DevOps, and the source from which design, estimation, test planning, and release acceptance are derived.

**Project vision.** Retail investors who hold volatile crypto assets alongside traditional equities rarely have tooling that shows how risky their *combined* holdings are. Institutional-grade metrics such as volatility, concentration, and drawdown are hard to compute and harder to interpret. The System closes this gap by pairing deterministic quantitative risk metrics with LLM-generated, plain-language explanations, exposure warnings, and actionable recommendations, so that a non-expert can understand portfolio risk within minutes and act on it.

**Core objective.** Enable a registered user to (1) create portfolios, (2) record stock and crypto positions, (3) view live valuation and PnL, (4) run an AI-assisted risk analysis, and (5) generate, export, and archive a consolidated risk report — securely, reliably, and in English or Russian.

**Business value.**

| Value driver | Description | Key requirements |
| --- | --- | --- |
| Unified cross-asset risk view | One place to assess crypto and equity exposure together | FR-05, FR-09, FR-12 |
| Explainable AI insights | Natural-language explanations lower the expertise barrier, while figures stay deterministic and auditable | FR-12, FR-13, FR-15 |
| Early warning | User-defined drawdown and concentration thresholds | FR-14 |
| Decision-ready reporting | Consolidated reports, PDF export, and a historical archive | FR-16, FR-17, FR-18 |
| Trust and security | Hardened authentication, input validation, and audit logging | FR-19, FR-20, FR-22, NFR-02 |
| Platform foundation | Documented REST API, i18n, and a stateless, horizontally scalable backend | FR-21, FR-23, NFR-08 |

**MVP release criteria (proposed).** The MVP is releasable when (a) all *Must* functional requirements and all *Must* user-story scenarios pass acceptance testing, (b) NFR-01 to NFR-08 are met or carry a recorded waiver, and (c) every High-severity open issue in §7 is resolved or consciously accepted by the product owner.

### 1.2 Project Scope (In-Scope Modules)

The MVP comprises five functional modules plus cross-cutting platform capabilities, delivered as a browser-based web client, a stateless backend REST API, a relational data store, a cache, and integration adapters for external market-data and LLM services.

| Module | The System **will** | The System **will not** (MVP) | FRs | User stories |
| --- | --- | --- | --- | --- |
| **Auth** | Register users by email and password; authenticate and issue an Access JWT and Refresh Token; terminate sessions; recover passwords through a time-limited emailed token; optionally support TOTP-based MFA | Offer social/SSO login, passwordless sign-in, or administrator-provisioned accounts | FR-01 to FR-04 | US-01, US-02, US-03, US-12 |
| **Portfolio** | Create, read, update, and delete portfolios of stocks and cryptocurrencies; add assets with identifier, quantity, purchase price, and timestamp; fetch and cache market quotes; tag portfolios by strategy | Import holdings from brokers, exchanges, or wallets; hold funds or private keys; value assets in several currencies; share portfolios | FR-05 to FR-08 | US-04, US-05, US-13 |
| **Position** | Calculate real-time valuation and Unrealized/Realized PnL; record partial or full closures; show target vs. actual allocation | Execute orders; compute taxes; model fees, dividends, or corporate actions | FR-09 to FR-11 | US-06, US-07 |
| **Risk** | Aggregate holdings; compute volatility, concentration, and historical drawdown; obtain LLM-generated risk descriptions, warnings, and recommendations; manage alert thresholds; keep an audit trail of analyses | Place or rebalance trades; give personalised regulated investment advice; forecast prices or promise outcomes | FR-12 to FR-15 | US-08, US-09, US-14 |
| **Report** | Generate risk summary reports with structured text and asset-distribution charts; export them to PDF; archive them | Send scheduled or emailed reports; produce tax or regulatory filings | FR-16 to FR-18 | US-10, US-11, US-15 |
| **Cross-cutting** | Audit logging; payload sanitization and validation; English/Russian localization; secure password hashing; documented REST API; light/dark themes; rate limiting | Provide an administrative web console (administrators work through the logging pipeline and deployment tooling) | FR-19 to FR-25 | US-16 |

**System context.** The System serves end users through the web client and depends on four external parties: market-data provider(s) for stock and crypto quotes and history, an LLM provider for natural-language risk analysis, an email delivery service for password recovery, and a centralized logging pipeline for audit and error events.

### 1.3 User Roles & Personas

| Role | Description | Technical skill level | Primary goals | Stories |
| --- | --- | --- | --- | --- |
| **R-01 Retail Investor / User** | Self-registered individual who holds stocks and/or cryptocurrencies and wants to understand and monitor portfolio risk | **Low to moderate.** Comfortable with web applications and basic investing vocabulary (positions, PnL); no quantitative-finance or programming background assumed. A small subset of power users may consume the REST API (FR-23) and are moderately to highly technical | Track holdings and PnL; understand risk in plain language; set alert thresholds; produce, export, and archive reports | US-01 to US-15 |
| **R-02 System Administrator** | Operator responsible for deployment, monitoring, security auditing, and incident troubleshooting | **High.** Proficient with Linux, containers (Docker/Compose), log aggregation, monitoring, and security hygiene | Audit access events; diagnose errors; keep the platform available and horizontally scalable | US-16; NFR-06 to NFR-08 |

**Persona snapshots.** *R-01* is a mixed-asset investor who holds a handful of equities and several crypto assets on different platforms and tracks them in spreadsheets; they want a fast answer to “Am I over-exposed?” without learning financial jargon, and need plain-language explanations, visible data freshness, and exportable evidence. *R-02* is an experienced DevOps/SRE engineer who needs structured logs free of secrets, a one-command local environment, and stateless services that scale behind a load balancer.

**Role aliases.** The role labels used in the user stories — *new user*, *registered user*, *user who forgot my password*, *investor*, *trader*, *risk-conscious investor*, and *authenticated user* — all refer to **R-01**; *System Administrator* is **R-02**.

**Access model.** Users can reach only the portfolios, positions, analyses, reports, and thresholds they own (ASM-07). Administrators do not browse user business data through the application in the MVP. **External (non-human) actors:** market-data provider(s), LLM provider, email delivery service, and centralized logging pipeline.

#### 1.3.1 Role Model Extension (v0.2)

*Status: **Derived** from §1.3 and the Jira task for SRS v0.2; role R-03 is **Proposed**. Both are informative until the product owner accepts them.*

SRS v0.2 adds role-based access control (RBAC) on top of the ownership rule of ASM-07 (see §5.6). The role values used by the API map to the roles of §1.3 as follows.

| API role value | SRS role | Status | Source |
| --- | --- | --- | --- |
| USER | **R-01 Retail Investor / User** | Derived | §1.3 |
| ANALYST | **R-03 Analyst** | **Proposed** | Jira task; neither v0.1 nor the task defines the role (OI-18) |
| ADMIN | **R-02 System Administrator** | Derived | §1.3 |

The row below extends the §1.3 table in the same layout.

| Role | Description | Technical skill level | Primary goals | Stories |
| --- | --- | --- | --- | --- |
| **R-03 Analyst (Proposed)** | Role value ANALYST named in the Jira task. Its duties, data scope, and assignment are undefined (OI-18); this SRS assigns it no business duty | Not defined (OI-18) | Not defined (OI-18) | None |

*Role rules (Proposed, informative)*

- The Backend API shall assign every authenticated caller exactly one of the role values USER, ANALYST, or ADMIN (ASM-13).
- The Backend API shall apply an ownership check (ASM-07) to every request whose permission in §5.6 is Own.
- The Backend API shall answer a caller with status 403 on every operation whose permission for the caller’s role in §5.6 is Deny.
- Role assignment takes place outside the API in Phase 1 (ASM-13, OI-19).
- The role aliases of §1.3 are unchanged: every role label in the user stories still refers to R-01.

### 1.4 Business & Technical Assumptions, Constraints, and Dependencies

#### Assumptions

| ID | Assumption | Impact if invalid |
| --- | --- | --- |
| ASM-01 | Users enter holdings manually; there is no broker, exchange, or wallet synchronization in the MVP | Data accuracy depends on user input; automated import becomes a Phase 2 driver |
| ASM-02 | Portfolios are valued in a single base currency (US dollars), consistent with the dollar-denominated examples in US-05 | Multi-currency support needs FX data sources and data-model changes |
| ASM-03 | External sources provide enough price history for stocks and crypto to compute volatility and drawdown | Risk metrics degrade or are unavailable for illiquid or newly listed assets |
| ASM-04 | Only aggregated quantitative metrics, never personally identifying data, are sent to the LLM provider (FR-12) | Privacy and regulatory exposure; data-processing terms must be re-assessed |
| ASM-05 | LLM output is advisory and non-deterministic; deterministic calculations remain the source of truth for every displayed figure | Inconsistent or hallucinated figures would erode user trust |
| ASM-06 | Users run the latest stable Chrome or Firefox with JavaScript enabled (NFR-05) | Rendering defects on other browsers are accepted |
| ASM-07 | Authorization is ownership-based: a user can read and modify only their own data | Role-based access control would have to be introduced |
| ASM-08 | Timestamps are stored in UTC and rendered in the user’s locale and time zone | Inconsistent timestamps in valuation and reports |
| ASM-09 | The 10-portfolio limit (US-04) is a business rule; the 50-position workload (NFR-01) is a test baseline, not a hard cap per portfolio | A per-portfolio position cap would need to be defined |
| ASM-10 | **Proposed.** The endpoint paths, methods, status codes, identifiers (opaque strings), cookie name (`refresh_token`), pagination parameters (`page` default 1; `pageSize` default 20, maximum 100), and the 100-character portfolio-name limit in §5.5 and Appendix D are design proposals that realise FR-23 within CON-08 | Interface names and limits change; clients and tests written against v0.2 need rework |
| ASM-11 | **Proposed.** The Backend API answers an unauthenticated call to a protected endpoint with 401, a role without permission with 403, and a request for another user’s resource with 404, so that the existence of the resource is not disclosed | Status codes in §5.5, the legend of §5.6, and Appendix D change; a 403 would reveal that the resource exists |
| ASM-12 | **Proposed.** A risk analysis is delivered in two stages: the create call returns the deterministic metrics, and the LLM narrative is read afterwards from the analysis resource within the 10-second bound (OI-03) | If one synchronous response is required, the 2-second and 10-second targets conflict again (OI-03) and EP-22 and EP-24 are redesigned |
| ASM-13 | **Proposed.** The role of an authenticated user (USER, ANALYST, or ADMIN) is a user attribute assigned outside the API and carried in the Access JWT; v0.1 defines no role attribute (§5.3) and no role-assignment function (OI-19) | A role change takes effect only after the 15-minute Access JWT expires (NFR-02); otherwise every request needs a data-store lookup that affects NFR-01 and NFR-08 |

#### Constraints

| ID | Constraint | Source |
| --- | --- | --- |
| CON-01 | **Regulatory.** The System provides informational analytics, not personalised investment advice. Applicable data-protection (e.g., GDPR), financial-services, and crypto-asset rules must be reviewed by qualified legal counsel before public launch, and the UI must be able to display disclaimers next to AI output | Project brief §1.4; OI-17 |
| CON-02 | **Provider quotas.** Market-data and LLM calls are subject to third-party rate limits and cost; caching (FR-07) and timeouts (US-08) must keep usage within quota | FR-07, US-08 |
| CON-03 | **Deployment.** The full stack (frontend, backend API, database, cache) starts with a single `docker compose up`; the backend is stateless | NFR-06, NFR-08 |
| CON-04 | **Client.** Support is limited to the latest stable Google Chrome and Mozilla Firefox | NFR-05 |
| CON-05 | **Localization.** English and Russian | FR-21 |
| CON-06 | **Security baseline.** bcrypt or Argon2 password hashing, 15-minute Access JWT, password policy, and payload validation | FR-20, FR-22, NFR-02, NFR-03 |
| CON-07 | **Quality and service targets.** p95 below 2.0 s under the reference workload, at least 60% backend test coverage, and at least 99.5% monthly availability | NFR-01, NFR-04, NFR-07 |
| CON-08 | **API contract.** The API uses REST over HTTPS with JSON bodies, the versioned base path `/api/v1`, `application/problem+json` errors (RFC 9457), and the 15-minute Access JWT as Bearer token; OI-14 leaves the external access model open | FR-23 design note; NFR-02; OI-14 |
| CON-09 | **Authorization layering.** Role permissions (§5.6) apply on top of ownership checks (ASM-07), and the Backend API evaluates both on every protected request without in-process session state | ASM-07; NFR-08; §5.6 |

#### Dependencies

| ID | Dependency | Principal risk | Design intent |
| --- | --- | --- | --- |
| DEP-01 | Third-party market-data feeds (quotes and history for stocks and crypto) | Outages, latency, delayed data, licensing and quota limits, inconsistent symbol coverage | TTL cache; last-known-price fallback with a “Data delayed / cached” indicator (US-06); provider-adapter abstraction; quota monitoring |
| DEP-02 | LLM provider API (FR-13) | Unavailability, latency, rate limits, model drift, cost | 10-second timeout with retry prompt (US-08); provider-agnostic adapter; versioned prompts; schema-validated responses |
| DEP-03 | Transactional email service (FR-03) | Delivery delays or failures | Queued sending with retries; neutral user-facing messaging |
| DEP-04 | Docker Engine and Compose (NFR-06) | Environment variance between developer machines | Pinned image versions; documented prerequisites; sample environment file |
| DEP-05 | Centralized logging pipeline (FR-19, US-16) | Log loss; accidental secret leakage | Redaction before write; buffered shipping |
| DEP-06 | PDF rendering component (FR-17) | Chart fidelity; Cyrillic glyph coverage for Russian reports | Server-side rendering with embedded Cyrillic-capable fonts |
| DEP-07 | TOTP-compatible authenticator apps and libraries (FR-04) | Clock drift between server and device | RFC 6238 implementation with a tolerance window |
| DEP-08 | OpenAPI 3.1-compatible validator and documentation renderer (FR-23) | Differences in 3.1 (JSON Schema 2020-12) support between tools; a skeleton that one tool accepts and another rejects | Pinned tool versions; validation of Appendix D with the selected tool before publication |

### 1.5 Out of Scope (Phase 2 & Beyond)

The following items are explicitly excluded from the MVP (MoSCoW “Won’t” for this release). Each exclusion is a deliberate scoping decision, not an oversight.

| ID | Excluded capability | Rationale |
| --- | --- | --- |
| OOS-01 | Direct exchange or broker API **automated trading** (order placement and execution) | Regulatory, security, and liability complexity; the MVP analyses risk but never trades |
| OOS-02 | **Social trading network** features (following, copy trading, public profiles, leaderboards, shared feeds) | Not core to risk analysis; adds moderation and privacy scope |
| OOS-03 | **Automated portfolio rebalancing executions** (the System only recommends and never executes) | Depends on OOS-01; FR-13 limits output to recommendations |
| OOS-04 | Automated holdings import through exchange, broker, or wallet integrations | Manual entry only (ASM-01) |
| OOS-05 | Custody and payments: holding funds, deposits, withdrawals, or storing wallet private keys, seed phrases, or exchange API secrets | Security liability and regulatory scope |
| OOS-06 | Personalised regulated investment advice and suitability or appropriateness assessments | CON-01 |
| OOS-07 | Asset classes beyond stocks and cryptocurrencies (options, futures, forex, bonds, commodities) | Stocks and crypto only (FR-05) |
| OOS-08 | Multi-currency valuation and FX conversion | ASM-02 |
| OOS-09 | Tax calculation, dividends and staking income, fees, and corporate actions (splits, mergers) | Data-model and accuracy complexity; see OI-15 |
| OOS-10 | Native mobile applications, and browsers other than the latest stable Chrome and Firefox | NFR-05 |
| OOS-11 | Social/OAuth/SSO login and passwordless authentication | Auth is email and password only (FR-01, FR-02) |
| OOS-12 | Administrative web console or user-management UI | US-16 is satisfied through logging |
| OOS-13 | Proprietary model training, hosting, or fine-tuning | The MVP consumes an external LLM through its API |
| OOS-14 | Shared portfolios, advisor–client roles, and other multi-user collaboration | Single-owner data model (ASM-07) |
| OOS-15 | Push/SMS notifications and scheduled or emailed report delivery | Alert notification channel is pending (OI-04) |
| OOS-16 | Endpoints for administrative user management, role assignment, and cross-user access to business data by ANALYST or ADMIN | The §1.3 Access model, OOS-12, and OOS-14; see OI-18 and OI-19 |

### 1.6 Definitions, Acronyms, and Abbreviations

| Term | Definition |
| --- | --- |
| **Access JWT** | Short-lived JSON Web Token presented as a Bearer token to authorize API requests; valid for 15 minutes (NFR-02) |
| **AI risk module** | Backend component that prepares quantitative metrics, calls the LLM, and validates and normalizes the response |
| **API** | Application Programming Interface |
| **Argon2** | Memory-hard password-hashing function, winner of the 2015 Password Hashing Competition, specified in RFC 9106 |
| **Asset** | Tradable instrument; a stock or a cryptocurrency in the MVP |
| **Audit log** | Structured, append-only record of security-relevant actions and system errors |
| **bcrypt** | Adaptive password-hashing function based on the Blowfish cipher, with a built-in per-hash salt |
| **Cache** | Fast temporary store for market quotes and rate-limit counters |
| **Concentration risk** | Risk arising from a large share of portfolio value sitting in one asset, one asset class, or a group of correlated assets |
| **CRUD** | Create, Read, Update, Delete |
| **Drawdown** | Decline from a peak to a subsequent trough in value; *maximum drawdown* is the largest such decline in a period |
| **Given/When/Then** | Gherkin-style notation for acceptance scenarios: precondition, action, expected outcome |
| **HTTP-only cookie** | Cookie that browser scripts cannot read, which limits token theft through XSS |
| **i18n** | Internationalization: designing software so that it can be adapted to several languages and locales |
| **JWT** | JSON Web Token (RFC 7519) |
| **LLM** | Large Language Model |
| **MFA** | Multi-Factor Authentication |
| **MoSCoW** | Prioritization scheme: Must, Should, Could, Won’t |
| **MVP** | Minimum Viable Product |
| **NFR** | Non-Functional Requirement |
| **p95** | 95th-percentile latency: 95% of requests complete within this time |
| **PnL** | Profit and Loss. *Unrealized* PnL is the gain or loss on an open position at the current market price; *Realized* PnL is the gain or loss locked in by closing all or part of a position |
| **Portfolio** | User-defined collection of positions |
| **Position** | A holding of one asset in a portfolio, with quantity, purchase price, and purchase timestamp; open until fully closed |
| **Quote** | Current or last-known market price of an asset from an external feed |
| **Refresh Token** | Longer-lived credential, kept in a secure HTTP-only cookie, used to obtain new Access JWTs |
| **REST** | Representational State Transfer, an architectural style for HTTP APIs |
| **SQLi** | SQL injection |
| **SRS** | Software Requirements Specification |
| **Stateless** | Design in which no per-user session state is held inside a service instance between requests, so any instance can serve any request |
| **TOTP** | Time-based One-Time Password (RFC 6238) |
| **UTC** | Coordinated Universal Time |
| **VaR** | Value at Risk: a statistical estimate of the maximum loss over a time horizon at a given confidence level. Not mandated by FR-12 in v0.1; a candidate metric for later releases |
| **Volatility** | Standard deviation of asset or portfolio returns over a window, usually annualized |
| **XSS** | Cross-Site Scripting |

#### 1.6.1 Extended Glossary (v0.2)

*Status: **Derived**. The §1.6 table above is unchanged. Where a term overlaps a §1.6 term, its Basis column says “Refines §1.6” and the row extends the §1.6 definition without contradicting it. Numeric examples are illustrative. Terms tagged “Reserved for future releases” are not used by any Phase 1 requirement.*

| Term | Definition | Status in this SRS | Used in | Basis |
| --- | --- | --- | --- | --- |
| **Alert threshold** | User-configured limit, stored per portfolio, on either maximum portfolio drawdown or asset concentration; a computed metric beyond the limit is a breach. Value is a percentage between 0 and 100 (US-14 P3). How a breach is notified is open (OI-04). | Used in Phase 1 | FR-14; US-14; §5.3; EP-25 to EP-28 | FR-14; US-14; §5.3 (Alert threshold) |
| **Annualization** | Scaling of a periodic statistic, such as the standard deviation of returns, to a one-year basis so that figures from different asset classes are comparable. For daily log returns, σ_annual = σ_daily × √N, where N is the number of observation periods per year (about 252 for equities and 365 for crypto; FR-12 design note). The square-root rule assumes independent, identically distributed returns; N is not fixed in v0.1 (OI-09). | Used in Phase 1 | §1.6 (Volatility); FR-12 design note; OI-09 | §1.6; FR-12 design note; OI-09 |
| **Asset class** | Category of tradable instrument; in Phase 1 either stock or cryptocurrency. | Used in Phase 1 | FR-05; US-09 S2; §5.3 (Asset master); OOS-07 | FR-05; §5.3 |
| **Concentration** | Measure of how much portfolio value sits in one asset, one asset class, or a group of correlated assets. The Phase 1 indicators are the largest single-asset weight, the top-N weight share, and the Herfindahl–Hirschman Index. Weight w_i = market value of position i ÷ total portfolio market value; unit: percent of portfolio market value. | Used in Phase 1 | §1.6 (Concentration risk); FR-12; FR-14; US-09 | Refines §1.6 (Concentration risk); FR-12 design note |
| **Confidence level** | Probability, written c, that the loss over the horizon stays at or below the VaR threshold. A higher c produces a larger VaR. Unit: percent, or a fraction between 0 and 1; example: 95 % (illustrative). | Reserved for future releases | §1.6 (VaR); §1.6.1 (VaR, Historical VaR) | §1.6 (VaR) |
| **Correlation** | Statistical measure of how the returns of two assets move together, from −1 (opposite) through 0 (unrelated) to +1 (identical). Dimensionless; the usual estimator is the Pearson coefficient of periodic log returns over the look-back window. The method is not fixed in v0.1 (OI-09), and FR-12 does not list correlation (OI-02). | Used in Phase 1 | US-09 S1; FR-12 and FR-15 design notes; OI-02 | US-09; OI-02; OI-09 |
| **Cost basis** | Amount paid for the open quantity of a position, used as the reference for Unrealized PnL. Σ quantity × purchase price over the open quantity (FR-09 design note); unit: US dollars (ASM-02). The method for partial closures is not fixed (OI-08). | Used in Phase 1 | FR-09 and FR-10 design notes; OI-08 | FR-09 design note; OI-08 |
| **Drawdown** | Decline from a peak to a subsequent trough in value; maximum drawdown is the largest such decline over the look-back window. Drawdown at time t = (V_t − peak value up to t) ÷ peak value up to t, where V is portfolio value; unit: percent of the peak. The sign convention for reporting is not fixed in v0.1 (OI-09). | Used in Phase 1 | §1.6; FR-12; FR-14; US-14 | Refines §1.6 (Drawdown); FR-12 design note |
| **Endpoint** | API endpoint: one HTTP method plus a URL path under the base path `/api/v1`, through which a client uses a single capability of the Backend REST API. | Used in Phase 1 | §5.5; §5.6; §6.3; Appendix D | FR-23; Jira task |
| **Expected Shortfall** | Average loss over the cases in which the loss exceeds the VaR at the same confidence level and horizon; it describes the tail severity that VaR does not. ES(c, h) is the expected value of the loss L given that L exceeds VaR(c, h); same units and parameters as VaR; not used in v0.1. | Reserved for future releases | §1.6.1 (VaR) | Derived; complements §1.6 (VaR) |
| **Herfindahl–Hirschman Index** | Concentration measure equal to the sum of squared position weights; it ranges from 1/N for N equal weights to 1 for a single position. HHI = Σ w_i², with weights as fractions that sum to 1; dimensionless (some sources scale it by 10,000). | Used in Phase 1 | FR-12 design note; §1.6.1 (Concentration) | FR-12 design note |
| **Historical VaR** | Non-parametric VaR estimate taken from the empirical distribution of historical portfolio returns over a look-back window, without assuming a distribution shape. Estimate = loss at the (1 − c) quantile of the historical horizon-h returns, applied to the current portfolio value; illustrative: 500 daily returns, c = 95 %, and h = 1 day give a VaR near the 25th-worst return. Loss sign convention and horizon scaling: not fixed in v0.1 (OI-09). | Reserved for future releases | §1.6 (VaR); §1.6.1 (VaR, Look-back window) | §1.6 (VaR); FR-12 does not mandate VaR |
| **Horizon** | Length of the future period over which a risk measure such as VaR applies; it differs from the look-back window, which covers past data. Unit: days; illustrative values: 1 day and 10 days. The method that scales a one-day estimate to longer horizons is not fixed in v0.1 (OI-09). | Reserved for future releases | §1.6 (VaR); §1.6.1 (VaR, Historical VaR) | §1.6 (VaR) |
| **Log return** | Periodic return computed as the natural logarithm of the ratio of consecutive prices; log returns add up over time. r_t = ln(P_t ÷ P_(t−1)); dimensionless. | Used in Phase 1 | FR-12 design note | FR-12 design note |
| **Look-back window** | Span of past observations that a metric uses, for example for volatility, correlation, or historical drawdown. Unit: days or observations; length not fixed in v0.1 (OI-09). | Used in Phase 1 | FR-12 design note; OI-09 | FR-12 design note; OI-09 |
| **OpenAPI** | Machine-readable specification format (OpenAPI Specification 3.x) that describes the endpoints, schemas, and security of a REST API. | Used in Phase 1 | FR-23; §5.5; Appendix D | FR-23 |
| **Ownership-based authorization** | Access rule under which a user reads and modifies only the resources that user owns; ASM-07 states it for Phase 1. | Used in Phase 1 | ASM-07; §1.3 (Access model); §5.6 | ASM-07 |
| **Permission** | Right of a role to perform one operation on one endpoint; §5.6 states each permission as Own, All, Deny, or Public. | Used in Phase 1 | §5.6 | Jira task; §5.6 |
| **Phase 1** | The MVP release defined by the SRS v0.1 baseline: FR-01 to FR-25, US-01 to US-16, and NFR-01 to NFR-08. The exclusions of §1.5 (“Phase 2 & Beyond”) and the items marked “Out of Phase 1” in §6.3 fall outside its implementation scope. | Used in Phase 1 | §1.1; §1.5; §6.3; Appendix D | §1.1; §1.5 |
| **Portfolio** | User-defined collection of positions, owned by exactly one user and valued in US dollars; a user owns at most 10 portfolios. Market value = Σ position market values; unit: US dollars (ASM-02). | Used in Phase 1 | §1.6; FR-05; BR-06; §5.3 | Refines §1.6 (Portfolio); BR-06; ASM-02 |
| **Position** | Holding of one asset in a portfolio, with quantity, purchase price, and purchase timestamp; open until fully closed. Market value = quantity × current price (FR-09 design note); quantity and purchase price are above zero (BR-08). | Used in Phase 1 | §1.6; FR-06; FR-09; BR-08 | Refines §1.6 (Position); BR-08 |
| **Problem details** | JSON error object with the media type `application/problem+json`, defined by RFC 9457 and carrying the members type, title, status, detail, and instance. | Used in Phase 1 | §5.5; Appendix D | FR-23 design note (consistent error model); Proposed choice of RFC 9457 |
| **RBAC** | Role-Based Access Control: authorization model in which permissions belong to roles and a user obtains permissions through the role. | Used in Phase 1 | ASM-07; §5.6 | ASM-07; Jira task |
| **Risk level** | Qualitative classification of portfolio risk with the values Low, Medium, and High (BR-12); the LLM output carries it as the overall risk level. The thresholds that map metrics to a level are not defined (OI-09). | Used in Phase 1 | BR-12; FR-13 design note; US-09 S3 | BR-12; OI-09 |
| **Risk score** | Numeric measure of portfolio risk. v0.1 does not use it and defines no scale; it uses the risk level instead. Scale, formula, and mapping to the risk level are not defined (OI-09, OI-22). | Reserved for future releases | §1.6.1 | Jira task; OI-22 |
| **Role** | Named set of permissions assigned to an authenticated identity; this SRS uses USER (R-01), ADMIN (R-02), and ANALYST (R-03, Proposed). | Used in Phase 1 | §1.3; §1.3.1; §5.6 | §1.3; Jira task |
| **VaR** | Value at Risk: the loss threshold that is not exceeded with probability equal to the confidence level over the horizon. It does not state how large the loss is beyond the threshold. P(L ≤ VaR(c, h)) = c, where L is the portfolio loss over horizon h; unit: US dollars or percent of portfolio value; parameters: confidence level c, horizon h, and, for Historical VaR, the look-back window. Loss sign convention and horizon scaling: not fixed in v0.1 (OI-09). | Reserved for future releases | §1.6; §1.6.1 | Refines §1.6 (VaR); not mandated by FR-12 |
| **Volatility** | Standard deviation of periodic log returns of an asset or portfolio over the look-back window, annualized. σ_annual = stdev(r_t) × √N; unit: percent per year; N and the window are not fixed in v0.1 (OI-09). | Used in Phase 1 | §1.6; FR-12; US-09 S1 | Refines §1.6 (Volatility); FR-12 design note |

### 1.7 References

1. IEEE Std 830-1998 — *IEEE Recommended Practice for Software Requirements Specifications*.
2. ISO/IEC/IEEE 29148:2018 — *Systems and software engineering — Life cycle processes — Requirements engineering*.
3. RFC 7519 — *JSON Web Token (JWT)*.
4. RFC 6238 — *TOTP: Time-Based One-Time Password Algorithm*.
5. RFC 6265 — *HTTP State Management Mechanism* (cookies), and its successor revisions.
6. RFC 9106 — *Argon2 Memory-Hard Function for Password Hashing and Proof-of-Work Applications*.
7. OWASP Application Security Verification Standard (ASVS) and OWASP Top 10 — baselines for FR-20, FR-22, and FR-25.
8. OWASP Password Storage Cheat Sheet — current hashing-parameter guidance for FR-22.
9. Project brief — *SRS v0.1 Specification & Task List* (source of the baseline requirement identifiers).

### 1.8 Document Overview

§2 specifies the 25 functional requirements by module. §3 gives the 16 user stories with Given/When/Then acceptance scenarios. §4 specifies the 8 non-functional requirements with measurable acceptance thresholds. §5 provides supplementary specifications (reference architecture, external interfaces, conceptual data model, derived business rules). §6 contains the traceability matrices, §7 records open issues and recommendations for v0.2, and §8 holds the appendices.

**v0.2 additions (Derived or Proposed; informative).** §1.3.1 extends the role model, §1.6.1 extends the glossary, §5.5 catalogs the API endpoints, §5.6 specifies the roles and permissions matrix, §6.3 traces the 25 functional requirements to endpoints, Appendix D holds the OpenAPI 3.1 skeleton, and Appendix E logs every change from v0.1. The counts of 25 functional requirements, 16 user stories, and 8 non-functional requirements are unchanged.

## 2. Functional Requirements (FR-01 to FR-25)

### 2.1 Conventions & Priority Summary

Every requirement starts with “The system shall…” and carries a MoSCoW priority. “Traces to” links each requirement to user stories (US), non-functional requirements (NFR), and open issues (OI); “Ver.” gives the verification method (T, I, A, D). The *Requirement* column is the normative baseline wording.

| Priority | Count | Requirements |
| --- | --- | --- |
| **Must** | 10 | FR-01, FR-02, FR-05, FR-06, FR-09, FR-12, FR-13, FR-16, FR-19, FR-20 |
| **Should** | 7 | FR-03, FR-07, FR-10, FR-14, FR-17, FR-21, FR-22 |
| **Could** | 8 | FR-04, FR-08, FR-11, FR-15, FR-18, FR-23, FR-24, FR-25 |
| **Won’t** | 0 | None assigned in v0.1; exclusions are listed in §1.5 |
| **Total** | **25** | All modules |

### 2.2 Module: Auth (Authentication & Security)

| ID | Priority | Requirement | Traces to | Ver. |
| --- | --- | --- | --- | --- |
| **FR-01** | Must | The system shall allow users to register using a unique email address and password. | US-01; NFR-03; FR-22 | T |
| **FR-02** | Must | The system shall authenticate users and issue a short-lived Access JWT and Refresh Token upon valid login. | US-02, US-12; NFR-02; OI-10 | T |
| **FR-03** | Should | The system shall provide a password recovery mechanism via a time-limited token sent to the user’s registered email. | US-03; OI-10 | T |
| **FR-04** | Could | The system shall support Multi-Factor Authentication (MFA) via TOTP authenticator applications. | None in v0.1 (OI-06) | T, D |

*Design notes (informative)*

- **Uniqueness:** compare normalized (trimmed, case-folded) email addresses and back the rule with a database unique constraint.
- **Tokens:** the Access JWT travels in the `Authorization: Bearer` header; the Refresh Token lives in a `Secure`, `HttpOnly`, `SameSite` cookie and is stored server-side as a hash so that it can be rotated on use and revoked on logout (US-12). Lifetime and rotation policy: OI-10.
- **Password recovery:** single-use, high-entropy tokens stored hashed with a short expiry; respond identically whether or not the email is registered, to prevent account enumeration.
- **MFA:** RFC 6238 TOTP with enrolment by shared secret or QR code, recovery codes, and a challenge step between credential verification and token issuance.

### 2.3 Module: Portfolio (Portfolio Management)

| ID | Priority | Requirement | Traces to | Ver. |
| --- | --- | --- | --- | --- |
| **FR-05** | Must | The system shall allow users to create, read, update, and delete (CRUD) investment portfolios containing stocks and cryptocurrencies. | US-04, US-13 | T |
| **FR-06** | Must | The system shall allow users to add financial assets to a portfolio specifying asset identifier, quantity, purchase price, and timestamp. | US-05 | T |
| **FR-07** | Should | The system shall automatically fetch and cache real-time market quotes for stocks and crypto assets from external financial APIs. | US-05, US-06; NFR-01; OI-01, OI-11 | T |
| **FR-08** | Could | The system shall allow users to tag or group portfolios by custom investment strategies. | None in v0.1 (OI-06) | T |

*Design notes (informative)*

- **Asset identity:** keep a canonical asset master (symbol, asset class, market or provider identifier) so that identical tickers on different markets, and crypto symbols across chains, are not conflated.
- **Quote service:** shared TTL cache, request coalescing, per-provider quota tracking, and freshness metadata (an `as_of` timestamp) returned to the client for the “Data delayed / cached” indicator (US-06).
- **Business rules:** at most 10 portfolios per user (BR-06); quantity and purchase price greater than zero (BR-08); deleting a portfolio cascades to its positions (BR-14).

### 2.4 Module: Position (Position Tracking)

| ID | Priority | Requirement | Traces to | Ver. |
| --- | --- | --- | --- | --- |
| **FR-09** | Must | The system shall calculate and display real-time valuation and Unrealized/Realized PnL (Profit and Loss) for every open position. | US-06; NFR-01; OI-08 | T |
| **FR-10** | Should | The system shall allow users to record partial or full position closures, automatically updating realized PnL logs. | US-07; OI-08 | T |
| **FR-11** | Could | The system shall calculate and display target vs. actual portfolio asset allocation percentages. | None in v0.1 (OI-06) | T |

*Design notes (informative)*

- **Calculations:** market value = quantity × current price; cost basis = the sum of quantity × purchase price over the open quantity; Unrealized PnL = market value − cost basis; Unrealized PnL % = Unrealized PnL ÷ cost basis × 100; Realized PnL on closure = closed quantity × (closing price − cost per unit of the closed quantity).
- **Cost-basis method:** the method used to attribute cost to partial closures (FIFO, weighted average, or specific lot) is not defined in the brief (OI-08).
- **Precision:** use arbitrary-precision decimal arithmetic, never binary floating point, for monetary values and quantities; crypto quantities commonly need eight or more decimal places.
- **Allocation:** actual % = position market value ÷ total portfolio market value; how users enter target percentages is to be specified when FR-11 is scheduled.
- **Outside the model:** fees, dividends, splits, and other corporate actions (OOS-09, OI-15).

### 2.5 Module: Risk (AI Risk Analysis)

| ID | Priority | Requirement | Traces to | Ver. |
| --- | --- | --- | --- | --- |
| **FR-12** | Must | The system shall aggregate portfolio holdings and transmit quantitative metrics (volatility, concentration, historical drawdown) to the AI risk module. | US-08; NFR-01; OI-02, OI-09 | T, A |
| **FR-13** | Must | The system shall generate natural-language risk descriptions, exposure warnings, and actionable recommendations using the LLM integration. | US-08, US-09; OI-03 | T, D |
| **FR-14** | Should | The system shall allow users to configure custom alert thresholds for maximum portfolio drawdown or asset concentration limits. | US-14; OI-04 | T |
| **FR-15** | Could | The system shall maintain an audit trail of historical AI risk analysis executions for performance comparison. | None dedicated; relates to US-08 and FR-18 (OI-06, OI-12) | T |

*Design notes (informative)*

- **Metrics (indicative definitions):** *volatility* is the annualized standard deviation of periodic log returns over a defined look-back window; *concentration* is the largest single-asset weight, the top-N weight share, and the Herfindahl–Hirschman Index (the sum of the squared position weights); *historical drawdown* is the maximum peak-to-trough decline of the reconstructed portfolio value series over the look-back window. Equities (about 252 trading days a year) and crypto (365 days) annualize differently, so the methodology must be fixed explicitly (OI-09).
- **LLM contract:** the input is a metrics payload only, with no personal data or credentials (ASM-04). The output is schema-validated structured sections: summary, concentration, correlations, volatility, cross-asset balance, recommendations, and an overall risk level (Low / Medium / High). Figures shown to users are rendered from computed metrics, not copied from LLM prose (ASM-05). Prompts are versioned; calls are guarded by a 10-second timeout, bounded retries, and a circuit breaker; a provider-agnostic adapter prevents vendor lock-in.
- **Latency:** compute and render the deterministic metrics first, then present the LLM narrative progressively within the 10-second bound (OI-03).
- **Audit-trail record (FR-15):** timestamp, user, portfolio, metrics snapshot, prompt version, model identifier, response, status, and latency.

### 2.6 Module: Report (Reporting & Export)

| ID | Priority | Requirement | Traces to | Ver. |
| --- | --- | --- | --- | --- |
| **FR-16** | Must | The system shall generate comprehensive portfolio risk summary reports featuring structured text and visual asset distribution charts. | US-10 | T, I |
| **FR-17** | Should | The system shall allow users to export generated risk reports into downloadable PDF files. | US-11 | T, I |
| **FR-18** | Could | The system shall store generated reports in a user-accessible historical archive. | US-15; OI-12 | T |

*Design notes (informative)*

- **Report anatomy:** header (portfolio, generation time, data-freshness indicator), valuation summary, asset-distribution chart(s), metrics table, AI assessment with risk-level indicator, and a disclaimer block (CON-01).
- **PDF:** render server-side with embedded Cyrillic-capable fonts so that English and Russian reports look the same on screen and in print (DEP-06).
- **Archive:** store immutable snapshots with metadata (portfolio, timestamp, analysis run); retention and deletion policy: OI-12.

### 2.7 Cross-Cutting & Technical System Requirements

| ID | Priority | Requirement | Traces to | Ver. |
| --- | --- | --- | --- | --- |
| **FR-19** | Must | The system shall maintain structured audit logs for security-critical actions and backend system errors. | US-16 | T, I |
| **FR-20** | Must | The system shall sanitize and validate all incoming API request payloads to prevent injection attacks (SQLi, XSS). | US-01, US-04, US-05 (input scenarios) | T, A |
| **FR-21** | Should | The system shall support internationalization (i18n) with English and Russian localizations. | All UI stories; OI-13 | T, I |
| **FR-22** | Should | The system shall store passwords securely using industry-standard hashing algorithms (bcrypt or Argon2) with dynamic salts. | US-01; NFR-03; OI-05 | I, T |
| **FR-23** | Could | The system shall expose a documented RESTful API for external analytical tools. | None in v0.1 (OI-06, OI-14) | I, D |
| **FR-24** | Could | The system shall support user-selectable light and dark UI themes. | None in v0.1 (OI-06) | D |
| **FR-25** | Could | The system shall implement API rate-limiting on sensitive endpoints to prevent brute-force attacks. | US-02; OI-05 | T |

*Design notes (informative)*

- **Audit logging (FR-19):** JSON-structured entries with UTC timestamp, event type, user ID (if identified), client IP, correlation ID, and outcome; passwords, tokens, and key material are redacted before the entry is written (BR-15).
- **Validation (FR-20):** schema-based allow-list validation at the API boundary, parameterized queries or an ORM for all database access, contextual output encoding in the client, and request-size limits.
- **i18n (FR-21):** externalized message catalogs, locale-aware number, currency, and date formatting, and Russian plural rules (one / few / many).
- **Password hashing (FR-22):** a unique random salt per password, with cost parameters taken from the current OWASP Password Storage Cheat Sheet at implementation time; note bcrypt’s 72-byte input limit.
- **REST API (FR-23):** an OpenAPI 3.x description, a versioned base path, a consistent error model, and the same Bearer-token authentication as the web client unless OI-14 decides otherwise.
- **Rate limiting (FR-25):** applies to login, registration, password recovery, and MFA verification; counters live in the shared cache rather than process memory, to preserve NFR-08; responses use HTTP 429 with a `Retry-After` header.
- **Themes (FR-24):** design tokens as CSS variables, defaulting to the operating-system preference, with the user’s choice persisted.

## 3. User Stories (US-01 to US-16) with Acceptance Criteria

### 3.1 Conventions & Story Index

Stories use the format *As a \<role>, I want to \<goal>, so that \<benefit>.* Every **Must** story carries three explicit Given/When/Then scenarios (the required minimum is two). The baseline defines no scenarios for **Should** and **Could** stories; where this document adds some, they are marked **Proposed** and remain informative until the product owner accepts them. Role labels in the stories are aliases of R-01 or R-02 (§1.3).

| ID | Module | Title | Priority | Primary FRs | Scenarios |
| --- | --- | --- | --- | --- | --- |
| US-01 | Auth | User registration | Must | FR-01, FR-20, FR-22 | 3 |
| US-02 | Auth | User login | Must | FR-02, FR-25 | 3 |
| US-03 | Auth | Password reset request | Should | FR-03 | 3 proposed |
| US-04 | Portfolio | Create a portfolio | Must | FR-05, FR-20 | 3 |
| US-05 | Portfolio | Add assets to a portfolio | Must | FR-06, FR-07, FR-20 | 3 |
| US-06 | Position | View real-time values and PnL | Must | FR-07, FR-09 | 3 |
| US-07 | Position | Record a position closure | Should | FR-10 | 3 proposed |
| US-08 | Risk | Execute an AI risk analysis | Must | FR-12, FR-13 | 3 |
| US-09 | Risk | Read structured AI risk explanations | Must | FR-13 | 3 |
| US-10 | Report | Generate a risk summary report | Must | FR-16 | 3 |
| US-11 | Report | Export a report to PDF | Should | FR-17 | 2 proposed |
| US-12 | Auth | Log out | Must | FR-02 (partial; OI-06) | 3 |
| US-13 | Portfolio | Delete a portfolio | Must | FR-05 | 3 |
| US-14 | Risk | Set drawdown alert thresholds | Should | FR-14 | 3 proposed |
| US-15 | Report | Access the report archive | Could | FR-18 | 2 proposed |
| US-16 | System | Log security events and errors | Must | FR-19 | 3 |

Priority distribution: 11 Must, 4 Should, 1 Could (16 stories).

### 3.2 Story Specifications

#### US-01 — User Registration · Auth · Must

**Story:** As a new user, I want to register using my email and password, so that I can access my private dashboard.

- **Scenario 1 (Successful Registration):** **Given** the user is on the registration page, **When** they enter a valid email address and a password meeting complexity rules and click “Register”, **Then** the system creates the user account and redirects them to the login screen with a success notification.
- **Scenario 2 (Duplicate Email Error):** **Given** an account already exists with user@example.com, **When** a new user attempts to register with user@example.com, **Then** the system displays an error message stating “Email is already registered” and prevents account creation.
- **Scenario 3 (Password Complexity Failure):** **Given** the user enters a password shorter than 8 characters, **When** they submit the registration form, **Then** the system displays a validation error indicating that the password must contain at least 8 characters.

#### US-02 — User Login · Auth · Must

**Story:** As a registered user, I want to log into the system with my credentials, so that I can access my saved portfolios and risk reports.

- **Scenario 1 (Valid Login Credentials):** **Given** the user is on the login page, **When** they submit correct credentials, **Then** the system generates a JWT Access Token, sets a secure HTTP-only Refresh Token cookie, and grants access to the dashboard.
- **Scenario 2 (Invalid Password):** **Given** the user enters a correct email but an incorrect password, **When** they click “Log In”, **Then** the system displays an “Invalid email or password” error message without disclosing which credential failed.
- **Scenario 3 (Session Token Access):** **Given** the user has successfully authenticated and received an Access JWT, **When** they navigate to protected endpoints, **Then** the system authorizes requests using the Bearer token in the HTTP Authorization header.

#### US-03 — Password Reset Request · Auth · Should

**Story:** As a user who forgot my password, I want to request a password reset via email, so that I can regain access to my account.

**Proposed scenarios (informative):**

- **Scenario P1 (Reset Email Issued):** **Given** a registered user on the “Forgot password” page, **When** they submit their registered email address, **Then** the system sends a single-use, time-limited reset link to that address and displays a neutral confirmation message.
- **Scenario P2 (Unknown Email Handled Neutrally):** **Given** an email address that is not registered, **When** it is submitted on the “Forgot password” page, **Then** the system displays the same neutral confirmation message and sends no email.
- **Scenario P3 (Expired Token):** **Given** a reset token whose validity period has elapsed, **When** the user opens the reset link, **Then** the system rejects the token and offers to request a new one.

#### US-04 — Create a Portfolio · Portfolio · Must

**Story:** As an investor, I want to create a new investment portfolio, so that I can organize and analyze distinct crypto and stock strategies.

- **Scenario 1 (Create Named Portfolio):** **Given** an authenticated user on the portfolio page, **When** they click “Create Portfolio”, enter a valid name (e.g., “Crypto Growth”), and submit, **Then** the new portfolio is created and appears in the user’s active portfolio list.
- **Scenario 2 (Empty Name Validation):** **Given** the portfolio creation form is open, **When** the user leaves the portfolio name blank and clicks “Save”, **Then** the system prevents submission and displays a field validation error.
- **Scenario 3 (Max Portfolio Limit Check):** **Given** a user has reached the maximum allowed limit of 10 portfolios, **When** they attempt to create an additional portfolio, **Then** the system displays an information banner suggesting the removal of old portfolios before adding new ones.

#### US-05 — Add Assets to a Portfolio · Portfolio · Must

**Story:** As an investor, I want to add financial assets (crypto or stocks) to my portfolio, so that I can track my real investments in the system.

- **Scenario 1 (Add Stock Asset):** **Given** an open portfolio view, **When** the user searches for ticker “AAPL”, enters a quantity of 10 and purchase price of $180, and saves, **Then** AAPL is added to the portfolio holding list with calculated total initial value.
- **Scenario 2 (Add Crypto Asset):** **Given** an open portfolio view, **When** the user selects cryptocurrency “BTC”, inputs quantity 0.5 at $60,000, and submits, **Then** BTC is added to the portfolio and triggers initial valuation fetching.
- **Scenario 3 (Negative Quantity Input):** **Given** the user is adding a position, **When** they input a negative quantity or negative purchase price, **Then** the system rejects the entry with an error message stating “Values must be greater than zero”.

#### US-06 — View Real-Time Values and PnL · Position · Must

**Story:** As a trader, I want to view real-time market values and PnL metrics for all open positions, so that I can assess performance instantly.

- **Scenario 1 (Live PnL Calculation):** **Given** a portfolio contains active holdings (e.g., AAPL, BTC), **When** the user views the positions dashboard, **Then** the system fetches current prices and displays calculated Unrealized PnL (percentage and absolute value) for each asset.
- **Scenario 2 (API Quote Unavailable Fallback):** **Given** an external quote service is temporarily unreachable, **When** the position list renders, **Then** the system displays the last cached market price with a visual indicator showing “Data delayed / cached”.
- **Scenario 3 (Total Portfolio Value Aggregate):** **Given** multiple positions exist in a portfolio, **When** the user views the summary header, **Then** the system displays the aggregate market value updated according to current individual position rates.

#### US-07 — Record a Position Closure · Position · Should

**Story:** As an investor, I want to record a partial or full position closure, so that I can lock in realized gains/losses and update remaining inventory.

**Proposed scenarios (informative):**

- **Scenario P1 (Partial Closure):** **Given** an open position of 10 units, **When** the user records a closure of 4 units at a given price, **Then** the open quantity becomes 6 units and the realized PnL for the 4 closed units is recorded.
- **Scenario P2 (Full Closure):** **Given** an open position, **When** the user records a closure of the entire quantity, **Then** the position is marked closed, leaves the open-positions view, and its realized PnL is retained in the realized PnL log.
- **Scenario P3 (Over-Closure Rejected):** **Given** an open position of 10 units, **When** the user attempts to close 12 units, **Then** the system rejects the entry with a validation error and changes no data.

#### US-08 — Execute an AI Risk Analysis · Risk · Must

**Story:** As a user, I want to execute an AI risk analysis on my portfolio, so that I can receive quantitative risk parameters and AI insights.

- **Scenario 1 (Successful AI Execution):** **Given** a portfolio with populated positions, **When** the user clicks “Run AI Risk Analysis”, **Then** the system submits calculated metrics (volatility, concentration) to the LLM backend and renders the output within 2 seconds.
- **Scenario 2 (Empty Portfolio Trigger):** **Given** a newly created portfolio with zero added assets, **When** the user attempts to click “Run AI Risk Analysis”, **Then** the button is disabled with a tooltip stating “Add at least one asset to analyze risk”.
- **Scenario 3 (LLM Service Timeout Handling):** **Given** the upstream LLM service fails to respond within 10 seconds, **When** the user requests a risk analysis, **Then** the system aborts the request safely, displays a retry prompt, and logs the service timeout event.

#### US-09 — Read Structured AI Risk Explanations · Risk · Must

**Story:** As a user, I want to read structured AI explanations of portfolio risks, so that I can understand threats regarding my crypto and stock asset concentration.

- **Scenario 1 (Structured Insights Rendering):** **Given** the AI analysis engine finishes processing, **When** the user views the analysis results, **Then** the system displays text sections breaking down concentration risks, asset correlations, and volatility warnings.
- **Scenario 2 (Multi-Asset Class Analysis):** **Given** a portfolio containing both high-volatility crypto and traditional stock equities, **When** the AI risk report generates, **Then** the report explicitly highlights cross-asset class balance and market exposure factors.
- **Scenario 3 (Clear Formatting):** **Given** text risk recommendations are returned from the LLM, **When** displayed on screen, **Then** the text is formatted using standard headings, bullet points, and risk level indicators (Low / Medium / High).

#### US-10 — Generate a Risk Summary Report · Report · Must

**Story:** As a user, I want to generate a portfolio risk summary report, so that I have a single consolidated summary of my financial risks.

- **Scenario 1 (Generate On-Screen Summary):** **Given** complete position and AI risk data, **When** the user clicks “Generate Report”, **Then** the system compiles a view containing asset distribution graphs, metric tables, and the AI assessment summary.
- **Scenario 2 (Re-generating Report after Asset Update):** **Given** an existing report was generated earlier, **When** the user adds a new asset and clicks “Refresh Report”, **Then** the report updates all calculations and AI feedback to reflect the revised holdings.
- **Scenario 3 (Missing Data Handling):** **Given** risk evaluation was not run prior to report creation, **When** the user attempts to view the full report, **Then** the system prompts the user to execute the AI risk analysis first.

#### US-11 — Export a Report to PDF · Report · Should

**Story:** As a user, I want to export my generated report to a PDF document, so that I can save or print my analysis for offline review.

**Proposed scenarios (informative):**

- **Scenario P1 (Successful PDF Export):** **Given** a generated report is displayed, **When** the user clicks “Export to PDF”, **Then** the system produces a downloadable PDF containing the report’s text, charts, and metric tables.
- **Scenario P2 (Export Without Report):** **Given** no report has been generated for the portfolio, **When** the user views the export control, **Then** the control is disabled or prompts the user to generate a report first.

#### US-12 — Log Out · Auth · Must

**Story:** As an authenticated user, I want to explicitly log out of the application, so that my session is terminated on shared devices.

- **Scenario 1 (Standard Logout):** **Given** an active authenticated session, **When** the user clicks “Log Out”, **Then** the system clears access tokens from client storage, invalidates refresh tokens, and redirects to the login screen.
- **Scenario 2 (Session Clearance Verification):** **Given** a user has logged out, **When** they click the browser’s “Back” button, **Then** the system blocks access to cached protected views and enforces re-authentication.
- **Scenario 3 (Multi-Tab Logout):** **Given** the application is open across multiple browser tabs, **When** the user logs out from one tab, **Then** refreshing or navigating in the other tabs prompts the login screen.

#### US-13 — Delete a Portfolio · Portfolio · Must

**Story:** As an investor, I want to delete an unwanted portfolio, so that I can remove outdated strategies.

- **Scenario 1 (Confirmed Deletion):** **Given** an existing portfolio selected by the user, **When** they click “Delete Portfolio” and confirm the modal warning, **Then** the portfolio and all connected position records are removed from the database and UI.
- **Scenario 2 (Cancellation of Deletion):** **Given** the user clicked “Delete Portfolio” and the confirmation modal is open, **When** they click “Cancel”, **Then** the modal closes and no data is modified or deleted.
- **Scenario 3 (Cascade Removal Verification):** **Given** a deleted portfolio contained 5 positions, **When** deletion executes, **Then** all 5 position records are cascade-deleted from backend persistence.

#### US-14 — Set Drawdown Alert Thresholds · Risk · Should

**Story:** As a risk-conscious investor, I want to set custom alert thresholds for portfolio drawdown, so that I am notified when portfolio value falls beyond target parameters.

**Proposed scenarios (informative):**

- **Scenario P1 (Set Threshold):** **Given** an authenticated user viewing a portfolio, **When** they set a maximum drawdown threshold (e.g., 20%), **Then** the system saves the threshold for that portfolio.
- **Scenario P2 (Threshold Breach Notification):** **Given** a threshold is configured, **When** the portfolio’s computed drawdown exceeds it, **Then** the system notifies the user through the channel defined under OI-04.
- **Scenario P3 (Invalid Threshold):** **Given** the threshold form is open, **When** the user enters a value outside the 0–100% range, **Then** the system rejects the entry with a validation error.

#### US-15 — Access the Report Archive · Report · Could

**Story:** As a user, I want to access an archive of past risk reports, so that I can track my portfolio risk history over time.

**Proposed scenarios (informative):**

- **Scenario P1 (Archive Listing):** **Given** the user has generated several reports, **When** they open the report archive, **Then** the system lists the reports for that portfolio in reverse chronological order with generation date and risk level.
- **Scenario P2 (Open Archived Report):** **Given** the archive is displayed, **When** the user opens an archived report, **Then** the system shows the report as it was generated, unaffected by later changes to the holdings.

#### US-16 — Log Security Events and Errors · System · Must

**Story:** As a System Administrator, I want the system to log security events and application errors, so that I can audit access and troubleshoot operational issues.

- **Scenario 1 (Security Event Logging):** **Given** a failed login attempt or password change, **When** the event occurs, **Then** the system appends a structured log entry containing timestamp, IP address, user ID (if identified), and event type.
- **Scenario 2 (Backend Exception Logging):** **Given** an unexpected internal error (HTTP 500) occurs in a REST endpoint, **When** caught by middleware, **Then** the stack trace and context are logged to the centralized logging pipeline without revealing raw technical traces to the user.
- **Scenario 3 (Log Sanitization):** **Given** any logged system action, **When** log payloads are generated, **Then** passwords, JWT tokens, and private key data are redacted before writing to storage.

## 4. Non-Functional Requirements (NFR-01 to NFR-08)

The baseline wording of each NFR is reproduced verbatim. The brief assigns no MoSCoW priority to NFRs, so they are treated as mandatory release criteria for the MVP unless a waiver is recorded in §7. Each entry adds a measurable acceptance threshold and a verification approach (informative).

#### NFR-01 — Performance (API & Calculation Latency)

**Requirement:** Portfolio risk calculation endpoints and database response time (p95) shall execute in less than 2.0 seconds under a standard test workload of 50 active positions per portfolio.

- **Acceptance threshold:** p95 latency below 2.0 s.
- **Verification (T, A):** automated load test of the risk-calculation and portfolio-read endpoints against a seeded portfolio of 50 positions; record p50, p95, and p99 and fail the build on regression. Concurrency, dataset, and reference hardware are to be fixed (OI-16).
- **Traces to:** FR-09, FR-12; US-06, US-08.
- **Note:** the LLM round-trip is outside this budget (OI-03).

#### NFR-02 — Security (Token Lifespan)

**Requirement:** Access JWT tokens shall have a strict expiration time of exactly 15 minutes from issuance.

- **Acceptance threshold:** `exp` minus `iat` equals 900 seconds in every issued Access JWT; an expired token is rejected with HTTP 401.
- **Verification (T):** unit test decoding issued tokens, plus an integration test with a controllable clock that confirms acceptance just before and rejection just after the 15-minute mark.
- **Traces to:** FR-02; US-02.

#### NFR-03 — Security (Password Policy)

**Requirement:** User passwords shall enforce a minimum length of at least 8 characters (including upper/lowercase letters and numbers).

- **Acceptance threshold:** a password is accepted only if it has at least 8 characters including an upper-case letter, a lower-case letter, and a digit; enforcement is server-side, with client-side checks for usability only.
- **Verification (T):** boundary tests at 7 and 8 characters and for each missing character class, at API and UI level.
- **Traces to:** FR-01, FR-22; US-01.
- **Note:** US-01 Scenario 3 exercises the length rule only (OI-07).

#### NFR-04 — Reliability & Code Quality

**Requirement:** Backend core business services shall maintain unit and integration test code coverage of at least 60%.

- **Acceptance threshold:** combined unit and integration coverage of at least 60% across the backend core business services (Auth, Portfolio, Position, Risk, Report).
- **Verification (I, A):** coverage report generated in CI with a build gate at 60%. Coverage type (line or branch) and the exact service scope are to be fixed (OI-16).
- **Traces to:** all backend functional requirements.

#### NFR-05 — Browser Compatibility

**Requirement:** The web client shall render correctly on the latest stable versions of Google Chrome and Mozilla Firefox.

- **Acceptance threshold:** every Must user-story scenario passes on the latest stable Chrome and Firefox, with no layout-breaking defects.
- **Verification (T):** cross-browser end-to-end suite run on both browsers for each release, with browser versions recorded in the test report.
- **Traces to:** UI scenarios of US-01 to US-16; OOS-10.

#### NFR-06 — Developer Experience (DevEx Deployment)

**Requirement:** The complete application stack (frontend, backend API, database, and cache) must launch in a local environment using a single command (`docker compose up`).

- **Acceptance threshold:** on a clean machine with Docker and Compose installed, one command starts all four components, applies database migrations and seed data, and reaches healthy status without manual steps.
- **Verification (D, T):** scripted fresh-clone smoke test in CI.
- **Note:** because the System depends on third-party keys (market data, LLM), the default Compose profile should run against mock providers or documented placeholder variables so that the single-command promise holds without secrets (DEP-01, DEP-02).

#### NFR-07 — System Availability (Uptime)

**Requirement:** Core backend API services shall maintain a monthly operational availability target of at least 99.5% uptime.

- **Acceptance threshold:** availability = (total minutes − downtime minutes) ÷ total minutes, at least 99.5% per calendar month; that is at most about 3 h 36 min of downtime in a 30-day month.
- **Verification (A):** external synthetic monitoring of a health endpoint and a representative authenticated call, with a monthly availability report. Exclusions (planned maintenance, third-party outages) are to be defined (OI-16).
- **Traces to:** NFR-08; goals of R-02.

#### NFR-08 — Scalability

**Requirement:** The backend services must maintain a stateless design to allow horizontal scaling behind a standard load balancer.

- **Acceptance threshold:** no request depends on in-process session state; two or more instances behind a load balancer serve traffic correctly, and stopping one instance causes no user-visible session loss.
- **Verification (T, I):** CI scenario with at least two API replicas behind a load balancer, plus a design-review checklist (no local state or files, a shared cache for quotes and rate-limit counters, refresh sessions persisted in the data store, configuration through the environment).
- **Traces to:** FR-07, FR-25; NFR-07.

## 5. Supplementary Specifications (Informative)

### 5.1 Product Perspective & Reference Architecture

The System is a self-contained web application made of the logical components below. Technology choices are deliberately left open, except where the baseline fixes them (containerized stack, stateless backend).

| Component | Responsibility | Related requirements |
| --- | --- | --- |
| Web client | UI, i18n, themes, token handling, client-side validation | FR-21, FR-24, NFR-05 |
| Backend REST API (stateless) | Authentication and authorization, validation, orchestration, error handling | FR-01 to FR-03, FR-20, FR-23, NFR-08 |
| Quote service | Provider adapters, TTL cache, fallback, freshness metadata | FR-07, DEP-01 |
| Calculation engine | Valuation, PnL, volatility, concentration, drawdown | FR-09, FR-10, FR-12, NFR-01 |
| AI / LLM adapter | Prompt assembly, provider calls, timeout and retry, response validation | FR-13, DEP-02 |
| Relational data store | Users, sessions, portfolios, positions, closures, thresholds, analysis runs, reports | §5.3 |
| Cache | Quotes and rate-limit counters | FR-07, FR-25 |
| Report renderer | On-screen report assembly and PDF export | FR-16, FR-17, DEP-06 |
| Email gateway | Password-recovery messages | FR-03, DEP-03 |
| Logging pipeline | Central audit and error logs with redaction | FR-19, US-16 |

### 5.2 External Interface Requirements

- **User interface:** a browser-based client with these screens: Register, Login, Forgot/Reset Password, Portfolio List, Portfolio Detail (positions dashboard), Add Position, Close Position, AI Risk Analysis, Report View, Report Archive, Alert Thresholds, and Settings (language, theme, MFA). Validation errors appear next to the offending field, destructive actions require confirmation, and raw technical traces are never shown (BR-16).
- **Software interfaces:** HTTPS/JSON integrations with the market-data provider(s) and the LLM provider through adapters; SMTP or HTTPS integration with the email service; log shipping to the centralized logging pipeline.
- **Communications interfaces:** all client–server and server–provider traffic is expected to use TLS; the Refresh Token cookie carries the `Secure`, `HttpOnly`, and `SameSite` attributes.
- **Hardware interfaces:** none.

### 5.3 Logical Data Requirements (Conceptual)

Conceptual entities only; the physical schema is a design-phase deliverable.

| Entity | Key attributes | Rules |
| --- | --- | --- |
| User | id, email (unique), password hash, locale, theme, MFA settings, created at | Email unique (BR-01); every data object has exactly one owner |
| Refresh session | id, user id, token hash, issued at, expires at, revoked at | Revoked on logout (BR-05) |
| Password-reset token | id, user id, token hash, expires at, used at | Single use; time-limited |
| Portfolio | id, user id, name, strategy tags, created at | Name required (BR-07); at most 10 per user (BR-06) |
| Asset (master) | id, symbol, name, asset class (stock or crypto), market or provider reference | Shared reference data |
| Position | id, portfolio id, asset id, quantity, purchase price, purchased at, status | Quantity and price above zero (BR-08); cascade-deleted with the portfolio (BR-14) |
| Position closure | id, position id, quantity, closing price, closed at, realized PnL | Closed quantity not above open quantity (proposed, US-07) |
| Alert threshold | id, portfolio id, type (max drawdown or concentration), value | Range-validated (proposed, US-14) |
| Risk analysis run | id, portfolio id, metrics snapshot, prompt version, model id, output, status, latency, created at | Basis of the FR-15 audit trail |
| Report | id, portfolio id, analysis run id, content snapshot, created at | Archive entry (FR-18); retention per OI-12 |
| Quote (cache) | asset id, price, as-of timestamp, source | TTL-based; carries a stale flag |
| Audit event (logging pipeline) | timestamp, event type, user id (if known), IP, correlation id, outcome | Redacted before write (BR-15) |

### 5.4 Derived Business Rules

The rules below were extracted from the baseline scenarios and NFRs so that developers and testers have a single reference. They add no new requirements.

| ID | Rule | Source |
| --- | --- | --- |
| BR-01 | An email address can belong to only one account; a duplicate registration is rejected with “Email is already registered” | FR-01; US-01 S2 |
| BR-02 | Passwords need at least 8 characters, including upper-case and lower-case letters and digits | NFR-03; US-01 S3 |
| BR-03 | A failed login never discloses which credential was wrong; the message is “Invalid email or password” | US-02 S2 |
| BR-04 | The Access JWT expires exactly 15 minutes after issuance and is sent as a Bearer token; the Refresh Token is held in a secure HTTP-only cookie | NFR-02; US-02 S1, S3 |
| BR-05 | Logout clears client-side access tokens and invalidates refresh tokens; protected views cannot be reached with the Back button or from other tabs | US-12 S1 to S3 |
| BR-06 | A user may own at most 10 portfolios | US-04 S3 |
| BR-07 | A portfolio name is required | US-04 S2 |
| BR-08 | Quantity and purchase price must be greater than zero; otherwise “Values must be greater than zero” | US-05 S3 |
| BR-09 | If quotes are unavailable, the last cached price is shown with the “Data delayed / cached” indicator | US-06 S2 |
| BR-10 | Risk analysis is disabled for a portfolio with no assets, with the tooltip “Add at least one asset to analyze risk” | US-08 S2 |
| BR-11 | An LLM call that does not answer within 10 seconds is aborted, a retry prompt is shown, and the timeout is logged | US-08 S3 |
| BR-12 | Risk levels are expressed as Low, Medium, or High | US-09 S3 |
| BR-13 | A full report requires a prior AI risk analysis | US-10 S3 |
| BR-14 | Deleting a portfolio requires explicit confirmation and cascades to all of its positions | US-13 S1 to S3 |
| BR-15 | Security and error log entries carry timestamp, IP address, user ID (if identified), and event type; passwords, JWTs, and private-key data are redacted before storage | US-16 S1, S3 |
| BR-16 | Raw technical traces are never shown to end users | US-16 S2 |

### 5.5 API Endpoint Catalog (v0.2)

*Status: **Derived** from FR-01 to FR-25, US-01 to US-16, NFR-06, and NFR-07. Endpoint names, paths, and schemas are **Proposed** design choices and remain informative until the product owner accepts them. The catalog realises FR-23 and leaves the external access model open (OI-14).*

The catalog lists 34 endpoints in six modules (tags Auth, Portfolio, Position, Risk, Report, System). Each path below starts with the base path `/api/v1`. The OpenAPI skeleton of the same endpoints is Appendix D, and the mapping of every FR to an endpoint is §6.3.

#### Catalog conventions (Proposed)

1. The Backend API shall expose every endpoint over HTTPS under the versioned base path `/api/v1` (CON-08).
2. The Backend API shall name resource paths with plural nouns.
3. The Backend API shall give each operation a unique lowerCamelCase `operationId`.
4. The Backend API shall return every error as `application/problem+json` (RFC 9457; FR-23 design note).
5. The Backend API shall authenticate each Bearer endpoint with the Access JWT in the `Authorization: Bearer` header (FR-02 design note; NFR-02).
6. The Backend API shall set the Refresh Token only as a `Secure`, `HttpOnly`, `SameSite` cookie (FR-02 design note; §5.2).
7. The Backend API shall read the Refresh Token cookie only on EP-03 and EP-04.
8. The Backend API shall answer a request that exceeds a rate limit with status 429 and a `Retry-After` header (FR-25 design note).
9. The Backend API shall paginate every list endpoint with the query parameters `page` and `pageSize` (ASM-10).
10. The Backend API shall answer a Bearer endpoint call without a valid Access JWT with status 401 (NFR-02).
11. The Backend API shall answer a caller whose role is Deny for the endpoint with status 403 (§5.6).
12. The Backend API shall answer a request for another user’s resource with status 404 (ASM-11).
13. The Backend API shall return the deterministic risk metrics before the LLM narrative (ASM-12, OI-03).

*Reading notes (informative)*

- **Auth column.** Public means that no Access JWT is required; the endpoint validates its own credential (password, token, code, or Refresh Token cookie). Bearer means that the Access JWT is required.
- **Resource paths.** The Auth module also uses the action segments `register`, `login`, `refresh`, and `logout`, which follow common authentication practice. The settings endpoints (EP-10, EP-11) sit in the Auth module because they update the User entity (§5.3), and EP-17 sits in the Portfolio module because US-05 uses it when adding assets.
- **Error statuses.** The Error statuses column lists the statuses specific to each endpoint. Every endpoint may also return 500 as problem details without a stack trace (BR-16).

#### Endpoint contract

| EP ID | Method and path | operationId | Module | Auth | Request → success response | Error statuses |
| --- | --- | --- | --- | --- | --- | --- |
| **EP-01** | `POST /api/v1/auth/register` | `registerUser` | Auth | Public | `RegisterRequest` → 201 `User` | 400, 409, 422, 429 |
| **EP-02** | `POST /api/v1/auth/login` | `loginUser` | Auth | Public | `LoginRequest` → 200 `LoginResponse` (+ Refresh Token cookie) | 400, 401, 422, 429 |
| **EP-03** | `POST /api/v1/auth/refresh` | `refreshAccessToken` | Auth | Public | Refresh Token cookie → 200 `TokenResponse` (+ rotated cookie) | 401 |
| **EP-04** | `POST /api/v1/auth/logout` | `logoutUser` | Auth | Public | Refresh Token cookie → 204 (cookie cleared) | — |
| **EP-05** | `POST /api/v1/auth/password-reset-requests` | `requestPasswordReset` | Auth | Public | `PasswordResetRequest` → 202 (neutral confirmation) | 400, 422, 429 |
| **EP-06** | `POST /api/v1/auth/password-reset-confirmations` | `confirmPasswordReset` | Auth | Public | `PasswordResetConfirmation` → 204 | 400, 422, 429 |
| **EP-07** | `POST /api/v1/auth/mfa/enrollments` | `createMfaEnrollment` | Auth | Bearer | — → 201 `MfaEnrollment` | 401, 403, 409 |
| **EP-08** | `POST /api/v1/auth/mfa/enrollment-confirmations` | `confirmMfaEnrollment` | Auth | Bearer | `MfaCode` → 200 `MfaRecoveryCodes` | 400, 401, 403, 422 |
| **EP-09** | `POST /api/v1/auth/mfa/verifications` | `verifyMfaChallenge` | Auth | Public | `MfaVerification` → 200 `TokenResponse` (+ Refresh Token cookie) | 400, 401, 422, 429 |
| **EP-10** | `GET /api/v1/users/me/settings` | `getUserSettings` | Auth | Bearer | — → 200 `UserSettings` | 401, 403 |
| **EP-11** | `PATCH /api/v1/users/me/settings` | `updateUserSettings` | Auth | Bearer | `UserSettingsUpdate` → 200 `UserSettings` | 400, 401, 403, 422 |
| **EP-12** | `POST /api/v1/portfolios` | `createPortfolio` | Portfolio | Bearer | `PortfolioCreate` → 201 `Portfolio` | 400, 401, 403, 409, 422 |
| **EP-13** | `GET /api/v1/portfolios` | `listPortfolios` | Portfolio | Bearer | query `page`, `pageSize`, `strategyTag` → 200 `PortfolioPage` | 400, 401, 403 |
| **EP-14** | `GET /api/v1/portfolios/{portfolioId}` | `getPortfolio` | Portfolio | Bearer | — → 200 `Portfolio` | 401, 403, 404 |
| **EP-15** | `PATCH /api/v1/portfolios/{portfolioId}` | `updatePortfolio` | Portfolio | Bearer | `PortfolioUpdate` → 200 `Portfolio` | 400, 401, 403, 404, 422 |
| **EP-16** | `DELETE /api/v1/portfolios/{portfolioId}` | `deletePortfolio` | Portfolio | Bearer | — → 204 | 401, 403, 404 |
| **EP-17** | `GET /api/v1/assets` | `searchAssets` | Portfolio | Bearer | query `query`, `assetClass`, `page`, `pageSize` → 200 `AssetPage` | 400, 401, 403 |
| **EP-18** | `POST /api/v1/portfolios/{portfolioId}/positions` | `createPosition` | Position | Bearer | `PositionCreate` → 201 `Position` | 400, 401, 403, 404, 422 |
| **EP-19** | `GET /api/v1/portfolios/{portfolioId}/positions` | `listPositions` | Position | Bearer | query `status`, `page`, `pageSize` → 200 `PositionList` | 400, 401, 403, 404 |
| **EP-20** | `POST /api/v1/portfolios/{portfolioId}/positions/{positionId}/closures` | `createPositionClosure` | Position | Bearer | `ClosureCreate` → 201 `PositionClosure` | 400, 401, 403, 404, 422 |
| **EP-21** | `GET /api/v1/portfolios/{portfolioId}/positions/{positionId}/closures` | `listPositionClosures` | Position | Bearer | query `page`, `pageSize` → 200 `PositionClosurePage` | 400, 401, 403, 404 |
| **EP-22** | `POST /api/v1/portfolios/{portfolioId}/risk-analyses` | `createRiskAnalysis` | Risk | Bearer | — → 201 `RiskAnalysis` (metrics; narrative `pending`) | 401, 403, 404, 409 |
| **EP-23** | `GET /api/v1/portfolios/{portfolioId}/risk-analyses` | `listRiskAnalyses` | Risk | Bearer | query `page`, `pageSize` → 200 `RiskAnalysisPage` | 400, 401, 403, 404 |
| **EP-24** | `GET /api/v1/portfolios/{portfolioId}/risk-analyses/{analysisId}` | `getRiskAnalysis` | Risk | Bearer | — → 200 `RiskAnalysis` | 401, 403, 404 |
| **EP-25** | `POST /api/v1/portfolios/{portfolioId}/alert-thresholds` | `createAlertThreshold` | Risk | Bearer | `AlertThresholdCreate` → 201 `AlertThreshold` | 400, 401, 403, 404, 422 |
| **EP-26** | `GET /api/v1/portfolios/{portfolioId}/alert-thresholds` | `listAlertThresholds` | Risk | Bearer | query `page`, `pageSize` → 200 `AlertThresholdPage` | 400, 401, 403, 404 |
| **EP-27** | `PATCH /api/v1/portfolios/{portfolioId}/alert-thresholds/{thresholdId}` | `updateAlertThreshold` | Risk | Bearer | `AlertThresholdUpdate` → 200 `AlertThreshold` | 400, 401, 403, 404, 422 |
| **EP-28** | `DELETE /api/v1/portfolios/{portfolioId}/alert-thresholds/{thresholdId}` | `deleteAlertThreshold` | Risk | Bearer | — → 204 | 401, 403, 404 |
| **EP-29** | `POST /api/v1/portfolios/{portfolioId}/reports` | `createReport` | Report | Bearer | `ReportCreate` (optional) → 201 `Report` | 400, 401, 403, 404, 409, 422 |
| **EP-30** | `GET /api/v1/portfolios/{portfolioId}/reports` | `listReports` | Report | Bearer | query `page`, `pageSize` → 200 `ReportSummaryPage` | 400, 401, 403, 404 |
| **EP-31** | `GET /api/v1/portfolios/{portfolioId}/reports/{reportId}` | `getReport` | Report | Bearer | — → 200 `Report` | 401, 403, 404 |
| **EP-32** | `GET /api/v1/portfolios/{portfolioId}/reports/{reportId}/pdf` | `exportReportPdf` | Report | Bearer | — → 200 `application/pdf` | 401, 403, 404 |
| **EP-33** | `GET /api/v1/health` | `getHealth` | System | Public | — → 200 `Health` | — |
| **EP-34** | `GET /api/v1/openapi.json` | `getOpenApiDocument` | System | Public | — → 200 OpenAPI 3.1 document (`application/json`) | — |

#### Endpoint attributes

Constant attributes of every row: Version 0.2 · Owner \[To be completed by the document owner\] · Type Interface · Source: the FR, US, or NFR named in the Implements column. Priority is inherited: it is the highest MoSCoW priority among the implementing FRs, and an endpoint that implements only NFRs shows “Must (NFR)” because §4 treats NFRs as mandatory release criteria. Risk is the delivery risk (L, M, H) and Difficulty is the build effort (S, M, L).

| EP ID | Implements (FR/US/NFR) | Priority (inherited) | Risk (L/M/H) | Difficulty (S/M/L) | Rationale |
| --- | --- | --- | --- | --- | --- |
| **EP-01** | FR-01, US-01, NFR-03 | Must | M | S | Creates the account; enforces a unique email (BR-01) and the password policy (BR-02). |
| **EP-02** | FR-02, US-02, NFR-02 | Must | H | M | Issues the 15-minute Access JWT (NFR-02) and sets the Refresh Token cookie; returns an MFA challenge when FR-04 is active; the failure message does not disclose the wrong credential (BR-03). |
| **EP-03** | FR-02, NFR-02 | Must | M | M | Exchanges a valid Refresh Token for a new Access JWT and rotates the token on use (FR-02 design note); lifetime and rotation: OI-10. |
| **EP-04** | FR-02, US-12 | Must | M | S | Revokes the Refresh Token session (BR-05); US-12 has no dedicated FR, so the endpoint traces to FR-02 (partial) under OI-06 and OI-23. |
| **EP-05** | FR-03, US-03 | Should | M | S | Starts the time-limited recovery and answers identically whether or not the email is registered (FR-03 design note). |
| **EP-06** | FR-03, US-03, NFR-03 | Should | M | S | Completes recovery with a single-use, time-limited token (FR-03 design note) and applies the password policy (BR-02). |
| **EP-07** | FR-04 | Could | M | M | Starts TOTP enrolment by shared secret or QR code (FR-04 design note; DEP-07). |
| **EP-08** | FR-04 | Could | M | S | Confirms enrolment with a first valid code and issues recovery codes (FR-04 design note). |
| **EP-09** | FR-04 | Could | H | M | Adds the challenge step between credential verification and token issuance (FR-04 design note). |
| **EP-10** | FR-21, FR-24 | Should | L | S | Returns the locale and theme stored on the User entity (§5.3) for the Settings screen (§5.2). |
| **EP-11** | FR-21, FR-24 | Should | L | S | Persists the user’s language and theme choice (FR-21, FR-24). |
| **EP-12** | FR-05, FR-08, US-04 | Must | L | S | Creates a portfolio with a required name (BR-07) and optional strategy tags (FR-08); returns 409 when the 10-portfolio limit is reached (BR-06). |
| **EP-13** | FR-05, FR-08, US-04 | Must | L | S | Lists the caller’s portfolios and filters them by strategy tag (FR-08). |
| **EP-14** | FR-05 | Must | L | S | Reads one portfolio (the read operation of CRUD in FR-05). |
| **EP-15** | FR-05, FR-08 | Must | L | S | Updates the portfolio name or strategy tags. |
| **EP-16** | FR-05, US-13 | Must | M | S | Deletes a portfolio and cascades to its positions (BR-14); the fate of analyses, reports, and thresholds is open (OI-12). |
| **EP-17** | FR-06, US-05 | Must | M | M | Searches the Asset master by symbol or name so that users can pick a stock or cryptocurrency (US-05 S1, S2); the supported universe is open (OI-15). |
| **EP-18** | FR-06, FR-07, US-05 | Must | M | M | Adds a position with asset, quantity, purchase price, and timestamp; rejects values not above zero (BR-08); triggers the initial valuation fetch (US-05 S2). |
| **EP-19** | FR-07, FR-09, US-06, NFR-01 | Must | H | L | Returns positions with market value, Unrealized and Realized PnL, the aggregate summary, and freshness metadata (`asOf`, `stale`) for the “Data delayed / cached” indicator (BR-09). |
| **EP-20** | FR-10, US-07 | Should | H | M | Records a partial or full closure and updates the realized PnL log; returns 422 when the closed quantity exceeds the open quantity (US-07 P3); the cost-basis method is open (OI-08). |
| **EP-21** | FR-10, US-07 | Should | M | S | Lists the realized PnL log entries of one position (US-07 P2). |
| **EP-22** | FR-12, FR-13, US-08, NFR-01 | Must | H | L | Aggregates holdings, returns the deterministic metrics first, and starts the LLM narrative (OI-03, ASM-12); returns 409 for a portfolio without positions (BR-10). |
| **EP-23** | FR-15 | Could | L | S | Lists past analysis runs of a portfolio as the audit trail (FR-15 design note). |
| **EP-24** | FR-13, FR-15, US-08, US-09 | Must | H | M | Returns one run with its narrative status and structured narrative (US-09); an LLM call that exceeds 10 seconds ends as `timedOut` (BR-11). |
| **EP-25** | FR-14, US-14 | Should | L | S | Creates a threshold of type maxDrawdown or concentration; values outside 0–100 % are rejected (US-14 P3). |
| **EP-26** | FR-14, US-14 | Should | L | S | Lists the thresholds configured for a portfolio. |
| **EP-27** | FR-14, US-14 | Should | L | S | Changes the value of an existing threshold within 0–100 % (US-14 P3). |
| **EP-28** | FR-14 | Should | L | S | Removes a threshold so that a user can stop monitoring a limit. |
| **EP-29** | FR-16, FR-18, US-10 | Must | M | M | Generates a report snapshot from a completed analysis and stores it in the archive; returns 409 when no analysis exists (BR-13). |
| **EP-30** | FR-18, US-15 | Could | L | S | Lists archived reports in reverse chronological order with date and risk level (US-15 P1); retention is open (OI-12). |
| **EP-31** | FR-16, FR-18, US-10, US-15 | Must | L | S | Returns the stored snapshot as generated, unaffected by later changes to the holdings (US-15 P2). |
| **EP-32** | FR-17, US-11 | Should | M | M | Returns the PDF rendering with embedded Cyrillic-capable fonts (DEP-06). |
| **EP-33** | NFR-06, NFR-07 | Must (NFR) | L | S | Serves the health check used by synthetic monitoring (NFR-07) and by the healthy-status criterion of the single-command launch (NFR-06). |
| **EP-34** | FR-23 | Could | L | S | Serves the OpenAPI description for external analytical tools (FR-23); the external access model stays open (OI-14). |

#### Capabilities with no endpoint in v0.2

- Administrative user management, role assignment, and cross-user access to business data: OOS-12, OOS-16, OI-19.
- Delivery of threshold-breach notifications: OI-04, OOS-15.
- Target allocation (FR-11): Out of Phase 1 (Proposed), OI-21.
- Update or deletion of a position: OI-24.
- API keys, quotas, and client credentials for external analytical tools: OI-14.

### 5.6 Roles and Permissions Matrix (RBAC)

*Status: **Derived** for USER (R-01) and ADMIN (R-02); **Proposed** for ANALYST (R-03). The matrix is informative until the product owner accepts it.*

The matrix crosses the three roles with every endpoint of §5.5. It adds role permissions on top of the ownership rule of ASM-07 and contains no empty cell.

**Legend (closed vocabulary).**

- **Own** — the caller’s own resources; the Backend API performs the ownership check of ASM-07.
- **All** — any user’s resources; it also denotes unrestricted read access to shared reference data that has no owner (Asset master, §5.3).
- **Deny** — the Backend API answers an authenticated caller with status 403.
- **Public** — no Access JWT is required; the permission is role-independent.
- An unauthenticated call to a protected endpoint returns status 401, and a request for another user’s resource returns status 404 (ASM-11).

*Role defaults (Proposed)*

- **USER** receives Own on every resource of the caller (ASM-07; §1.3 Access model).
- **ANALYST** receives Deny on every write operation and on every operation that exposes other users’ business data, Own on read operations, and All on shared reference data only (least privilege; OI-18).
- **ADMIN** receives Deny on every Bearer endpoint (§1.3 Access model; OOS-12) and keeps access to the Public endpoints (OI-19).

#### Auth module (EP-01 to EP-11)

| EP ID | Operation | Authentication | USER | ANALYST | ADMIN | Basis (FR/ASM/OOS/OI) |
| --- | --- | --- | --- | --- | --- | --- |
| EP-01 | Register a user account | Public | Public | Public | Public | FR-01; no Access JWT required (CON-08) |
| EP-02 | Log in with email and password | Public | Public | Public | Public | FR-02; no Access JWT required (CON-08) |
| EP-03 | Obtain a new Access JWT with the Refresh Token | Public | Public | Public | Public | FR-02; no Access JWT required (CON-08) |
| EP-04 | Log out and revoke the Refresh Token | Public | Public | Public | Public | FR-02; no Access JWT required (CON-08) |
| EP-05 | Request a password reset email | Public | Public | Public | Public | FR-03; no Access JWT required (CON-08) |
| EP-06 | Confirm a password reset | Public | Public | Public | Public | FR-03; no Access JWT required (CON-08) |
| EP-07 | Start TOTP enrolment | Bearer | Own | Deny | Deny | FR-04; ASM-07; OI-18; OOS-12 |
| EP-08 | Confirm TOTP enrolment | Bearer | Own | Deny | Deny | FR-04; ASM-07; OI-18; OOS-12 |
| EP-09 | Verify an MFA code to complete login | Public | Public | Public | Public | FR-04; no Access JWT required (CON-08) |
| EP-10 | Read user settings | Bearer | Own | Own | Deny | FR-21; ASM-07; OI-18; OOS-12 |
| EP-11 | Update user settings | Bearer | Own | Deny | Deny | FR-21; ASM-07; OI-18; OOS-12 |

#### Portfolio module (EP-12 to EP-17)

| EP ID | Operation | Authentication | USER | ANALYST | ADMIN | Basis (FR/ASM/OOS/OI) |
| --- | --- | --- | --- | --- | --- | --- |
| EP-12 | Create a portfolio | Bearer | Own | Deny | Deny | FR-05; ASM-07; OI-18; OOS-12 |
| EP-13 | List portfolios | Bearer | Own | Own | Deny | FR-05; ASM-07; OI-18; OOS-12 |
| EP-14 | Read a portfolio | Bearer | Own | Own | Deny | FR-05; ASM-07; OI-18; OOS-12 |
| EP-15 | Update a portfolio | Bearer | Own | Deny | Deny | FR-05; ASM-07; OI-18; OOS-12 |
| EP-16 | Delete a portfolio | Bearer | Own | Deny | Deny | FR-05; ASM-07; OI-18; OOS-12 |
| EP-17 | Search assets | Bearer | All | All | Deny | US-05; §5.3 (Asset master: shared reference data); OI-18; OOS-12 |

#### Position module (EP-18 to EP-21)

| EP ID | Operation | Authentication | USER | ANALYST | ADMIN | Basis (FR/ASM/OOS/OI) |
| --- | --- | --- | --- | --- | --- | --- |
| EP-18 | Add a position to a portfolio | Bearer | Own | Deny | Deny | FR-06; ASM-07; OI-18; OOS-12 |
| EP-19 | List positions with valuation and PnL | Bearer | Own | Own | Deny | FR-07; ASM-07; OI-18; OOS-12 |
| EP-20 | Record a position closure | Bearer | Own | Deny | Deny | FR-10; ASM-07; OI-18; OOS-12 |
| EP-21 | List closures of a position | Bearer | Own | Own | Deny | FR-10; ASM-07; OI-18; OOS-12 |

#### Risk module (EP-22 to EP-28)

| EP ID | Operation | Authentication | USER | ANALYST | ADMIN | Basis (FR/ASM/OOS/OI) |
| --- | --- | --- | --- | --- | --- | --- |
| EP-22 | Start a risk analysis | Bearer | Own | Deny | Deny | FR-12; ASM-07; OI-18; OOS-12 |
| EP-23 | List risk analyses of a portfolio | Bearer | Own | Own | Deny | FR-15; ASM-07; OI-18; OOS-12 |
| EP-24 | Read a risk analysis | Bearer | Own | Own | Deny | FR-13; ASM-07; OI-18; OOS-12 |
| EP-25 | Create an alert threshold | Bearer | Own | Deny | Deny | FR-14; ASM-07; OI-18; OOS-12 |
| EP-26 | List alert thresholds | Bearer | Own | Own | Deny | FR-14; ASM-07; OI-18; OOS-12 |
| EP-27 | Update an alert threshold | Bearer | Own | Deny | Deny | FR-14; ASM-07; OI-18; OOS-12 |
| EP-28 | Delete an alert threshold | Bearer | Own | Deny | Deny | FR-14; ASM-07; OI-18; OOS-12 |

#### Report module (EP-29 to EP-32)

| EP ID | Operation | Authentication | USER | ANALYST | ADMIN | Basis (FR/ASM/OOS/OI) |
| --- | --- | --- | --- | --- | --- | --- |
| EP-29 | Generate a risk report | Bearer | Own | Deny | Deny | FR-16; ASM-07; OI-18; OOS-12 |
| EP-30 | List archived reports | Bearer | Own | Own | Deny | FR-18; ASM-07; OI-18; OOS-12 |
| EP-31 | Read a report | Bearer | Own | Own | Deny | FR-16; ASM-07; OI-18; OOS-12 |
| EP-32 | Export a report as PDF | Bearer | Own | Own | Deny | FR-17; ASM-07; OI-18; OOS-12 |

#### System module (EP-33, EP-34)

| EP ID | Operation | Authentication | USER | ANALYST | ADMIN | Basis (FR/ASM/OOS/OI) |
| --- | --- | --- | --- | --- | --- | --- |
| EP-33 | Check service health | Public | Public | Public | Public | NFR-06; no Access JWT required (CON-08) |
| EP-34 | Read the OpenAPI description | Public | Public | Public | Public | FR-23; no Access JWT required (CON-08) |

Matrix size: 34 endpoints × 3 roles = 102 cells (27 Public, 35 Own, 2 All, 38 Deny). The only All cells are the USER and ANALYST cells of EP-17, which rest on §5.3 (Asset master as shared reference data); ADMIN has no All cell.

#### Impact on v0.1

No v0.1 statement is edited. The table states how v0.2 treats each statement that touches roles and access.

| v0.1 statement | v0.2 treatment |
| --- | --- |
| ASM-07: “Authorization is ownership-based: a user can read and modify only their own data” | Stays valid for USER. RBAC is added on top: every Own cell applies the ownership check, and CON-09 records the layering |
| §1.3 Access model: “Administrators do not browse user business data through the application in the MVP” | ADMIN is Deny on every Bearer endpoint of §5.6; ADMIN keeps only the Public endpoints (OI-19) |
| OOS-12: no administrative web console or user-management UI | The catalog has no administrative endpoint; OOS-16 extends the exclusion to cross-user access and role assignment |
| OOS-14: no shared portfolios, advisor–client roles, or other multi-user collaboration | ANALYST (R-03) is not an advisor–client role and has no access to other users’ portfolios (Deny); its definition is open (OI-18) |
| §1.3 Role aliases: every story role label refers to R-01, and System Administrator is R-02 | Unchanged; R-03 is added in §1.3.1 and appears in no user story |
| §1.2 Auth, “will not”: administrator-provisioned accounts | Roles are assigned outside the API in Phase 1 (ASM-13, OI-19) |
| §5.3 User entity: no role attribute | The role is treated as a user attribute carried in the Access JWT (ASM-13); the physical schema remains a design-phase deliverable |

## 6. Requirements Traceability

### 6.1 Functional Requirement Traceability Matrix

| FR | Priority | User stories | Related NFRs | Related derived items |
| --- | --- | --- | --- | --- |
| FR-01 | Must | US-01 | NFR-03 | BR-01, BR-02 |
| FR-02 | Must | US-02, US-12 | NFR-02 | BR-03, BR-04, BR-05, OI-10 |
| FR-03 | Should | US-03 | — | OI-10 |
| FR-04 | Could | — | — | OI-06 |
| FR-05 | Must | US-04, US-13 | — | BR-06, BR-07, BR-14 |
| FR-06 | Must | US-05 | — | BR-08 |
| FR-07 | Should | US-05, US-06 | NFR-01, NFR-08 | BR-09, OI-01, OI-11 |
| FR-08 | Could | — | — | OI-06 |
| FR-09 | Must | US-06 | NFR-01 | OI-08 |
| FR-10 | Should | US-07 | — | OI-08 |
| FR-11 | Could | — | — | OI-06 |
| FR-12 | Must | US-08 | NFR-01 | BR-10, OI-02, OI-09 |
| FR-13 | Must | US-08, US-09 | — | BR-11, BR-12, OI-03 |
| FR-14 | Should | US-14 | — | OI-04 |
| FR-15 | Could | — (relates to US-08) | — | OI-06, OI-12 |
| FR-16 | Must | US-10 | — | BR-13 |
| FR-17 | Should | US-11 | — | DEP-06 |
| FR-18 | Could | US-15 | — | OI-12 |
| FR-19 | Must | US-16 | — | BR-15, BR-16 |
| FR-20 | Must | US-01, US-04, US-05 | — | OI-06 |
| FR-21 | Should | All UI stories | — | OI-13 |
| FR-22 | Should | US-01 | NFR-03 | OI-05 |
| FR-23 | Could | — | — | OI-06, OI-14 |
| FR-24 | Could | — | — | OI-06 |
| FR-25 | Could | US-02 | NFR-08 | OI-05 |

### 6.2 Coverage Observations

- Every user story maps to at least one functional requirement. US-12 (log out) is covered only partially, by FR-02, because no requirement states session termination explicitly (OI-06).
- Functional requirements with no dedicated user story: FR-04, FR-08, FR-11, FR-15, FR-23, and FR-24 (OI-06).
- Every Must functional requirement is exercised by at least one Must user story. FR-20 is exercised only indirectly, through the field-validation scenarios of US-01, US-04, and US-05, so SQLi- and XSS-specific tests should be added to the test plan.
- Every NFR carries a measurable acceptance threshold and a verification approach (§4).

### 6.3 FR-to-Endpoint Traceability Matrix

*Status: **Derived**. The mapping is informative until the product owner accepts it; the Jira acceptance criterion requires that every FR maps to an endpoint or carries the label “Out of Phase 1”.*

Each FR has exactly one mapping type:

- **Endpoint** — the FR is realised by at least one endpoint of §5.5.
- **Cross-cutting (API boundary)** — the FR describes behaviour with no endpoint of its own; the Targets column names the OpenAPI construct and the endpoints it affects (OI-20).
- **Out of Phase 1** — the FR is not implemented in the MVP release; the Note column cites the basis (OOS, MoSCoW Won’t, or OI).

| FR | Priority | Mapping type | Targets | Note |
| --- | --- | --- | --- | --- |
| FR-01 | Must | Endpoint | EP-01 | POST /api/v1/auth/register |
| FR-02 | Must | Endpoint | EP-02 to EP-04 | Login (EP-02), refresh (EP-03), and logout (EP-04); logout traces to US-12 under OI-06 and OI-23 |
| FR-03 | Should | Endpoint | EP-05, EP-06 | Request (EP-05) and confirm (EP-06) a password reset; token lifetime: OI-10 |
| FR-04 | Could | Endpoint | EP-07 to EP-09 | Enrol (EP-07), confirm (EP-08), and verify (EP-09) TOTP |
| FR-05 | Must | Endpoint | EP-12 to EP-16 | CRUD: create EP-12, read EP-13 and EP-14, update EP-15, delete EP-16 |
| FR-06 | Must | Endpoint | EP-17, EP-18 | Asset search (EP-17) and position creation (EP-18) |
| FR-07 | Should | Endpoint | EP-18, EP-19 | Quote fetching and caching run on the server; EP-19 returns the freshness metadata (`asOf`, `stale`) and EP-18 triggers the initial valuation; OI-01, OI-11 |
| FR-08 | Could | Endpoint | EP-12, EP-13, EP-15 | Strategy tags are a portfolio property (EP-12, EP-15) and a list filter (EP-13) |
| FR-09 | Must | Endpoint | EP-19 | Valuation and PnL are returned with the positions (EP-19); cost basis: OI-08 |
| FR-10 | Should | Endpoint | EP-20, EP-21 | Closure recording (EP-20) and the realized PnL log (EP-21); cost basis: OI-08 |
| FR-11 | Could | Out of Phase 1 | — | **Proposed deferral** (OI-21). Basis: the FR-11 design note (target entry “to be specified when FR-11 is scheduled”) and OI-06 (“add stories or confirm deferral”). Rationale: an endpoint cannot be specified without inventing how users enter target percentages. Target phase: not decided |
| FR-12 | Must | Endpoint | EP-22 | EP-22 returns the deterministic metrics first (OI-03, ASM-12); correlation and asset-class exposure are not included (OI-02); methodology: OI-09 |
| FR-13 | Must | Endpoint | EP-22, EP-24 | EP-22 starts the narrative and EP-24 returns its status and sections; two-stage delivery per ASM-12 (OI-03) |
| FR-14 | Should | Endpoint | EP-25 to EP-28 | Threshold configuration only; notification delivery is not an endpoint (OI-04, OOS-15) |
| FR-15 | Could | Endpoint | EP-23, EP-24 | Analysis history through EP-23 and EP-24; retention: OI-12 |
| FR-16 | Must | Endpoint | EP-29, EP-31 | Generation (EP-29) and viewing (EP-31) |
| FR-17 | Should | Endpoint | EP-32 | PDF export (EP-32); fonts: DEP-06 |
| FR-18 | Could | Endpoint | EP-29 to EP-31 | Storage on generation (EP-29), archive listing (EP-30), opening (EP-31); retention: OI-12 |
| FR-19 | Must | Cross-cutting (API boundary) | `components.responses.Problem` (status 500 without a stack trace, BR-16; header `X-Correlation-Id`) on every EP; security events arise at EP-02, EP-03, and EP-06 | No endpoint of its own; US-16 is satisfied through logging (OOS-12); OI-20 |
| FR-20 | Must | Cross-cutting (API boundary) | Schema constraints (`required`, `additionalProperties: false`, `pattern`, `minimum`, `maximum`) with status 400 or 422 as `application/problem+json` on EP-01, EP-02, EP-05, EP-06, EP-08, EP-09, EP-11 to EP-13, EP-15, EP-17 to EP-21, EP-23, EP-25 to EP-27, EP-29, EP-30 | Request-size limits are enforced by the gateway or framework (FR-20 design note) and are not modelled per operation; OI-20 |
| FR-21 | Should | Endpoint | EP-10, EP-11 | Persists the user’s locale (§5.3); message catalogs and plural rules stay in the web client (§5.1); localization of API messages: OI-13; OI-20 |
| FR-22 | Should | Cross-cutting (API boundary) | Schema properties `password` and `newPassword` with `writeOnly: true` and `format: password` on EP-01, EP-02, and EP-06 | Hashing runs on the server and has no API surface beyond write-only password fields; OI-20 |
| FR-23 | Could | Endpoint | EP-34 | EP-34 serves the OpenAPI description, and the whole catalog realises FR-23; access model (tokens, API keys, quotas): OI-14 |
| FR-24 | Could | Endpoint | EP-10, EP-11 | Theme values `light`, `dark`, and `system` (FR-24 design note: operating-system default) |
| FR-25 | Could | Cross-cutting (API boundary) | Status 429 with a `Retry-After` header (`components.responses.TooManyRequests`) on EP-01, EP-02, EP-05, EP-06, and EP-09 | Counters live in the shared cache (FR-25 design note); OI-20 |

*Coverage observations (informative)*

- 25 of 25 FRs are mapped: 20 Endpoint, 4 Cross-cutting (API boundary), and 1 Out of Phase 1 (Proposed deferral, OI-21).
- Each of the 34 endpoints implements at least one FR, except EP-33, which implements NFR-06 and NFR-07; no endpoint is an orphan.
- US-01 to US-15 are reached by at least one endpoint; US-16 is covered by the Cross-cutting mapping of FR-19.
- The mapping adds no capability to the baseline; it does not change any FR, US, or NFR.

## 7. Open Issues, Gaps & Recommendations (Informative)

The review below records inconsistencies and gaps found while consolidating the baseline. None changes the baseline wording in v0.1; each is a recommendation for the v0.2 review. **Severity:** H = could block release or cause rework; M = needs a decision before the affected feature is built; L = clarification only.

| ID | Sev. | Observation | Recommendation | Affects |
| --- | --- | --- | --- | --- |
| OI-01 | H | **Priority inversion.** FR-07 (Should) supplies the prices that Must items FR-09 and US-06 (and US-05 Scenario 2) depend on | Promote FR-07 to Must, or define a manual-price fallback for the MVP | FR-07, FR-09, US-06 |
| OI-02 | H | **Metric gap.** US-09 Scenarios 1 and 2 require asset correlations and cross-asset-class exposure, but FR-12 lists only volatility, concentration, and historical drawdown | Extend FR-12 with correlation and asset-class exposure metrics | FR-12, US-09 |
| OI-03 | H | **Latency conflict.** US-08 Scenario 1 requires output within 2 seconds, yet the LLM timeout is 10 seconds (US-08 Scenario 3) and NFR-01 covers calculation endpoints and database time only; LLM round-trips often exceed 2 seconds | State that the 2 seconds apply to the deterministic metrics, and that the LLM narrative arrives progressively within the 10-second bound | US-08, NFR-01 |
| OI-04 | M | **Notification gap.** US-14 promises a notification, but FR-14 covers only threshold configuration; channel, evaluation trigger, and frequency are undefined | Add a functional requirement for threshold evaluation and in-app and/or email notification | FR-14, US-14 |
| OI-05 | H | **Security priorities.** FR-22 (password hashing) is Should although Must items FR-01 and FR-02 depend on it; FR-25 (rate limiting) is Could although login is exposed to brute force | Promote FR-22 to Must and consider promoting FR-25 | FR-22, FR-25 |
| OI-06 | L | **Traceability gaps.** US-12 (log out) has no dedicated FR; FR-04, FR-08, FR-11, FR-15, FR-23, and FR-24 have no dedicated user story; FR-20 is exercised only by field-validation scenarios | Add a logout/session-revocation FR; add stories or confirm deferral; add SQLi- and XSS-specific acceptance scenarios | US-12, FR-04, FR-20 |
| OI-07 | M | **Password-policy coverage.** NFR-03 requires upper-case, lower-case, and digits, but US-01 Scenario 3 tests length only | Add scenarios for each missing character class | NFR-03, US-01 |
| OI-08 | M | **Cost basis.** Realized PnL (FR-10) needs a method for attributing cost on partial closures (FIFO, weighted average, specific lot) | Select and document the method | FR-09, FR-10 |
| OI-09 | M | **Risk methodology.** Look-back windows, return type, annualization (about 252 vs. 365 days), correlation method, and the thresholds that map metrics to Low / Medium / High are undefined | Publish a risk-methodology appendix before implementation | FR-12, FR-13, US-09 |
| OI-10 | M | **Token lifetimes.** Refresh-token lifetime and rotation, and the password-reset token validity window, are undefined (NFR-02 fixes only the Access JWT) | Define values and the rotation policy | FR-02, FR-03 |
| OI-11 | M | **“Real-time” data.** FR-07, FR-09, and US-06 say real-time, yet free-tier providers are often delayed or polled | Define the maximum quote age and refresh interval per asset class | FR-07, FR-09 |
| OI-12 | M | **Data lifecycle.** US-13 deletes positions, but the fate of analysis runs, reports, archive entries, and thresholds is unspecified, as are retention and erasure rules (e.g., GDPR) | Define retention, deletion, and data-export rules | FR-15, FR-18, US-13 |
| OI-13 | L | **i18n scope.** FR-21 does not say whether AI narratives, PDF reports, validation messages, and logs are localized | Clarify, and instruct the LLM to answer in the user’s locale | FR-21, FR-13 |
| OI-14 | L | **External API access.** FR-23 leaves authentication for external tools (user tokens vs. API keys), versioning, and quotas open | Define the access model before exposing the API | FR-23 |
| OI-15 | M | **Asset universe and corporate actions.** Supported exchanges, ETFs, and tokens are undefined; splits and dividends are not handled and can distort PnL | Define the supported universe and document known limitations | FR-05, FR-09, OOS-09 |
| OI-16 | L | **Measurability.** NFR-01 lacks a concurrency and hardware profile; NFR-04 lacks the coverage type and scope; NFR-07 lacks exclusions (planned maintenance, third-party outages) | Define the test profile, coverage type, and SLO exclusions | NFR-01, NFR-04, NFR-07 |
| OI-17 | H | **Legal and regulatory.** The classification of recommendations (FR-13), data-processing terms with LLM and market-data vendors, and crypto-asset and data-protection rules need legal review | Obtain counsel’s review and add disclaimers before public launch | CON-01, FR-13 |
| OI-18 | H | **ANALYST role undefined.** The Jira task names the role ANALYST, but neither v0.1 nor the task defines its duties, data scope, or assignment. §1.3.1 records it as R-03 (Proposed) with least-privilege defaults: Deny on writes and on other users’ data, Own on reads, and All on shared reference data. Account self-service writes (settings, MFA enrolment) are Deny under that default | Product owner to define the R-03 duties, whether ANALYST may see aggregated or anonymized data of other users (which also needs a review under OI-12 and OI-17), and whether ANALYST may use account self-service | R-03; §5.6; EP-07; EP-08; EP-11 |
| OI-19 | M | **Role assignment and ADMIN permissions.** v0.1 has no role attribute (§5.3), excludes administrator-provisioned accounts (§1.2) and an admin console (OOS-12), and says administrators do not browse user business data (§1.3). ADMIN therefore holds no Bearer endpoint permission in §5.6 (Deny on all), and no role can be assigned through the API | Decide how roles are provisioned in Phase 1 (for example, seed data or deployment tooling) and whether an operational ADMIN endpoint is needed beyond the public health endpoint | R-02; §5.6; ASM-13; OOS-12; OOS-16 |
| OI-20 | M | **Cross-cutting and partial FR mappings.** The Jira acceptance criterion knows only “endpoint” or “Out of Phase 1”. FR-19, FR-20, FR-22, and FR-25 describe behaviour with no endpoint of their own, so §6.3 maps them as Cross-cutting (API boundary) with an OpenAPI construct and the affected endpoints. FR-21 is mapped to the settings endpoints, which persist the locale only | Product owner to confirm this treatment, or to name an endpoint or a deferral for each of these FRs | §6.3; FR-19; FR-20; FR-21; FR-22; FR-25 |
| OI-21 | M | **FR-11 deferral (Proposed).** FR-11 (Could) has no user story (OI-06), and its design note leaves target entry “to be specified when FR-11 is scheduled”. No endpoint can be specified without inventing the target-entry contract, so §6.3 marks FR-11 “Out of Phase 1” as a Proposed deferral | Confirm the deferral and the target phase, or define target entry so that endpoints can be added | FR-11; §6.3 |
| OI-22 | M | **Risk score undefined.** The Jira task requires the term “risk score” and §1.6.1 defines it, but v0.1 uses only the risk level Low / Medium / High (BR-12); the scale, the formula, and the mapping between score and level are undefined (OI-09) | Fix the scale and mapping in the risk-methodology appendix (OI-09), or confirm that Phase 1 uses the risk level only | §1.6.1; BR-12; FR-13 |
| OI-23 | L | **Logout requirement (Proposed FR).** US-12 has no dedicated FR (OI-06), so EP-04 traces to FR-02 (partial) and US-12 | Proposed FR, unnumbered and not part of the baseline (v0.2 · Owner \[To be completed by the document owner\] · Source US-12, OI-06 · Priority Must, following US-12 · Risk L · Difficulty S · Type Functional): “The system shall end the caller’s session on request and invalidate its Refresh Token.” Product owner to accept or reject; v0.2 does not add it to §2 | FR-02; US-12; EP-04 |
| OI-24 | L | **Position correction.** v0.1 allows a user to add a position (FR-06) and to close it (FR-10) but defines no way to edit or delete a position entered by mistake, so §5.5 has no update or delete endpoint for positions | Confirm that corrections are outside the MVP, or add a requirement from which endpoints follow | FR-06; FR-10; §5.5 |

## 8. Appendices

### Appendix A — Baseline Completeness Check

| Check | Result |
| --- | --- |
| FR-01 to FR-25 present, each starting “The system shall…” with a MoSCoW priority | 25 of 25 (10 Must, 7 Should, 8 Could) |
| US-01 to US-16 present in the required format | 16 of 16 (11 Must, 4 Should, 1 Could) |
| Every Must user story has 2–3 Given/When/Then scenarios | 11 of 11 (3 scenarios each) |
| NFR-01 to NFR-08 present | 8 of 8 |
| Section 1 sub-sections 1.1 to 1.6 present | 6 of 6 (plus 1.7 References and 1.8 Document Overview) |
| Modules covered: Auth, Portfolio, Position, Risk, Report, and cross-cutting | 6 of 6 |

### Appendix B — Suggested Next Steps (toward v0.2)

1. Resolve, or consciously accept, the five High-severity open issues: OI-01, OI-02, OI-03, OI-05, and OI-17.
2. Publish the risk-methodology appendix (OI-09) and the cost-basis decision (OI-08).
3. Confirm or amend the proposed scenarios for US-03, US-07, US-11, US-14, and US-15.
4. Define the performance-test profile, coverage metric, and availability exclusions (OI-16).
5. Derive the test plan and sprint backlog from §2, §3, and §6.

### Appendix C — Review & Approval

### Appendix D — OpenAPI 3.1 Skeleton (Informative)

*Status: **Derived** from §5.5 and §5.6. The skeleton lists exactly the 34 endpoints of the catalog; no examples are included.*

```yaml
# Informative skeleton (SRS v0.2, Appendix D). Status codes per operation are listed in section 5.5.
# Every operation declares the shared Problem response as default; 429 is declared where FR-25 applies.
# Values not fixed by v0.1 (identifier format, page sizes, name length, cookie name) are Proposed (ASM-10).
openapi: 3.1.0
info:
  title: AI-Powered Crypto & Stock Risk Analysis Platform API
  version: 0.2.0
  description: Skeleton of the Phase 1 REST API described in SRS v0.2 section 5.5. Informative; no examples.
servers:
  - url: https://{host}/api/v1
    variables: {host: {default: localhost}}
tags:
  - {name: Auth, description: "Registration, login, session, password recovery, MFA, and user settings"}
  - {name: Portfolio, description: "Portfolios and the asset search"}
  - {name: Position, description: "Positions, valuation, and closures"}
  - {name: Risk, description: "Risk analyses and alert thresholds"}
  - {name: Report, description: "Risk reports, archive, and PDF export"}
  - {name: System, description: "Health check and API description"}
paths:
  '/auth/register':
    post:
      summary: Register a user account
      operationId: registerUser
      tags: [Auth]
      security: []
      requestBody: {$ref: '#/components/requestBodies/RegisterRequest'}
      responses:
        '201': {$ref: '#/components/responses/RegisterUserResponse'}
        '429': {$ref: '#/components/responses/TooManyRequests'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/login':
    post:
      summary: Log in with email and password
      operationId: loginUser
      tags: [Auth]
      security: []
      requestBody: {$ref: '#/components/requestBodies/LoginRequest'}
      responses:
        '200': {$ref: '#/components/responses/LoginUserResponse'}
        '429': {$ref: '#/components/responses/TooManyRequests'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/refresh':
    post:
      summary: Obtain a new Access JWT with the Refresh Token
      operationId: refreshAccessToken
      tags: [Auth]
      security: []
      parameters: [{$ref: '#/components/parameters/RefreshTokenCookie'}]
      responses:
        '200': {$ref: '#/components/responses/RefreshAccessTokenResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/logout':
    post:
      summary: Log out and revoke the Refresh Token
      operationId: logoutUser
      tags: [Auth]
      security: []
      parameters: [{$ref: '#/components/parameters/RefreshTokenCookie'}]
      responses:
        '204': {$ref: '#/components/responses/LogoutUserResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/password-reset-requests':
    post:
      summary: Request a password reset email
      operationId: requestPasswordReset
      tags: [Auth]
      security: []
      requestBody: {$ref: '#/components/requestBodies/PasswordResetRequest'}
      responses:
        '202': {$ref: '#/components/responses/RequestPasswordResetResponse'}
        '429': {$ref: '#/components/responses/TooManyRequests'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/password-reset-confirmations':
    post:
      summary: Confirm a password reset
      operationId: confirmPasswordReset
      tags: [Auth]
      security: []
      requestBody: {$ref: '#/components/requestBodies/PasswordResetConfirmation'}
      responses:
        '204': {$ref: '#/components/responses/ConfirmPasswordResetResponse'}
        '429': {$ref: '#/components/responses/TooManyRequests'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/mfa/enrollments':
    post:
      summary: Start TOTP enrolment
      operationId: createMfaEnrollment
      tags: [Auth]
      security: [{bearerAuth: []}]
      responses:
        '201': {$ref: '#/components/responses/CreateMfaEnrollmentResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/mfa/enrollment-confirmations':
    post:
      summary: Confirm TOTP enrolment
      operationId: confirmMfaEnrollment
      tags: [Auth]
      security: [{bearerAuth: []}]
      requestBody: {$ref: '#/components/requestBodies/MfaCode'}
      responses:
        '200': {$ref: '#/components/responses/ConfirmMfaEnrollmentResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/auth/mfa/verifications':
    post:
      summary: Verify an MFA code to complete login
      operationId: verifyMfaChallenge
      tags: [Auth]
      security: []
      requestBody: {$ref: '#/components/requestBodies/MfaVerification'}
      responses:
        '200': {$ref: '#/components/responses/VerifyMfaChallengeResponse'}
        '429': {$ref: '#/components/responses/TooManyRequests'}
        default: {$ref: '#/components/responses/Problem'}
  '/users/me/settings':
    get:
      summary: Read user settings
      operationId: getUserSettings
      tags: [Auth]
      security: [{bearerAuth: []}]
      responses:
        '200': {$ref: '#/components/responses/GetUserSettingsResponse'}
        default: {$ref: '#/components/responses/Problem'}
    patch:
      summary: Update user settings
      operationId: updateUserSettings
      tags: [Auth]
      security: [{bearerAuth: []}]
      requestBody: {$ref: '#/components/requestBodies/UserSettingsUpdate'}
      responses:
        '200': {$ref: '#/components/responses/UpdateUserSettingsResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios':
    post:
      summary: Create a portfolio
      operationId: createPortfolio
      tags: [Portfolio]
      security: [{bearerAuth: []}]
      requestBody: {$ref: '#/components/requestBodies/PortfolioCreate'}
      responses:
        '201': {$ref: '#/components/responses/CreatePortfolioResponse'}
        default: {$ref: '#/components/responses/Problem'}
    get:
      summary: List portfolios
      operationId: listPortfolios
      tags: [Portfolio]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}, {$ref: '#/components/parameters/StrategyTag'}]
      responses:
        '200': {$ref: '#/components/responses/ListPortfoliosResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}':
    get:
      summary: Read a portfolio
      operationId: getPortfolio
      tags: [Portfolio]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      responses:
        '200': {$ref: '#/components/responses/GetPortfolioResponse'}
        default: {$ref: '#/components/responses/Problem'}
    patch:
      summary: Update a portfolio
      operationId: updatePortfolio
      tags: [Portfolio]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      requestBody: {$ref: '#/components/requestBodies/PortfolioUpdate'}
      responses:
        '200': {$ref: '#/components/responses/UpdatePortfolioResponse'}
        default: {$ref: '#/components/responses/Problem'}
    delete:
      summary: Delete a portfolio
      operationId: deletePortfolio
      tags: [Portfolio]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      responses:
        '204': {$ref: '#/components/responses/DeletePortfolioResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/assets':
    get:
      summary: Search assets
      operationId: searchAssets
      tags: [Portfolio]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/AssetQuery'}, {$ref: '#/components/parameters/AssetClassFilter'}, {$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}]
      responses:
        '200': {$ref: '#/components/responses/SearchAssetsResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/positions':
    post:
      summary: Add a position to a portfolio
      operationId: createPosition
      tags: [Position]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      requestBody: {$ref: '#/components/requestBodies/PositionCreate'}
      responses:
        '201': {$ref: '#/components/responses/CreatePositionResponse'}
        default: {$ref: '#/components/responses/Problem'}
    get:
      summary: List positions with valuation and PnL
      operationId: listPositions
      tags: [Position]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/PositionStatus'}, {$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}]
      responses:
        '200': {$ref: '#/components/responses/ListPositionsResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/positions/{positionId}/closures':
    post:
      summary: Record a position closure
      operationId: createPositionClosure
      tags: [Position]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/PositionId'}]
      requestBody: {$ref: '#/components/requestBodies/ClosureCreate'}
      responses:
        '201': {$ref: '#/components/responses/CreatePositionClosureResponse'}
        default: {$ref: '#/components/responses/Problem'}
    get:
      summary: List closures of a position
      operationId: listPositionClosures
      tags: [Position]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/PositionId'}, {$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}]
      responses:
        '200': {$ref: '#/components/responses/ListPositionClosuresResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/risk-analyses':
    post:
      summary: Start a risk analysis
      operationId: createRiskAnalysis
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      responses:
        '201': {$ref: '#/components/responses/CreateRiskAnalysisResponse'}
        default: {$ref: '#/components/responses/Problem'}
    get:
      summary: List risk analyses of a portfolio
      operationId: listRiskAnalyses
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}]
      responses:
        '200': {$ref: '#/components/responses/ListRiskAnalysesResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/risk-analyses/{analysisId}':
    get:
      summary: Read a risk analysis
      operationId: getRiskAnalysis
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/AnalysisId'}]
      responses:
        '200': {$ref: '#/components/responses/GetRiskAnalysisResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/alert-thresholds':
    post:
      summary: Create an alert threshold
      operationId: createAlertThreshold
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      requestBody: {$ref: '#/components/requestBodies/AlertThresholdCreate'}
      responses:
        '201': {$ref: '#/components/responses/CreateAlertThresholdResponse'}
        default: {$ref: '#/components/responses/Problem'}
    get:
      summary: List alert thresholds
      operationId: listAlertThresholds
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}]
      responses:
        '200': {$ref: '#/components/responses/ListAlertThresholdsResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/alert-thresholds/{thresholdId}':
    patch:
      summary: Update an alert threshold
      operationId: updateAlertThreshold
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/ThresholdId'}]
      requestBody: {$ref: '#/components/requestBodies/AlertThresholdUpdate'}
      responses:
        '200': {$ref: '#/components/responses/UpdateAlertThresholdResponse'}
        default: {$ref: '#/components/responses/Problem'}
    delete:
      summary: Delete an alert threshold
      operationId: deleteAlertThreshold
      tags: [Risk]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/ThresholdId'}]
      responses:
        '204': {$ref: '#/components/responses/DeleteAlertThresholdResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/reports':
    post:
      summary: Generate a risk report
      operationId: createReport
      tags: [Report]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}]
      requestBody: {$ref: '#/components/requestBodies/ReportCreate'}
      responses:
        '201': {$ref: '#/components/responses/CreateReportResponse'}
        default: {$ref: '#/components/responses/Problem'}
    get:
      summary: List archived reports
      operationId: listReports
      tags: [Report]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/Page'}, {$ref: '#/components/parameters/PageSize'}]
      responses:
        '200': {$ref: '#/components/responses/ListReportsResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/reports/{reportId}':
    get:
      summary: Read a report
      operationId: getReport
      tags: [Report]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/ReportId'}]
      responses:
        '200': {$ref: '#/components/responses/GetReportResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/portfolios/{portfolioId}/reports/{reportId}/pdf':
    get:
      summary: Export a report as PDF
      operationId: exportReportPdf
      tags: [Report]
      security: [{bearerAuth: []}]
      parameters: [{$ref: '#/components/parameters/PortfolioId'}, {$ref: '#/components/parameters/ReportId'}]
      responses:
        '200': {$ref: '#/components/responses/ExportReportPdfResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/health':
    get:
      summary: Check service health
      operationId: getHealth
      tags: [System]
      security: []
      responses:
        '200': {$ref: '#/components/responses/GetHealthResponse'}
        default: {$ref: '#/components/responses/Problem'}
  '/openapi.json':
    get:
      summary: Read the OpenAPI description
      operationId: getOpenApiDocument
      tags: [System]
      security: []
      responses:
        '200': {$ref: '#/components/responses/GetOpenApiDocumentResponse'}
        default: {$ref: '#/components/responses/Problem'}
components:
  securitySchemes:
    bearerAuth: {type: http, scheme: bearer, bearerFormat: JWT, description: "Access JWT valid for 15 minutes (NFR-02)"}
  parameters:
    PortfolioId: {name: portfolioId, in: path, required: true, schema: {type: string}}
    PositionId: {name: positionId, in: path, required: true, schema: {type: string}}
    AnalysisId: {name: analysisId, in: path, required: true, schema: {type: string}}
    ThresholdId: {name: thresholdId, in: path, required: true, schema: {type: string}}
    ReportId: {name: reportId, in: path, required: true, schema: {type: string}}
    Page: {name: page, in: query, schema: {type: integer, minimum: 1, default: 1}}
    PageSize: {name: pageSize, in: query, schema: {type: integer, minimum: 1, maximum: 100, default: 20}}
    StrategyTag: {name: strategyTag, in: query, schema: {type: string}}
    PositionStatus: {name: status, in: query, schema: {type: string, enum: [open, closed]}}
    AssetQuery: {name: query, in: query, required: true, schema: {type: string, minLength: 1}}
    AssetClassFilter: {name: assetClass, in: query, schema: {type: string, enum: [stock, crypto]}}
    RefreshTokenCookie: {name: refresh_token, in: cookie, required: true, schema: {type: string}}
  headers:
    SetRefreshCookie: {description: "Refresh Token cookie (HttpOnly, Secure, SameSite); cleared on logout", schema: {type: string}}
    CorrelationId: {description: "Correlation identifier that also appears in the audit log entry (FR-19)", schema: {type: string}}
    RetryAfter: {description: "Seconds to wait before the next attempt (FR-25)", schema: {type: integer, minimum: 0}}
  requestBodies:
    RegisterRequest: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/RegisterRequest'}}}}
    LoginRequest: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/LoginRequest'}}}}
    PasswordResetRequest: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/PasswordResetRequest'}}}}
    PasswordResetConfirmation: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/PasswordResetConfirmation'}}}}
    MfaCode: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/MfaCode'}}}}
    MfaVerification: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/MfaVerification'}}}}
    UserSettingsUpdate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/UserSettingsUpdate'}}}}
    PortfolioCreate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/PortfolioCreate'}}}}
    PortfolioUpdate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/PortfolioUpdate'}}}}
    PositionCreate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/PositionCreate'}}}}
    ClosureCreate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/ClosureCreate'}}}}
    AlertThresholdCreate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/AlertThresholdCreate'}}}}
    AlertThresholdUpdate: {required: true, content: {application/json: {schema: {$ref: '#/components/schemas/AlertThresholdUpdate'}}}}
    ReportCreate: {required: false, content: {application/json: {schema: {$ref: '#/components/schemas/ReportCreate'}}}}
  responses:
    Problem: {description: "RFC 9457 problem details; status codes per operation are listed in SRS section 5.5", headers: {X-Correlation-Id: {$ref: '#/components/headers/CorrelationId'}}, content: {application/problem+json: {schema: {$ref: '#/components/schemas/ProblemDetails'}}}}
    TooManyRequests: {description: "Rate limit exceeded (FR-25)", headers: {Retry-After: {$ref: '#/components/headers/RetryAfter'}}, content: {application/problem+json: {schema: {$ref: '#/components/schemas/ProblemDetails'}}}}
    RegisterUserResponse: {description: "Account created", content: {application/json: {schema: {$ref: '#/components/schemas/User'}}}}
    LoginUserResponse: {description: "Tokens issued, or an MFA challenge returned", headers: {Set-Cookie: {$ref: '#/components/headers/SetRefreshCookie'}}, content: {application/json: {schema: {$ref: '#/components/schemas/LoginResponse'}}}}
    RefreshAccessTokenResponse: {description: "New Access JWT issued; Refresh Token rotated", headers: {Set-Cookie: {$ref: '#/components/headers/SetRefreshCookie'}}, content: {application/json: {schema: {$ref: '#/components/schemas/TokenResponse'}}}}
    LogoutUserResponse: {description: "Session ended; Refresh Token revoked and cookie cleared", headers: {Set-Cookie: {$ref: '#/components/headers/SetRefreshCookie'}}}
    RequestPasswordResetResponse: {description: "Neutral confirmation, identical for known and unknown emails"}
    ConfirmPasswordResetResponse: {description: "Password changed"}
    CreateMfaEnrollmentResponse: {description: "Enrolment secret issued", content: {application/json: {schema: {$ref: '#/components/schemas/MfaEnrollment'}}}}
    ConfirmMfaEnrollmentResponse: {description: "Enrolment confirmed; recovery codes issued", content: {application/json: {schema: {$ref: '#/components/schemas/MfaRecoveryCodes'}}}}
    VerifyMfaChallengeResponse: {description: "Tokens issued", headers: {Set-Cookie: {$ref: '#/components/headers/SetRefreshCookie'}}, content: {application/json: {schema: {$ref: '#/components/schemas/TokenResponse'}}}}
    GetUserSettingsResponse: {description: "Settings returned", content: {application/json: {schema: {$ref: '#/components/schemas/UserSettings'}}}}
    UpdateUserSettingsResponse: {description: "Settings updated", content: {application/json: {schema: {$ref: '#/components/schemas/UserSettings'}}}}
    CreatePortfolioResponse: {description: "Portfolio created", content: {application/json: {schema: {$ref: '#/components/schemas/Portfolio'}}}}
    ListPortfoliosResponse: {description: "Portfolios returned", content: {application/json: {schema: {$ref: '#/components/schemas/PortfolioPage'}}}}
    GetPortfolioResponse: {description: "Portfolio returned", content: {application/json: {schema: {$ref: '#/components/schemas/Portfolio'}}}}
    UpdatePortfolioResponse: {description: "Portfolio updated", content: {application/json: {schema: {$ref: '#/components/schemas/Portfolio'}}}}
    DeletePortfolioResponse: {description: "Portfolio and its positions deleted"}
    SearchAssetsResponse: {description: "Matching assets returned", content: {application/json: {schema: {$ref: '#/components/schemas/AssetPage'}}}}
    CreatePositionResponse: {description: "Position created", content: {application/json: {schema: {$ref: '#/components/schemas/Position'}}}}
    ListPositionsResponse: {description: "Positions with valuation returned", content: {application/json: {schema: {$ref: '#/components/schemas/PositionList'}}}}
    CreatePositionClosureResponse: {description: "Closure recorded", content: {application/json: {schema: {$ref: '#/components/schemas/PositionClosure'}}}}
    ListPositionClosuresResponse: {description: "Closures returned", content: {application/json: {schema: {$ref: '#/components/schemas/PositionClosurePage'}}}}
    CreateRiskAnalysisResponse: {description: "Deterministic metrics returned; narrative pending", content: {application/json: {schema: {$ref: '#/components/schemas/RiskAnalysis'}}}}
    ListRiskAnalysesResponse: {description: "Analysis runs returned", content: {application/json: {schema: {$ref: '#/components/schemas/RiskAnalysisPage'}}}}
    GetRiskAnalysisResponse: {description: "Analysis run returned", content: {application/json: {schema: {$ref: '#/components/schemas/RiskAnalysis'}}}}
    CreateAlertThresholdResponse: {description: "Threshold created", content: {application/json: {schema: {$ref: '#/components/schemas/AlertThreshold'}}}}
    ListAlertThresholdsResponse: {description: "Thresholds returned", content: {application/json: {schema: {$ref: '#/components/schemas/AlertThresholdPage'}}}}
    UpdateAlertThresholdResponse: {description: "Threshold updated", content: {application/json: {schema: {$ref: '#/components/schemas/AlertThreshold'}}}}
    DeleteAlertThresholdResponse: {description: "Threshold deleted"}
    CreateReportResponse: {description: "Report generated and archived", content: {application/json: {schema: {$ref: '#/components/schemas/Report'}}}}
    ListReportsResponse: {description: "Archived reports returned", content: {application/json: {schema: {$ref: '#/components/schemas/ReportSummaryPage'}}}}
    GetReportResponse: {description: "Report returned", content: {application/json: {schema: {$ref: '#/components/schemas/Report'}}}}
    ExportReportPdfResponse: {description: "PDF file returned", content: {application/pdf: {schema: {type: string, format: binary}}}}
    GetHealthResponse: {description: "Service is up", content: {application/json: {schema: {$ref: '#/components/schemas/Health'}}}}
    GetOpenApiDocumentResponse: {description: "OpenAPI document returned", content: {application/json: {schema: {type: object}}}}
  schemas:
    ProblemDetails: {type: object, required: [type, title, status], properties: {type: {type: string, format: uri-reference}, title: {type: string}, status: {type: integer}, detail: {type: string}, instance: {type: string, format: uri-reference}, errors: {type: array, items: {$ref: '#/components/schemas/FieldError'}}}}
    FieldError: {type: object, required: [field, message], properties: {field: {type: string}, message: {type: string}}}
    Decimal: {type: string, pattern: '^-?[0-9]+(\.[0-9]+)?$', description: "Signed decimal in plain notation"}
    PositiveDecimal: {type: string, pattern: '^(?!0+(\.0+)?$)[0-9]+(\.[0-9]+)?$', description: "Decimal greater than zero in plain notation"}
    PageInfo: {type: object, required: [page, pageSize, total], properties: {page: {type: integer}, pageSize: {type: integer}, total: {type: integer}}}
    RegisterRequest: {type: object, additionalProperties: false, required: [email, password], properties: {email: {type: string, format: email, maxLength: 254}, password: {type: string, format: password, writeOnly: true, minLength: 8}}}
    User: {type: object, required: [id, email, createdAt], properties: {id: {type: string}, email: {type: string, format: email}, createdAt: {type: string, format: date-time}}}
    LoginRequest: {type: object, additionalProperties: false, required: [email, password], properties: {email: {type: string, format: email}, password: {type: string, format: password, writeOnly: true}}}
    TokenResponse: {type: object, required: [accessToken, tokenType, expiresIn], properties: {accessToken: {type: string}, tokenType: {type: string, const: Bearer}, expiresIn: {type: integer, const: 900}}}
    MfaChallenge: {type: object, required: [mfaRequired, mfaToken], properties: {mfaRequired: {type: boolean, const: true}, mfaToken: {type: string}}}
    LoginResponse: {oneOf: [{$ref: '#/components/schemas/TokenResponse'}, {$ref: '#/components/schemas/MfaChallenge'}]}
    PasswordResetRequest: {type: object, additionalProperties: false, required: [email], properties: {email: {type: string, format: email}}}
    PasswordResetConfirmation: {type: object, additionalProperties: false, required: [token, newPassword], properties: {token: {type: string}, newPassword: {type: string, format: password, writeOnly: true, minLength: 8}}}
    MfaEnrollment: {type: object, required: [secret, otpauthUri], properties: {secret: {type: string}, otpauthUri: {type: string}}}
    MfaCode: {type: object, additionalProperties: false, required: [code], properties: {code: {type: string, pattern: '^[0-9]{6,8}$'}}}
    MfaRecoveryCodes: {type: object, required: [recoveryCodes], properties: {recoveryCodes: {type: array, items: {type: string}}}}
    MfaVerification: {type: object, additionalProperties: false, required: [mfaToken, code], properties: {mfaToken: {type: string}, code: {type: string, minLength: 6}}}
    UserSettings: {type: object, required: [locale, theme], properties: {locale: {type: string, enum: [en, ru]}, theme: {type: string, enum: [light, dark, system]}, mfaEnabled: {type: boolean, readOnly: true}}}
    UserSettingsUpdate: {type: object, additionalProperties: false, minProperties: 1, properties: {locale: {type: string, enum: [en, ru]}, theme: {type: string, enum: [light, dark, system]}}}
    PortfolioCreate: {type: object, additionalProperties: false, required: [name], properties: {name: {type: string, minLength: 1, maxLength: 100}, strategyTags: {type: array, items: {type: string}}}}
    PortfolioUpdate: {type: object, additionalProperties: false, minProperties: 1, properties: {name: {type: string, minLength: 1, maxLength: 100}, strategyTags: {type: array, items: {type: string}}}}
    Portfolio: {type: object, required: [id, name, createdAt], properties: {id: {type: string}, name: {type: string}, strategyTags: {type: array, items: {type: string}}, createdAt: {type: string, format: date-time}}}
    PortfolioPage: {type: object, required: [items, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/Portfolio'}}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    Asset: {type: object, required: [id, symbol, name, assetClass], properties: {id: {type: string}, symbol: {type: string}, name: {type: string}, assetClass: {type: string, enum: [stock, crypto]}, providerRef: {type: string}}}
    AssetPage: {type: object, required: [items, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/Asset'}}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    PositionCreate: {type: object, additionalProperties: false, required: [assetId, quantity, purchasePrice, purchasedAt], properties: {assetId: {type: string}, quantity: {$ref: '#/components/schemas/PositiveDecimal'}, purchasePrice: {$ref: '#/components/schemas/PositiveDecimal'}, purchasedAt: {type: string, format: date-time}}}
    Position: {type: object, required: [id, assetId, quantity, purchasePrice, purchasedAt, status], properties: {id: {type: string}, assetId: {type: string}, quantity: {$ref: '#/components/schemas/PositiveDecimal'}, purchasePrice: {$ref: '#/components/schemas/PositiveDecimal'}, purchasedAt: {type: string, format: date-time}, status: {type: string, enum: [open, closed]}, currentPrice: {$ref: '#/components/schemas/Decimal'}, marketValue: {$ref: '#/components/schemas/Decimal'}, unrealizedPnl: {$ref: '#/components/schemas/Decimal'}, unrealizedPnlPercent: {$ref: '#/components/schemas/Decimal'}, realizedPnl: {$ref: '#/components/schemas/Decimal'}, asOf: {type: string, format: date-time}, stale: {type: boolean}}}
    PortfolioValuation: {type: object, required: [totalMarketValue, totalUnrealizedPnl, asOf, stale], properties: {totalMarketValue: {$ref: '#/components/schemas/Decimal'}, totalUnrealizedPnl: {$ref: '#/components/schemas/Decimal'}, totalRealizedPnl: {$ref: '#/components/schemas/Decimal'}, asOf: {type: string, format: date-time}, stale: {type: boolean}}}
    PositionList: {type: object, required: [items, summary, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/Position'}}, summary: {$ref: '#/components/schemas/PortfolioValuation'}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    ClosureCreate: {type: object, additionalProperties: false, required: [quantity, closingPrice, closedAt], properties: {quantity: {$ref: '#/components/schemas/PositiveDecimal'}, closingPrice: {$ref: '#/components/schemas/PositiveDecimal'}, closedAt: {type: string, format: date-time}}}
    PositionClosure: {type: object, required: [id, positionId, quantity, closingPrice, closedAt, realizedPnl], properties: {id: {type: string}, positionId: {type: string}, quantity: {$ref: '#/components/schemas/PositiveDecimal'}, closingPrice: {$ref: '#/components/schemas/PositiveDecimal'}, closedAt: {type: string, format: date-time}, realizedPnl: {$ref: '#/components/schemas/Decimal'}}}
    PositionClosurePage: {type: object, required: [items, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/PositionClosure'}}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    RiskMetrics: {type: object, required: [volatility, concentration, historicalDrawdown, asOf], properties: {volatility: {$ref: '#/components/schemas/Decimal'}, concentration: {type: object, properties: {largestWeight: {$ref: '#/components/schemas/Decimal'}, topNWeightShare: {$ref: '#/components/schemas/Decimal'}, herfindahlHirschmanIndex: {$ref: '#/components/schemas/Decimal'}}}, historicalDrawdown: {$ref: '#/components/schemas/Decimal'}, asOf: {type: string, format: date-time}}}
    RiskNarrative: {type: object, required: [summary, riskLevel], properties: {summary: {type: string}, concentration: {type: string}, correlations: {type: string}, volatility: {type: string}, crossAssetBalance: {type: string}, recommendations: {type: string}, riskLevel: {type: string, enum: [Low, Medium, High]}}}
    RiskAnalysis: {type: object, required: [id, portfolioId, createdAt, metrics, narrativeStatus], properties: {id: {type: string}, portfolioId: {type: string}, createdAt: {type: string, format: date-time}, metrics: {$ref: '#/components/schemas/RiskMetrics'}, narrativeStatus: {type: string, enum: [pending, completed, timedOut, failed]}, narrative: {$ref: '#/components/schemas/RiskNarrative'}, promptVersion: {type: string}, modelId: {type: string}, latencyMs: {type: integer}}}
    RiskAnalysisPage: {type: object, required: [items, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/RiskAnalysis'}}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    AlertThresholdCreate: {type: object, additionalProperties: false, required: [type, value], properties: {type: {type: string, enum: [maxDrawdown, concentration]}, value: {type: number, minimum: 0, maximum: 100}}}
    AlertThresholdUpdate: {type: object, additionalProperties: false, required: [value], properties: {value: {type: number, minimum: 0, maximum: 100}}}
    AlertThreshold: {type: object, required: [id, portfolioId, type, value], properties: {id: {type: string}, portfolioId: {type: string}, type: {type: string, enum: [maxDrawdown, concentration]}, value: {type: number, minimum: 0, maximum: 100}}}
    AlertThresholdPage: {type: object, required: [items, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/AlertThreshold'}}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    ReportCreate: {type: object, additionalProperties: false, properties: {analysisId: {type: string}}}
    Report: {type: object, required: [id, portfolioId, analysisId, createdAt, riskLevel], properties: {id: {type: string}, portfolioId: {type: string}, analysisId: {type: string}, createdAt: {type: string, format: date-time}, riskLevel: {type: string, enum: [Low, Medium, High]}, content: {type: object}}}
    ReportSummary: {type: object, required: [id, createdAt, riskLevel], properties: {id: {type: string}, createdAt: {type: string, format: date-time}, riskLevel: {type: string, enum: [Low, Medium, High]}}}
    ReportSummaryPage: {type: object, required: [items, pageInfo], properties: {items: {type: array, items: {$ref: '#/components/schemas/ReportSummary'}}, pageInfo: {$ref: '#/components/schemas/PageInfo'}}}
    Health: {type: object, required: [status], properties: {status: {type: string, enum: [up]}}}
```

### Appendix E — Delta Log (v0.1 → v0.2)

Every difference from SRS v0.1 is listed below. Appendix A, Appendix B, Appendix C, and all FR, US, NFR, and BR items are unchanged.

| Section | Change type | Description |
| --- | --- | --- |
| Title line | Metadata edit | Version changed from v0.1 to v0.2 |
| Subtitle line | Metadata edit | Version changed to 0.2 and date to 23 November 2026 |
| Document Control: Version / status | Metadata edit | Version changed from 0.1 to 0.2; status text unchanged |
| Document Control: Date | Metadata edit | Date changed to 23 November 2026 |
| Document Control: Derived identifiers | Metadata edit | EP (API endpoints) appended |
| Revision History | Row appended | Row for version 0.2 appended |
| Document Conventions: IEEE 830 compliance map | Index update | Location cells appended: row 1.3 (§1.6.1), row 2.3 (§1.3.1, §5.6), row 3.1 (§5.5) |
| §1.3.1 (new) | Added | Role Model Extension: USER, ANALYST, and ADMIN mapped to R-01, R-03 (Proposed), and R-02 |
| §1.4 Assumptions | Row appended | ASM-10 to ASM-13 |
| §1.4 Constraints | Row appended | CON-08 and CON-09 |
| §1.4 Dependencies | Row appended | DEP-08 |
| §1.5 Out of Scope | Row appended | OOS-16 |
| §1.6.1 (new) | Added | Extended Glossary with 28 terms |
| §1.8 Document Overview | Index update | Paragraph appended that lists the v0.2 additions; the v0.1 paragraph is unchanged |
| §5.5 (new) | Added | API Endpoint Catalog: 34 endpoints (EP-01 to EP-34), contract table, attribute table |
| §5.6 (new) | Added | Roles and Permissions Matrix (102 cells) and Impact on v0.1 table |
| §6.3 (new) | Added | FR-to-Endpoint Traceability Matrix for FR-01 to FR-25 |
| §7 Open Issues | Row appended | OI-18 to OI-24 |
| Appendix D (new) | Added | OpenAPI 3.1 skeleton of the 34 endpoints |
| Appendix E (new) | Added | This Delta Log |

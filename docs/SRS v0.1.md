SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

# Software Requirements Specification (SRS) v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

_Version 0.1 (Draft) · 28 October 2026 · Structure: IEEE Std 830 · Status: Draft for stakeholder review_ 

## Document Control 

|Attribute|Value|
|---|---|
|Document title|Software Requirements Specification — AI-<br>Powered Cryptocurrency & Stock Risk Analysis<br>Platform (working title)|
|Version / status|0.1 — Draft for stakeholder review (MVP baseline<br>candidate)|
|Date|28 October 2026|
|Governing structure|IEEE Std 830-1998, applying requirement-quality<br>practices from ISO/IEC/IEEE 29148|
|Author / owner|[To be completed by the document owner]|
|Baseline identifiers|FR-01 to FR-25 (functional), US-01 to US-16 (user<br>stories), NFR-01 to NFR-08 (non-functional)|
|Derived identifiers (informative)|BR (business rules), ASM (assumptions), CON<br>(constraints), DEP (dependencies), OOS (out of<br>scope), OI (open issues)|



### Revision History 

|Version|Date|Author|Description|
|---|---|---|---|
|0.1|2026-10-28|[Author]|Initial draft: FR-01 to FR-25, US-01 to US-16, NFR-01|
||||to NFR-08, and supplementary specifications|



Page 1 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

## Document Conventions & IEEE 830 Compliance Map 

**Requirement language.** “Shall” marks a mandatory, verifiable requirement; “should” a strong recommendation; “may” or “could” an optional capability. Functional requirements carry a MoSCoW priority: **Must** (mandatory for the MVP release), **Should** (important; the release is viable without it, but a workaround is needed), **Could** (desirable if capacity allows), **Won’t** (explicitly excluded from this release; see §1.5). 

**Normative vs. informative.** Requirement tables (§2), user stories and scenarios (§3), and non-functional requirements (§4) are _normative_ . Anything labelled _Design note_ , _Proposed_ , or _Derived_ , and all of §5–§8, is _informative_ and does not change the baseline. 

**Verification methods. T** = Test, **I** = Inspection, **A** = Analysis, **D** = Demonstration. 

**Quoted UI strings** are English reference strings; they are localized per FR-21. 

**Standard note.** IEEE 830-1998 has been superseded by ISO/IEC/IEEE 29148 (2011, revised 2018). This SRS keeps the IEEE 830 structure as the project brief requires, and applies the 29148 quality attributes (unambiguous, verifiable, consistent, traceable) to every requirement. 

|IEEE 830 section|Location in this SRS|
|---|---|
|1.1 Purpose; 1.2 Scope|§1.1, §1.2, §1.5|
|1.3 Definitions, acronyms, abbreviations|§1.6|
|1.4 References; 1.5 Overview|§1.7, §1.8|
|2.1 Product perspective|§1.2, §5.1|
|2.2 Product functions|§1.2, §2|
|2.3 User characteristics|§1.3|
|2.4 Constraints; 2.5 Assumptions and<br>dependencies|§1.4|
|2.6 Apportioning of requirements|§1.5, §2.1|
|3.1 External interface requirements|§5.2|
|3.2 Functional requirements|§2, §3|
|3.3 Performance requirements|§4 (NFR-01, NFR-07)|
|3.4 Logical database requirements|§5.3|
|3.5 Design constraints|§1.4, §4|



Page 2 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|IEEE 830 section|Location in this SRS|
|---|---|
|3.6 Software system attributes|§4|
|3.7 Organizing the specific requirements|§2, §3 (by module and feature), §6|
|Appendices|§8|



## 1. Document Skeleton & Introduction (IEEE 830 Section 1 & 2 Alignment) 

### 1.1 Document Purpose & Project Vision 

**Purpose.** This SRS defines the functional, non-functional, and behavioural requirements of the Minimum Viable Product (MVP) of an AI-powered cryptocurrency and stock risk analysis application (“the System”). It is the reference baseline for product management, architecture, engineering, QA, security, and DevOps, and the source from which design, estimation, test planning, and release acceptance are derived. 

**Project vision.** Retail investors who hold volatile crypto assets alongside traditional equities rarely have tooling that shows how risky their _combined_ holdings are. Institutional-grade metrics such as volatility, concentration, and drawdown are hard to compute and harder to interpret. The System closes this gap by pairing deterministic quantitative risk metrics with LLM-generated, plain-language explanations, exposure warnings, and actionable recommendations, so that a non-expert can understand portfolio risk within minutes and act on it. 

**Core objective.** Enable a registered user to (1) create portfolios, (2) record stock and crypto positions, (3) view live valuation and PnL, (4) run an AI-assisted risk analysis, and (5) generate, export, and archive a consolidated risk report — securely, reliably, and in English or Russian. 

#### **Business value.** 

|Value driver|Description|Key requirements|
|---|---|---|
|Unified cross-asset|One place to assess crypto and equity|FR-05, FR-09, FR-12|
|risk view|exposure together||
|Explainable AI insights|Natural-language explanations lower<br>the expertise barrier, while figures stay<br>deterministic and auditable|FR-12, FR-13, FR-15|



Page 3 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Value driver|Description|Key requirements|
|---|---|---|
|Early warning|User-defined drawdown and<br>concentration thresholds|FR-14|
|Decision-ready<br>reporting|Consolidated reports, PDF export, and<br>a historical archive|FR-16, FR-17, FR-18|
|Trust and security|Hardened authentication, input<br>validation, and audit logging|FR-19, FR-20, FR-22,<br>NFR-02|
|Platform foundation|Documented REST API, i18n, and a<br>stateless, horizontally scalable<br>backend|FR-21, FR-23, NFR-08|



**MVP release criteria (proposed).** The MVP is releasable when (a) all _Must_ functional requirements and all _Must_ user-story scenarios pass acceptance testing, (b) NFR-01 to NFR-08 are met or carry a recorded waiver, and (c) every High-severity open issue in §7 is resolved or consciously accepted by the product owner. 

### 1.2 Project Scope (In-Scope Modules) 

The MVP comprises five functional modules plus cross-cutting platform capabilities, delivered as a browser-based web client, a stateless backend REST API, a relational data store, a cache, and integration adapters for external market-data and LLM services. 

|Module|The System**will**|The System**will not**<br>(MVP)|FRs|User<br>stories|
|---|---|---|---|---|
|**Auth**|Register users by email and|Offer social/SSO login,|FR-01|US-01, US-|
||password; authenticate<br>and issue an Access JWT<br>and Refresh Token;<br>terminate sessions;<br>recover passwords through<br>a time-limited emailed<br>token; optionally support<br>TOTP-based MFA|passwordless sign-in, or<br>administrator-<br>provisioned accounts|to FR-<br>04|02, US-03,<br>US-12|



Page 4 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Module|The System**will**|The System**will not**<br>(MVP)|FRs|User<br>stories|
|---|---|---|---|---|
|**Portfolio**|Create, read, update, and<br>delete portfolios of stocks<br>and cryptocurrencies; add<br>assets with identifier,<br>quantity, purchase price,<br>and timestamp; fetch and<br>cache market quotes; tag<br>portfolios by strategy|Import holdings from<br>brokers, exchanges, or<br>wallets; hold funds or<br>private keys; value<br>assets in several<br>currencies; share<br>portfolios|FR-05<br>to FR-<br>08|US-04, US-<br>05, US-13|
|**Position**|Calculate real-time<br>valuation and<br>Unrealized/Realized PnL;<br>record partial or full<br>closures; show target vs.<br>actual allocation|Execute orders;<br>compute taxes; model<br>fees, dividends, or<br>corporate actions|FR-09<br>to FR-<br>11|US-06, US-<br>07|
|**Risk**|Aggregate holdings;<br>compute volatility,<br>concentration, and<br>historical drawdown; obtain<br>LLM-generated risk<br>descriptions, warnings, and<br>recommendations; manage<br>alert thresholds; keep an<br>audit trail of analyses|Place or rebalance<br>trades; give<br>personalised regulated<br>investment advice;<br>forecast prices or<br>promise outcomes|FR-12<br>to FR-<br>15|US-08, US-<br>09, US-14|
|**Report**|Generate risk summary<br>reports with structured<br>text and asset-distribution<br>charts; export them to PDF;<br>archive them|Send scheduled or<br>emailed reports;<br>produce tax or<br>regulatory filings|FR-16<br>to FR-<br>18|US-10, US-<br>11, US-15|
|**Cross-**<br>**cutting**|Audit logging; payload<br>sanitization and validation;<br>English/Russian<br>localization; secure<br>password hashing;<br>documented REST API;<br>light/dark themes; rate<br>limiting|Provide an<br>administrative web<br>console (administrators<br>work through the<br>logging pipeline and<br>deployment tooling)|FR-19<br>to FR-<br>25|US-16|



Page 5 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

**System context.** The System serves end users through the web client and depends on four external parties: market-data provider(s) for stock and crypto quotes and history, an LLM provider for natural-language risk analysis, an email delivery service for password recovery, and a centralized logging pipeline for audit and error events. 

### 1.3 User Roles & Personas 

|Role|Description|Technical skill level|Primary goals|Stories|
|---|---|---|---|---|
|**R-01 Retail**<br>**Investor / User**|Self-registered<br>individual who<br>holds stocks and/or<br>cryptocurrencies<br>and wants to<br>understand and<br>monitor portfolio<br>risk|**Low to moderate.**<br>Comfortable with<br>web applications and<br>basic investing<br>vocabulary<br>(positions, PnL); no<br>quantitative-finance<br>or programming<br>background assumed.<br>A small subset of<br>power users may<br>consume the REST<br>API (FR-23) and are<br>moderately to highly<br>technical|Track holdings<br>and PnL;<br>understand<br>risk in plain<br>language; set<br>alert<br>thresholds;<br>produce,<br>export, and<br>archive<br>reports|US-01 to<br>US-15|
|**R-02 System**<br>**Administrator**|Operator<br>responsible for<br>deployment,<br>monitoring,<br>security auditing,<br>and incident<br>troubleshooting|**High.**Proficient with<br>Linux, containers<br>(Docker/Compose),<br>log aggregation,<br>monitoring, and<br>security hygiene|Audit access<br>events;<br>diagnose<br>errors; keep<br>the platform<br>available and<br>horizontally<br>scalable|US-16;<br>NFR-06<br>to NFR-<br>08|



**Persona snapshots.** _R-01_ is a mixed-asset investor who holds a handful of equities and several crypto assets on different platforms and tracks them in spreadsheets; they want a fast answer to “Am I over-exposed?” without learning financial jargon, and need plainlanguage explanations, visible data freshness, and exportable evidence. _R-02_ is an experienced DevOps/SRE engineer who needs structured logs free of secrets, a onecommand local environment, and stateless services that scale behind a load balancer. 

**Role aliases.** The role labels used in the user stories — _new user_ , _registered user_ , _user who forgot my password_ , _investor_ , _trader_ , _risk-conscious investor_ , and _authenticated user_ — all refer to **R-01** ; _System Administrator_ is **R-02** . 

Page 6 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

**Access model.** Users can reach only the portfolios, positions, analyses, reports, and thresholds they own (ASM-07). Administrators do not browse user business data through the application in the MVP. **External (non-human) actors:** market-data provider(s), LLM provider, email delivery service, and centralized logging pipeline. 

1.4 Business & Technical Assumptions, Constraints, and Dependencies 

#### **Assumptions** 

|ID|Assumption|Impact if invalid|
|---|---|---|
|ASM-<br>01|Users enter holdings manually; there is<br>no broker, exchange, or wallet<br>synchronization in the MVP|Data accuracy depends on user input;<br>automated import becomes a Phase 2<br>driver|
|ASM-<br>02|Portfolios are valued in a single base<br>currency (US dollars), consistent with<br>the dollar-denominated examples in US-<br>05|Multi-currency support needs FX data<br>sources and data-model changes|
|ASM-<br>03|External sources provide enough price<br>history for stocks and crypto to<br>compute volatility and drawdown|Risk metrics degrade or are<br>unavailable for illiquid or newly listed<br>assets|
|ASM-|Only aggregated quantitative metrics,|Privacy and regulatory exposure; data-|
|04|never personally identifying data, are<br>sent to the LLM provider (FR-12)|processing terms must be re-assessed|
|ASM-<br>05|LLM output is advisory and non-<br>deterministic; deterministic<br>calculations remain the source of truth<br>for every displayed figure|Inconsistent or hallucinated figures<br>would erode user trust|
|ASM-<br>06|Users run the latest stable Chrome or<br>Firefox with JavaScript enabled (NFR-<br>05)|Rendering defects on other browsers<br>are accepted|
|ASM-<br>07|Authorization is ownership-based: a<br>user can read and modify only their own<br>data|Role-based access control would have<br>to be introduced|
|ASM-<br>08|Timestamps are stored in UTC and<br>rendered in the user’s locale and time<br>zone|Inconsistent timestamps in valuation<br>and reports|



Page 7 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Assumption|Impact if invalid|
|---|---|---|
|ASM-|The 10-portfolio limit (US-04) is a|A per-portfolio position cap would|
|09|business rule; the 50-position workload<br>(NFR-01) is a test baseline, not a hard<br>cap per portfolio|need to be defined|



#### **Constraints** 

|ID|Constraint|Source|
|---|---|---|
|CON-01|**Regulatory.**The System provides informational<br>analytics, not personalised investment advice.<br>Applicable data-protection (e.g., GDPR),<br>financial-services, and crypto-asset rules must<br>be reviewed by qualified legal counsel before<br>public launch, and the UI must be able to display<br>disclaimers next to AI output|Project brief §1.4; OI-17|
|CON-<br>02|**Provider quotas.**Market-data and LLM calls are<br>subject to third-party rate limits and cost;<br>caching (FR-07) and timeouts (US-08) must keep<br>usage within quota|FR-07, US-08|
|CON-<br>03|**Deployment.**The full stack (frontend, backend<br>API, database, cache) starts with a single<br>`docker`<br>`compose up`; the backend is stateless|NFR-06, NFR-08|
|CON-<br>04|**Client.**Support is limited to the latest stable<br>Google Chrome and Mozilla Firefox|NFR-05|
|CON-<br>05|**Localization.**English and Russian|FR-21|
|CON-<br>06|**Security baseline.**bcrypt or Argon2 password<br>hashing, 15-minute Access JWT, password policy,<br>and payload validation|FR-20, FR-22, NFR-02, NFR-<br>03|
|CON-07|**Quality and service targets.**p95 below 2.0 s<br>under the reference workload, at least 60%<br>backend test coverage, and at least 99.5%<br>monthly availability|NFR-01, NFR-04, NFR-07|



Page 8 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

#### **Dependencies** 

|ID|Dependency|Principal risk|Design intent|
|---|---|---|---|
|DEP-<br>01|Third-party market-data<br>feeds (quotes and<br>history for stocks and<br>crypto)|Outages, latency,<br>delayed data, licensing<br>and quota limits,<br>inconsistent symbol<br>coverage|TTL cache; last-known-<br>price fallback with a “Data<br>delayed / cached”<br>indicator (US-06);<br>provider-adapter<br>abstraction; quota<br>monitoring|
|DEP-<br>02|LLM provider API (FR-13)|Unavailability, latency,<br>rate limits, model drift,<br>cost|10-second timeout with<br>retry prompt (US-08);<br>provider-agnostic adapter;<br>versioned prompts;<br>schema-validated<br>responses|
|DEP-<br>03|Transactional email<br>service (FR-03)|Delivery delays or<br>failures|Queued sending with<br>retries; neutral user-facing<br>messaging|
|DEP-|Docker Engine and|Environment variance|Pinned image versions;|
|04|Compose (NFR-06)|between developer<br>machines|documented<br>prerequisites; sample<br>environment file|
|DEP-<br>05|Centralized logging<br>pipeline (FR-19, US-16)|Log loss; accidental<br>secret leakage|Redaction before write;<br>buffered shipping|
|DEP-<br>06|PDF rendering<br>component (FR-17)|Chart fidelity; Cyrillic<br>glyph coverage for<br>Russian reports|Server-side rendering with<br>embedded Cyrillic-<br>capable fonts|
|DEP-|TOTP-compatible|Clock drift between|RFC 6238 implementation|
|07|authenticator apps and<br>libraries (FR-04)|server and device|with a tolerance window|



### 1.5 Out of Scope (Phase 2 & Beyond) 

The following items are explicitly excluded from the MVP (MoSCoW “Won’t” for this release). Each exclusion is a deliberate scoping decision, not an oversight. 

Page 9 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Excluded capability|Rationale|
|---|---|---|
|OOS-<br>01|Direct exchange or broker API<br>**automated trading**(order placement<br>and execution)|Regulatory, security, and liability<br>complexity; the MVP analyses risk but<br>never trades|
|OOS-<br>02|**Social trading network**features<br>(following, copy trading, public profiles,<br>leaderboards, shared feeds)|Not core to risk analysis; adds<br>moderation and privacy scope|
|OOS-<br>03|**Automated portfolio rebalancing**<br>**executions**(the System only<br>recommends and never executes)|Depends on OOS-01; FR-13 limits<br>output to recommendations|
|OOS-<br>04|Automated holdings import through<br>exchange, broker, or wallet integrations|Manual entry only (ASM-01)|
|OOS-<br>05|Custody and payments: holding funds,<br>deposits, withdrawals, or storing wallet<br>private keys, seed phrases, or exchange<br>API secrets|Security liability and regulatory scope|
|OOS-<br>06|Personalised regulated investment<br>advice and suitability or<br>appropriateness assessments|CON-01|
|OOS-<br>07|Asset classes beyond stocks and<br>cryptocurrencies (options, futures,<br>forex, bonds, commodities)|Stocks and crypto only (FR-05)|
|OOS-<br>08|Multi-currency valuation and FX<br>conversion|ASM-02|
|OOS-<br>09|Tax calculation, dividends and staking<br>income, fees, and corporate actions<br>(splits, mergers)|Data-model and accuracy complexity;<br>see OI-15|
|OOS-<br>10|Native mobile applications, and<br>browsers other than the latest stable<br>Chrome and Firefox|NFR-05|
|OOS-11|Social/OAuth/SSO login and<br>passwordless authentication|Auth is email and password only (FR-01,<br>FR-02)|
|OOS-<br>12|Administrative web console or user-<br>management UI|US-16 is satisfied through logging|



Page 10 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Excluded capability|Rationale|
|---|---|---|
|OOS-|Proprietary model training, hosting, or|The MVP consumes an external LLM|
|13|fine-tuning|through its API|
|OOS-|Shared portfolios, advisor–client roles,|Single-owner data model (ASM-07)|
|14|and other multi-user collaboration||
|OOS-|Push/SMS notifications and scheduled|Alert notification channel is pending|
|15|or emailed report delivery|(OI-04)|



### 1.6 Definitions, Acronyms, and Abbreviations 

|Term|Definition|
|---|---|
|**Access JWT**|Short-lived JSON Web Token presented as a Bearer<br>token to authorize API requests; valid for 15<br>minutes (NFR-02)|
|**AI risk module**|Backend component that prepares quantitative<br>metrics, calls the LLM, and validates and<br>normalizes the response|
|**API**|Application Programming Interface|
|**Argon2**|Memory-hard password-hashing function, winner<br>of the 2015 Password Hashing Competition,<br>specified in RFC 9106|
|**Asset**|Tradable instrument; a stock or a cryptocurrency in<br>the MVP|
|**Audit log**|Structured, append-only record of security-<br>relevant actions and system errors|
|**bcrypt**|Adaptive password-hashing function based on the<br>Blowfish cipher, with a built-in per-hash salt|
|**Cache**|Fast temporary store for market quotes and rate-<br>limit counters|
|**Concentration risk**|Risk arising from a large share of portfolio value<br>sitting in one asset, one asset class, or a group of<br>correlated assets|
|**CRUD**|Create, Read, Update, Delete|



Page 11 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Term|Definition|
|---|---|
|**Drawdown**|Decline from a peak to a subsequent trough in<br>value;_maximum drawdown_is the largest such<br>decline in a period|
|**Given/When/Then**|Gherkin-style notation for acceptance scenarios:<br>precondition, action, expected outcome|
|**HTTP-only cookie**|Cookie that browser scripts cannot read, which<br>limits token theft through XSS|
|**i18n**|Internationalization: designing software so that it<br>can be adapted to several languages and locales|
|**JWT**|JSON Web Token (RFC 7519)|
|**LLM**|Large Language Model|
|**MFA**|Multi-Factor Authentication|
|**MoSCoW**|Prioritization scheme: Must, Should, Could, Won’t|
|**MVP**|Minimum Viable Product|
|**NFR**|Non-Functional Requirement|
|**p95**|95th-percentile latency: 95% of requests complete<br>within this time|
|**PnL**|Profit and Loss._Unrealized_PnL is the gain or loss<br>on an open position at the current market price;<br>_Realized_PnL is the gain or loss locked in by closing<br>all or part of a position|
|**Portfolio**|User-defined collection of positions|
|**Position**|A holding of one asset in a portfolio, with quantity,<br>purchase price, and purchase timestamp; open<br>until fully closed|
|**Quote**|Current or last-known market price of an asset<br>from an external feed|
|**Refresh Token**|Longer-lived credential, kept in a secure HTTP-only<br>cookie, used to obtain new Access JWTs|
|**REST**|Representational State Transfer, an architectural<br>style for HTTP APIs|
|**SQLi**|SQL injection|



Page 12 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Term|Definition|
|---|---|
|**SRS**|Software Requirements Specification|
|**Stateless**|Design in which no per-user session state is held<br>inside a service instance between requests, so any<br>instance can serve any request|
|**TOTP**|Time-based One-Time Password (RFC 6238)|
|**UTC**|Coordinated Universal Time|
|**VaR**|Value at Risk: a statistical estimate of the maximum<br>loss over a time horizon at a given confidence level.<br>Not mandated by FR-12 in v0.1; a candidate metric<br>for later releases|
|**Volatility**|Standard deviation of asset or portfolio returns<br>over a window, usually annualized|
|**XSS**|Cross-Site Scripting|



### 1.7 References 

1. IEEE Std 830-1998 — _IEEE Recommended Practice for Software Requirements Specifications_ . 

2. ISO/IEC/IEEE 29148:2018 — _Systems and software engineering — Life cycle processes — Requirements engineering_ . 

3. RFC 7519 — _JSON Web Token (JWT)_ . 

4. RFC 6238 — _TOTP: Time-Based One-Time Password Algorithm_ . 

5. RFC 6265 — _HTTP State Management Mechanism_ (cookies), and its successor revisions. 

6. RFC 9106 — _Argon2 Memory-Hard Function for Password Hashing and Proof-of-Work Applications_ . 

7. OWASP Application Security Verification Standard (ASVS) and OWASP Top 10 — baselines for FR-20, FR-22, and FR-25. 

8. OWASP Password Storage Cheat Sheet — current hashing-parameter guidance for FR22. 

9. Project brief — _SRS v0.1 Specification & Task List_ (source of the baseline requirement identifiers). 

Page 13 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

### 1.8 Document Overview 

§2 specifies the 25 functional requirements by module. §3 gives the 16 user stories with Given/When/Then acceptance scenarios. §4 specifies the 8 non-functional requirements with measurable acceptance thresholds. §5 provides supplementary specifications (reference architecture, external interfaces, conceptual data model, derived business rules). §6 contains the traceability matrices, §7 records open issues and recommendations for v0.2, and §8 holds the appendices. 

## 2. Functional Requirements (FR-01 to FR-25) 

### 2.1 Conventions & Priority Summary 

Every requirement starts with “The system shall…” and carries a MoSCoW priority. “Traces to” links each requirement to user stories (US), non-functional requirements (NFR), and open issues (OI); “Ver.” gives the verification method (T, I, A, D). The _Requirement_ column is the normative baseline wording. 

|Priority|Count|Requirements|
|---|---|---|
|**Must**|10|FR-01, FR-02, FR-05, FR-06, FR-09, FR-12, FR-13, FR-<br>16, FR-19, FR-20|
|**Should**|7|FR-03, FR-07, FR-10, FR-14, FR-17, FR-21, FR-22|
|**Could**|8|FR-04, FR-08, FR-11, FR-15, FR-18, FR-23, FR-24, FR-<br>25|
|**Won’t**|0|None assigned in v0.1; exclusions are listed in §1.5|
|**Total**|**25**|All modules|



### 2.2 Module: Auth (Authentication & Security) 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**|Must|The system shall allow users to register|US-01; NFR-03; FR-22|T|
|**01**||using a unique email address and<br>password.|||
|**FR-**|Must|The system shall authenticate users and|US-02, US-12; NFR-02;|T|
|**02**||issue a short-lived Access JWT and<br>Refresh Token upon valid login.|OI-10||



Page 14 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**|Should|The system shall provide a password|US-03; OI-10|T|
|**03**||recovery mechanism via a time-limited<br>token sent to the user’s registered email.|||
|**FR-**|Could|The system shall support Multi-Factor|None in v0.1 (OI-06)|T, D|
|**04**||Authentication (MFA) via TOTP<br>authenticator applications.|||



#### _Design notes (informative)_ 

- **Uniqueness:** compare normalized (trimmed, case-folded) email addresses and back the rule with a database unique constraint. 

- **Tokens:** the Access JWT travels in the `Authorization: Bearer` header; the Refresh Token lives in a `Secure` , `HttpOnly` , `SameSite` cookie and is stored server-side as a hash so that it can be rotated on use and revoked on logout (US-12). Lifetime and rotation policy: OI-10. 

- **Password recovery:** single-use, high-entropy tokens stored hashed with a short expiry; respond identically whether or not the email is registered, to prevent account enumeration. 

- **MFA:** RFC 6238 TOTP with enrolment by shared secret or QR code, recovery codes, and a challenge step between credential verification and token issuance. 

### 2.3 Module: Portfolio (Portfolio Management) 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**<br>**05**|Must|The system shall allow users to create,<br>read, update, and delete (CRUD)<br>investment portfolios containing stocks<br>and cryptocurrencies.|US-04, US-13|T|
|**FR-**<br>**06**|Must|The system shall allow users to add<br>financial assets to a portfolio<br>specifying asset identifier, quantity,<br>purchase price, and timestamp.|US-05|T|
|**FR-**<br>**07**|Should|The system shall automatically fetch<br>and cache real-time market quotes for<br>stocks and crypto assets from external<br>financial APIs.|US-05, US-06; NFR-01;<br>OI-01, OI-11|T|



Page 15 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**|Could|The system shall allow users to tag or|None in v0.1 (OI-06)|T|
|**08**||group portfolios by custom investment<br>strategies.|||



#### _Design notes (informative)_ 

- **Asset identity:** keep a canonical asset master (symbol, asset class, market or provider identifier) so that identical tickers on different markets, and crypto symbols across chains, are not conflated. 

- **Quote service:** shared TTL cache, request coalescing, per-provider quota tracking, and freshness metadata (an `as_of` timestamp) returned to the client for the “Data delayed / cached” indicator (US-06). 

- **Business rules:** at most 10 portfolios per user (BR-06); quantity and purchase price greater than zero (BR-08); deleting a portfolio cascades to its positions (BR-14). 

### 2.4 Module: Position (Position Tracking) 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**<br>**09**|Must|The system shall calculate and display real-<br>time valuation and Unrealized/Realized PnL<br>(Profit and Loss) for every open position.|US-06; NFR-01;<br>OI-08|T|
|**FR-**<br>**10**|Should|The system shall allow users to record partial<br>or full position closures, automatically<br>updating realized PnL logs.|US-07; OI-08|T|
|**FR-11**|Could|The system shall calculate and display target<br>vs. actual portfolio asset allocation<br>percentages.|None in v0.1 (OI-<br>06)|T|



#### _Design notes (informative)_ 

- **Calculations:** market value = quantity × current price; cost basis = the sum of quantity × purchase price over the open quantity; Unrealized PnL = market value − cost basis; Unrealized PnL % = Unrealized PnL ÷ cost basis × 100; Realized PnL on closure = closed quantity × (closing price − cost per unit of the closed quantity). 

- **Cost-basis method:** the method used to attribute cost to partial closures (FIFO, weighted average, or specific lot) is not defined in the brief (OI-08). 

Page 16 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **Precision:** use arbitrary-precision decimal arithmetic, never binary floating point, for monetary values and quantities; crypto quantities commonly need eight or more decimal places. 

- **Allocation:** actual % = position market value ÷ total portfolio market value; how users enter target percentages is to be specified when FR-11 is scheduled. 

- **Outside the model:** fees, dividends, splits, and other corporate actions (OOS-09, OI15). 

### 2.5 Module: Risk (AI Risk Analysis) 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**<br>**12**|Must|The system shall aggregate<br>portfolio holdings and transmit<br>quantitative metrics (volatility,<br>concentration, historical<br>drawdown) to the AI risk module.|US-08; NFR-01; OI-02, OI-09|T, A|
|**FR-**<br>**13**|Must|The system shall generate<br>natural-language risk<br>descriptions, exposure warnings,<br>and actionable recommendations<br>using the LLM integration.|US-08, US-09; OI-03|T, D|
|**FR-**<br>**14**|Should|The system shall allow users to<br>configure custom alert thresholds<br>for maximum portfolio drawdown<br>or asset concentration limits.|US-14; OI-04|T|
|**FR-**<br>**15**|Could|The system shall maintain an<br>audit trail of historical AI risk<br>analysis executions for<br>performance comparison.|None dedicated; relates to<br>US-08 and FR-18 (OI-06, OI-12)|T|



#### _Design notes (informative)_ 

- **Metrics (indicative definitions):** _volatility_ is the annualized standard deviation of periodic log returns over a defined look-back window; _concentration_ is the largest single-asset weight, the top-N weight share, and the Herfindahl–Hirschman Index (the sum of the squared position weights); _historical drawdown_ is the maximum peak-totrough decline of the reconstructed portfolio value series over the look-back window. Equities (about 252 trading days a year) and crypto (365 days) annualize differently, so the methodology must be fixed explicitly (OI-09). 

Page 17 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **LLM contract:** the input is a metrics payload only, with no personal data or credentials (ASM-04). The output is schema-validated structured sections: summary, concentration, correlations, volatility, cross-asset balance, recommendations, and an overall risk level (Low / Medium / High). Figures shown to users are rendered from computed metrics, not copied from LLM prose (ASM-05). Prompts are versioned; calls are guarded by a 10-second timeout, bounded retries, and a circuit breaker; a provideragnostic adapter prevents vendor lock-in. 

- **Latency:** compute and render the deterministic metrics first, then present the LLM narrative progressively within the 10-second bound (OI-03). 

- **Audit-trail record (FR-15):** timestamp, user, portfolio, metrics snapshot, prompt version, model identifier, response, status, and latency. 

### 2.6 Module: Report (Reporting & Export) 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-16**|Must|The system shall generate comprehensive portfolio<br>risk summary reports featuring structured text and<br>visual asset distribution charts.|US-10|T, I|
|**FR-17**|Should|The system shall allow users to export generated<br>risk reports into downloadable PDF files.|US-11|T, I|
|**FR-18**|Could|The system shall store generated reports in a user-<br>accessible historical archive.|US-15; OI-12|T|



#### _Design notes (informative)_ 

- **Report anatomy:** header (portfolio, generation time, data-freshness indicator), valuation summary, asset-distribution chart(s), metrics table, AI assessment with risklevel indicator, and a disclaimer block (CON-01). 

- **PDF:** render server-side with embedded Cyrillic-capable fonts so that English and Russian reports look the same on screen and in print (DEP-06). 

- **Archive:** store immutable snapshots with metadata (portfolio, timestamp, analysis run); retention and deletion policy: OI-12. 

Page 18 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

### 2.7 Cross-Cutting & Technical System Requirements 

|ID|Priority|Requirement|Traces to|Ver.|
|---|---|---|---|---|
|**FR-**<br>**19**|Must|The system shall maintain structured<br>audit logs for security-critical actions<br>and backend system errors.|US-16|T, I|
|**FR-**<br>**20**|Must|The system shall sanitize and validate<br>all incoming API request payloads to<br>prevent injection attacks (SQLi, XSS).|US-01, US-04, US-05<br>(input scenarios)|T, A|
|**FR-**<br>**21**|Should|The system shall support<br>internationalization (i18n) with<br>English and Russian localizations.|All UI stories; OI-13|T, I|
|**FR-**<br>**22**|Should|The system shall store passwords<br>securely using industry-standard<br>hashing algorithms (bcrypt or<br>Argon2) with dynamic salts.|US-01; NFR-03; OI-05|I, T|
|**FR-**<br>**23**|Could|The system shall expose a<br>documented RESTful API for external<br>analytical tools.|None in v0.1 (OI-06, OI-14)|I, D|
|**FR-**<br>**24**|Could|The system shall support user-<br>selectable light and dark UI themes.|None in v0.1 (OI-06)|D|
|**FR-**<br>**25**|Could|The system shall implement API rate-<br>limiting on sensitive endpoints to<br>prevent brute-force attacks.|US-02; OI-05|T|



#### _Design notes (informative)_ 

- **Audit logging (FR-19):** JSON-structured entries with UTC timestamp, event type, user ID (if identified), client IP, correlation ID, and outcome; passwords, tokens, and key material are redacted before the entry is written (BR-15). 

- **Validation (FR-20):** schema-based allow-list validation at the API boundary, parameterized queries or an ORM for all database access, contextual output encoding in the client, and request-size limits. 

- **i18n (FR-21):** externalized message catalogs, locale-aware number, currency, and date formatting, and Russian plural rules (one / few / many). 

- **Password hashing (FR-22):** a unique random salt per password, with cost parameters taken from the current OWASP Password Storage Cheat Sheet at implementation time; note bcrypt’s 72-byte input limit. 

Page 19 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **REST API (FR-23):** an OpenAPI 3.x description, a versioned base path, a consistent error model, and the same Bearer-token authentication as the web client unless OI-14 decides otherwise. 

- **Rate limiting (FR-25):** applies to login, registration, password recovery, and MFA verification; counters live in the shared cache rather than process memory, to preserve NFR-08; responses use HTTP 429 with a `Retry-After` header. 

- **Themes (FR-24):** design tokens as CSS variables, defaulting to the operating-system preference, with the user’s choice persisted. 

## 3. User Stories (US-01 to US-16) with Acceptance Criteria 

### 3.1 Conventions & Story Index 

Stories use the format _As a <role>, I want to <goal>, so that <benefit>._ Every **Must** story carries three explicit Given/When/Then scenarios (the required minimum is two). The baseline defines no scenarios for **Should** and **Could** stories; where this document adds some, they are marked **Proposed** and remain informative until the product owner accepts them. Role labels in the stories are aliases of R-01 or R-02 (§1.3). 

|ID|Module|Title|Priority|Primary FRs|Scenarios|
|---|---|---|---|---|---|
|US-01|Auth|User registration|Must|FR-01, FR-20, FR-22|3|
|US-02|Auth|User login|Must|FR-02, FR-25|3|
|US-03|Auth|Password reset request|Should|FR-03|3 proposed|
|US-04|Portfolio|Create a portfolio|Must|FR-05, FR-20|3|
|US-05|Portfolio|Add assets to a portfolio|Must|FR-06, FR-07, FR-20|3|
|US-06|Position|View real-time values and PnL|Must|FR-07, FR-09|3|
|US-07|Position|Record a position closure|Should|FR-10|3 proposed|
|US-08|Risk|Execute an AI risk analysis|Must|FR-12, FR-13|3|
|US-09|Risk|Read structured AI risk explanations|Must|FR-13|3|
|US-10|Report|Generate a risk summary report|Must|FR-16|3|
|US-11|Report|Export a report to PDF|Should|FR-17|2 proposed|
|US-12|Auth|Log out|Must|FR-02 (partial; OI-06)|3|
|US-13|Portfolio|Delete a portfolio|Must|FR-05|3|
|US-14|Risk|Set drawdown alert thresholds|Should|FR-14|3 proposed|
|US-15|Report|Access the report archive|Could|FR-18|2 proposed|
|US-16|System|Log security events and errors|Must|FR-19|3|



Page 20 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

Priority distribution: 11 Must, 4 Should, 1 Could (16 stories). 

### 3.2 Story Specifications 

#### **US-01 — User Registration · Auth · Must** 

**Story:** As a new user, I want to register using my email and password, so that I can access my private dashboard. 

- **Scenario 1 (Successful Registration): Given** the user is on the registration page, **When** they enter a valid email address and a password meeting complexity rules and click “Register”, **Then** the system creates the user account and redirects them to the login screen with a success notification. 

- **Scenario 2 (Duplicate Email Error): Given** an account already exists with user@example.com, **When** a new user attempts to register with user@example.com, **Then** the system displays an error message stating “Email is already registered” and prevents account creation. 

- **Scenario 3 (Password Complexity Failure): Given** the user enters a password shorter than 8 characters, **When** they submit the registration form, **Then** the system displays a validation error indicating that the password must contain at least 8 characters. 

#### **US-02 — User Login · Auth · Must** 

**Story:** As a registered user, I want to log into the system with my credentials, so that I can access my saved portfolios and risk reports. 

- **Scenario 1 (Valid Login Credentials): Given** the user is on the login page, **When** they submit correct credentials, **Then** the system generates a JWT Access Token, sets a secure HTTP-only Refresh Token cookie, and grants access to the dashboard. 

- **Scenario 2 (Invalid Password): Given** the user enters a correct email but an incorrect password, **When** they click “Log In”, **Then** the system displays an “Invalid email or password” error message without disclosing which credential failed. 

- **Scenario 3 (Session Token Access): Given** the user has successfully authenticated and received an Access JWT, **When** they navigate to protected endpoints, **Then** the system authorizes requests using the Bearer token in the HTTP Authorization header. 

#### **US-03 — Password Reset Request · Auth · Should** 

**Story:** As a user who forgot my password, I want to request a password reset via email, so that I can regain access to my account. 

#### **Proposed scenarios (informative):** 

Page 21 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **Scenario P1 (Reset Email Issued): Given** a registered user on the “Forgot password” page, **When** they submit their registered email address, **Then** the system sends a single-use, time-limited reset link to that address and displays a neutral confirmation message. 

- **Scenario P2 (Unknown Email Handled Neutrally): Given** an email address that is not registered, **When** it is submitted on the “Forgot password” page, **Then** the system displays the same neutral confirmation message and sends no email. 

- **Scenario P3 (Expired Token): Given** a reset token whose validity period has elapsed, **When** the user opens the reset link, **Then** the system rejects the token and offers to request a new one. 

#### **US-04 — Create a Portfolio · Portfolio · Must** 

**Story:** As an investor, I want to create a new investment portfolio, so that I can organize and analyze distinct crypto and stock strategies. 

- **Scenario 1 (Create Named Portfolio): Given** an authenticated user on the portfolio page, **When** they click “Create Portfolio”, enter a valid name (e.g., “Crypto Growth”), and submit, **Then** the new portfolio is created and appears in the user’s active portfolio list. 

- **Scenario 2 (Empty Name Validation): Given** the portfolio creation form is open, **When** the user leaves the portfolio name blank and clicks “Save”, **Then** the system prevents submission and displays a field validation error. 

- **Scenario 3 (Max Portfolio Limit Check): Given** a user has reached the maximum allowed limit of 10 portfolios, **When** they attempt to create an additional portfolio, **Then** the system displays an information banner suggesting the removal of old portfolios before adding new ones. 

#### **US-05 — Add Assets to a Portfolio · Portfolio · Must** 

**Story:** As an investor, I want to add financial assets (crypto or stocks) to my portfolio, so that I can track my real investments in the system. 

- **Scenario 1 (Add Stock Asset): Given** an open portfolio view, **When** the user searches for ticker “AAPL”, enters a quantity of 10 and purchase price of $180, and saves, **Then** AAPL is added to the portfolio holding list with calculated total initial value. 

- **Scenario 2 (Add Crypto Asset): Given** an open portfolio view, **When** the user selects cryptocurrency “BTC”, inputs quantity 0.5 at $60,000, and submits, **Then** BTC is added to the portfolio and triggers initial valuation fetching. 

- **Scenario 3 (Negative Quantity Input): Given** the user is adding a position, **When** they input a negative quantity or negative purchase price, **Then** the system rejects the entry with an error message stating “Values must be greater than zero”. 

Page 22 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

#### **US-06 — View Real-Time Values and PnL · Position · Must** 

**Story:** As a trader, I want to view real-time market values and PnL metrics for all open positions, so that I can assess performance instantly. 

- **Scenario 1 (Live PnL Calculation): Given** a portfolio contains active holdings (e.g., AAPL, BTC), **When** the user views the positions dashboard, **Then** the system fetches current prices and displays calculated Unrealized PnL (percentage and absolute value) for each asset. 

- **Scenario 2 (API Quote Unavailable Fallback): Given** an external quote service is temporarily unreachable, **When** the position list renders, **Then** the system displays the last cached market price with a visual indicator showing “Data delayed / cached”. 

- **Scenario 3 (Total Portfolio Value Aggregate): Given** multiple positions exist in a portfolio, **When** the user views the summary header, **Then** the system displays the aggregate market value updated according to current individual position rates. 

#### **US-07 — Record a Position Closure · Position · Should** 

**Story:** As an investor, I want to record a partial or full position closure, so that I can lock in realized gains/losses and update remaining inventory. 

#### **Proposed scenarios (informative):** 

- **Scenario P1 (Partial Closure): Given** an open position of 10 units, **When** the user records a closure of 4 units at a given price, **Then** the open quantity becomes 6 units and the realized PnL for the 4 closed units is recorded. 

- **Scenario P2 (Full Closure): Given** an open position, **When** the user records a closure of the entire quantity, **Then** the position is marked closed, leaves the open-positions view, and its realized PnL is retained in the realized PnL log. 

- **Scenario P3 (Over-Closure Rejected): Given** an open position of 10 units, **When** the user attempts to close 12 units, **Then** the system rejects the entry with a validation error and changes no data. 

#### **US-08 — Execute an AI Risk Analysis · Risk · Must** 

**Story:** As a user, I want to execute an AI risk analysis on my portfolio, so that I can receive quantitative risk parameters and AI insights. 

- **Scenario 1 (Successful AI Execution): Given** a portfolio with populated positions, **When** the user clicks “Run AI Risk Analysis”, **Then** the system submits calculated metrics (volatility, concentration) to the LLM backend and renders the output within 2 seconds. 

Page 23 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **Scenario 2 (Empty Portfolio Trigger): Given** a newly created portfolio with zero added assets, **When** the user attempts to click “Run AI Risk Analysis”, **Then** the button is disabled with a tooltip stating “Add at least one asset to analyze risk”. 

- **Scenario 3 (LLM Service Timeout Handling): Given** the upstream LLM service fails to respond within 10 seconds, **When** the user requests a risk analysis, **Then** the system aborts the request safely, displays a retry prompt, and logs the service timeout event. 

#### **US-09 — Read Structured AI Risk Explanations · Risk · Must** 

**Story:** As a user, I want to read structured AI explanations of portfolio risks, so that I can understand threats regarding my crypto and stock asset concentration. 

- **Scenario 1 (Structured Insights Rendering): Given** the AI analysis engine finishes processing, **When** the user views the analysis results, **Then** the system displays text sections breaking down concentration risks, asset correlations, and volatility warnings. 

- **Scenario 2 (Multi-Asset Class Analysis): Given** a portfolio containing both highvolatility crypto and traditional stock equities, **When** the AI risk report generates, **Then** the report explicitly highlights cross-asset class balance and market exposure factors. 

- **Scenario 3 (Clear Formatting): Given** text risk recommendations are returned from the LLM, **When** displayed on screen, **Then** the text is formatted using standard headings, bullet points, and risk level indicators (Low / Medium / High). 

#### **US-10 — Generate a Risk Summary Report · Report · Must** 

**Story:** As a user, I want to generate a portfolio risk summary report, so that I have a single consolidated summary of my financial risks. 

- **Scenario 1 (Generate On-Screen Summary): Given** complete position and AI risk data, **When** the user clicks “Generate Report”, **Then** the system compiles a view containing asset distribution graphs, metric tables, and the AI assessment summary. 

- **Scenario 2 (Re-generating Report after Asset Update): Given** an existing report was generated earlier, **When** the user adds a new asset and clicks “Refresh Report”, **Then** the report updates all calculations and AI feedback to reflect the revised holdings. 

- **Scenario 3 (Missing Data Handling): Given** risk evaluation was not run prior to report creation, **When** the user attempts to view the full report, **Then** the system prompts the user to execute the AI risk analysis first. 

#### **US-11 — Export a Report to PDF · Report · Should** 

**Story:** As a user, I want to export my generated report to a PDF document, so that I can save or print my analysis for offline review. 

#### **Proposed scenarios (informative):** 

Page 24 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **Scenario P1 (Successful PDF Export): Given** a generated report is displayed, **When** the user clicks “Export to PDF”, **Then** the system produces a downloadable PDF containing the report’s text, charts, and metric tables. 

- **Scenario P2 (Export Without Report): Given** no report has been generated for the portfolio, **When** the user views the export control, **Then** the control is disabled or prompts the user to generate a report first. 

#### **US-12 — Log Out · Auth · Must** 

**Story:** As an authenticated user, I want to explicitly log out of the application, so that my session is terminated on shared devices. 

- **Scenario 1 (Standard Logout): Given** an active authenticated session, **When** the user clicks “Log Out”, **Then** the system clears access tokens from client storage, invalidates refresh tokens, and redirects to the login screen. 

- **Scenario 2 (Session Clearance Verification): Given** a user has logged out, **When** they click the browser’s “Back” button, **Then** the system blocks access to cached protected views and enforces re-authentication. 

- **Scenario 3 (Multi-Tab Logout): Given** the application is open across multiple browser tabs, **When** the user logs out from one tab, **Then** refreshing or navigating in the other tabs prompts the login screen. 

#### **US-13 — Delete a Portfolio · Portfolio · Must** 

**Story:** As an investor, I want to delete an unwanted portfolio, so that I can remove outdated strategies. 

- **Scenario 1 (Confirmed Deletion): Given** an existing portfolio selected by the user, **When** they click “Delete Portfolio” and confirm the modal warning, **Then** the portfolio and all connected position records are removed from the database and UI. 

- **Scenario 2 (Cancellation of Deletion): Given** the user clicked “Delete Portfolio” and the confirmation modal is open, **When** they click “Cancel”, **Then** the modal closes and no data is modified or deleted. 

- **Scenario 3 (Cascade Removal Verification): Given** a deleted portfolio contained 5 positions, **When** deletion executes, **Then** all 5 position records are cascade-deleted from backend persistence. 

#### **US-14 — Set Drawdown Alert Thresholds · Risk · Should** 

**Story:** As a risk-conscious investor, I want to set custom alert thresholds for portfolio drawdown, so that I am notified when portfolio value falls beyond target parameters. 

**Proposed scenarios (informative):** 

Page 25 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **Scenario P1 (Set Threshold): Given** an authenticated user viewing a portfolio, **When** they set a maximum drawdown threshold (e.g., 20%), **Then** the system saves the threshold for that portfolio. 

- **Scenario P2 (Threshold Breach Notification): Given** a threshold is configured, **When** the portfolio’s computed drawdown exceeds it, **Then** the system notifies the user through the channel defined under OI-04. 

- **Scenario P3 (Invalid Threshold): Given** the threshold form is open, **When** the user enters a value outside the 0–100% range, **Then** the system rejects the entry with a validation error. 

#### **US-15 — Access the Report Archive · Report · Could** 

**Story:** As a user, I want to access an archive of past risk reports, so that I can track my portfolio risk history over time. 

#### **Proposed scenarios (informative):** 

- **Scenario P1 (Archive Listing): Given** the user has generated several reports, **When** they open the report archive, **Then** the system lists the reports for that portfolio in reverse chronological order with generation date and risk level. 

- **Scenario P2 (Open Archived Report): Given** the archive is displayed, **When** the user opens an archived report, **Then** the system shows the report as it was generated, unaffected by later changes to the holdings. 

#### **US-16 — Log Security Events and Errors · System · Must** 

**Story:** As a System Administrator, I want the system to log security events and application errors, so that I can audit access and troubleshoot operational issues. 

- **Scenario 1 (Security Event Logging): Given** a failed login attempt or password change, **When** the event occurs, **Then** the system appends a structured log entry containing timestamp, IP address, user ID (if identified), and event type. 

- **Scenario 2 (Backend Exception Logging): Given** an unexpected internal error (HTTP 500) occurs in a REST endpoint, **When** caught by middleware, **Then** the stack trace and context are logged to the centralized logging pipeline without revealing raw technical traces to the user. 

- **Scenario 3 (Log Sanitization): Given** any logged system action, **When** log payloads are generated, **Then** passwords, JWT tokens, and private key data are redacted before writing to storage. 

Page 26 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

## 4. Non-Functional Requirements (NFR-01 to NFR-08) 

The baseline wording of each NFR is reproduced verbatim. The brief assigns no MoSCoW priority to NFRs, so they are treated as mandatory release criteria for the MVP unless a waiver is recorded in §7. Each entry adds a measurable acceptance threshold and a verification approach (informative). 

#### **NFR-01 — Performance (API & Calculation Latency)** 

**Requirement:** Portfolio risk calculation endpoints and database response time (p95) shall execute in less than 2.0 seconds under a standard test workload of 50 active positions per portfolio. 

- **Acceptance threshold:** p95 latency below 2.0 s. 

- **Verification (T, A):** automated load test of the risk-calculation and portfolio-read endpoints against a seeded portfolio of 50 positions; record p50, p95, and p99 and fail the build on regression. Concurrency, dataset, and reference hardware are to be fixed (OI-16). 

- **Traces to:** FR-09, FR-12; US-06, US-08. 

- **Note:** the LLM round-trip is outside this budget (OI-03). 

#### **NFR-02 — Security (Token Lifespan)** 

**Requirement:** Access JWT tokens shall have a strict expiration time of exactly 15 minutes from issuance. 

- **Acceptance threshold:** `exp` minus `iat` equals 900 seconds in every issued Access JWT; an expired token is rejected with HTTP 401. 

- **Verification (T):** unit test decoding issued tokens, plus an integration test with a controllable clock that confirms acceptance just before and rejection just after the 15minute mark. 

- **Traces to:** FR-02; US-02. 

#### **NFR-03 — Security (Password Policy)** 

**Requirement:** User passwords shall enforce a minimum length of at least 8 characters (including upper/lowercase letters and numbers). 

- **Acceptance threshold:** a password is accepted only if it has at least 8 characters including an upper-case letter, a lower-case letter, and a digit; enforcement is serverside, with client-side checks for usability only. 

- **Verification (T):** boundary tests at 7 and 8 characters and for each missing character class, at API and UI level. 

- **Traces to:** FR-01, FR-22; US-01. 

Page 27 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

**Note:** US-01 Scenario 3 exercises the length rule only (OI-07). 

#### **NFR-04 — Reliability & Code Quality** 

**Requirement:** Backend core business services shall maintain unit and integration test code coverage of at least 60%. 

- **Acceptance threshold:** combined unit and integration coverage of at least 60% across the backend core business services (Auth, Portfolio, Position, Risk, Report). 

- **Verification (I, A):** coverage report generated in CI with a build gate at 60%. Coverage type (line or branch) and the exact service scope are to be fixed (OI-16). 

- **Traces to:** all backend functional requirements. 

#### **NFR-05 — Browser Compatibility** 

**Requirement:** The web client shall render correctly on the latest stable versions of Google Chrome and Mozilla Firefox. 

- **Acceptance threshold:** every Must user-story scenario passes on the latest stable Chrome and Firefox, with no layout-breaking defects. 

- **Verification (T):** cross-browser end-to-end suite run on both browsers for each release, with browser versions recorded in the test report. 

- **Traces to:** UI scenarios of US-01 to US-16; OOS-10. 

#### **NFR-06 — Developer Experience (DevEx Deployment)** 

**Requirement:** The complete application stack (frontend, backend API, database, and cache) must launch in a local environment using a single command ( `docker compose up` ). 

- **Acceptance threshold:** on a clean machine with Docker and Compose installed, one command starts all four components, applies database migrations and seed data, and reaches healthy status without manual steps. 

- **Verification (D, T):** scripted fresh-clone smoke test in CI. 

- **Note:** because the System depends on third-party keys (market data, LLM), the default Compose profile should run against mock providers or documented placeholder variables so that the single-command promise holds without secrets (DEP-01, DEP-02). 

#### **NFR-07 — System Availability (Uptime)** 

**Requirement:** Core backend API services shall maintain a monthly operational availability target of at least 99.5% uptime. 

Page 28 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

- **Acceptance threshold:** availability = (total minutes − downtime minutes) ÷ total minutes, at least 99.5% per calendar month; that is at most about 3 h 36 min of downtime in a 30-day month. 

- **Verification (A):** external synthetic monitoring of a health endpoint and a representative authenticated call, with a monthly availability report. Exclusions (planned maintenance, third-party outages) are to be defined (OI-16). 

- **Traces to:** NFR-08; goals of R-02. 

#### **NFR-08 — Scalability** 

**Requirement:** The backend services must maintain a stateless design to allow horizontal scaling behind a standard load balancer. 

- **Acceptance threshold:** no request depends on in-process session state; two or more instances behind a load balancer serve traffic correctly, and stopping one instance causes no user-visible session loss. 

- **Verification (T, I):** CI scenario with at least two API replicas behind a load balancer, plus a design-review checklist (no local state or files, a shared cache for quotes and ratelimit counters, refresh sessions persisted in the data store, configuration through the environment). 

- **Traces to:** FR-07, FR-25; NFR-07. 

## 5. Supplementary Specifications (Informative) 

### 5.1 Product Perspective & Reference Architecture 

The System is a self-contained web application made of the logical components below. Technology choices are deliberately left open, except where the baseline fixes them (containerized stack, stateless backend). 

|Component|Responsibility|Related requirements|
|---|---|---|
|Web client|UI, i18n, themes, token handling,<br>client-side validation|FR-21, FR-24, NFR-05|
|Backend REST API<br>(stateless)|Authentication and authorization,<br>validation, orchestration, error<br>handling|FR-01 to FR-03, FR-20, FR-<br>23, NFR-08|
|Quote service|Provider adapters, TTL cache,<br>fallback, freshness metadata|FR-07, DEP-01|



Page 29 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Component|Responsibility|Related requirements|
|---|---|---|
|Calculation engine|Valuation, PnL, volatility,<br>concentration, drawdown|FR-09, FR-10, FR-12, NFR-01|
|AI / LLM adapter|Prompt assembly, provider calls,<br>timeout and retry, response<br>validation|FR-13, DEP-02|
|Relational data store|Users, sessions, portfolios,<br>positions, closures, thresholds,<br>analysis runs, reports|§5.3|
|Cache|Quotes and rate-limit counters|FR-07, FR-25|
|Report renderer|On-screen report assembly and PDF<br>export|FR-16, FR-17, DEP-06|
|Email gateway|Password-recovery messages|FR-03, DEP-03|
|Logging pipeline|Central audit and error logs with<br>redaction|FR-19, US-16|



### 5.2 External Interface Requirements 

- **User interface:** a browser-based client with these screens: Register, Login, Forgot/Reset Password, Portfolio List, Portfolio Detail (positions dashboard), Add Position, Close Position, AI Risk Analysis, Report View, Report Archive, Alert Thresholds, and Settings (language, theme, MFA). Validation errors appear next to the offending field, destructive actions require confirmation, and raw technical traces are never shown (BR-16). 

- **Software interfaces:** HTTPS/JSON integrations with the market-data provider(s) and the LLM provider through adapters; SMTP or HTTPS integration with the email service; log shipping to the centralized logging pipeline. 

- **Communications interfaces:** all client–server and server–provider traffic is expected to use TLS; the Refresh Token cookie carries the `Secure` , `HttpOnly` , and `SameSite` attributes. 

- **Hardware interfaces:** none. 

### 5.3 Logical Data Requirements (Conceptual) 

Conceptual entities only; the physical schema is a design-phase deliverable. 

Page 30 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Entity|Key attributes|Rules|
|---|---|---|
|User|id, email (unique), password hash,<br>locale, theme, MFA settings,<br>created at|Email unique (BR-01); every<br>data object has exactly one<br>owner|
|Refresh session|id, user id, token hash, issued at,<br>expires at, revoked at|Revoked on logout (BR-05)|
|Password-reset<br>token|id, user id, token hash, expires at,<br>used at|Single use; time-limited|
|Portfolio|id, user id, name, strategy tags,<br>created at|Name required (BR-07); at most<br>10 per user (BR-06)|
|Asset (master)|id, symbol, name, asset class<br>(stock or crypto), market or<br>provider reference|Shared reference data|
|Position|id, portfolio id, asset id, quantity,<br>purchase price, purchased at,<br>status|Quantity and price above zero<br>(BR-08); cascade-deleted with<br>the portfolio (BR-14)|
|Position closure|id, position id, quantity, closing<br>price, closed at, realized PnL|Closed quantity not above open<br>quantity (proposed, US-07)|
|Alert threshold|id, portfolio id, type (max<br>drawdown or concentration),<br>value|Range-validated (proposed, US-<br>14)|
|Risk analysis run|id, portfolio id, metrics snapshot,<br>prompt version, model id, output,<br>status, latency, created at|Basis of the FR-15 audit trail|
|Report|id, portfolio id, analysis run id,<br>content snapshot, created at|Archive entry (FR-18); retention<br>per OI-12|
|Quote (cache)|asset id, price, as-of timestamp,<br>source|TTL-based; carries a stale flag|
|Audit event (logging<br>pipeline)|timestamp, event type, user id (if<br>known), IP, correlation id,<br>outcome|Redacted before write (BR-15)|



### 5.4 Derived Business Rules 

The rules below were extracted from the baseline scenarios and NFRs so that developers and testers have a single reference. They add no new requirements. 

Page 31 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Rule|Source|
|---|---|---|
|BR-01|An email address can belong to only one account; a<br>duplicate registration is rejected with “Email is<br>already registered”|FR-01; US-01 S2|
|BR-02|Passwords need at least 8 characters, including<br>upper-case and lower-case letters and digits|NFR-03; US-01 S3|
|BR-03|A failed login never discloses which credential was<br>wrong; the message is “Invalid email or password”|US-02 S2|
|BR-04|The Access JWT expires exactly 15 minutes after<br>issuance and is sent as a Bearer token; the Refresh<br>Token is held in a secure HTTP-only cookie|NFR-02; US-02 S1, S3|
|BR-05|Logout clears client-side access tokens and<br>invalidates refresh tokens; protected views cannot<br>be reached with the Back button or from other<br>tabs|US-12 S1 to S3|
|BR-06|A user may own at most 10 portfolios|US-04 S3|
|BR-07|A portfolio name is required|US-04 S2|
|BR-08|Quantity and purchase price must be greater than<br>zero; otherwise “Values must be greater than zero”|US-05 S3|
|BR-09|If quotes are unavailable, the last cached price is<br>shown with the “Data delayed / cached” indicator|US-06 S2|
|BR-10|Risk analysis is disabled for a portfolio with no<br>assets, with the tooltip “Add at least one asset to<br>analyze risk”|US-08 S2|
|BR-11|An LLM call that does not answer within 10<br>seconds is aborted, a retry prompt is shown, and<br>the timeout is logged|US-08 S3|
|BR-12|Risk levels are expressed as Low, Medium, or High|US-09 S3|
|BR-13|A full report requires a prior AI risk analysis|US-10 S3|
|BR-14|Deleting a portfolio requires explicit confirmation<br>and cascades to all of its positions|US-13 S1 to S3|



Page 32 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Rule|Source|
|---|---|---|
|BR-15|Security and error log entries carry timestamp, IP|US-16 S1, S3|
||address, user ID (if identified), and event type;||
||passwords, JWTs, and private-key data are<br>redacted before storage||
|BR-16|Raw technical traces are never shown to end users|US-16 S2|



## 6. Requirements Traceability 

### 6.1 Functional Requirement Traceability Matrix 

|FR|Priority|User stories|Related NFRs|Related derived items|
|---|---|---|---|---|
|FR-01|Must|US-01|NFR-03|BR-01, BR-02|
|FR-02|Must|US-02, US-12|NFR-02|BR-03, BR-04, BR-05, OI-10|
|FR-03|Should|US-03|—|OI-10|
|FR-04|Could|—|—|OI-06|
|FR-05|Must|US-04, US-13|—|BR-06, BR-07, BR-14|
|FR-06|Must|US-05|—|BR-08|
|FR-07|Should|US-05, US-06|NFR-01, NFR-08|BR-09, OI-01, OI-11|
|FR-08|Could|—|—|OI-06|
|FR-09|Must|US-06|NFR-01|OI-08|
|FR-10|Should|US-07|—|OI-08|
|FR-11|Could|—|—|OI-06|
|FR-12|Must|US-08|NFR-01|BR-10, OI-02, OI-09|
|FR-13|Must|US-08, US-09|—|BR-11, BR-12, OI-03|
|FR-14|Should|US-14|—|OI-04|
|FR-15|Could|— (relates to US-08)|—|OI-06, OI-12|
|FR-16|Must|US-10|—|BR-13|
|FR-17|Should|US-11|—|DEP-06|
|FR-18|Could|US-15|—|OI-12|



Page 33 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|FR|Priority|User stories|Related NFRs|Related derived items|
|---|---|---|---|---|
|FR-19|Must|US-16|—|BR-15, BR-16|
|FR-20|Must|US-01, US-04, US-05|—|OI-06|
|FR-21|Should|All UI stories|—|OI-13|
|FR-22|Should|US-01|NFR-03|OI-05|
|FR-23|Could|—|—|OI-06, OI-14|
|FR-24|Could|—|—|OI-06|
|FR-25|Could|US-02|NFR-08|OI-05|



### 6.2 Coverage Observations 

- Every user story maps to at least one functional requirement. US-12 (log out) is covered only partially, by FR-02, because no requirement states session termination explicitly (OI-06). 

- Functional requirements with no dedicated user story: FR-04, FR-08, FR-11, FR-15, FR23, and FR-24 (OI-06). 

- Every Must functional requirement is exercised by at least one Must user story. FR-20 is exercised only indirectly, through the field-validation scenarios of US-01, US-04, and US-05, so SQLi- and XSS-specific tests should be added to the test plan. 

- Every NFR carries a measurable acceptance threshold and a verification approach (§4). 

## 7. Open Issues, Gaps & Recommendations (Informative) 

The review below records inconsistencies and gaps found while consolidating the baseline. None changes the baseline wording in v0.1; each is a recommendation for the v0.2 review. **Severity:** H = could block release or cause rework; M = needs a decision before the affected feature is built; L = clarification only. 

|ID|Sev.|Observation|Recommendation|Affects|
|---|---|---|---|---|
|OI-|H|**Priority inversion.**FR-07|Promote FR-07 to Must, or|FR-07, FR-|
|01||(Should) supplies the prices<br>that Must items FR-09 and<br>US-06 (and US-05 Scenario 2)<br>depend on|define a manual-price<br>fallback for the MVP|09, US-06|



Page 34 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Sev.|Observation|Recommendation|Affects|
|---|---|---|---|---|
|OI-<br>02|H|**Metric gap.**US-09 Scenarios<br>1 and 2 require asset<br>correlations and cross-asset-<br>class exposure, but FR-12<br>lists only volatility,<br>concentration, and historical<br>drawdown|Extend FR-12 with<br>correlation and asset-class<br>exposure metrics|FR-12, US-09|
|OI-<br>03|H|**Latency conflict.**US-08<br>Scenario 1 requires output<br>within 2 seconds, yet the<br>LLM timeout is 10 seconds<br>(US-08 Scenario 3) and NFR-<br>01 covers calculation<br>endpoints and database time<br>only; LLM round-trips often<br>exceed 2 seconds|State that the 2 seconds<br>apply to the deterministic<br>metrics, and that the LLM<br>narrative arrives<br>progressively within the 10-<br>second bound|US-08, NFR-<br>01|
|OI-<br>04|M|**Notification gap.**US-14<br>promises a notification, but<br>FR-14 covers only threshold<br>configuration; channel,<br>evaluation trigger, and<br>frequency are undefined|Add a functional<br>requirement for threshold<br>evaluation and in-app and/or<br>email notification|FR-14, US-14|
|OI-<br>05|H|**Security priorities.**FR-22<br>(password hashing) is Should<br>although Must items FR-01<br>and FR-02 depend on it; FR-<br>25 (rate limiting) is Could<br>although login is exposed to<br>brute force|Promote FR-22 to Must and<br>consider promoting FR-25|FR-22, FR-25|
|OI-<br>06|L|**Traceability gaps.**US-12 (log<br>out) has no dedicated FR; FR-<br>04, FR-08, FR-11, FR-15, FR-23,<br>and FR-24 have no dedicated<br>user story; FR-20 is exercised<br>only by field-validation<br>scenarios|Add a logout/session-<br>revocation FR; add stories or<br>confirm deferral; add SQLi-<br>and XSS-specific acceptance<br>scenarios|US-12, FR-04,<br>FR-20|



Page 35 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Sev.|Observation|Recommendation|Affects|
|---|---|---|---|---|
|OI-<br>07|M|**Password-policy coverage.**<br>NFR-03 requires upper-case,<br>lower-case, and digits, but<br>US-01 Scenario 3 tests length<br>only|Add scenarios for each<br>missing character class|NFR-03, US-<br>01|
|OI-<br>08|M|**Cost basis.**Realized PnL (FR-<br>10) needs a method for<br>attributing cost on partial<br>closures (FIFO, weighted<br>average, specific lot)|Select and document the<br>method|FR-09, FR-10|
|OI-<br>09|M|**Risk methodology.**Look-<br>back windows, return type,<br>annualization (about 252 vs.<br>365 days), correlation<br>method, and the thresholds<br>that map metrics to Low /<br>Medium / High are undefined|Publish a risk-methodology<br>appendix before<br>implementation|FR-12, FR-13,<br>US-09|
|OI-<br>10|M|**Token lifetimes.**Refresh-<br>token lifetime and rotation,<br>and the password-reset<br>token validity window, are<br>undefined (NFR-02 fixes only<br>the Access JWT)|Define values and the<br>rotation policy|FR-02, FR-03|
|OI-<br>11|M|**“Real-time” data.**FR-07, FR-<br>09, and US-06 say real-time,<br>yet free-tier providers are<br>often delayed or polled|Define the maximum quote<br>age and refresh interval per<br>asset class|FR-07, FR-09|
|OI-<br>12|M|**Data lifecycle.**US-13 deletes<br>positions, but the fate of<br>analysis runs, reports,<br>archive entries, and<br>thresholds is unspecified, as<br>are retention and erasure<br>rules (e.g., GDPR)|Define retention, deletion,<br>and data-export rules|FR-15, FR-18,<br>US-13|
|OI-<br>13|L|**i18n scope.**FR-21 does not<br>say whether AI narratives,<br>PDF reports, validation<br>messages, and logs are<br>localized|Clarify, and instruct the LLM<br>to answer in the user’s locale|FR-21, FR-13|



Page 36 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|ID|Sev.|Observation|Recommendation|Affects|
|---|---|---|---|---|
|OI-<br>14|L|**External API access.**FR-23<br>leaves authentication for<br>external tools (user tokens<br>vs. API keys), versioning, and<br>quotas open|Define the access model<br>before exposing the API|FR-23|
|OI-<br>15|M|**Asset universe and**<br>**corporate actions.**<br>Supported exchanges, ETFs,<br>and tokens are undefined;<br>splits and dividends are not<br>handled and can distort PnL|Define the supported<br>universe and document<br>known limitations|FR-05, FR-<br>09, OOS-09|
|OI-<br>16|L|**Measurability.**NFR-01 lacks<br>a concurrency and hardware<br>profile; NFR-04 lacks the<br>coverage type and scope;<br>NFR-07 lacks exclusions<br>(planned maintenance, third-<br>party outages)|Define the test profile,<br>coverage type, and SLO<br>exclusions|NFR-01,<br>NFR-04,<br>NFR-07|
|OI-<br>17|H|**Legal and regulatory.**The<br>classification of<br>recommendations (FR-13),<br>data-processing terms with<br>LLM and market-data<br>vendors, and crypto-asset<br>and data-protection rules<br>need legal review|Obtain counsel’s review and<br>add disclaimers before<br>public launch|CON-01, FR-<br>13|



## 8. Appendices 

### Appendix A — Baseline Completeness Check 

|Check|Result|
|---|---|
|FR-01 to FR-25 present, each starting “The|25 of 25 (10 Must, 7 Should, 8 Could)|
|system shall…” with a MoSCoW priority||
|US-01 to US-16 present in the required format|16 of 16 (11 Must, 4 Should, 1 Could)|



Page 37 of 38 

SRS v0.1 — AI-Powered Crypto & Stock Risk Analysis Platform 

|Check|Result|
|---|---|
|Every Must user story has 2–3|11 of 11 (3 scenarios each)|
|Given/When/Then scenarios||
|NFR-01 to NFR-08 present|8 of 8|
|Section 1 sub-sections 1.1 to 1.6 present|6 of 6 (plus 1.7 References and 1.8 Document<br>Overview)|
|Modules covered: Auth, Portfolio, Position,|6 of 6|
|Risk, Report, and cross-cutting||



### Appendix B — Suggested Next Steps (toward v0.2) 

1. Resolve, or consciously accept, the five High-severity open issues: OI-01, OI-02, OI-03, OI-05, and OI-17. 

2. Publish the risk-methodology appendix (OI-09) and the cost-basis decision (OI-08). 

3. Confirm or amend the proposed scenarios for US-03, US-07, US-11, US-14, and US-15. 

4. Define the performance-test profile, coverage metric, and availability exclusions (OI16). 

5. Derive the test plan and sprint backlog from §2, §3, and §6. 

### Appendix C — Review & Approval 

Page 38 of 38 


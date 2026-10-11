# Multi-Framework QA Automation Portfolio

[![CI Pipeline](https://github.com/usharinarhayanaa-gif/qa-automation-portfolio/actions/workflows/ci.yml/badge.svg)](https://github.com/usharinarhayanaa-gif/qa-automation-portfolio/actions/workflows/ci.yml)

A test automation engineering portfolio showcasing cross-framework coverage across **Playwright (TypeScript)** and **Selenium WebDriver (Java + TestNG)** targeting SauceDemo, Automation Exercise, and The Internet Herokuapp.

## 📁 Repository Architecture

```text
qa-automation-portfolio/
├── .github/workflows/
│   └── ci.yml                          # Dual-job CI pipeline for Playwright & Selenium
├── playwright-typescript-framework/    # Modern Web & REST API Framework
│   ├── src/pages/                      # Page Object Model implementations
│   ├── tests/
│   │   ├── saucedemo/                  # Data-driven auth, sorting, E2E checkout
│   │   ├── automationexercise/         # Catalog search, cart UI, REST API suite
│   │   └── theinternet/                # Alerts, iFrames, dynamic loading
│   └── playwright.config.ts
└── selenium-java-framework/            # Enterprise Web Framework
    ├── src/main/java/com/portfolio/qa/
    │   ├── pages/                      # Page Objects with explicit WebDriverWait
    │   ├── listeners/                  # TestListener for Base64 failure screenshots
    │   └── reports/                    # ExtentReports v5 Manager
    ├── src/test/java/com/portfolio/qa/
    │   └── tests/                      # SauceDemo, AutomationExercise, TheInternet
    ├── pom.xml
    └── testng.xml                      # Suite runner with listener bindings
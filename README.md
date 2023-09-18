# Selenium TestNG Java Framework

This is a test automation framework using Selenium, TestNG, and Java. It's designed to provide a robust and scalable solution for web-based automation tests.

## Features

- **Page Object Model (POM)**: A design pattern for maintainability and readability.
- **Cross-browser Testing**: Run tests on multiple browsers.
- **Parallel Execution**: Support for running tests in parallel using TestNG.
- **Reporting**: Built-in test reports using TestNG reporters.
- **Modularity**: Modular and reusable components for rapid test creation.
- **Logging**: Integration with logging utilities for comprehensive logs.

## Prerequisites

1. Java JDK (version 8 or higher).
2. Maven (used for project dependencies and build lifecycle).

## Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/bbagishbek/expero
2. **Navigate to project directory**:
    ```bash
   cd expero
3. Install dependencies:   
    ```bash
    mvn clean install

## Running Tests
Execute all tests using the following command:
    ```bash
        mvn test -DsuiteXmlFile=testng.xml
    ```
## Configuration
- Configuration files are located in src/main/resources/
- Update the config.properties file for default configurations like browser choice, application URL, etc.
- Browser drivers are managed by WebDriverManagerLibrary. Ensure the drivers match the browser versions you intend to use.

## Reporting
- Post test execution, check reports in target/surefire-reports/index.html.
![Screenshot](report_from_execution_on_09/17/2023.png)
- Logs can be found in the root project directory (test.log)
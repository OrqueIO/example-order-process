# Orqueio BPM - Example Order Process

This project demonstrates the integration between a **BPMN process** and a **DMN decision** using **Orqueio BPM Engine** and **Spring Boot**.  
It automates an order workflow that includes a DMN evaluation to determine the customer notification type and sends it automatically.

## Features

- Complete BPMN order processing workflow with multiple service tasks
- Payment capture, discount application, and order preparation automation
- Shipping and delivery payment handling
- Order cancellation support
- H2 embedded database (no external database setup required)
- Simple HTML form interface to create orders
- Spring Boot 4.0.0 integration
- Orqueio BPM Engine 2.0.0
- Lombok support for cleaner code

## Prerequisites

- Java 21
- H2 Database (embedded, no external setup required)

## Setup

### 1. Add Orqueio dependencies

To embed the Orqueio Engine with Enterprise Webapps and REST API, add the following Maven coordinates to your `pom.xml`:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.orqueio.bpm</groupId>
      <artifactId>orqueio-bom</artifactId>
      <version>${orqueio.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>io.orqueio.bpm.springboot</groupId>
    <artifactId>orqueio-bpm-spring-boot-starter-webapp</artifactId>
  </dependency>

  <dependency>
    <groupId>io.orqueio.bpm.springboot</groupId>
    <artifactId>orqueio-bpm-spring-boot-starter-rest</artifactId>
  </dependency>

  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>

  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
</dependencies>
```

### 2. Create the application class

Create an application class annotated with `@SpringBootApplication` and `@EnableProcessApplication`.
Include a `processes.xml` file under the `META-INF` directory.

```java
@SpringBootApplication
@EnableProcessApplication
public class OrderApplication {

    @Autowired
    private RuntimeService runtimeService;
    public static void main(String... args) {
        SpringApplication.run(OrderApplication.class, args);
    }
    @EventListener
    public void onPostDeploy(PostDeployEvent event) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("orderId", "ORD-1001");
        vars.put("premium", "High");
        vars.put("status", "VIP");

        runtimeService.startProcessInstanceByKey("OrderProcess", vars);
        System.out.println("Processus OrderProcess démarré avec les variables : " + vars);
    }
}
```

### 3. Add BPMN, DMN, and Forms

Place your BPMN and DMN files in the classpath — they will be automatically deployed and registered by the process application.
Add your HTML forms under the `/resources/static/forms` directory.

### 4. Add Delegate Classes

Implement your business logic inside Java Delegate classes.

Example:

```java
@Component("DecideNotification")
public class EvaluateNotificationDecisionDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String premium = (String) execution.getVariable("premium");
        String status = (String) execution.getVariable("status");
        VariableMap variables = Variables.createVariables()
            .putValue("premium", premium)
            .putValue("status", status);
        DecisionService decisionService = execution.getProcessEngineServices().getDecisionService();
        VariableMap result = (VariableMap) decisionService
            .evaluateDecisionTableByKey("NotificationDecision")
            .variables(variables)
            .evaluate()
            .get(0);
        Object notification = result.get("notification");
        execution.setVariable("notification", notification);
    }
}
```

Use the delegate in your BPMN model via the Implementation field: `orqueio:delegateExpression="#{sendNotification}"`

### 5. Configuration

The project uses H2 embedded database by default - no external database setup required.
The database is configured automatically by Spring Boot and Orqueio Engine.

### 6. Run the application and use Orqueio Platform

You can build the application with `mvn clean install` and then run it with the `java -jar` command.
You can also execute the application with `mvn spring-boot:run`.

Then you can access the Orqueio Webapps in your browser: `http://localhost:8080/` (provide login/password
from `application.yaml`, default: demo/demo).

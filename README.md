# Spring Boot - best practices example with RabbitMQ and Spring Cloud Stream

Many online sources show outdated/deprecated implementations of Spring Boot & messaging.

This is an attempt to show an easy way to use RabbitMQ using latest best practices of Spring Boot & Spring Cloud Stream.

Expands on the article [Event Driven Microservices With Spring Cloud Stream and RabbitMQ](https://medium.com/javarevisited/event-driven-microservices-with-spring-cloud-stream-and-rabbitmq-add4166fe223) and the [function-composition-rabbit](https://github.com/spring-cloud/spring-cloud-stream-samples/tree/main/multi-functions-samples/function-composition-rabbit) sample in the official Spring Cloud samples collection.

## Description

The Java code is not RabbitMQ specific. It is agnostic messaging, bound to a RabbitMQ service through configuration (`resources/application.yml`) and declaring the dependency `spring-cloud-stream-binder-rabbit`.

Code includes:

- Message consumer
- Message processor (consumes message -> publishes new message)
- Message publisher (using [StreamBridge](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_using_streambridge))
- [Dead letter queue](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_dlq_dead_letter_queue)
- REST endpoint: publishes request parameter value as new message
- Unit testing with [BDD Mockito](https://www.baeldung.com/bdd-mockito)
    - and [parametrized tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-sources-ValueSource)
- [Integration testing with Stream test binder](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_test_binder_usage)
- Use of [@Component](https://www.baeldung.com/spring-component-repository-service) and constructor based dependency injection
    - No XML needed to configure beans!
    - No use of `@Autowired`!
        - [Spring DI Patterns - The Good, the Bad, and the Ugly](https://dzone.com/articles/spring-di-patterns-the-good-the-bad-and-the-ugly)
        - [When To Not Autowire in Spring Boot](https://eng.zemosolabs.com/when-not-to-autowire-in-spring-spring-boot-93e6a01cb793)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.enabling)
    - [Spring Cloud Stream has Actuator bindings](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#binding_visualization_control)

## Install & Run instructions

- A running RabbitMQ server. (The app will automatically connect to it)
- `./mvnw clean install`
    - `mvnw` is a Maven wrapper included with Spring Initializr.
- `./mvnw spring-boot:run`

To use the message publisher, POST to `/text` with the parameter `text`.

Example: `curl -d "text=hi" -X POST http://localhost:8080/text -v`

The POST publishes a message to the exchange consumed by the `Function` bean `textToTextWrapperProcessor`. This bean is configured for its return value to be published as a message to the exchange which the `Consumer` bean `wrappedTextConsumer` consumes from.

## How to re-create a similar app

- [Spring Initializr](https://start.spring.io/)
    - Very convenient tool to quickly create a Spring Boot project.
- Add dependencies `spring-cloud-stream` and a binder such as `spring-cloud-stream-binder-rabbit`
- Add a [Function bean](https://spring.io/projects/spring-cloud-function#overview) and include it in the configured definitions: `spring.cloud.stream.function.definition: beanName`

To bind to a particular exchange & queue:

```yaml
spring:
  cloud:
    stream:
      function:
        definition: beanName
      bindings:
        beanName-in-0:
          # Creates a queue named `text.scsExample` bound to `text` exchange
          #  (queue name format is "[destination].[group]")
          destination: text  # name of the AMQP exchange to bind to
          group: scsExample
        beanName-out-0:
          # name of the AMQP exchange to publish to
          # Does not create a queue. If no queues exist but you wish to view published
          #   messages, you can manually create a queue bound to
          #   this exchange in the RabbitMQ UI.
          destination: text.wrapped
```

## Exchange & queue names conventions suggestions

- Name exchanges after the type of data that is published to them.
- Avoid the use of environment names in queue or exchange names.
    - Environments should be separated by using multiple virtual hosts or multiple actual RabbitMQ servers.
- Consider naming queues by combining the exchange name and the name of the app that declared the queue.
    - Example: a `device` exchange consumed by a Device Service app would be named `device.device-service`

## References

- Official:
    - [Spring Cloud Stream reference](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html)
    - [Spring Cloud Stream samples](https://github.com/spring-cloud/spring-cloud-stream-samples/)
        - Very similar to this repo, but simpler: https://github.com/spring-cloud/spring-cloud-stream-samples/tree/main/multi-functions-samples/function-composition-rabbit
    - [Functions for Spring Cloud Stream (part 1)](https://spring.io/blog/2020/07/13/introducing-java-functions-for-spring-cloud-stream-applications-part-0) and [part 2](https://spring.io/blog/2020/07/20/introducing-java-functions-for-spring-cloud-stream-applications-part-1)
- Articles:
    - [Event Driven Microservices with Spring Cloud Stream and RabbitMQ](https://medium.com/javarevisited/event-driven-microservices-with-spring-cloud-stream-and-rabbitmq-add4166fe223) by Medium (2021)

## Contributions

Any corrections or suggestions are welcome. Please raise a Github issue or contact the owner directly.

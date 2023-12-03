# Gamja: Simplifying Event Sourcing in Java

Gamja🥔는 Java 기반의 오픈 소스로 Domain-Driven-Design(DDD) 시스템을 쉽게 구축할 수 있도록 돕습니다. 이벤트 소싱을 위한 필수 구성 요소를 제공하여 개발자가 핵심 비즈니스 로직에 집중할 수 있도록 하며, 이를 통해 이벤트 소싱의 복잡성을 간소화합니다.

Gamja🥔 is an open-source, Java-based initiative designed to facilitate the easy construction of Domain-Driven Design (DDD) systems. By providing essential components for Event Sourcing, the project allows developers to concentrate on core business logic, thereby simplifying the intricacies of Event Sourcing.


## Key Features

- 
- 
- 

# Module Overview

1. **core**: 프로젝트의 기반을 이루는 핵심 모듈로 이벤트 소싱 애그리게이트에 필요한 작업을 수행하기 위한 구성 요소와 같은 필수 기능 제공

2. **identity**: 각 이벤트에 대해 고유한 식별자를 생성하는 `IdGenerator` 를 제공

3. **serializer**: 이벤트 객체를 외부에서 전송하거나 저장할 수 있는 형식으로 변환하는 Serializer 를 제공

4. **storage**: aggregate 의 초기 상태를 설정하고 새로운 Aggregate 인스턴스를 생성하는 `AggregateInitializer`, 발생한 이벤트를 저장하고 관리하는 `EventStorage`, aggregate 의 현재 상태를 조회하거나 저장된 이벤트로 aggregate를 재구성하는데 사용되는 `AggregateRepository` 등의 기능을 제공 

5. **test-supports**: 용이한 테스트 코드 작성을 위한 기능 제공



# Getting Started



# Documentation

For detailed information and examples, refer to our [Documentation](link-to-docs).

# Contributing

We welcome contributions! Check out our [Contribution Guidelines](link-to-contributing-guidelines) to get started. If you encounter any issues or have suggestions, please [create an issue](link-to-issue-tracker).

# License

This project is licensed under the [MIT License](link-to-license).




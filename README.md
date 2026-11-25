<h1 align="center">  
Offline Mode for Spring Boot 
</h1>
<p align="center">
Spring Boot Starter provides an Offline Mode for Spring Boot application.</p>

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=maciejkopecpl_offline-mode-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=maciejkopecpl_offline-mode-spring-boot-starter)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=maciejkopecpl_offline-mode-spring-boot-starter&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=maciejkopecpl_offline-mode-spring-boot-starter)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=maciejkopecpl_offline-mode-spring-boot-starter&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=maciejkopecpl_offline-mode-spring-boot-starter)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=maciejkopecpl_offline-mode-spring-boot-starter&metric=coverage)](https://sonarcloud.io/summary/new_code?id=maciejkopecpl_offline-mode-spring-boot-starter)
[![Build](https://github.com/maciejkopecpl/offline-mode-spring-boot-starter/actions/workflows/build.yml/badge.svg)](https://github.com/maciejkopecpl/offline-mode-spring-boot-starter/actions/workflows/build.yml)

## 🚀 Quick start

1. Add dependency to your project

Maven

```xml

<dependency>
    <groupId>pl.maciejkopec</groupId>
    <artifactId>offline-mode-spring-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

Gradle

```groovy
implementation 'pl.maciejkopec:offline-mode-spring-boot-starter:3.0.0'
```

2. Enable Offline Mode in `LEARNING` mode

application.yaml

```yaml
offline-mode:
  mode: learning
  enabled: true
```

3. Mark method with `@OfflineMode` annotation

```java

@Service
class DemoService {

    @OfflineMode
    public String demo() {
        return "test" + LocalDate.now();
    }
}
```

4. Run your application. This library will capture the returned data and store it in the configured directory (the default is `offline`).
5. Switch Offline Mode to `SERVING` mode.

application.yaml

```yaml
offline-mode:
  mode: serving
  enabled: true
```

When a matching response file exists, the `demo()` method won't be executed. Instead, its recorded return data will be
served from the file captured in the previous step. If no matching file exists, the method will execute normally.

See [documentation](https://github.com/maciejkopecpl/offline-mode-spring-boot-starter/wiki) for more examples.

## ✔️ Requirements
#### 3.X version
- Spring Boot 4
- Java 21

#### 2.X version
- Spring Boot 3
- Java 21

#### 1.X version
- Spring Boot 2
- Java 16

## 🧐 What's the purpose?

This starter is useful when you want to avoid calling external services, databases etc. There are a few possible
use-cases, e.g.

* when you cannot run all services your application depends on in the local environment, and you want to run your
  application without calling these services,
* when you want to avoid calling services that are billed per call,
* when your development environment is not stable and impacts the development of the service that you own,
* when data quality is poor or hard to replicate,
* when you are performing PoC,
* when the service you want to use is not ready yet, it is easy to mock the response of such service,
* and more!

### 💡Why not just use cache?

For the cache to work you first need to make a call. If the returned data depends on the input parameters, that becomes
a problem, because you need to make many calls to capture data. Also, the cache's main purpose is to avoid expensive
calls, once data is cached it doesn't change.

With this starter, you can prepare data on your own. You can run your application in `LEARNING` mode to speed this
process up, but nothing stops you from creating the response file from scratch. It is very easy to provide many
responses based on the input parameters, which makes this tool great for testing and development!

### ⚠️Limitations
Maps with complex keys are not supported.

### ⛔ Production use

This is not meant for production! The problems this starter helps to solve are mainly related to lower environments or
PoC-type projects. If you cannot depend on some service in production then you should look for a different solution.

It should not be a replacement for caching solution.


## 👨‍⚖️ License

[MIT](LICENSE)
  <p align="center" style="font-style: italic;">
Feel free to use any part from this repository.
</p>

<h1 align="center">  
Offline Mode for Spring Boot 
</h1>
<p align="center">
Spring Boot Starter providing an Offline Mode for Spring Boot application.
</p>

## 🚀 Quick start

1. Add dependency to your project

Maven

```
<dependency>
  <groupId>pl.maciejkopec</groupId>
  <artifactId>offline-mode-spring-boot-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Gradle

```
implementation 'pl.maciejkopec:offline-mode-spring-boot-starter:1.0.0-SNAPSHOT'
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

4. Run your application. This library will capture the returned data and stored serialized data in `offline` folder.
5. Turn Offline Mode in `SERVING` mode

application.yaml

```yaml
offline-mode:
  mode: serving
  enabled: true
```

The execution of `demo()` method won't happen now. Instead, the return data will be served from serialized file captured
in previous step.

## 🧐 What's the purpose?

This starter is useful when you want to avoid calling external services, databases etc. There are few possible use
cases, e.g.

* when you cannot run all services your application depends on in local environment, and you want to run your
  application without calling these services,
* when you want to avoid calling services that are billed per-call,
* when your development environment is not stable, and impacts development of service that you own,
* when data quality is poor or hard to replicate,
* when you are performing PoC,
* when the service you want to use is not ready yet, it is easy to mock the response of such service,
* and more!

### 💡Why not just use cache?

For cache to work you first need to make a call. If the returned data depends on the input parameters, that becomes a
problem. Also, cache main purpose is to avoid expensive calls, once data is cached it doesn't change.

With this starter, you can prepare data by your own. You can run your application in `LEARNING` mode to speed this
process up, but nothing stops you from creating the response file from scratch. It is very easy to provide many
responses based on the input parameters, which makes this tool great for testing and development!

### ⛔ Production use

This is not meant for production! The problems this starter helps to solve are mainly related with lower environments or
PoC type projects. If you cannot depend on some service in production then you should look for different solution.

It should not be a replacement for caching solution.

## 👨‍⚖️ License

[MIT](LICENSE)
  <p align="center" style="font-style: italic;">
Feel free to use any part from this repository.
</p>

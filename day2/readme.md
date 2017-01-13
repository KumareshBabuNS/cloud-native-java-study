# Cloud Native Java Workshop - 2.Making a Spring Boot application Production Ready

# Spring Boot Actuator

HTTP 엔드 포인트, JMX 또는 원격 쉘(SSH 또는 텔넷)을 통해 애플리케이션을 관리하고 관찰.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



## Endpoints ( [docs](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints) )

| 경로              | 설명                                       |
| --------------- | ---------------------------------------- |
| autoconfig      | 어떤 자동 구성 조건이 통과하고 실패하는지 나타내는 자동 구성 보고서   |
| beans           | 컨텍스트에 있는 모든 빈과 빈 사이의 관계                  |
| configprops     | 기본 값을 비롯하여 구성 프로퍼티에 빈이 어떻게 주입되었는지 나타냄    |
| dump            | 스레드 활동의 스냅샵 덤프 조회                        |
| env             | 모든 환경 프로퍼티 조회 (사용자이름, JRE, )             |
| health          | HealthIndicator 구현체가 제공하는 헬스 메트릭 (Status, DiskCpace, DB 등) |
| info            | info로 시작하는 사용자 정의 된 애플리케이션 정보 (git 최종 커밋 아이디,시간, 브랜치 정보) |
| mappings        | 모든 URI 경로와 해당 경로를 포함한 컨트롤러 매핑 (액추에이터 엔드포인트 포함) |
| metrics         | 메모리 사용량과 HTTP 요청 카운터 등 여러 애플맄에시녀 매트릭     |
| trace           | HTTP 요청의 timestamp, headers 등 기본 트레이스 정보 |
| shutdown (POST) | 애플리케이션을 종료 (not default)                 |
| +               | 등등                                       |



### Remote Shell

원격 쉘로도 모니터링 가능.

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-remote-shell</artifactId>
</dependency>
```

```shell
$ ssh user@localhost -p 2000
# autoconfig / beans / metrics
```



### HAL Browser

```xml
<dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-rest-hal-browser</artifactId>
</dependency>
```

hal+json 미디어 타입 응답을 조회할 수 있는 API 브라우저. 

- 루트 URI를 방문할 때 HAL 브라우저 페이지로 리다이렉트.



### JMX

```xml
# applicaton.properties
endpoints.jmx.domain=myapp
endpoints.jmx.unique-names=true

# VM Option
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=false
```



### Custom HealthIndicators

임의로 추가한 헬스인디케이터는 `/health` 에서 확인할 수 있다.

```java
@Bean
HealthIndicator healthIndicator() {
  int errorCode = check();
  if (errorCode != 0) {
    return () -> Health.down().withDetail("Error Code", ERROR_CODE).build();
  }

  return () -> Health.up().build();
}
```



## Metrics ( [docs](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints) )

Actuator 안에 포함 된 서비스. 애플리케이션의 메모리 상태의 다양한 게이지(단일 값), 카운터(증감) 을 제공.

이런 정보들을 [Redis](https://redis.io/), [OpenTSDB](http://opentsdb.net/),  [Statsd](https://github.com/etsy/statsd), [JMX](http://www.oracle.com/technetwork/articles/java/javamanagement-140525.html) 등으로 내보낼 수도 있음.



## Cloud Foundry ( Pivotal Web Service [Site](http://run.pivotal.io/) )

- PaaS (Platform as a Service).
- Cloud Foundry 를 사용하기 위해 Pivotal Web Service에 가입.

```shell
# PWS 인증
$ cf login -a https://api.run.pivotal.io

# 빌드 후 cf 배포
$ mvn -DskipTests=true clean install
$ cd target
$ du -hs res*jar
$ java -jar reservation-service.jar 
$ cf push -p reservation-service.jar my-app
```



## Graphite ( [Site](https://graphiteapp.org) )

```shell
$ eval $(docker-machine default)
$ ./bin/graphite.sh
```





## git-commit-id-plugin
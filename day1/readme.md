# Cloud Native Java Workshop - 1.Bootcamp

# 스프링부트 (Spring Boot)

```xml
// 스프링부트 POM.XML 예제

<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
  </dependency>
</dependencies>
```

- 스프링프레임워크 달리 스프링부트 에서는 pom.xml 에 `<dependency>` 버전을 일일이 명시하지 않음.
- 개발자는 사용 할 라이브러리를 고르기만 하면 라이브러리의 의존성(의존성 전이, 호환성) 까지도 스프링부트에서 모두 관리를 대신 함.
- 참고 [Spring Boot Reference Guide: 13.1 Dependency management](http://docs.spring.io/spring-boot/docs/1.4.3.RELEASE/reference/htmlsingle/#using-boot-dependency-management)

# 엔티티 (Entity)

```java
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private String reservationName;

    protected Reservation() {} // 기본 생성자의 접근 제어

    public Reservation(final String reservationName) {
        this.reservationName = reservationName;
    }

	// getters
}
```

- JPA/Hibernate 와 사용하기 위해서는 기본 생성자가 필요 (reflection 기술을 사용하기 때문)
- 단지 JPA를 위해 기본 생성자를 추가 했다면, 잘못 호출되는 것을 예방 하기 위해 `protected` 또는 package-private(default) 으로 접근제한.

# 레파지토리 (Repository)

```java
@RepositoryRestResource
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @RestResource(path = "by-name")
    Collection<Reservation> findByReservationName(@Param("rn") String rn);
}
```

- `JpaRepository` 인터페이스를 상속하는 것만으로 손쉽게 구현.
- Spring Data Core 에서 NoSQL을 위한 기술 / RDB를 위한 기술로 나누어짐.
- 메소드네이밍을 바탕으로 자동 질의문(Query) 생성.
- Spring Data Rest 기술의 경우 `@RepositoryRestResource` 어노테이션을 명시하면 별도의 Controller 구현 없이 레파지토리 리소스를 노출. ( 참고 [Spring Data REST: 5.2. The collection resource](http://docs.spring.io/spring-data/rest/docs/3.0.0.M1/reference/html/#repository-resources.collection-resource) )
- 위 예제 소스의 경우 `/reservations` 자동 생성(소문자, 복수형).

# CommandLineRunner

```java
@Bean
CommandLineRunner runner(ReservationRepository repository) {
  return args ->
    Arrays.asList("A,B,C,D,E".split(","))
    .forEach(x -> repository.save(new Reservation(x)));
}
```

- 스프링부트애플리케이션 시작 시, Bean을 스캔하며 `CommandLineRunner` 인터페이스를 구현하는 Bean 이나 리턴하는 Component 를 실행. ( 참고 [Spring Boot features 23.8 Using the ApplicationRunner or CommandLineRunner](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-spring-application.html#boot-features-command-line-runner) )


- `@Component`와 `@Bean`의 차이 (참고 [stackoverflow.com](http://stackoverflow.com/questions/10604298/spring-component-versus-bean) )

# HATEOAS

```json
{
  "_links" : {
    "reservations" : {
      "href" : "http://localhost/reservations{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost/profile"
    }
  }
}
```

- 하이퍼미디어 기반의 웹사이트는 요청에 대한 응답으로 하이퍼미디어 링크를 포함하여 해당 사이트의 REST 인터페이스를 동적으로 탐색할 수 있는 정보를 제공.
- 참고 [위키 백과: REST](https://ko.wikipedia.org/wiki/REST) 내용 중 애플리케이션의 상태에 대한 엔진으로서 하이퍼미디어 부분
- 참고 [마틴파울러 - Richardson Maturity Model (번역)](http://jinson.tistory.com/190)
- 참고 [https://spring.io/understanding/HATEOAS](https://spring.io/understanding/HATEOAS)
- 참고 [http://blog.woniper.net/219](http://blog.woniper.net/219)


# JOOQ

```shell
$ rm -fr gensrc
$ mvn clean generate-sources -Pgenerate
```

JPA 기반으로 되어 있는 샘플 소스를 JOOQ로 동작하도록 변경. 아래 샘플을 참고.

- [spring-boot-sample-jooq](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-samples/spring-boot-sample-jooq/README.adoc)

# 더 생각해 보면 좋을 주제들

스터디 진행 중 가볍게 토론 했던 주제지만 더 고민 해 보면 좋을 내용들.

- 의존 관계 주입 시에 `@Autowired` 어노테이션을 사용하는 방식과 `생성자를 통한` 방식의 장단점. ( 참고 [Why I Changed My Mind About Field Injection?](https://www.petrikainulainen.net/software-development/design/why-i-changed-my-mind-about-field-injection/) )
- 단위 테스트시 Mock 사용에 대하여 (Mockito 등).
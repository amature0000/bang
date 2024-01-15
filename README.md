# bang 보드 게임 온라인

- 프로젝트 참여자 : 박장우, 주현수
  - [pjw81226](https://github.com/pjw81226)
  - [amature0000](https://github.com/amature0000)

- build.gradle dependencies
```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	runtimeOnly 'com.h2database:h2'
	compileOnly 'org.projectlombok:lombok:1.18.28'
	annotationProcessor 'org.projectlombok:lombok:1.18.28'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0'

	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
}
```

- application.properties
```
jwt.token.secret= //64byte 이상 string Key 필요
spring.datasource.url=jdbc:h2:~/testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```
- 또는 시도 : datasource.url
```
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
```

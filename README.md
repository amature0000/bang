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
}
```

- application.properties
```
```

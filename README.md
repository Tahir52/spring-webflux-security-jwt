# Spring WebFlux with Jwt Authentication

This is a demo of a spring webflux application with spring security and JWT authentication, using spring boot libraries.
Written in TDD style, accessible from the commit history.\
A sample JWT and corresponding public key are provided in the tests.\

### Requirements
- Java 11
- **\[OPTIONAL\]** A JWKS Endpoint, e.g. [JWKS.json](src/test/resources/mappings/jwks.json)

### Tests
- Integration tests with all use cases are in [com.tahir.webfluxjwt.controller.ResourceControllerTest](src/test/java/com/tahir/webfluxjwt/controller/ResourceControllerTest.java)
- Test cases are written using a sample JWT as well as spring security test mocks 
- Run the tests using:
```shell
./mvnw test
```
OR
```bat
mvnw.cmd test
```

### Running the application
```shell
./mvnw spring-boot:run [--jwt.jwks.url=LINK_TO_JWKS_ENDPOINT] [--jwt.public-key=JWT_RSA_PUBLIC_KEY]
```
OR
```bat
mvnw.cmd spring-boot:run [--jwt.jwks.url=LINK_TO_JWKS_ENDPOINT] [--jwt.public-key=JWT_RSA_PUBLIC_KEY]
```


### Alternate Implementation References

- https://ard333.medium.com/authentication-and-authorization-using-jwt-on-spring-webflux-29b81f813e78
- https://medium.com/@jaidenashmore/jwt-authentication-in-spring-boot-webflux-6880c96247c7
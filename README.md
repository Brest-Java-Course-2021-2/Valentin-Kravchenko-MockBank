### Training Project for the Java Web Development Course 2021-2
![MockBank](https://img.shields.io/badge/MockBank-1.0-0d6efd?labelColor=lightgrey)
### Project Details
An overall description of the project is presented [here](/docs/srs/MockBank.md)
### Project Tech Stack
![Java](https://img.shields.io/badge/Java-11-007396?logo=java&)  
![Maven](https://img.shields.io/badge/Maven-3.8.1-C71A36?logo=apachemaven)  
![Spring Boot](https://img.shields.io/badge/SpringBoot-2.6.1-6DB33F?logo=springboot)  
![Spring Core](https://img.shields.io/badge/SpringCore-5.3.13-6DB33F?logo=spring)  
![Spring JDBC](https://img.shields.io/badge/SpringJDBC-5.3.13-6DB33F?logo=spring)  
![Spring MVC](https://img.shields.io/badge/SpringMVC-5.3.13-6DB33F?logo=spring)  
![Spring WebClient](https://img.shields.io/badge/SpringWebClient-5.3.13-6DB33F?logo=spring)  
![Hibernate Validator](https://img.shields.io/badge/HibernateValidator-6.2.0-59666C?logo=hibernate)  
![JUnit](https://img.shields.io/badge/JUnit-5.8.1-25A162?logo=junit5)  
![Spring Thymeleaf](https://img.shields.io/badge/SpringThymeleaf-3.0.12-005F0F?logo=thymeleaf)  
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?logo=bootstrap)  
![H2](https://img.shields.io/badge/H2-1.4.200-01B4E4)  
![Docker](https://img.shields.io/badge/Docker-20.10.11-2496ED?logo=docker)  
### Project Requirements
Docker Engine v20
### Run Application 
#### Create Docker Network
    docker network create mockbank-net
#### Run Rest Server
    docker run -d -p 8090:8090 --name mockbank-rest --network mockbank-net valvikx/mockbank-rest
The list of available REST endpoints is presented [here](/docs/curl/RestEndpoints.md) 
#### Run Client Application
    docker run -d -p 8080:8080 --env BASE_URL=http://mockbank-rest:8090 --name mockbank-web --network mockbank-net valvikx/mockbank-web    
Go to [http://localhost:8080/](http://localhost:8080/) for open a start page
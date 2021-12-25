## MockBank
### Training Project for the Java Web Development course 2021-2
### Project Details
An overall description of the project is presented [here](/docs/srs/MockBank.md)
### Project Tech Stack

### Project Requirements
Docker Engine v20
### Application Run
#### Create Docker Network
    docker network create mockbank-net
#### Run Rest Server
    docker run -d -p 8090:8090 --name mockbank-rest --network mockbank-net valvikx/mockbank-rest
#### Run Client Application
    docker run -d -p 8080:8080 --env BASE_URL=http://mockbank-rest:8090 --network mockbank-net --name mockbank-web valvikx/mockbank-web    
Go to [http://localhost:8080/](http://localhost:8080/) for open a start page
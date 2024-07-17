# About the application
The following document briefly describes different aspects of code that was implemented. You will find described in this document different choices which well taken during the implementation and why. You will also be instructed about how to run the application.

## The choice of the DB

I am aware that the code challenge stated that we should use a relational database but once I saw that we are dealing with ttl, I opted for redis (which is not relation database, I admit).
Redis was chosen for the following reasons:
1. It supports ttl out of the box
2. Do not reinvent the will. I know that there is a relation db (dynamodb) but I do not have an experience with them already. Choosing a normal relational db and implement ttl handling through threads would have been an overkill for me. 

## The algorithm to generate Ids
I wrote a service which generates ids from a fixed set of alphanumeric characters. I chose to proceed that way because:
1. I have full control of how ids are generated
2. Ids are generated in such way that the shortest available Id is the one which is always assigned.

As the algorithm uses recursion, it may become slower. It can be improved using principals of dynamic programming. 

## Tests
I used mockito to write test cases. For a reason that I do not know, certain tests simply fail. 

## Running the application
The application can be run as a normal spring-boot application using the following command: 

```
$ mvn spring-boot:run
```
For this to work one would need to have redis-server running on the machine which is not ideal. 

In order to fix this, I worked on including docker-compose which would package all the dependencies for the application to run without installing anything else (except docker). Unfortunately, the built application image fails to connect to the dockerized redis (I will try to find time and fix it no matter is the outcome of this challenge).

The command to run the docker through docker-compose is: 
```
$ docker-compose up -d
```

Note that swagger has been added to the code for easy testing. Swagger can be launch by running the following url in any brower of your choice: 
```
http://localhost:8080//swagger-ui.html
```
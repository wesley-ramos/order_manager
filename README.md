![Build](https://github.com/wesley-ramos/order_manager/workflows/Build/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/344d9f04dbf51b59b015/maintainability)](https://codeclimate.com/github/wesley-ramos/order_manager/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/344d9f04dbf51b59b015/test_coverage)](https://codeclimate.com/github/wesley-ramos/order_manager/test_coverage)

### Requirements
- Docker:24.0.6
- Docker compose: 2.20.3
  
# Context
This project is a simplified version of an order manager, its purpose is to demonstrate some skills for the selection process in which I am participating.

# Specifications
- All entities (User, Order, Item, StockMovement) must have an endpoint to create, update, delete and list;
- When an order is created, it should try to satisfy it with the current stock;
- When a stock movement is created, the system should try to attribute it to an order that isn't complete;
- Trace the list of stock movements that were used to complete the order, and vice-versa;
- Show current completion of each order;
- API should make by java 8 with Spring Boot + Spring JPA or Jave EE + Hibernate, PostgreSQL, GIT, log4j (or other);

# How to execute the project?
Just clone the repository and run docker compose in the project root.
```shell
docker-compose up -d
```
# What are the available endpoints?
Just access the api documentation at the URL below.
```shell
http://localhost:8080/docs
```
You can make requests there
![API](/docs/api.png).

# How to view stock movement assignments?
Just view the application logs
```shell
docker logs webapp -f
```
![API](/docs/logs.png).

## Points for improvements
- Add pagination on endpoints that list records.
- Add hyperlinks (HATEOAS).
- Add validation to the delete endpoints (Do not allow the deletion of records that are being used in the Order or StockMovement).
- Create service to remove assignments when an order or stock movement is deleted, ensuring information consistency.

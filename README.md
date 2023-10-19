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
- When an order is complete, send a notification by email to the user that created it;
- Trace the list of stock movements that were used to complete the order, and vice-versa;
- Show current completion of each order;
- Write a log file with: orders completed, stock movements, email sent and errors.
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

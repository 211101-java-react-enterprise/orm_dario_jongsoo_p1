# Custom Object Relational Mapping Framework

## Description

This project is to create a custom object relational mapping (ORM) framework. This framework will allow for a simplified and SQL-free interaction with the relational data source. the custom ORM is to abstract JDBC boilerplate logic from the application which uses it.

## Tech Stack

* Java 8
* JUnit
* Mockito
* Apache Maven
* Jackson library (for JSON marshalling/unmarshalling)
* Java EE Servlet API (v4.0+)
* PostGreSQL deployed on AWS RDS
* AWS CodeBuild
* AWS CodePipeline
* Git SCM (on GitHub)

## Features

* CRUD operations are supported for two domain objects via the web application's exposed endpoints
* JDBC logic is abstracted away by the custom ORM
* Programmatic persistence of entities (basic CRUD support) using custom ORM
* File-based or programmatic configuration of entities

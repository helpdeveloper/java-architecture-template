# 2. Use MySQL as the Primary Database

## Context
For this sample project, we need to decide on the primary database to use. We have two options: MySQL and MongoDB.

## Decision
We will use MySQL as the primary database for our project.

## Consequences
- MySQL is a widely used relational database management system.
- It provides strong data consistency and ACID compliance.
- It is well-supported and has a large community.
- Not possible to store unstructured data like MongoDB.
- Not as scalable as MongoDB for large datasets.

## Alternatives Considered
- MongoDB: A NoSQL database that stores data in flexible, JSON-like documents.
- PostgreSQL: An open-source relational database management system known for its extensibility and standards compliance.
- SQLite: A lightweight, serverless database engine that is self-contained and easy to set up.

## Rationale
We chose MySQL because it is a well-established relational database system with strong data consistency and ACID compliance. 
It is widely used and supported, making it a good choice for our project's requirements.

## Date
2024-12-31

## Status
Accepted

## Links
- [MySQL](https://www.mysql.com/)
- [MongoDB](https://www.mongodb.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [SQLite](https://www.sqlite.org/)
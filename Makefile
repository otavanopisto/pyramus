SHELL := /bin/bash

.PHONY: help
help:
	@echo ""
	@echo "make build			- Build the whole application"
	@echo "make clean			- Clean the whole application"
	@echo "make produce-ddl		- Produce the database definition from Entities"
	@echo ""

.PHONY: clean
clean:
	mvn clean

.PHONY: build
build:
	mvn clean compile

.PHONY: produce-ddl
produce-ddl:
	mvn clean process-classes -Pgen-ddl
	@echo ""
	@echo "Find DDL in pyramus/target/sql"

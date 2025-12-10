FROM maven:3.6.3-jdk-14
WORKDIR /tests
COPY . .
CMD mvn clean test
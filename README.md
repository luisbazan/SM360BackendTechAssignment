
# SM360 Backend Tech Assignment

REST service for managing listings for online advertising service.

## Run Locally

Clone the project

```bash
  git clone https://github.com/luisbazan/SM360BackendTechAssignment.git
```

Go to the project directory

```bash
  cd SM360BackendTechAssignment
```

Install dependencies

```bash
  Java 11
```

Start the server

```bash
  ./gradlew bootRun
```


## Swagger Documentation

[Documentation](http://localhost:8080/api/v1/swagger-ui/index.html)

## Generate Java Documentation
```bash
  ./gradlew javadoc
```
## Running Tests

To run tests, run the following command

```bash
  ./gradlew test
```


## Improvements

We need to add spring security for the apis
and spring actuator in order to know the health and metrics of apis. 
# bookingserv

	docker-compose up -d
	(OR)
	docker run -it sinagarajan/bookingserv:2.2

## Sample Requests and Responses
Following are the sample requests and responses for bookingserv application

##### SUCCESS REQUEST ( POST )

	curl -k -X POST -H "X-IDEMPOTENCY-ID:ab4eb03b-6b8d-41f7-a859-2a98e441265f" -H "Content-type:application/json" -d '{"first_name":"siva","last_name":"n","dob":"18/09/1987","totalPrice":9,"checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","deposit":200,"address":{"line1":"30","line2":"md","city":"chennai","state":"tn","country":"India","zipcode":"600100"}}'  "http://localhost:8080/v1/bfs/booking"

	{"id":1,"first_name":"siva","last_name":"n","dob":"18/09/1987","checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","totalPrice":9,"deposit":200,"address":{"line1":"30","line2":"md","city":"chennai","state":"tn","country":"India","zipcode":"600100"}}

##### SUCCESS REQUEST ( GET )

    curl -k "http://localhost:8080/v1/bfs/booking"

    [{"id":1,"first_name":"siva","last_name":"n","dob":"18/09/1987","checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","totalPrice":9,"deposit":200,"address":{"line1":"30","line2":"md","city":"chennai","state":"tn","country":"India","zipcode":"600100"}},{"id":3,"first_name":"siva","last_name":"n","dob":"18/09/1987","checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","totalPrice":9,"deposit":200,"address":{"line1":"30","line2":"md","city":"chennai","state":"tn","country":"India","zipcode":"600100"}}]

##### DUPLICATE REQUEST (Returns original response)
    curl -k -X POST -H "X-IDEMPOTENCY-ID:ab4eb03b-6b8d-41f7-a859-2a98e441265f" -H "Content-type:application/json" -d '{"first_name":"siva","last_name":"n","dob":"18/09/1987","totalPrice":9,"checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","deposit":200,"address":{"line1":"30","line2":"md","city":"chennai","state":"tn","country":"India","zipcode":"600100"}}'  "http://localhost:8080/v1/bfs/booking"

	{"id":1,"first_name":"siva","last_name":"n","dob":"18/09/1987","checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","totalPrice":9,"deposit":200,"address":{"line1":"30","line2":"md","city":"chennai","state":"tn","country":"India","zipcode":"600100"}}

##### BAD_REQUEST
    curl -k -X POST -H "X-IDEMPOTENCY-ID:ab4eb03b-6b8d-41f7-a859-2344e4416453d" -H "Content-type:application/json" -d '{"first_name":"siva","last_name":"","dob":"09/1987","totalPrice":-2,"checkin":"2021-10-10T12:00:00-0700","checkout":"2021-10-21T12:00:00-0700","deposit":-1,"address":{"line1":"30","city":"chennai","state":"tn","country":"India","zipcode":"600100"}}'  "http://localhost:8080/v1/bfs/booking"

    {"status":"BAD_REQUEST","timestamp":"24-07-2021 10:51:41.507.50.5","message":"Validation error","subErrors":[{"object":"booking","field":"deposit","rejectedValue":-1,"message":"must be greater than or equal to 0"},{"object":"booking","field":"dob","rejectedValue":"09/1987","message":"must match \"([0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/[0-9]{4}\""},{"object":"booking","field":"totalPrice","rejectedValue":-2,"message":"must be greater than or equal to 0"},{"object":"booking","field":"lastName","rejectedValue":"","message":"size must be between 1 and 255"}]}


## Application Design Overview
bookingserv system design has following modules (For simplicity, design diagram is limited to the flow of data for POST, same design applies for GET calls as well)

<img src="https://github.com/sinagarajan/bookingserv/blob/main/Design.jpeg?raw=true" alt="drawing" width="450"/>

## Idempotency 
bookingserv system is designed as an idempotent receiver. 
- User need to pass an optional header value X-IDEMPOTENCY-ID to enforce idempotency
- bookingserv ignores duplicate requests with the same idempotency token and returns the original request's response for the duplicate request
- bookingServ has a cache that expires in 15 minutes to store the idempotency tokens and the corresponding responses

## Application Functionalities
bookingserv is a spring boot rest application which would provide the CRUD operations for `Booking` resource.

bookingserv exposes 2 functionalities as REST APIs
- Create a new booking in the system with following details
    - first_name - First name of the user
    - last_name - Last name of the user
    - dob - Date of Birth in DD/MM/YYYY format
    - checkin - Date & Time of Checkin in yyyy-MM-dd'T'HH:mm:ssZ format
    - checkout - Date & Time of Checkout in yyyy-MM-dd'T'HH:mm:ssZ format
    - deposit - Deposit amount (Any value >= 0)
    - totalPrice - Total price for the booking (Any value >= 0)
    - address - Address details of the user. It contains line1,line2,city,state, country and zipcode. line2 is optional parameter
      User can send an optional Idempotency token (X-IDEMPOTENCY-ID) in the request header to avoid duplicate processing.
- Get all bookings to retrieve all the bookings from the system
_____________________________________________________________________________________________________

## Application Overview
bookingserv is a spring boot rest application which would provide the CRUD operations for `Booking` resource.

There are three modules in this application
- bookingservApi - This module contains the interface.
    - `v1/schema/booking.json` defines the booking resource.
    - `jsonschema2pojo-maven-plugin` is being used to create `Booking POJO` from json file.
    - `BookingResource.java` is the interface for CRUD operations on `Booking` resource.
        - POST `/v1/bfs/booking` endpoint defined to create the resource.
- bookingservImplementation - This module contains the implementation for the rest endpoints.
    - `BookingResourceImpl.java` implements the `BookingResource` interface.
- bookingservFunctionalTests - This module would have the functional tests.

## How to run the application
- Please have Maven version `3.3.3` & Java 8 on your system.
- Use command `mvn clean install` to build the project.
- Use command `mvn spring-boot:run` from `bookingservImplementation` folder to run the project.
- Use postman or curl to access `http://localhost:8080/v1/bfs/booking` POST or GET endpoint.

## Assignment
We would like you to enhance the existing project and see you complete the following requirements:

- `booking.json` has only `name`, and `id` elements. Please add `date of birth`, `checkin`, `checkout`, `totalprice`, `deposit` and `address` elements to the `Booking` resource. Address will have `line1`, `line2`, `city`, `state`, `country` and `zip_code` elements. `line2` is an optional element.
- Add one more operation in `BookingResource` to Get All the bookings. `BookingResource` will have two operations, one to create, and another to retrieve all bookings.
- Implement create and get all the bookings operations in `BookingResourceImpl.java`.
- Please add the unit tests to validate your implementation.
- Please use h2 in-memory database or any other in-memory database to persist the `Booking` resource. Dependency for h2 in-memory database is already added to the parent pom.
- Please make sure the validations done for the requests.
- Response codes are as per rest guidelines.
- Error handling in case of failures.
- Implement idempotency logic to avoid duplicate resource creation.

## Assignment submission
Thank you very much for your time to take this test. Please upload this complete solution in Github and send us the link to `bfs-sor-interview@paypal.com`.

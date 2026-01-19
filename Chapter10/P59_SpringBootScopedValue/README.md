# Migrating a Spring Boot app to `StructuredTaskScope` and `ScopedValue`
Write a Spring Boot application representing a website of a cable company (_CableOne_) that provides 
clients with the possibility to check online the health of Internet, mobile, and television services.
 The logged client (e.g., Mark) clicks on a button to start the check (`http://localhost:8080/healthcheck/Mark`).
 The website performs the health check of the services for that client and provides the results via email. 
Each health check request is labeled by a generated ticket (a `UUID` string).
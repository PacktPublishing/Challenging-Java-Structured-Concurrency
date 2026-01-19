# Implementing a HTTP web server 
Creating an HTTP web server in Java is relatively straightforward thanks to the `HttpServer` class, 
which is part of the `com.sun.net.httpserver` package. Use this API to implement an HTTP web server 
that allows us to choose between platform threads and virtual threads, as well as decide on either non-locking or locking mechanisms.
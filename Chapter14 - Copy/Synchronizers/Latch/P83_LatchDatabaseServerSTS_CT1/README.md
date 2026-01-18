# Challenge Time 
In the context of the previous application, let’s consider that the parent thread is 
a virtual thread. Practically, we initiate the database server via the following 
code: 

```
Thread.ofVirtual().start(() -> {
  try {
    new DatabaseServer().startDatabaseServer();
  } catch (InterruptedException ex) {}
});
```

The main thread will not wait for this virtual thread to complete since the virtual 
thread is a daemon thread. Modify the previous snippet of code to rely on 
`CountDownLatch` for ensuring the completion of the virtual thread (parent thread).
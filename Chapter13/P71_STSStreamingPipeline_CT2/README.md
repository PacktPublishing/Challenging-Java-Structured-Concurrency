# Challenge time 
In the previous example, we have used `ExecutorService` and `Future` to implement 
our streaming pipeline. Challenge yourself to rewrite this code by replacing 
`ExecutorService` with `StructuredTaskScope`, and `Future` with `Subtask`.
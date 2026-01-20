# Challenge Time 
Consider an array of 10 threads that are waiting to acquire a lock for a task. 
While these threads are dormant, another thread determines that some of them 
will not be needed. This thread will interrupt those unnecessary threads, and 
each interruption should be logged accordingly. The remaining threads will then 
execute the task once and terminate. Implement this scenario in code using the 
`lockInterruptibly()` method. 
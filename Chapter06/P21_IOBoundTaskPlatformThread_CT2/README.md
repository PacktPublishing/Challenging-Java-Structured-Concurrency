# Challenge time 
Consider that you have several I/O bound tasks equal to the number of available processors. 
Next, execute all these tasks in a thread-per-request fashion with executors 
(`newThreadPerTaskExecutor(defaultThreadFactory()`), and `newVirtualThreadPerTaskExecutor()`, 
and without executors (create and start the threads directly). Do it first using only platform 
threads and then using virtual threads. Next, perform the same thing, but switch from I/O 
bound tasks to CPU-bound tasks. Finally, pool the conclusions.
# Challenge Time 
Modify the previous application to display the state of each subtask as in the following example: 

```
Downloaded file_0 by VirtualThread[#28]/runnable@ForkJoinPool-1-worker-4  
Downloaded file_3 by VirtualThread[#35]/runnable@ForkJoinPool-1-worker-4  
Downloaded file_1 by VirtualThread[#30]/runnable@ForkJoinPool-1-worker-4  
Closing scope ...  
SUCCESS  
SUCCESS  
FAILED  
SUCCESS  
FAILED  
FAILED
```

So, here, we had 6 subtasks; 3 of them were completed successfully, and 3 failed.
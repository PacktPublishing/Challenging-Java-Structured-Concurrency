# `mapConcurrent()` doesn’t throw `InterruptedException`
When we talk about thread interruptions, we need to distinguish between the thread that executes the stream 
and the virtual threads internally spawned by `mapConcurrent()` to achieve maximum concurrency. 
Next, provide a snippet of code that interrupts the thread that executes the stream and explain the behavior.
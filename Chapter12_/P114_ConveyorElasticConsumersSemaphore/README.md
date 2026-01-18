# Elastic number of consumer threads
Let’s assume that the producer threads can provide workload spikes that the consumer threads should
 handle during these moments by increasing their number. After the producer threads reduce the workload,
 the consumers should decrease as well. Provide an implementation of this scenario.
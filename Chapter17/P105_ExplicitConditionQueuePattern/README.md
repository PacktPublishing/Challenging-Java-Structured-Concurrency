# Using condition queues/variables pattern 
Consider the following real-life scenario: we need to implement an application that provides spelling checks
 for a given text. The application should count and highlight misspelled words.
 In the free or trial version, users are allowed a maximum of 10 misspellings per round
 and must wait 10 seconds before a new round of checks becomes available. Therefore, every
 10 seconds, a new round of 10 misspellings is accessible to non-premium clients. Provide
 an implementation of this scenario via condition queues/variables pattern.
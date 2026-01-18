# Challenge time  
In the previous examples, we waited for both ECS services to return a result. However, if we 
want to retrieve only the first result (regardless of which ECS service it comes from) and ignore 
the other one, we can modify our assembler using `anySuccessfulOrThrow()`. Give it a try and 
implement this approach.
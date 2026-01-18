# Challenge time  
Consider the following scenario: You have to develop a mobile application that finds the first 
courier available in the current area. For this, the application relies on `invokeAny()` to 
concurrently contact several couriers with a timeout of 3 seconds. The application returns the 
first courier that is available or a message that informs the user about the current status of 
their request. 
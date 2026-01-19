# Challenge time  
What will happen if a `ScopedValue` is used outside its intended scope? For instance, what occurs 
if the `electricChargeLocator()`, responsible for communication with the server of the 
ElectricCharge company, attempts to query the `CE_PRIVATE_ECS_DISCOUNT` scoped value, which is 
specific to the CarElectric company?  
The answer is straightforward: a `NoSuchElementException` will be thrown, and there will be no 
request sent to the ElectricCharge server. However, according to our current design—which 
you may feel free to improve—we will not see this exception, meaning we won’t be aware of it. 
Currently, our design suppresses this exception under a `PrivateECSException`.  
With this in mind, please modify the application to allow the `NoSuchElementException` to be 
thrown and ensure it is captured in the logs. 
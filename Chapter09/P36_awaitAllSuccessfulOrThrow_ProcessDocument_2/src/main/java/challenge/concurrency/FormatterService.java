package challenge.concurrency;

public final class FormatterService {
   
    private FormatterService() {
        throw new AssertionError("Cannot be instantiated");
    }
    
    public static boolean format() {
        
        if(Math.random() < 0.5d) { throw new  FormatServiceException(); }
        
        return true; // false
    }
}

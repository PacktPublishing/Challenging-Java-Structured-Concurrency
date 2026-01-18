package challenge.concurrency;

public final class ValidatorService {

    private ValidatorService() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static Validator validate() {
        
        if(Math.random() < 0.5d) { throw new  ValidatorServiceException(); }
        
        return Validator.VALID_A; // VALID_B, VALID_C, NOT_VALID
    }
}

package challenge.concurrency;

import java.util.UUID;

final class IdGenerator {   
    
    private IdGenerator() {
        throw new AssertionError("Cannot be instantiated");
    }
    
    static String fetchStringId() {
        return UUID.randomUUID().toString();
    }    
}
package challenge.concurrency;

import java.util.List;

public final class SpellingService {
   
    private SpellingService() {
        throw new AssertionError("Cannot be instantiated");
    }
    
    public static List<Spelling> spellingCheck() {
        
        if(Math.random() < 0.5d) { throw new  SpellingServiceException(); }
        
        return List.of(new Spelling(), new Spelling(), new Spelling());
    }
}

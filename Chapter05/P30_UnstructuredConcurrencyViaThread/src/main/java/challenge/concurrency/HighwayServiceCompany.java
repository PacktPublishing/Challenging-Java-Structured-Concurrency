package challenge.concurrency;

public class HighwayServiceCompany {
    
    private final String name;    

    public HighwayServiceCompany(String name) {
        this.name = name;
    }
    
    public String signHighwayPart(HighwaySignPartType bpt) {
        return name + " - " + bpt;
    }

    public String getName() {
        return name;
    }        
}

package challenge.concurrency;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class Part {

    private final String name;
    private final Callable<Integer> partcall;
    private Set<Part> siblings = new HashSet<>();

    @SuppressWarnings("unchecked")
    public Part(String name, Callable<Integer> partcall, Part... siblings) {
        this.name = name;
        this.partcall = partcall;
        this.siblings = Set.of(siblings);
    }

    public String getName() {
        return name;
    }

    public Set<Part> getSiblings() {
        return siblings;
    }

    public Callable<Integer> getPartcall() {
        return partcall;
    }

    @Override
    public String toString() {
        return name + siblings;
    }
}

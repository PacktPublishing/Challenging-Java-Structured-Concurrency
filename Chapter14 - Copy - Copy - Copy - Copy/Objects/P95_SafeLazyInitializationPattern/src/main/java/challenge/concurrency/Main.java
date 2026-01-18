package challenge.concurrency;

public class Main {
	// asta ca o introducere
//
    public static void main(String[] args) {
        Foo foo = new Foo();
        Foo.Buzz buzz1 = foo.new Buzz();
        Foo.Buzz buzz2 = foo.new Buzz();
        Foo.Buzz buzz3 = foo.new Buzz();
        
        System.out.println();
        
        OldStyleConnection osc1 = OldStyleConnection.get();
        OldStyleConnection osc2 = OldStyleConnection.get();
        OldStyleConnection osc3 = OldStyleConnection.get();
        
        System.out.println();
        
        NewStyleConnection nsc1 = NewStyleConnection.get();
        NewStyleConnection nsc2 = NewStyleConnection.get();
        NewStyleConnection nsc3 = NewStyleConnection.get();
    }
}

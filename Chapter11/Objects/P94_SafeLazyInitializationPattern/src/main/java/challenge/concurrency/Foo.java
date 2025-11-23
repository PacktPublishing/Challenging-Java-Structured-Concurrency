package challenge.concurrency;

public class Foo {

    public class Buzz {

        {
            System.out.println("Non-static block initializer ...");
        }

        static {
            System.out.println("Static block initializer ...");
        }
    }
}

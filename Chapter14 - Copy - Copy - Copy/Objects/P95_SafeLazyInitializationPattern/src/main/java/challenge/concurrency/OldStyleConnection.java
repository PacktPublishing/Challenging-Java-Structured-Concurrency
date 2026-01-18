package challenge.concurrency;

public class OldStyleConnection { // singleton

    private OldStyleConnection() {
    }

    private static class LazyConnection { // holder

        static final OldStyleConnection INSTANCE = new OldStyleConnection();

        static {
            System.out.println("Initializing connection (old style) ..." + INSTANCE);
        }
    }

    public static OldStyleConnection get() {
        return LazyConnection.INSTANCE;
    }
}

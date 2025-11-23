package challenge.concurrency;

public class NewStyleConnection { // singleton

    private NewStyleConnection() {
    }

    public static NewStyleConnection get() {

        class LazyConnection { // holder

            static final NewStyleConnection INSTANCE = new NewStyleConnection();

            static {
                System.out.println("Initializing connection (JDK 16+ style) ..." + INSTANCE);
            }
        }

        return LazyConnection.INSTANCE;
    }
}

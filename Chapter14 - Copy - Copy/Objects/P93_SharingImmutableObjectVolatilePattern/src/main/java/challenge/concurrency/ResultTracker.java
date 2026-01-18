package challenge.concurrency;

public class ResultTracker {
    
    private volatile Result result = new Result(1, 2025);
    
    public void newResult(int id, int value) {
        result = new Result(id, value);
    }

    public Result getResult() {
        return result;
    }        
    
    static final class Result {
        
        private final int id;
        private final int value;

        public Result(int id, int value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Result{" + "id=" + id + ", value=" + value + '}';
        }                
    }
}

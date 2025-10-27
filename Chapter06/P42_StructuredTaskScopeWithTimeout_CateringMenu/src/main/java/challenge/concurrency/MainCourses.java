package challenge.concurrency;

public final class MainCourses {

    private MainCourses() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static MainCourse orderMainCourse() {
                
        if(Math.random() < 0.1d) { throw new  MainCourseException(); }
        
        try { Thread.sleep((long) (Math.random() * 1500)); } catch (InterruptedException ex) {}
        
        return new MainCourse();
    }
}

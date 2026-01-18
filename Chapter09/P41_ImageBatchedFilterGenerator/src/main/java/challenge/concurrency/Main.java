package challenge.concurrency;

import java.io.File;
import java.util.List;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<File> images = List.of(new File("img1.png"), new File("img2.png"),
                new File("img3.png"), new File("img4.png"), new File("img5.png"));
        
        batchingImgs(images);
    }

    private static void batchingImgs(List<File> images) throws InterruptedException {

        try (var scope = open(Joiner.<File>awaitAll())) {

            for (File image : images) {
                scope.fork(() -> applyImageFilters(image));
            }

            scope.join();
        } 
    }

    private static void applyImageFilters(File image) {
        
        try (var scope = open(Joiner.<File>awaitAll())) {
            
            Subtask<File> clarendonSubtask = scope.fork(() -> applyClarendon(image));
            Subtask<File> junoSubtask = scope.fork(() -> applyJuno(image));
            Subtask<File> larkSubtask = scope.fork(() -> applyLark(image));
            
            try { scope.join(); } catch (InterruptedException ex) {}
            
            if(clarendonSubtask.state() == FAILED 
                    && junoSubtask.state() == FAILED 
                    && larkSubtask.state() == FAILED) {
                logger.warning(() -> "No filter could be applied to the image " + image);
            }
        }
    }
    
    private static void applyClarendon(File image) {
        if (Math.random() < 0.5d) { throw new RuntimeException("The Clarendon filter cannot be applied to " + image); }
        logger.info(() -> "Apply the filter Clarendon to " + image);
    }
    
    private static void applyJuno(File image) {
        if (Math.random() < 0.5d) { throw new RuntimeException("The Juno filter cannot be applied to " + image); }
        logger.info(() -> "Apply the filter Juno to " + image);
    }
    
    private static void applyLark(File image) {
        if (Math.random() < 0.5d) { throw new RuntimeException("The Lark filter cannot be applied to " + image); }
        logger.info(() -> "Apply the filter Lark to " + image);
    }
}

package challenge.concurrency;

import java.io.IOException;
import java.lang.ScopedValue.Carrier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final ScopedValue<Integer> SRC_BEGIN = ScopedValue.newInstance();
    private static final ScopedValue<Integer> SRC_END = ScopedValue.newInstance();
    private static final ScopedValue<char[]> DST = ScopedValue.newInstance();
    private static final ScopedValue<Integer> DST_BEGIN = ScopedValue.newInstance();
    private static final ScopedValue<Path> PATH = ScopedValue.newInstance();
    
    public static void printSubchars(String str) {
        
        str.getChars(SRC_BEGIN.orElse(0), SRC_END.orElse(str.length()),
                DST.orElseThrow(IllegalArgumentException::new),
                        DST_BEGIN.orElse(0));   
        
        logger.info(Arrays.toString(DST.orElse(new char[0])));
    }        

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info("ScopedValue/Runnable");
        logger.info("--------------------");

        Carrier SRC_BEGIN_CR = ScopedValue.where(SRC_BEGIN, 0);
        Carrier SRC_END_CR = ScopedValue.where(SRC_END, 5);
        Carrier DST_CR = ScopedValue.where(DST, new char[5]);
        Carrier DST_BEGIN_CR = ScopedValue.where(DST_BEGIN, 0);

        "Hello World".getChars(
                SRC_BEGIN_CR.get(SRC_BEGIN), SRC_END_CR.get(SRC_END), 
                DST_CR.get(DST), DST_BEGIN_CR.get(DST_BEGIN));

        logger.info(Arrays.toString(DST_CR.get(DST)));

        ScopedValue.where(SRC_BEGIN, 0).where(SRC_END, 5)
                .where(DST, new char[5]).where(DST_BEGIN, 0).run(() -> printSubchars("HelloWorld"));  
        
        System.out.println();
        logger.info("ScopedValue/CallableOp");
        logger.info("----------------------");
        
        ScopedValue.CallableOp<List<String>, IOException> readFileOp 
                = () -> Files.readAllLines(
                        Path.of("src\\main\\java\\challenge\\concurrency\\Main.java"));
        
        List<String> files;
        try {            
            files = readFileOp.call();
            logger.info(() -> "Lines number: " + files.size());
        } catch (IOException ex) {
            logger.severe(ex.toString());
        }
        
        Carrier PATH_CR = ScopedValue.where(PATH, 
                Path.of("src\\main\\java\\challenge\\concurrency\\Main.java"));
        
        ScopedValue.CallableOp<List<String>, IOException> readFileOpSV 
                = () -> Files.readAllLines(PATH_CR.get(PATH));
        
        List<String> filessv;
        try {            
            filessv = readFileOpSV.call();
            logger.info(() -> "Lines number: " + filessv.size());
        } catch (IOException ex) {
            logger.severe(ex.toString());
        }        
    }
}

package challenge.concurrency;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int elem = 14;
    private static final int[] arr = new int[]{1, 3, 6, 8, 10, 12, 14, 16, 20, 22};

    private static int comparator(MemorySegment i1, MemorySegment i2) {   
        
        //try { Thread.sleep(1000); } catch (InterruptedException ex) {} // added to force pinning
        
        return Integer.compare(i1.get(ValueLayout.JAVA_INT, 0),
                i2.get(ValueLayout.JAVA_INT, 0));
    }

    public static void main(String[] args) throws Throwable {
        
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        MethodHandle comparatorHandle = MethodHandles.lookup()
                .findStatic(Main.class, "comparator", MethodType.methodType(
                        int.class, MemorySegment.class, MemorySegment.class));

        Linker linker = Linker.nativeLinker();
        SymbolLookup libLookup = linker.defaultLookup();

        try (Arena arena = Arena.ofShared()) {
            
            Thread.ofVirtual().name("vtCallForeignCode").start(() -> {

                MemorySegment comparatorFunc = linker.upcallStub(comparatorHandle,
                        FunctionDescriptor.of(ValueLayout.JAVA_INT,
                                ValueLayout.ADDRESS.withTargetLayout(
                                        MemoryLayout.sequenceLayout(1, ValueLayout.JAVA_INT)),
                                ValueLayout.ADDRESS.withTargetLayout(
                                        MemoryLayout.sequenceLayout(1, ValueLayout.JAVA_INT))), arena);

                MemorySegment segmentBsearch = libLookup.find("bsearch").get();

                MethodHandle func = linker.downcallHandle(segmentBsearch, FunctionDescriptor.of(
                        ValueLayout.ADDRESS,
                        ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

                MemorySegment key = arena.allocateFrom(ValueLayout.JAVA_INT, elem);
                MemorySegment array = arena.allocateFrom(ValueLayout.JAVA_INT, arr);

                MemorySegment result;
                try {
                    result = (MemorySegment) func.invokeExact(
                            key, array, 10, ValueLayout.JAVA_INT.byteSize(), comparatorFunc);

                    if (result.equals(MemorySegment.NULL)) {
                        System.out.println("Element " + elem
                                + " not found in the given array " + Arrays.toString(arr));
                    } else {
                        System.out.println("Element value: "
                                + array.asOverlappingSlice(result).get().get(ValueLayout.JAVA_INT, 0));
                    }
                } catch (Throwable ex) {
                    logger.severe(ex.toString());
                }
            }).join();
        }
    }
}

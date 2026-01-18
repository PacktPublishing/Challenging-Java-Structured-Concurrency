# Hooking JDK 21 `dumpThreads()`
JDK 21 has added in `HotSpotDiagnosticMXBean` the `dumpThreads(String outputFile, HotSpotDiagnosticMXBean.ThreadDumpFormat format)` method. 
This method produces a thread dump in the specified output file and format. Exemplify its usage.
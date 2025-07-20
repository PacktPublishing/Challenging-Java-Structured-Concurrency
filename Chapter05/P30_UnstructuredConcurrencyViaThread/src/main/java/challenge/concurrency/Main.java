package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        HighwayContractSignThread roadThread = new HighwayContractSignThread(
                new HighwayServiceCompany("BestRoads"), HighwaySignPartType.ROAD);
        HighwayContractSignThread tunnelThread = new HighwayContractSignThread(
                new HighwayServiceCompany("TunnelsCo"), HighwaySignPartType.TUNNEL);
        HighwayContractSignThread bridgeThread = new HighwayContractSignThread(
                new HighwayServiceCompany("TheBridges"), HighwaySignPartType.BRIDGE);
        
        roadThread.start();
        tunnelThread.start();
        bridgeThread.start();
        
        roadThread.join();
        tunnelThread.join();
        bridgeThread.join();
        
        HighwayContract contract = new HighwayContract(
                roadThread.getHighwayPart(), tunnelThread.getHighwayPart(), bridgeThread.getHighwayPart());
        
        logger.info(contract.toString());
    }       
}
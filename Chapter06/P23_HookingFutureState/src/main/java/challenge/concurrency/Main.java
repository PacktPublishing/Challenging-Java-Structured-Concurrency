package challenge.concurrency;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.concurrent.Future.State.CANCELLED;
import static java.util.concurrent.Future.State.FAILED;
import static java.util.concurrent.Future.State.RUNNING;
import static java.util.concurrent.Future.State.SUCCESS;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        KitchenTeam kitchenTeam = professionalKitchenTeam();       
        logger.info(() -> "Team size: " + kitchenTeam.members().length);
    }

    public static KitchenTeam professionalKitchenTeam() throws InterruptedException {

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<String> kitchenTeam = new ArrayList<>();
            
            List<Future<String>> kitchenMembers = executor.invokeAll(
                    List.of(() -> kitchenMember(KitchenRole.EXECUTIVE_CHEF),
                            () -> kitchenMember(KitchenRole.SOUS_CHEF),
                            () -> kitchenMember(KitchenRole.LINE_COOK),
                            () -> kitchenMember(KitchenRole.PREP_COOK),
                            () -> kitchenMember(KitchenRole.PASTRY_CHEF),
                            () -> kitchenMember(KitchenRole.PANTRY_CHEF),
                            () -> kitchenMember(KitchenRole.EXPO),
                            () -> kitchenMember(KitchenRole.DISHWASHER)), 
                    3, TimeUnit.SECONDS
            );

            kitchenMembers.forEach(f -> {
                logger.info(() -> "Analyzing kitchen member state: " + f.state());
                switch (f.state()) {
                    case RUNNING -> throw new IllegalStateException("Ops! This should never happen ...");
                    case SUCCESS -> kitchenTeam.add(f.resultNow());                    
                    case FAILED -> logger.severe(() -> "Exception: " + f.exceptionNow().getMessage());
                    case CANCELLED -> logger.info("Ops! This request was cancelled ?!?");
                }
            });

            return new KitchenTeam(kitchenTeam.toArray(String[]::new));
        }
    }

    public static enum KitchenRole {
        EXECUTIVE_CHEF(1), 
        SOUS_CHEF(Integer.MAX_VALUE), // causes the exception since Integer.MAX_VALUE is not a valid code
        LINE_COOK(3), PREP_COOK(4),
        PASTRY_CHEF(5), PANTRY_CHEF(6), 
        EXPO(7), DISHWASHER(8);

        private final int code;

        KitchenRole(int code) { this.code = code; }

        private int code() { return code; }
    }

    public static String kitchenMember(KitchenRole role) throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpGetRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + role.code()))
                // or, .uri(URI.create("https://dummyjson.com/users/" + role.code()))                
                .build();

        HttpResponse<String> httpGetResponse = httpClient.send(
                httpGetRequest, HttpResponse.BodyHandlers.ofString());

        // PANTRY_CHEF
        if(role.code == 6) { Thread.sleep(2000); } // artificially added delay for timeout
        
        if (httpGetResponse.statusCode() == 200) {
            return httpGetResponse.body();
        }

        throw new MemberNotAvailableException("Member not available: "
                + httpGetResponse.statusCode());
    }
}

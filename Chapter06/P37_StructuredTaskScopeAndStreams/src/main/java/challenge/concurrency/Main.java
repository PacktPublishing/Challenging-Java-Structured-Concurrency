package challenge.concurrency;

import static challenge.concurrency.Main.KitchenRole.DISHWASHER;
import static challenge.concurrency.Main.KitchenRole.EXECUTIVE_CHEF;
import static challenge.concurrency.Main.KitchenRole.EXPO;
import static challenge.concurrency.Main.KitchenRole.LINE_COOK;
import static challenge.concurrency.Main.KitchenRole.PANTRY_CHEF;
import static challenge.concurrency.Main.KitchenRole.PASTRY_CHEF;
import static challenge.concurrency.Main.KitchenRole.PREP_COOK;
import static challenge.concurrency.Main.KitchenRole.SOUS_CHEF;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        KitchenTeam kitchenTeam = proffesionalKitchenTeam();       
        logger.info(() -> "Team size: " + kitchenTeam.members().length);
    }

    public static KitchenTeam proffesionalKitchenTeam() throws InterruptedException {

        try (StructuredTaskScope scope = new StructuredTaskScope<KitchenTeam>()) {

           List<Subtask> kitchenSubtasks = Stream.of(
                   EXECUTIVE_CHEF, SOUS_CHEF, LINE_COOK, PREP_COOK, 
                   PASTRY_CHEF, PANTRY_CHEF, EXPO, DISHWASHER)
                   .<Callable<String>>map(r -> () -> kitchenMember(r))
                   .map(scope::fork)
                   .toList();            
            
           scope.join();
           
           List<Throwable> failedSubtasks = kitchenSubtasks.stream()
                   .filter(t -> t.state() == Subtask.State.FAILED)
                   .map(Subtask::exception)
                   .toList();
           
           logger.severe(() -> "Failed subtasks: " + failedSubtasks);
           
           KitchenTeam kitchenTeam = kitchenSubtasks.stream()
                   .filter(t -> t.state() == Subtask.State.SUCCESS)
                   .map(Subtask::get) // or, better map(Supplier::get)
                   .collect(collectingAndThen(toList(),
                           list -> { return new KitchenTeam(list.toArray(String[]::new)); }));
            
            logger.info(() -> "Kitchen team: " + kitchenTeam);
                       
            return kitchenTeam;
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
         
        if (httpGetResponse.statusCode() == 200) {
            return httpGetResponse.body();
        }

        throw new MemberNotAvailableException("Member not available: "
                + httpGetResponse.statusCode());
    }
}

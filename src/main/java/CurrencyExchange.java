import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class CurrencyExchange {
    public static void main(String[] args){
        CurrencyExchangeUI.main(args);
    }
}

class Exchange {

    public static float getRate(String originalCurrency, String targetCurrency) {
        try {

            String apiKey = Config.getApiKey();

            if (apiKey == null) {
                System.out.println("Stopping app: API Key missing.");
                System.exit(1);
            }

            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + originalCurrency;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            float rate = json.getJSONObject("conversion_rates").getFloat(targetCurrency);
            return rate;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static String getDateTime() {
    LocalDateTime DateTime = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String timeStamp = DateTime.format(format);
    return timeStamp;
    }

    public static float calculateExchange() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("What currency would you like to exchange? ");
            String originalCurrency = sc.nextLine();
            
            System.out.print("please enter the amount of money you want to exchange: ");
            float money = sc.nextFloat();
            
            System.out.print("What currency would you like to change your money into? ");
            sc.nextLine();
            String targetCurrency = sc.nextLine();
            
            float rate = getRate(originalCurrency, targetCurrency);
            String timeDate = getDateTime();
            System.out.println(timeDate);
            float result = money * rate;

            // Save to file
            saveExchangeToFile(timeDate, money, originalCurrency, targetCurrency, result);

            return result;
        }
    }

    private static void saveExchangeToFile(String dateTime, float amount, String originalCurrency, String targetCurrency, float convertedAmount) {
        String fileName = "exchange_history.txt";
        String entry = String.format(
            "Date & Time: %s | Amount: %.2f %s -> %.2f %s%n",
            dateTime, amount, originalCurrency, convertedAmount, targetCurrency
        );
        try {
            File file = new File(fileName);
            System.out.println("Saving to: " + new File(".").getAbsolutePath());
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(entry);
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
        }
    }
}
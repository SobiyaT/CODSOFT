import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {
    private static final String API_KEY = "YOUR_EXCHANGE_RATE_API_KEY";
    private static final String API_URL = "https://open.er-api.com/v6/latest/";

    public static void main(String[] args) {
        try {
            // Get the exchange rates
            String baseCurrency = getUserInput("Enter the base currency code (e.g., USD): ");
            String targetCurrency = getUserInput("Enter the target currency code (e.g., EUR): ");
            double amount = Double.parseDouble(getUserInput("Enter the amount to convert: "));

            String exchangeRatesJson = getExchangeRates(baseCurrency);
            double conversionRate = extractConversionRate(exchangeRatesJson, targetCurrency);

            // Perform the conversion
            double convertedAmount = amount * conversionRate;

            // Display the result
            System.out.printf("%.2f %s is equal to %.2f %s%n",
                    amount, baseCurrency, convertedAmount, targetCurrency);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getUserInput(String prompt) throws IOException {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static String getExchangeRates(String baseCurrency) throws IOException {
        URL url = new URL(API_URL + baseCurrency + "?apikey=" + API_KEY);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return response.toString();
    }

    private static double extractConversionRate(String exchangeRatesJson, String targetCurrency) {
        // Parse the JSON and extract the conversion rate
        // You may use a JSON library like Jackson or Gson for more complex JSON parsing
        // For simplicity, we'll use a simple substring search here
        String rateStart = "\"" + targetCurrency + "\":";
        int startIndex = exchangeRatesJson.indexOf(rateStart) + rateStart.length();
        int endIndex = exchangeRatesJson.indexOf(",", startIndex);

        return Double.parseDouble(exchangeRatesJson.substring(startIndex, endIndex));
    }
}

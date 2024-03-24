import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyRateService {

    private final Map<String, List<Double>> currencyRatesHistory = new HashMap<>();
    private MessageService messageService;

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public CurrencyRateService() {
    }
    private static final Logger logger = Logger.getLogger(CurrencyRateService.class.getName());


    public CurrencyRateService(MessageService messageService) {
        this.messageService = messageService;
    }

    public List<Double> getCurrencyRates(String currencyPair) {
        return currencyRatesHistory.getOrDefault(currencyPair, Collections.emptyList());
    }

    public void updateCurrencyRates() {
        updateCurrencyRate("EURRUB");
        updateCurrencyRate("USDRUB");
    }

    private void updateCurrencyRate(String currencyPair) {
        double rate = getCurrencyRate(currencyPair);
        List<Double> rates = currencyRatesHistory.getOrDefault(currencyPair, new ArrayList<>());
        if (!rates.isEmpty() && rate != rates.get(rates.size() - 1)) {
            messageService.sendRateChangedMessage(currencyPair, rate);
        }
        rates.add(rate);
        if (rates.size() > 30) {
            rates.remove(0);
        }
        currencyRatesHistory.put(currencyPair, rates);
    }

    public double getCurrencyRate(String currencyPair) {
        System.out.println("getCurrencyRate called for " + currencyPair);
        try {
            URL url = new URL("https://api.frankfurter.app/latest?from=" + currencyPair.substring(0, 3) + "&to=" + currencyPair.substring(3));
            System.out.println("URL: " + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            reader.close();

            JsonObject jsonObject = JsonParser.parseString(line).getAsJsonObject();
            double rate = jsonObject.getAsJsonObject("rates").get(currencyPair.substring(3)).getAsDouble();

            System.out.println("Retrieved rate for " + currencyPair + ": " + rate);
            return rate;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception", e);
            return 0.0;
        } finally {
            System.out.println("getCurrencyRate finished for " + currencyPair);
        }
    }


}
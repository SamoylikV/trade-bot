import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class MessageService {
    private CurrencyRateService currencyRateService = new CurrencyRateService(this);
    private ChartService chartService = new ChartService();
    private MyBot myBot;

    public void setMyBot(MyBot myBot) {
        this.myBot = myBot;
    }

    public MessageService() {
    }
    private Long chatId;
    private double previousEurUsdRate = -1.0;
    private double previousUsdJpyRate = -1.0;


    public void sendStartMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Добро пожаловать! Вы можете проверить курсы валют, отправив команду /check.");
        try {
            myBot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Sent message: " + message);
    }

    public void sendSignals(Long chatId) {
        this.chatId = chatId;
        try {
            double eurRubRate = currencyRateService.getCurrencyRate("EURRUB");
            double usdRubRate = currencyRateService.getCurrencyRate("USDRUB");

            if (eurRubRate != previousEurUsdRate) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Курс EUR/RUB изменился: " + eurRubRate);
                myBot.execute(message);
                System.out.println("Sent message: " + message);
                previousEurUsdRate = eurRubRate;
            } else {
                System.out.println("EUR/RUB rate has not changed");
            }

            if (usdRubRate != previousUsdJpyRate) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Курс USD/RUB изменился: " + usdRubRate);
                myBot.execute(message);
                System.out.println("Sent message: " + message);
                previousUsdJpyRate = usdRubRate;
            } else {
                System.out.println("USD/RUB rate has not changed");
            }
            List<Double> eurRubRates = currencyRateService.getCurrencyRates("EURRUB");
            List<Double> usdRubRates = currencyRateService.getCurrencyRates("USDRUB");

            chartService.createChart(eurRubRates, "EURRUB");
            chartService.createChart(usdRubRates, "USDRUB");

            sendChart(chatId, "EURRUB");
            sendChart(chatId, "USDRUB");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRateChangedMessage(String currencyPair, double rate) {
        if (chatId != null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Курс " + currencyPair + " изменился: " + rate);
            try {
                myBot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendChart(Long chatId, String currencyPair) {
        try {
            SendPhoto sendPhotoRequest = new SendPhoto();
            sendPhotoRequest.setChatId(chatId);
            sendPhotoRequest.setPhoto(new InputFile(new File(currencyPair + "_chart.png")));
            myBot.execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
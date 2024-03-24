import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBot extends TelegramLongPollingBot {
    private static final Logger logger = Logger.getLogger(MyBot.class.getName());

    private final CurrencyRateService currencyRateService;
    private MessageService messageService;

    private String botUsername;
    private String botToken;
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public MyBot(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
        loadBotCredentials();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(currencyRateService::updateCurrencyRates, 0, 2, TimeUnit.MINUTES);
    }

    private void loadBotCredentials() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            String filename = "credentials.properties";
            input = MyBot.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            prop.load(input);

            botUsername = prop.getProperty("bot.username");
            botToken = prop.getProperty("bot.token");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("onUpdateReceived called");
        System.out.println("Received update: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (command.equals("/start")) {
                messageService.sendStartMessage(chatId);
            } else if (command.equals("/check")) {
                messageService.sendSignals(chatId);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            CurrencyRateService currencyRateService = new CurrencyRateService();
            MessageService messageService = new MessageService();
            MyBot myBot = new MyBot(currencyRateService);
            myBot.setMessageService(messageService);
            messageService.setMyBot(myBot);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(myBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
public class TradingStrategy {
    public static String analyzeMarket(double eurUsdRate, double usdJpyRate) {
        if (eurUsdRate > 1.2) {
            return "Евро вверх";
        }
        if (usdJpyRate > 110) {
            return "Доллар вверх";
        }
        return "";
    }
}

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChartService {
    public void createChart(List<Double> rates, String currencyPair) throws IOException {
        TimeSeries series = new TimeSeries("Currency Rate");

        for (int i = 0; i < rates.size(); i++) {
            series.add(new Minute(), rates.get(i));
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                currencyPair + " Rate",
                "Time",
                "Rate",
                dataset,
                false,
                false,
                false
        );

        ChartUtils.saveChartAsPNG(new File(currencyPair + "_chart.png"), chart, 600, 400);
    }

}

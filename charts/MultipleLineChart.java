/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package charts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generate line graph of past 7 days price variation of multiple cryptocurrencies
 */
public class MultipleLineChart {
    public static Logger logger = LogManager.getLogger(MultipleLineChart.class);
    public static HttpResponse<String> res(String url) {
        HttpResponse<String> res = null;
        try {
            HttpRequest request;
            if (url != null) {
                request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
            } else {
                throw new NullPointerException();
            }
            HttpClient client = HttpClient.newBuilder().build();
            res = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException ne){
            System.err.println("NullPointerException =>"+ne);
        }catch (IOException ioe){
            System.err.println( "IOException => "+ioe);
        }catch (InterruptedException ie){
            System.err.println( "InterruptedException => "+ie);
        }catch (Exception e){
            System.err.println("UnknownException => "+e);
        }
        return res;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Program started");
        String[] coins = {"near","polkadot","leo-token","uniswap"};
        List<String> allResponse = new ArrayList<>();
        for(String coin: coins) {
            logger.trace(coin);
            HttpResponse<String> response = res("https://api.coingecko.com/api/v3/coins/" + coin + "/market_chart?vs_currency=usd&days=14&interval=daily");

            if(response.statusCode() ==200) {
                logger.info("Data fetched from Crypto API");
                allResponse.add(response.body());
            }
            else {
                logger.error("Status code: "+response.statusCode());
            }
        }
        System.out.println(allResponse.size());
        List<String> price = new ArrayList<>();
        for(String q : allResponse) {
            logger.debug(q);
            JSONObject jsonObject;
            if(q.charAt(0) == '{') {
                jsonObject = new JSONObject(q);
                logger.debug(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("prices");

                price.add(jsonArray.toString());
            }
            else{
                logger.error("");
            }

        }
        System.out.println(price.get(0));
        int t = 0;
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(String p : price) {
            XYSeries xySeries = new XYSeries(coins[t]);
            for (int i = 0; i < 15; i++) {

                String c = p.replaceAll("],\\[","]],\\[[");
                String[] a = c.split("],\\[");

                String[] b = a[i].replace("[","").replace("]","").split(",");
                System.out.println(b[1]);
                xySeries.add(i + 1, Float.valueOf(b[1]));

            }
            dataset.addSeries(xySeries);
            t++;
        }

            JFreeChart chart = ChartFactory.createXYLineChart("Past 14 days price",
                    "Days", "Price", dataset, PlotOrientation.VERTICAL, true, true, false);
            try {
                ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/MultipleLineChart.jpeg"), chart, 800, 500);
                System.out.println("Multiple Line Chart created");
            } catch (Exception e) {
                System.err.println("error");
            }


    }
}

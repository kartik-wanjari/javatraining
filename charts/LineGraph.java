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
/**
 * This class generate Line graph of past 7 days of price variation of near protocol .
 */
public class LineGraph {
    public static Logger logger = LogManager.getLogger(LineGraph.class);
    /**
     * This method send http request and receive response
     * @param url API url
     * @return response from API
     */
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
        logger.info("Program started: Crypto Currency API");
        //fetch data
        HttpResponse<String> response = res("https://api.coingecko.com/api/v3/coins/near/market_chart?vs_currency=usd&days=7&interval=daily");


        if(response.statusCode()==200) {
            logger.info("Data fetched from crypto API");
            JSONArray jsonArray = new JSONArray();
            if(response.body().charAt(0)=='{') {
                logger.debug("Object data: "+response.body());
                JSONObject jsonObject = new JSONObject(response.body());
                System.out.println(jsonObject);
                jsonArray = jsonObject.getJSONArray("prices");
                logger.debug("Array: "+jsonArray);//fetching prices object values
                System.out.println(jsonArray);
            }
            logger.info("Collecting coordinates (day,price)");
            XYSeries xySeries = new XYSeries("near");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(jsonArray.get(i));
                logger.debug("Index "+i+": "+jsonArray.get(i));
                String[] a = jsonArray.get(i).toString().replace("[", "").replace("]", "").split(",");
                System.out.println(a[1]);
                logger.debug("X-axis: "+ i+ "  Y-axis: "+a[1]);

                xySeries.add(i , Float.valueOf(a[1]));
            }
            logger.info("Generating dataset for XYSeries Collection");
            XYSeriesCollection dataset = new XYSeriesCollection(xySeries);
            logger.info("Generating XYLine graph");
            JFreeChart chart = ChartFactory.createXYLineChart("Past 7 days price of Near Protocol",
                    "Days", "Price", dataset, PlotOrientation.VERTICAL, true, true, false);

            try {
                logger.info("");
                logger.warn("Destination path must be valid");
                ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/LineChart.jpeg"), chart, 500, 300);
                System.out.println("LineChart.jpeg created");
            } catch (Exception e) {
                System.err.println("error");
                logger.error("Destination path not valid");
            }
        }
        else {
            logger.error("Status code: "+response.statusCode());
        }

    }
}

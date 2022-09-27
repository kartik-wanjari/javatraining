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
import org.jfree.data.general.DefaultPieDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * This class generate pie chart of cryptocurrency and its market cap
 */
public class PieChart {
    public static Logger logger = LogManager.getLogger(PieChart.class);
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
    public static void main(String[] args) throws Exception {

        logger.info("url passed");
        HttpResponse<String> response = res("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=7&page=1&sparkline=false");
        if (response.statusCode() == 200) {
            logger.warn("response should not be null");
            JSONArray jsonArray;
            if (response.body().charAt(0) == '[') {
                jsonArray = new JSONArray(response.body());
                logger.info("response received");
                logger.debug(response.body());
            } else {
                logger.error("Json array not found");
                throw new Exception("Json array not found");
            }
            DefaultPieDataset pieDataset = new DefaultPieDataset<>();
            for (Object i : jsonArray) {
                if (i.toString().charAt(0) == '{') {
                    logger.debug(i);
                    JSONObject j = new JSONObject(i.toString());
                    logger.debug("Coin: " + j.get("id").toString() + "  Market cap: " + Long.valueOf(j.get("market_cap").toString()));
                    pieDataset.setValue(j.get("id").toString(), Long.valueOf(j.get("market_cap").toString()));
                } else {
                    throw new JSONException("");
                }
            }
            logger.info("Adding data to chart");
            JFreeChart chart = ChartFactory.createPieChart("Crypto Market-cap Summary", pieDataset, true, true, false);

            try {
                logger.info("Generating PieChart.jpeg");
                logger.warn("Destination Path should be valid");
                ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/PieChart.jpeg"), chart, 500, 300);
                System.out.println("Pie chart created");
            } catch (Exception e) {
                System.err.println("error");
                logger.error("Chart not created");
            }
            logger.info("End of program");
        } else {
            logger.error("Status code: " + response.statusCode());
        }
    }
}

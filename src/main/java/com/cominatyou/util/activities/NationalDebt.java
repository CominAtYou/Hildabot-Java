package com.cominatyou.util.activities;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.ActivityType;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class NationalDebt {
    protected static void setNationalDebtStatus(DiscordApi client) {
        CloseableHttpResponse response;
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            final HttpGet request = new HttpGet("https://www.treasurydirect.gov/NP_WS/debt/current?format=json");
            response = httpClient.execute(request);
        }
        catch (Exception e) {
            System.err.println("[NationalDebtActivity] Couldn't get the current national debt.");
            return;
        }

        String responseBody;
        try {
            responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            System.err.println("[NationalDebtActivity] Couldn't get the current national debt.");
            return;
        }

        final StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            System.err.println("[NationalDebtActivity] Couldn't get the current national debt.");
            return;
        }

        JSONObject resultObject;
        try {
            resultObject = new JSONObject(responseBody);
        }
        catch (Exception e) {
            System.err.println("[NationalDebtActivity] Couldn't parse the national debt JSON.");
            return;
        }

        final double debt = resultObject.getDouble("totalDebt");
        final String debtString = new DecimalFormat("###,###.##").format(debt);

        client.updateActivity(ActivityType.PLAYING, String.format("the current national debt of the United States is $%s", debtString));

        try {
            httpClient.close();
            response.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.cominatyou.util.activities;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.ActivityType;
import org.json.JSONObject;

import com.cominatyou.util.logging.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.DecimalFormat;

public class NationalDebt {
    private static final OkHttpClient httpClient = new OkHttpClient();
    protected static void setNationalDebtStatus(DiscordApi client) {
        final Request request = new Request.Builder().url("https://www.treasurydirect.gov/NP_WS/debt/current?format=json").build();

        Response response;
        String responseBody;
        try {
            response = httpClient.newCall(request).execute();
            responseBody = response.body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.error("NationalDebtActivity", "Couldn't get the national debt JSON.");
            return;
        }

        if (!response.isSuccessful()) {
            Log.error("NationalDebtActivity", "Couldn't get the national debt JSON.");
            return;
        }

        response.close();

        JSONObject resultObject;
        try {
            resultObject = new JSONObject(responseBody);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.error("NationalDebtActivity", "Couldn't parse the national debt JSON.");
            return;
        }

        final double debt = resultObject.getDouble("totalDebt");
        final String debtString = new DecimalFormat("###,###.##").format(debt);

        client.updateActivity(ActivityType.PLAYING, "the current national debt of the United States is $" + debtString);
    }
}

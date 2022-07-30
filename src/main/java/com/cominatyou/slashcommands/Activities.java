package com.cominatyou.slashcommands;

import static java.util.Map.entry;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRowBuilder;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.json.JSONObject;

import com.cominatyou.Config;
import com.cominatyou.util.Values;

public class Activities implements InteractionCommand {
    private static final Map<String, String> activityIds = Map.ofEntries(
            entry("watch_together", "880218394199220334"),
            entry("chess", "832012774040141894"),
            entry("checkers", "832013003968348200"),
            entry("sketch_heads", "902271654783242291"));

    private static final Map<String, String> activityNames = Map.ofEntries(
            entry("watch_together", "Watch Together"),
            entry("chess", "Chess in the Park"),
            entry("heckers", "Checkers in the Park"),
            entry("sketch_heads", "Sketch Heads"));

    private static long nextAvailable = Instant.now().getEpochSecond();
    private static int remaining = 5;

    public void execute(SlashCommandInteraction interaction) {
        final Server hildacord = interaction.getApi().getServerById(Values.HILDACORD_ID).get();
        final Role staffRole = hildacord.getRoleById(492577085743824906L).get();

        if (interaction.getServer().get().getId() == hildacord.getId() &&
                !interaction.getUser().getRoles(hildacord).contains(staffRole) &&
                !interaction.getServer().get().hasPermission(interaction.getUser(), PermissionType.START_EMBEDDED_ACTIVITIES) &&
                interaction.getUser().getId() != Values.COMIN_USER_ID) {
            interaction.createImmediateResponder().setContent("You don't have access to this.");
            return;
        }

        if (remaining == 0 && nextAvailable - Instant.now().getEpochSecond() > 0) {
            interaction.createImmediateResponder().setContent("That isn't available right now. Try again at <t:" + String.valueOf(nextAvailable) + ":t>").respond();
            return;
        } else if (remaining == 0 && nextAvailable - Instant.now().getEpochSecond() < 0) {
            remaining = 5;
        }

        if (--remaining == 4) {
            nextAvailable = Instant.now().getEpochSecond() + 600;
        }

        final String activity = interaction.getArguments().get(0).getStringValue().get();
        final ServerChannel channel = interaction.getArguments().get(1).getChannelValue().get();

        CloseableHttpResponse response;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            final HttpPost request = new HttpPost("https://discord.com/api/v10/channels/" + channel.getIdAsString() + "/invites");
            final StringEntity body = new StringEntity(createBody(activityIds.get(activity)));
            request.addHeader("Authorization", "Bot " + Config.TOKEN);
            request.addHeader("Content-Type", "application/json");
            request.setEntity(body);

            response = httpClient.execute(request);
        } catch (Exception e) {
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        String responseBody;
        try {
            responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.")
                    .setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        final StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            System.out.println(statusLine.getStatusCode() + ": " + responseBody);
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.")
                    .setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        JSONObject resultObject;
        try {
            resultObject = new JSONObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.")
                    .setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        interaction.createImmediateResponder()
            .setContent(String.format("Started %s in <#%d>.", activityNames.get(activity), channel.getId()))
            .addComponents(
                new ActionRowBuilder()
                        .addComponents(
                            new ButtonBuilder()
                                .setStyle(ButtonStyle.LINK)
                                .setLabel("Join")
                                .setUrl("https://discord.gg/" + resultObject.getString("code"))
                                .build())
                        .build())
            .respond();
    }

    private String createBody(String activityId) {
        return new JSONObject()
                .put("max_age", 180)
                .put("max_uses", 0)
                .put("temporary", false)
                .put("unique", true)
                .put("target_type", 2)
                .put("target_application_id", activityId)
                .toString();
    }
}

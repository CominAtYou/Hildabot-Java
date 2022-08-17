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
    private static final Map<String, EmbeddedActivity> availableActivities = Map.ofEntries(
            entry("watch_together", new EmbeddedActivity("Watch Together", "880218394199220334")),
            entry("chess", new EmbeddedActivity("Chess in the Park", "832012774040141894")),
            entry("checkers", new EmbeddedActivity("Checkers in the Park", "832013003968348200")),
            entry("sketch_heads", new EmbeddedActivity("Sketch Heads", "902271654783242291")),
            entry("golf", new EmbeddedActivity("Putt Party", "945737671223947305")),
            entry("ask_away", new EmbeddedActivity("Ask Away", "976052223358406656")),
            entry("know_what_i_meme", new EmbeddedActivity("Know what I Meme", "950505761862189096")));

    // Rate limit stuff since this command makes raw requests to the Discord API
    private static long nextAvailable = Instant.now().getEpochSecond();
    private static int remaining = 5;

    public void execute(SlashCommandInteraction interaction) {
        final Server hildacord = interaction.getApi().getServerById(Values.HILDACORD_ID).get();
        final Role staffRole = hildacord.getRoleById(492577085743824906L).get();

        // Only Hildacord Staff who have the "Start Activities" permission and I can use this command.
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
            final StringEntity body = new StringEntity(createBody(availableActivities.get(activity).getId()));
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
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        final StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            System.out.println(statusLine.getStatusCode() + ": " + responseBody);
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        JSONObject resultObject;
        try {
            resultObject = new JSONObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            interaction.createImmediateResponder().setContent("Something went wrong! Try again in a bit.").setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        interaction.createImmediateResponder()
            .setContent(String.format("Started %s in <#%d>.", availableActivities.get(activity).getName(), channel.getId()))
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
                .put("unique", false)
                .put("target_type", 2)
                .put("target_application_id", activityId)
                .toString();
    }
}

class EmbeddedActivity {
    private final String name;
    private final String id;

    /**
     * Get the name of the activity.
     * @return The name of the activity
     */
    public String getName() {
        return name;
    }

    /**
     * The snowflake ID of the activity.
     * @return The activity ID
     */
    public String getId() {
        return id;
    }

    /**
     * Instantiate a new activity.
     * @param name The name of the activity
     * @param id The activity's ID
     */
    public EmbeddedActivity(String name, String id) {
        this.name = name;
        this.id = id;
    }
}

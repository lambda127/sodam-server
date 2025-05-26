package com.sodam.login.Domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KakaoProfile {
    private Integer id;
    private LocalDateTime connectedAt;
    private String nickname;
    private String email;

    public KakaoProfile(String jsonResponseBody) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonResponseBody);
        JsonObject root = element.getAsJsonObject();

        if (root.has("id") && !root.get("id").isJsonNull()) {
            this.id = root.get("id").getAsInt();
        }

        if (root.has("connected_at") && !root.get("connected_at").isJsonNull()) {
            String connected_at = root.get("connected_at").getAsString();
            connected_at = connected_at.substring(0, connected_at.length() - 1);
            this.connectedAt = LocalDateTime.parse(connected_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        }

        if (root.has("properties") && root.get("properties").isJsonObject()) {
            JsonObject properties = root.getAsJsonObject("properties");
            if (properties.has("nickname") && !properties.get("nickname").isJsonNull()) {
                this.nickname = properties.get("nickname").getAsString();
            }
        }

        if (root.has("properties") && root.get("properties").isJsonObject()) {
            JsonObject properties = root.getAsJsonObject("properties");
            if (properties.has("email") && !properties.get("email").isJsonNull()) {
                this.nickname = properties.get("email").getAsString();
            }
        }

    }


}
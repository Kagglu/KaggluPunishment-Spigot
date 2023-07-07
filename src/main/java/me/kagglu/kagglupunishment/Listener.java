package me.kagglu.kagglupunishment;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String uuid = p.getUniqueId().toString();

        JSONArray json = KaggluPunishment.getInstance().getUuidJson();
        JSONObject user = null;

        for (int i = 0; i < json.size(); i++) {
            if (uuid.equals(((JSONObject) json.get(i)).get("uuid"))) {
                user = (JSONObject) json.get(i);
                if (!user.get("username").equals(p.getName())) {
                    user.put("username", p.getName());
                }
                break;
            }
        }
        if (user == null) {
            user = new JSONObject();
            user.put("uuid", uuid);
            user.put("username", p.getName());
            json.add(user);
        }
        KaggluPunishment.getInstance().writeUuidJsonString();
    }
}
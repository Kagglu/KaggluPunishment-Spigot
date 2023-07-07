package me.kagglu.kagglupunishment;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class CommandDelwarn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§4§lInvalid syntax! Use: /delwarn (username) (warning #)");
        }
        Player p = KaggluPunishment.getInstance().getServer().getPlayer(args[0]);
        String uuid = null;
        if (p == null) {
            JSONArray uuids = KaggluPunishment.getInstance().getUuidJson();
            for (int i = 0; i < uuids.size(); i++) {
                if (args[0].equalsIgnoreCase(((JSONObject) uuids.get(i)).get("username").toString())) {
                    uuid = ((JSONObject) uuids.get(i)).get("uuid").toString();
                }
            }
            if (uuid == null) {
                sender.sendMessage("§4§lPlayer not found!");
                return true;
            }
        } else {
            uuid = p.getUniqueId().toString();
        }
        JSONArray json = KaggluPunishment.getInstance().getWarnsJson();
        for (int i = 0; i < json.size(); i++) {
            if (uuid.equals(((JSONObject) json.get(i)).get("uuid"))) {
                JSONArray warns = (JSONArray) ((JSONObject) json.get(i)).get("warns");
                try {
                    Integer.parseInt(args[1]);
                } catch (Exception e) {
                    sender.sendMessage("§4§lPlease provide a valid warning number");
                }
                if (Integer.parseInt(args[1]) - 1 > warns.size() || Integer.parseInt(args[1]) < 1) {
                    sender.sendMessage("§4§lPlease provide a valid warning number");
                }
                warns.remove(Integer.parseInt(args[1]) - 1);
                ((JSONObject) (json.get(i))).put("warns", warns);
            }
        }
        sender.sendMessage("§2§lWarning #" + args[1] + " of " + args[0] + " deleted successfully!");
        KaggluPunishment.getInstance().writeWarnsJSONString();
        return true;
    }
}

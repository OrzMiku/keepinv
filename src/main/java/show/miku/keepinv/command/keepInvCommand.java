package show.miku.keepinv.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import show.miku.keepinv.KeepInv;
import show.miku.keepinv.config.Data;

import java.util.UUID;

public class keepInvCommand implements CommandExecutor {
    private final Data data;
    public keepInvCommand(KeepInv plugin) {
        this.data = plugin.getData();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("keepinv.command.keepinv")) {
            sendErrorMessage(commandSender, ErrorType.NO_PERMISSION);
            return true;
        }

        if(strings.length == 0) {
            // keepinv with no args
            if(commandSender instanceof Player player) {
                UUID uuid = getPlayerUUID(player);
                sendSuccessMessage(commandSender, player.getName(), toggleKeepInv(uuid));

            } else {
                commandSender.sendMessage(ChatColor.RED + "控制台必须指定玩家名！");
                return true;
            }
        }
        else if(strings.length == 1) {
            // keepinv with one arg
            if(!commandSender.hasPermission("keepinv.command.keepinv.others")) {
                sendErrorMessage(commandSender, ErrorType.NO_PERMISSION);
                return true;
            }
            UUID uuid = validateAndGetUUID(commandSender, strings[0]);
            if(uuid == null) return true;
            sendSuccessMessage(commandSender, strings[0], toggleKeepInv(uuid));
        }
        else if(strings.length == 2) {
            // keepinv with two args
            if(!commandSender.hasPermission("keepinv.command.keepinv.others")) {
                sendErrorMessage(commandSender, ErrorType.NO_PERMISSION);
                return true;
            }
            UUID uuid = validateAndGetUUID(commandSender, strings[0]);
            if(uuid == null) return true;
            if(strings[1].equalsIgnoreCase("true")) {
                if(addKeepInv(uuid)) sendSuccessMessage(commandSender, strings[0], true);
                else sendAlreadySetMessage(commandSender, strings[0], true);
            } else if(strings[1].equalsIgnoreCase("false")) {
                if(removeKeepInv(uuid)) sendSuccessMessage(commandSender, strings[0], false);
                else sendAlreadySetMessage(commandSender, strings[0], false);
            } else {
                sendErrorMessage(commandSender, ErrorType.WRONG_USAGE);
            }
        }
        else {
            sendErrorMessage(commandSender, ErrorType.WRONG_USAGE);
        }

        data.saveData();
        return true;
    }

    private UUID getPlayerUUID(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return player != null ? player.getUniqueId() : null;
    }

    private UUID validateAndGetUUID(CommandSender sender, String playerName) {
        UUID uuid = getPlayerUUID(playerName);
        if(uuid == null) sendErrorMessage(sender, ErrorType.PLAYER_NOT_FOUND);
        return uuid;
    }

    private UUID getPlayerUUID(Player player) {
        return player != null ? player.getUniqueId() : null;
    }

    private boolean toggleKeepInv(UUID uuid) {
        if(data.getKeepInvPlayers().contains(uuid)) {
            data.removePlayer(uuid);
            return false;
        } else {
            data.addPlayer(uuid);
            return true;
        }
    }

    private boolean addKeepInv(UUID uuid) {
        if(!data.getKeepInvPlayers().contains(uuid)) {
            data.addPlayer(uuid);
            return true;
        }
        return false;
    }

    private boolean removeKeepInv(UUID uuid) {
        if(data.getKeepInvPlayers().contains(uuid)) {
            data.removePlayer(uuid);
            return true;
        }
        return false;
    }

    private void sendSuccessMessage(CommandSender sender, String playerName, boolean enabled) {
        String message = ChatColor.DARK_GREEN + "已将 " + playerName + " 的死亡不掉落设置为 " + (enabled ? "true" : "false");
        sender.sendMessage(message);
    }

    private void sendAlreadySetMessage(CommandSender sender, String playerName, boolean enabled) {
        String message = ChatColor.RED + playerName + " 的死亡不掉落设置已经为 " + (enabled ? "true" : "false");
        sender.sendMessage(message);
    }

    enum ErrorType {
        NO_PERMISSION,
        WRONG_USAGE,
        PLAYER_NOT_FOUND,
    }

    private void sendErrorMessage(CommandSender sender, ErrorType errorType) {
        String message;
        if(errorType == ErrorType.NO_PERMISSION) message = ChatColor.RED + "你没有权限使用此命令！";
        else if(errorType == ErrorType.WRONG_USAGE) message = ChatColor.RED + "用法错误！";
        else if(errorType == ErrorType.PLAYER_NOT_FOUND) message = ChatColor.RED + "玩家不存在或不在线！";
        else message = "";
        sender.sendMessage(message);
    }
}

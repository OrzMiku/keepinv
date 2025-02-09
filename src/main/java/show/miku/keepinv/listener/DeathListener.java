package show.miku.keepinv.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import show.miku.keepinv.config.Data;

import java.util.UUID;

public class DeathListener implements Listener {
    private final Data data;

    public DeathListener(Data data) {
        this.data = data;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        if(data.getKeepInvPlayers().contains(uuid)) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }
}

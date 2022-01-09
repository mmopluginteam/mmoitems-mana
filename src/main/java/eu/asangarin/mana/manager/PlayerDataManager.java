package eu.asangarin.mana.manager;

import eu.asangarin.mana.api.ResourceData;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
	private final Map<UUID, ResourceData> map = new HashMap<>();

	public void setup(Player player) {
		if (!map.containsKey(player.getUniqueId()))
			map.put(player.getUniqueId(), new ResourceData(MMOPlayerData.get(player)));
	}

	public ResourceData get(OfflinePlayer player) {
		return get(player.getUniqueId());
	}

	public ResourceData get(UUID uuid) {
		return map.get(uuid);
	}

	public void unload(OfflinePlayer player) {
		map.remove(player.getUniqueId());
	}

	public Collection<ResourceData> getLoaded() {
		return map.values();
	}
}

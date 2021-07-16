package eu.asangarin.mana.comp;

import eu.asangarin.mana.MMOMana;
import eu.asangarin.mana.api.ResourceData;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.comp.rpg.RPGHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class DefaultHook implements RPGHandler, Listener {

	@EventHandler
	public void a(PlayerLevelChangeEvent event) {
		PlayerData.get(event.getPlayer()).getInventory().scheduleUpdate();
	}

	public RPGPlayer getInfo(PlayerData data) {
		return new ManaPlayer(data);
	}

	@Override
	public void refreshStats(PlayerData data) {
	}

	public class ManaPlayer extends RPGPlayer {
		private final ResourceData data;

		public ManaPlayer(PlayerData playerData) {
			super(playerData);

			this.data = MMOMana.plugin.dataManager.get(this.getPlayer());
		}

		public int getLevel() {
			return getPlayer().getLevel();
		}

		public String getClassName() {
			return "";
		}

		public double getMana() {
			return data.getMana();
		}

		public double getStamina() {
			return data.getStamina();
		}

		public void setMana(double value) {
			data.setMana(value);
		}

		public void setStamina(double value) {
			data.setStamina(value);
		}

		public void giveMana(double value) {
			data.giveMana(value);
		}

		public void giveStamina(double value) {
			data.giveStamina(value);
		}
	}
}

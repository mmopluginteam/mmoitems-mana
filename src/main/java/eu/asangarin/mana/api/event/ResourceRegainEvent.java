package eu.asangarin.mana.api.event;

import eu.asangarin.mana.api.ResourceData;
import eu.asangarin.mana.api.ResourceRegainReason;
import eu.asangarin.mana.api.ResourceType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ResourceRegainEvent extends Event implements Cancellable {
	private final ResourceType type;
	private final ResourceRegainReason reason;
	private final ResourceData player;

	private double amount;
	private boolean cancelled;

	private static final HandlerList handlers = new HandlerList();

	/**
	 * When resource is regained for any reason by a player
	 *
	 * @param player Player regaining resource
	 * @param amount Amount gained
	 * @param reason Reason of regain
	 * @param type   Type of resource, either MANA or STAMINA
	 */
	public ResourceRegainEvent(ResourceData player, double amount, ResourceRegainReason reason, ResourceType type) {
		this.player = player;
		this.reason = reason;
		this.amount = amount;
		this.type = type;
	}

	public ResourceData getPlayer() {
		return this.player;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ResourceRegainReason getReason() {
		return this.reason;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean value) {
		this.cancelled = value;
	}

	public ResourceType getType() {
		return type;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

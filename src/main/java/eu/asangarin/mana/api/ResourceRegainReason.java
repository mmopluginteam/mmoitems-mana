package eu.asangarin.mana.api;

public enum ResourceRegainReason {

	/**
	 * When {@link ResourceData#giveMana(double)} or similar function
	 * for stamina is used, this is the default regain reason used.
	 */
	PLUGIN,

	/**
	 * Any other reason not given by this enum.
	 */
	OTHER,

	/**
	 * Mana or stamina natural regeneration
	 */
	REGENERATION,

	/**
	 * Mana or stamina gained back by a MMOCore skill for instance
	 */
	SKILL
}

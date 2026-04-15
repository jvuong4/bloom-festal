package io.github.jvuong4.bloomfestal.item;

public class WaneFestal extends HealingStaff {
	protected float healingPotency = 4.0f;
	protected int range = 18;

	public WaneFestal(final Properties properties) {
		super(properties);
		super.healingPotency = this.healingPotency;
		super.range = this.range;
	}

}

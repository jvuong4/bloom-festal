package io.github.jvuong4.bloomfestal.item;

public class MoonFestal extends HealingStaff {
	protected float healingPotency = 24.0f;
	protected int range = 6;

	public MoonFestal(final Properties properties) {
		super(properties);
		super.healingPotency = this.healingPotency;
		super.range = this.range;
	}

}

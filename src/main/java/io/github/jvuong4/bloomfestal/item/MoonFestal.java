package io.github.jvuong4.bloomfestal.item;

public class MoonFestal extends HealingStaff {
	protected int healingPotency = 9;

	public MoonFestal(final Properties properties) {
		super(properties);
		super.healingPotency = this.healingPotency;
	}

}

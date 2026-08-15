package io.github.jvuong4.bloomfestal.item.ProjectileItems.HealingStaffVariant;

import io.github.jvuong4.bloomfestal.item.ProjectileItems.HealingStaff;

public class SunFestal extends HealingStaff {
	protected int healingPotency = 3;

	public SunFestal(final Properties properties) {
		super(properties);
		super.healingPotency = this.healingPotency;
	}

}

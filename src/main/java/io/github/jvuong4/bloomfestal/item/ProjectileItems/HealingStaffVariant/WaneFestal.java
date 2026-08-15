package io.github.jvuong4.bloomfestal.item.ProjectileItems.HealingStaffVariant;

import io.github.jvuong4.bloomfestal.item.ProjectileItems.HealingStaff;

public class WaneFestal extends HealingStaff {
	protected int healingPotency = 6;

	public WaneFestal(final Properties properties) {
		super(properties);
		super.healingPotency = this.healingPotency;
	}

}

package io.github.jvuong4.bloomfestal.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import io.github.mintynoura.dualstance.registries.DualStanceComponents;
import net.minecraft.world.phys.Vec3;

public class DualStanceCompat implements BFCompat {

	@Override
	public void initialize()
	{

	}


	public void teleportLinkedPlayer(Player user, Vec3 RewarpOrbPos)
	{
		for (ItemStack itemStack : user.getInventory()) {
			if (itemStack.has(DualStanceComponents.LINKED_MOB)) {

				if (itemStack.get(DualStanceComponents.LINKED_MOB) != null) {

				} else {
					return;
				}
			}
		}
	}


}

package io.github.jvuong4.bloomfestal.compat;

import io.github.jvuong4.bloomfestal.entity.RewarpOrb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import io.github.mintynoura.dualstance.registries.DualStanceComponents;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class DualStanceCompat implements BFCompat {

	@Override
	public void initialize() {}

	//this should teleport `user`'s pair up partner to `orb`, give or take a few blocks.
	public void teleportLinkedPlayer(Player user, RewarpOrb orb, ServerLevel level, Vec3 RewarpOrbOldPos)
	{
		for (ItemStack itemStack : user.getInventory()) {
			//do you have a paired heart seal?
			if (itemStack.has(DualStanceComponents.LINKED_MOB)) {
				//get partner if it exists
				Entity partner =
					itemStack.get(DualStanceComponents.LINKED_MOB).id() //this returns entity UUID, how to get entity from UUID?
						;

				//
				if (partner != null) {
					//alter tp location by a few blocks, it's okay and i wanna avoid annoying entity cramming esque issues
					double xVariation = 2.0F * orb.getRandom().nextDouble() - (double)1.0F;
					double zVariation = 2.0F * orb.getRandom().nextDouble() - (double)1.0F;

					//honestly this is what RewarpOrb does and that is copied from ender pearl code, so no clue how this is able to function
					Entity newTarget = partner.teleport(new TeleportTransition(level, RewarpOrbOldPos.add(xVariation,0,zVariation), partner.getDeltaMovement(), partner.getYRot(), partner.getXRot(), TeleportTransition.DO_NOTHING));

					//standard code stuffs you can prolly ignore
					//undo fall distance, i love undoing fall distance
					if (newTarget != null) {
						newTarget.resetFallDistance();
					}
					if (newTarget instanceof LivingEntity) {
						LivingEntity livingEntity = (LivingEntity)newTarget;
						livingEntity.resetCurrentImpulseContext();
					}

				}
			}
		}
	}


}

package io.github.jvuong4.bloomfestal.compat.StreetArt.itemColors;

import io.github.jvuong4.bloomfestal.compat.StreetArt.DyeblastItem;
import io.github.jvuong4.bloomfestal.compat.StreetArt.orbColors.PinkDyeblastOrb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PinkDyeblastItem extends DyeblastItem {
	public PinkDyeblastItem(final Properties properties) {
		super(properties);
	}

	@Override
	public void summonDyeblast(ServerLevel serverLevel, Player player, InteractionHand hand, ItemStack itemStack)
	{
		double d = 20.0;
		Vec3 viewVector = player.getViewVector(1.0F);
		Vec3 direction = new Vec3(viewVector.x, viewVector.y, viewVector.z);
		PinkDyeblastOrb entity = new PinkDyeblastOrb(serverLevel, player, (direction.normalize()).scale(d));
		entity.setPos(player.getX() + viewVector.x, player.getY(0.5) + 0.5, entity.getZ() + viewVector.z);
		Projectile.spawnProjectile(entity, serverLevel, itemStack);
	}

}

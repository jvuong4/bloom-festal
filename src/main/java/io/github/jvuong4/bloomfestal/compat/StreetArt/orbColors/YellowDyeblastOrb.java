package io.github.jvuong4.bloomfestal.compat.StreetArt.orbColors;

import com.streetart.component.ColorComponent;
import io.github.jvuong4.bloomfestal.compat.StreetArt.DyeblastOrb;
import io.github.jvuong4.bloomfestal.compat.StreetArt.StreetArtEntities;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class YellowDyeblastOrb extends DyeblastOrb {


	public YellowDyeblastOrb(final EntityType<? extends YellowDyeblastOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public YellowDyeblastOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(StreetArtEntities.YELLOW_DYEBLAST_ORB,level, mob, direction);
		initVals();
	}

	public YellowDyeblastOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(StreetArtEntities.YELLOW_DYEBLAST_ORB, level, x, y, z, direction);
		initVals();
	}

	protected void initVals()
	{
		defaultComponent = ColorComponent.YELLOW;
		int color = 16711680;
		try
		{
			final ItemStack item = this.getItem();
			ColorComponent component = ColorComponent.getOrDefaultComponent(item, defaultComponent);
			color = component.getOrDefaultOpaque(item, defaultComponent.argb);
		}
		catch(Exception e)
		{
			//boy whatever
		}
		damageParticle = new DustParticleOptions(color, 1.0F);
	}
}

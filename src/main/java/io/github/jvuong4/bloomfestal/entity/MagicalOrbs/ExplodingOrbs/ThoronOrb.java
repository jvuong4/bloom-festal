package io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrbs;

import io.github.jvuong4.bloomfestal.entity.LightningBolt.VisualLightning;
import io.github.jvuong4.bloomfestal.entity.MagicalOrbs.ExplodingOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ThoronOrb extends ExplodingOrb {

	public ThoronOrb(final EntityType<? extends ThoronOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public ThoronOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(BFEntities.THORON_ORB, level, mob, direction);
		initVals();
	}

	public ThoronOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(BFEntities.THORON_ORB, level, x, y, z, direction);
		initVals();
	}

	@Override
	protected void initVals()
	{
		switch(charge)
		{
			case 1:
				accelerationPower = 1;
				range = 8;
				potency = 12F;
				explosionRadius = 3.0F;
				break;
			case 2:
				accelerationPower = 1;
				range = 15;
				potency = 16F;
				explosionRadius = 5.0F;
				break;
			case 0:
			default:
				accelerationPower = 1;
				range = 6;
				potency = 6F;
				explosionRadius = 1.0F;
				break;
		}
		particleSpawnChance = 2.0F;
		explosionSound = BFSounds.THUNDER;
		damageParticle = ParticleTypes.ELECTRIC_SPARK;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.ELECTRIC_SPARK;
	}
	@Override
	protected void createParticleTrail() {
		//i just LOVE the smell of particles in the morning!
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			if(this.level() instanceof ServerLevel serverLevel)
			{
				serverLevel.sendParticles(trailParticle, position.x, position.y, position.z, 1, 0.0, 0.0, 0.00, 0.0);
				Vec3 prevDirection = this.getDeltaMovement().scale(-0.5);
				serverLevel.sendParticles(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 1, 0.0, 0.0, 0.0, 0.0);
			}
		}
	}
	@Override
	public SimpleParticleType smallExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_SMALL;
	}
	@Override
	public SimpleParticleType largeExplosionParticle()
	{
		return ParticleTypes.GUST_EMITTER_LARGE;
	}

	@Override
	protected void explode(ServerLevel level)
	{
		if(charge > 0) {
			VisualLightning thunder = new VisualLightning(BFEntities.VISUAL_LIGHTNING, level);
			thunder.setPos(this.position());
			thunder.setSilent(true);
			thunder.setVisualOnly(true);
			level.addFreshEntity(thunder);
		}
		super.explode(level);
	}
}

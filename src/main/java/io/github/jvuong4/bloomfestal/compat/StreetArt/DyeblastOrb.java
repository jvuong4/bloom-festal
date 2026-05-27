package io.github.jvuong4.bloomfestal.compat.StreetArt;

import com.streetart.PermissionUtil;
import com.streetart.SplashUtil;
import com.streetart.component.ColorComponent;
import com.streetart.entity.PaintBalloon;
import io.github.jvuong4.bloomfestal.entity.ExplodingOrb;
import io.github.jvuong4.bloomfestal.entity.LightningBolt.VisualLightning;
import io.github.jvuong4.bloomfestal.entity.ThoronOrb;
import io.github.jvuong4.bloomfestal.registry.BFEntities;
import io.github.jvuong4.bloomfestal.registry.BFSounds;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static com.streetart.AllEntityTypes.PAINT_BALLOON;
import static com.streetart.AllItems.PAINT_BALLOONS;

public class DyeblastOrb extends ExplodingOrb {

	public ParticleOptions damageParticle;
	public ColorComponent defaultComponent = ColorComponent.CLEAR;

	public DyeblastOrb(final EntityType<? extends DyeblastOrb> type, final Level level) {
		super(type, level);
		initVals();
	}

	public DyeblastOrb(final Level level, final LivingEntity mob, final Vec3 direction) {
		super(StreetArtEntities.DYEBLAST_ORB, level, mob, direction);
		initVals();
	}

	public DyeblastOrb(final Level level, final double x, final double y, final double z, final Vec3 direction) {
		super(StreetArtEntities.DYEBLAST_ORB, level, x, y, z, direction);
		initVals();
	}

	protected void initVals()
	{
		accelerationPower = 0.7;
		range = 3;
		potency = 8.0F;
		explosionRadius = 5.0F;
		particleSpawnChance = 2.0F;
		explosionSound = SoundEvents.GENERIC_SPLASH;

		int color = 16711680;
		try
		{
			final ItemStack item = this.getItem();
			ColorComponent component = ColorComponent.getOrDefaultComponent(item, ColorComponent.CLEAR);
			color = component.getOrDefaultOpaque(item, defaultComponent.argb);
		}
		catch(Exception e)
		{
			//boy whatever
		}
		damageParticle = new DustParticleOptions(color, 1.0F);
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return damageParticle;
	}
	@Override
	protected void createParticleTrail() {
		//i just LOVE the smell of particles in the morning!
		ParticleOptions trailParticle = this.getTrailParticle();
		Vec3 position = this.position();
		if (trailParticle != null) {
			for (int i = 0; i < 4; i++) {
				Vec3 prevDirection = this.getDeltaMovement().scale((i) * -0.25);
				this.level().addParticle(trailParticle, position.x + prevDirection.x, position.y + prevDirection.y, position.z + prevDirection.z, 0.0, 0.0, 0.0);
			}
		}
	}
	@Override
	public SimpleParticleType smallExplosionParticle()
	{
		return ParticleTypes.DAMAGE_INDICATOR;
	}
	@Override
	public SimpleParticleType largeExplosionParticle()
	{
		return ParticleTypes.DAMAGE_INDICATOR;
	}

	@Override
	protected void explode(ServerLevel level)
	{
		try
		{
			ItemStack item = this.getItem();
			ColorComponent component = ColorComponent.getOrDefaultComponent(item, defaultComponent);
			PaintBalloon balloon = new PaintBalloon(level, this.getX(),this.getY(),this.getZ(), item);
			level.addFreshEntity(balloon);
		}
		catch (Exception e)
		{
			//boy whatever
		}
		super.explode(level);
	}

	@Override
	protected void onHitBlock(final BlockHitResult hitResult) {
		if (this.level() instanceof final ServerLevel serverLevel) {
			final Vec3 splashOrigin = hitResult.getLocation()
				.add(hitResult.getDirection().getUnitVec3().scale(0.3))
				.subtract(this.getDeltaMovement().scale(0.3));

			final ItemStack item = this.getItem();
			final ColorComponent component = ColorComponent.getOrDefaultComponent(item, defaultComponent);

			final Player owner;
			if (this.getOwner() instanceof final Player player) {
				owner = player;
			} else {
				owner = null;
			}

			SplashUtil.createPaintSplash(owner, serverLevel, splashOrigin,
				5, 1000, 1f,
				SplashUtil.VariableThreshold.perlin(this.random),
				component.id,
				b -> PermissionUtil.splashingAllowed(b, serverLevel, owner));
		}

		super.onHitBlock(hitResult);
	}

	@Override
	protected void dealExplosionDamage(final ServerLevel level) {
		Vec3 rocketPos = this.position();

		for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(explosionRadius))) {
			if (!(this.distanceToSqr(target) > explosionRadius*explosionRadius)) {
				if(!(target.getUUID() == owner.getUUID()))
				{
					boolean canSee = false;
					for (int testStep = 0; testStep < 2; testStep++) {
						Vec3 to = new Vec3(target.getX(), target.getY(0.5 * testStep), target.getZ());
						HitResult clip = this.level().clip(new ClipContext(rocketPos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
						if (clip.getType() == HitResult.Type.MISS) {
							canSee = true;
							break;
						}
					}
					if (canSee) {
						//remove rapid decay for consistent damage
						float damage = potency * (float)Math.sqrt((explosionRadius - this.distanceTo(target)) / explosionRadius);
						target.hurtServer(level, this.damageSources().indirectMagic(this, this.getOwner()), damage);
						spawnDamageParticles(target, damageParticle, level);
					}
				}
			}
		}
		if (this.level() instanceof final ServerLevel serverLevel) {
			final Vec3 splashOrigin = this.position();

			final ItemStack item = this.getItem();
			final ColorComponent component = ColorComponent.getOrDefaultComponent(item, ColorComponent.CLEAR);

			final Player owner;
			if (this.getOwner() instanceof final Player player) {
				owner = player;
			} else {
				owner = null;
			}

			SplashUtil.createPaintSplash(owner, serverLevel, splashOrigin,
				5, 1000, 1f,
				SplashUtil.VariableThreshold.perlin(this.random),
				component.id,
				b -> PermissionUtil.splashingAllowed(b, serverLevel, owner));
		}
	}
}

package com.thetaciturnone.spoingle.entity;

import com.google.common.collect.ImmutableList;
import com.thetaciturnone.spoingle.Spoingle;
import com.thetaciturnone.spoingle.util.BucketableDry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class SpoingleEntity extends TameableEntity implements Angerable, BucketableDry {
	private static final TrackedData<Integer> ANGER_TIME;
	private static final UniformIntProvider ANGER_TIME_RANGE;
	public static final Predicate<LivingEntity> FOLLOW_TAMED_PREDICATE;
	@Nullable
	private UUID targetUuid;
	private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(SpoingleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected SpoingleEntity(EntityType<? extends SpoingleEntity> entityType, World world) {
		super(entityType, world);
		this.setTamed(false);
		this.setPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0F);
	}

	protected void initGoals() {
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4F));
		this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
		this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
		this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
		this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(10, new LookAroundGoal(this));
		this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
		this.targetSelector.add(2, new AttackWithOwnerGoal(this));
		this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
		this.targetSelector.add(5, new UntamedActiveTargetGoal(this, AnimalEntity.class, false, FOLLOW_TAMED_PREDICATE));
		this.targetSelector.add(6, new UntamedActiveTargetGoal(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
		this.targetSelector.add(7, new ActiveTargetGoal(this, AbstractSkeletonEntity.class, false));
		this.targetSelector.add(8, new UniversalAngerGoal(this, true));
	}

	public static DefaultAttributeContainer.Builder createSpoingleAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896).add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
	}



	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ANGER_TIME, 0);
		this.dataTracker.startTracking(FROM_BUCKET, false);
	}

	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("FromBucket", isFromBucket());
		this.writeAngerToNbt(nbt);
	}

	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		nbt.getBoolean("FromBucket");
		this.readAngerFromNbt(this.world, nbt);
	}

	public void tickMovement() {
		super.tickMovement();

		if (!this.world.isClient) {
			this.tickAngerLogic((ServerWorld)this.world, true);
		}

	}

	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.6F;
	}

	public boolean damage(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			Entity entity = source.getAttacker();
			if (!this.world.isClient) {
				this.setSitting(false);
			}

			if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof PersistentProjectileEntity)) {
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.damage(source, amount);
		}
	}

	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		if (tamed) {
			this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0);
			this.setHealth(20.0F);
		} else {
			this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0);
		}

		this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0);
	}

	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		if (this.world.isClient) {
			boolean bl = this.isOwner(player) || this.isTamed() || itemStack.isOf(Items.RAW_IRON) && !this.isTamed() && !this.hasAngerTime();
			return bl ? ActionResult.CONSUME : ActionResult.PASS;
		} else {
			if (this.isTamed()) {
				if (this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
					if (!player.getAbilities().creativeMode) {
						itemStack.decrement(1);
					}

					this.heal((float) item.getFoodComponent().getHunger());
					return ActionResult.SUCCESS;
				}
			} else if (itemStack.isOf(Items.RAW_IRON) && !this.hasAngerTime()) {
				if (!player.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}

				if (this.random.nextInt(3) == 0) {
					this.setOwner(player);
					this.navigation.stop();
					this.setTarget(null);
					this.world.sendEntityStatus(this, (byte) 7);
				} else {
					this.world.sendEntityStatus(this, (byte) 6);
				}

				return ActionResult.SUCCESS;
			}
		}
		return BucketableDry.tryBucket(player, hand, this).orElse(super.interactMob(player, hand));
	}

	public boolean isBreedingItem(ItemStack stack) {
		Item item = stack.getItem();
		return item.getDefaultStack().isOf(Items.COOKIE);
	}

	public int getLimitPerChunk() {
		return 8;
	}

	public int getAngerTime() {
		return this.dataTracker.get(ANGER_TIME);
	}

	public void setAngerTime(int ticks) {
		this.dataTracker.set(ANGER_TIME, ticks);
	}

	public void chooseRandomAngerTime() {
		this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
	}

	@Nullable
	public UUID getAngryAt() {
		return this.targetUuid;
	}

	public void setAngryAt(@Nullable UUID uuid) {
		this.targetUuid = uuid;
	}


	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		SpoingleEntity spoingle = SpoingleEntities.SPOINGLE.create(world);
		UUID uUID = this.getOwnerUuid();
		if (uUID != null) {
			spoingle.setOwnerUuid(uUID);
			spoingle.setTamed(true);
		}

		return spoingle;
	}

	public boolean canBreedWith(AnimalEntity other) {
		if (other == this) {
			return false;
		} else if (!this.isTamed()) {
			return false;
		} else if (!(other instanceof SpoingleEntity)) {
			return false;
		} else {
			SpoingleEntity spoingleEntity = (SpoingleEntity)other;
			if (!spoingleEntity.isTamed()) {
				return false;
			} else {
				return this.isInLove() && spoingleEntity.isInLove();
			}
		}
	}

	public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
		if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
			if (target instanceof SpoingleEntity) {
				SpoingleEntity wolfEntity = (SpoingleEntity)target;
				return !wolfEntity.isTamed() || wolfEntity.getOwner() != owner;
			} else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).shouldDamagePlayer((PlayerEntity)target)) {
				return false;
			} else {
				return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
			}
		} else {
			return false;
		}
	}

	public boolean canBeLeashedBy(PlayerEntity player) {
		return !this.hasAngerTime() && super.canBeLeashedBy(player);
	}

	public Vec3d getLeashOffset() {
		return new Vec3d(0.0, 0.6F * this.getStandingEyeHeight(), this.getWidth() * 0.4F);
	}

	protected SoundEvent getAmbientSound() {
		if (this.hasAngerTime()) {
			return Spoingle.ANGRY_SPOINGLE;
		} else {
			return Spoingle.IDLE_SPOINGLE;
		}
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return Spoingle.HURT_SPOINGLE;
	}

	protected SoundEvent getDeathSound() {
		return Spoingle.DEATH_SPOINGLE;
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	public boolean isFromBucket() {
		return this.dataTracker.get(FROM_BUCKET);
	}

	public void setFromBucket(boolean fromBucket) {
	}

	@Override
	public void copyDataToStack(ItemStack stack) {
		Bucketable.copyDataToStack(this, stack);
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		nbtCompound.putInt("Age", this.getBreedingAge());
		if (this.getOwnerUuid() != null) nbtCompound.putUuid("Owner", this.getOwnerUuid());
	}

	@Override
	public void copyDataFromNbt(NbtCompound nbt) {
		Bucketable.copyDataFromNbt(this, nbt);
		if (nbt.contains("Age")) {
			this.setBreedingAge(nbt.getInt("Age"));
		}
		if (nbt.contains("Owner")) {
			this.setOwnerUuid(nbt.getUuid("Owner"));
			this.setTamed(true);
		} else {
			this.setTamed(false);
		}
	}

	@Override
	public ItemStack getBucketItem() {
		return new ItemStack(Spoingle.SPOINGLE_BUCKET);
	}

	@Override
	public SoundEvent getBucketFillSound() {
		return Spoingle.BUCKET_FILL;
	}
	static {
		ANGER_TIME = DataTracker.registerData(SpoingleEntity.class, TrackedDataHandlerRegistry.INTEGER);
		FOLLOW_TAMED_PREDICATE = (entity) -> {
			EntityType<?> entityType = entity.getType();
			return entityType == EntityType.SHEEP || entityType == EntityType.RABBIT || entityType == EntityType.FOX;
		};
		ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
	}

}

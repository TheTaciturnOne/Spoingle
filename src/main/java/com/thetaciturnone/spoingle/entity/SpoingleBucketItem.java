package com.thetaciturnone.spoingle.entity;
import com.thetaciturnone.spoingle.Spoingle;
import com.thetaciturnone.spoingle.util.BucketableDry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SpoingleBucketItem extends Item {

    public SpoingleBucketItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null) {
			PlayerEntity owner = context.getPlayer();
			Hand hand = context.getHand();
			World world = context.getWorld();
			ItemStack stack = owner.getStackInHand(hand);
			SpoingleEntity spoingle = new SpoingleEntity(SpoingleEntities.SPOINGLE, context.getWorld());

			BlockHitResult blockHitResult = BucketItem.raycast(context.getWorld(), context.getPlayer(), RaycastContext.FluidHandling.SOURCE_ONLY);
			BlockPos blockPos = blockHitResult.getBlockPos();
			Direction direction = blockHitResult.getSide();
			BlockPos blockPos2 = blockPos.offset(direction);

			spoingle.setPosition(blockPos2.getX() + .5f, blockPos2.getY(), blockPos2.getZ() + .5f);
			spoingle.setFromBucket(true);
			spoingle.copyDataFromNbt(context.getStack().getOrCreateNbt());
			if (stack.hasCustomName()){
				spoingle.setCustomName(stack.getName());
			}

            context.getWorld().playSound(context.getPlayer(), context.getBlockPos(), Spoingle.BUCKET_EMPTY, SoundCategory.NEUTRAL, 1.0f, 1.4f);
            context.getWorld().spawnEntity(spoingle);

            if (context.getPlayer() != null && !context.getPlayer().getAbilities().creativeMode) {
                context.getPlayer().setStackInHand(context.getHand(), new ItemStack(Items.BUCKET));
            }

            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }
}

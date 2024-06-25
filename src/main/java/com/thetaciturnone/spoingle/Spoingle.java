package com.thetaciturnone.spoingle;

import com.thetaciturnone.spoingle.entity.SpoingleBucketItem;
import com.thetaciturnone.spoingle.entity.SpoingleEntities;
import com.thetaciturnone.spoingle.entity.SpoingleEntity;
import com.thetaciturnone.spoingle.painting.SpoinglePaintings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Spoingle implements ModInitializer {
	public static final Identifier IDLE_ID = new Identifier("spoingle:spoingle_idle");
	public static SoundEvent IDLE_SPOINGLE = new SoundEvent(IDLE_ID);
	public static final Identifier HURT_ID = new Identifier("spoingle:spoingle_hurt");
	public static SoundEvent HURT_SPOINGLE = new SoundEvent(HURT_ID);
	public static final Identifier DEATH_ID = new Identifier("spoingle:spoingle_death");
	public static SoundEvent DEATH_SPOINGLE = new SoundEvent(DEATH_ID);
	public static final Identifier ANGRY_ID = new Identifier("spoingle:spoingle_angry");
	public static SoundEvent ANGRY_SPOINGLE = new SoundEvent(ANGRY_ID);
	public static final Identifier BUCKET_FILL_ID = new Identifier("spoingle:spoingle_bucket_fill");
	public static SoundEvent BUCKET_FILL = new SoundEvent(BUCKET_FILL_ID);
	public static final Identifier BUCKET_EMPTY_ID = new Identifier("spoingle:spoingle_bucket_empty");
	public static SoundEvent BUCKET_EMPTY = new SoundEvent(BUCKET_EMPTY_ID);
	private static final Identifier DEEP_DARK_CHEST_ID
		= new Identifier("minecraft", "chests/ancient_city");
	public static final String MOD_ID = "spoingle";
	public static final Logger LOGGER = LoggerFactory.getLogger("spoingle");
	public static final Item SPOINGLE_SPAWN_EGG = registerItem("spoingle_spawn_egg",
		new SpawnEggItem(SpoingleEntities.SPOINGLE, 0xd2d2d2, 0x000000, new FabricItemSettings()));
	public static final Item SPOINGLE_BUCKET = registerItem("spoingle_bucket",
		new SpoingleBucketItem((new Item.Settings()).maxCount(1).group(ItemGroup.MISC)));
	public static void registerModItems() {
		Spoingle.LOGGER.debug("gpreokerioregon"+ Spoingle.MOD_ID);
	}


	@Override
	public void onInitialize() {
		Registry.register(Registry.SOUND_EVENT, IDLE_ID, IDLE_SPOINGLE);
		Registry.register(Registry.SOUND_EVENT, HURT_ID, HURT_SPOINGLE);
		Registry.register(Registry.SOUND_EVENT, DEATH_ID, DEATH_SPOINGLE);
		Registry.register(Registry.SOUND_EVENT, ANGRY_ID, ANGRY_SPOINGLE);
		Registry.register(Registry.SOUND_EVENT, BUCKET_FILL_ID, BUCKET_FILL);
		Registry.register(Registry.SOUND_EVENT, BUCKET_EMPTY_ID, BUCKET_EMPTY);
		SpoinglePaintings.registerSpoinglePaintings();
		Spoingle.registerModItems();
		Spoingle.modifyLootTables();
		FabricDefaultAttributeRegistry.register(SpoingleEntities.SPOINGLE, SpoingleEntity.createSpoingleAttributes());

	}
	private static Item registerItem(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(Spoingle.MOD_ID, name), item);
	}

	public static void modifyLootTables() {
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (DEEP_DARK_CHEST_ID.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1))
					.conditionally(RandomChanceLootCondition.builder(0.15f))
					.with(ItemEntry.builder(Spoingle.SPOINGLE_BUCKET))
					.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
				tableBuilder.pool(poolBuilder.build());
			}
		});
	}

}

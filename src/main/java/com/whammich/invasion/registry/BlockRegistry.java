package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import com.whammich.invasion.nexus.BlockNexus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);

    public static final RegistryObject<Block> NEXUS = BLOCKS.register("nexus",
            () -> new BlockNexus(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0F, 6000000.0F)
                    .sound(SoundType.GLASS)
                    .requiresCorrectToolForDrops()));

    private BlockRegistry() {
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ItemRegistry.ITEMS.register("nexus",
                () -> new BlockItem(NEXUS.get(), new Item.Properties()));
    }
}

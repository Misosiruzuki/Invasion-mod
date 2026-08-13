package com.whammich.invasion.registry;

import com.whammich.invasion.Reference;
import com.whammich.invasion.nexus.NexusBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MODID);

    public static final RegistryObject<BlockEntityType<NexusBlockEntity>> NEXUS =
            BLOCK_ENTITIES.register("nexus",
                    () -> BlockEntityType.Builder.of(NexusBlockEntity::new, BlockRegistry.NEXUS.get()).build(null));

    private BlockEntityRegistry() {
    }

    public static void register(IEventBus modBus) {
        BLOCK_ENTITIES.register(modBus);
    }
}

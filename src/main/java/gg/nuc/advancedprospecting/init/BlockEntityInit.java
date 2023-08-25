package gg.nuc.advancedprospecting.init;

import gg.nuc.advancedprospecting.AdvancedProspectingMain;
import gg.nuc.advancedprospecting.blockentities.DebugBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AdvancedProspectingMain.MOD_ID);

    public static final RegistryObject<BlockEntityType<DebugBlockEntity>> DEBUG_BLOCK = BLOCK_ENTITY_TYPES.register("debug_block", () -> BlockEntityType.Builder.of(DebugBlockEntity::new, BlockInit.DEBUG_BLOCK.get()).build(null));
}

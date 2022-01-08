package crimsonfluff.compasswaypoints.item;

import crimsonfluff.compasswaypoints.CompassWaypoints;
import crimsonfluff.compasswaypoints.init.initItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Optional;

public class WaypointCompassItem extends Item {
    public static final String POS = "pos";
    public static final String DIMENSION = "dim";

    public WaypointCompassItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if((!context.getPlayer().isSneaking() && context.getStack().hasNbt())) return super.useOnBlock(context);
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        world.playSound(null, blockPos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
        ItemStack itemStack = context.getStack();
        this.writeNbt(world.getRegistryKey(), blockPos, itemStack.getOrCreateNbt());
        return ActionResult.success(world.isClient);
    }

    @Override public boolean hasGlint(ItemStack stack){
        return stack.hasNbt();
    }

    private void writeNbt(RegistryKey<World> worldKey, BlockPos pos, NbtCompound nbt) {
        nbt.put(POS, NbtHelper.fromBlockPos(pos));
        World.CODEC.encodeStart(NbtOps.INSTANCE, worldKey).resultOrPartial(CompassWaypoints.LOGGER::error).ifPresent(nbtElement -> nbt.put(DIMENSION, (NbtElement)nbtElement));
    }
}

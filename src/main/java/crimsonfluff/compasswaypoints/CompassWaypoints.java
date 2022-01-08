package crimsonfluff.compasswaypoints;

import crimsonfluff.compasswaypoints.init.initItems;
import crimsonfluff.compasswaypoints.item.WaypointCompassItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompassWaypoints implements ModInitializer {
    public static final String MOD_ID = "compasswaypoints";
    public static final Logger LOGGER = LogManager.getLogger();
    private double prevAngle = 0.0D;
    private double prevWobble = 0.0D;
    private long prevWorldTime = 0L;
    @Override
    public void onInitialize() {
        initItems.register();
       FabricModelPredicateProviderRegistry.register(initItems.WAYPOINT_COMPASS_ITEM, new Identifier("angle"), (stack, world, livingEntity,x) -> {
            boolean isLiving = livingEntity != null;

            if (!isLiving && !stack.isInFrame() || !stack.hasNbt() || !stack.getNbt().contains(WaypointCompassItem.LODESTONE_POS_KEY)) return 0;

            Entity entity = isLiving ? livingEntity : stack.getFrame();

            if (world == null) world = (ClientWorld) entity.world;

            BlockPos blockPos = NbtHelper.toBlockPos(stack.getNbt().getCompound(WaypointCompassItem.LODESTONE_POS_KEY));

            double angle;
            double entityAngle = isLiving ? entity.getEyeY() : getFrameAngle((ItemFrameEntity) entity);

            entityAngle /= 360.0D;
            entityAngle = MathHelper.floorMod(entityAngle, 1.0D);
            double posAngle = getPosToAngle(blockPos, entity);
            posAngle /= Math.PI * 2D;
            angle = 0.5D - (entityAngle - 0.25D - posAngle);

            if (isLiving) angle = wobble(world, angle);

            return MathHelper.floorMod((float) angle, 1.0F);
        });
    }
    private double wobble(World world, double angle) {
        long worldTime = world.getTime();

        if (worldTime != prevWorldTime) {
            prevWorldTime = worldTime;
            double angleDifference = angle - prevAngle;
            angleDifference = MathHelper.floorMod(angleDifference + 0.5D, 1.0D) - 0.5D;

            prevWobble += angleDifference * 0.1D;
            prevWobble *= 0.8D;
            prevAngle = MathHelper.floorMod(prevAngle + prevWobble, 1.0D);
        }

        return prevAngle;
    }
    private double getFrameAngle(ItemFrameEntity entity) {
        return MathHelper.wrapDegrees(180 + entity.getHorizontalFacing().getOffsetY() * 90);
    }

    private double getPosToAngle(BlockPos pos, Entity entity) {
        return Math.atan2(pos.getZ() - entity.getZ(), pos.getX() - entity.getX());
    }
}

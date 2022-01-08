package crimsonfluff.compasswaypoints.client;

import crimsonfluff.compasswaypoints.init.initItems;
import crimsonfluff.compasswaypoints.item.WaypointCompassItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class CompassWaypointsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(initItems.WAYPOINT_COMPASS_ITEM, new Identifier("angle"), new UnclampedModelPredicateProvider() {
            private double rotation;
            private double rota;
            private long lastUpdateTick;

            @Override
            public float unclampedCall(ItemStack stack, ClientWorld world, LivingEntity entityLiving, int seed) {
                if(!stack.hasNbt()) return 0.0F;
                if (entityLiving == null && !stack.isInFrame() ) {
                    return 0.0F;
                } else {
                    final boolean entityExists = entityLiving != null;
                    final Entity entity = (Entity) (entityExists ? entityLiving : stack.getFrame());
                    if (world == null && entity.world instanceof ClientWorld) {
                        world = (ClientWorld) entity.world;
                    }

                    double rotation = entityExists ? (double) entity.getYaw() : getFrameRotation((ItemFrameEntity) entity);
                    rotation = rotation % 360.0D;
                    double adjusted = Math.PI - ((rotation - 90.0D) * 0.01745329238474369D - getAngle(world, entity, stack));

                    if (entityExists) {
                        adjusted = wobble(world, adjusted);
                    }

                    final float f = (float) (adjusted / (Math.PI * 2D));
                    return MathHelper.floorMod(f, 1.0F);
                }
            }

            private double wobble(ClientWorld world, double amount) {
				if (world.getTime() != lastUpdateTick) {
					lastUpdateTick = world.getTime();
					double d0 = amount - rotation;
					d0 = d0 % (Math.PI * 2D);
					d0 = MathHelper.clamp(d0, -1.0D, 1.0D);
					rota += d0 * 0.1D;
					rota *= 0.8D;
					rotation += rota;
				}

				return rotation;
			}

            private double getFrameRotation(ItemFrameEntity itemFrame) {
                return (double) MathHelper.wrapDegrees(180 + itemFrame.getHorizontalFacing().getHorizontal() * 90);
            }

            private double getAngle(ClientWorld world, Entity entity, ItemStack stack) {
                if (stack.getItem() == initItems.WAYPOINT_COMPASS_ITEM) {
                    BlockPos pos = NbtHelper.toBlockPos(stack.getNbt().getCompound(WaypointCompassItem.POS));
                    return Math.atan2((double) pos.getZ() - entity.getPos().z, (double) pos.getX() - entity.getPos().x);
                }
                return 0.0D;
            }

        });
    }
}

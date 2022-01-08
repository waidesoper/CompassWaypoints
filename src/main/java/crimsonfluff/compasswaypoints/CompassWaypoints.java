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


    @Override
    public void onInitialize() {
        initItems.register();
    }
}
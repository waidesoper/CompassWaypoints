package crimsonfluff.compasswaypoints.init;

import crimsonfluff.compasswaypoints.CompassWaypoints;
import crimsonfluff.compasswaypoints.item.WaypointCompassItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class initItems {
    public static Item WAYPOINT_COMPASS_ITEM = new WaypointCompassItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static void register(){
        Registry.register(Registry.ITEM, new Identifier(CompassWaypoints.MOD_ID,"waypoint_compass"),WAYPOINT_COMPASS_ITEM);
    }
}

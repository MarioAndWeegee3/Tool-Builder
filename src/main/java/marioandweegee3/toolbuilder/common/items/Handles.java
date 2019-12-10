package marioandweegee3.toolbuilder.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Handles {
    public static Item wood_handle = create("wood", false);
    public static Item stone_handle = create("stone", false);
    public static Item gold_handle = create("gold", false);
    public static Item bone_handle = create("bone", false);
    public static Item diamond_handle = create("diamond", false);

    public static Item wood_gripped_handle = create("wood", true);
    public static Item stone_gripped_handle = create("stone", true);
    public static Item gold_gripped_handle = create("gold", true);
    public static Item bone_gripped_handle = create("bone", true);
    public static Item diamond_gripped_handle = create("diamond", true);

    private static Handle create(String material, boolean grip){
        return new Handle(new Item.Settings().group(ItemGroup.MISC), material, grip);
    }
}
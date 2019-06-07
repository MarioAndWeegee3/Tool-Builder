package marioandweegee3.toolbuilder.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Handles {
    public static Item wood_handle = create();
    public static Item stone_handle = create();
    public static Item gold_handle = create();

    private static Item create(){
        return new Item( new Item.Settings().group(ItemGroup.MISC));
    }
}
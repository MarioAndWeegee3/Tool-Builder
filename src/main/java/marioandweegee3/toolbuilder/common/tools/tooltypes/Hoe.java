package marioandweegee3.toolbuilder.common.tools.tooltypes;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.registry.Registry;

public class Hoe extends HoeItem{
    private static float speed = ToolValues.HOE.getSpeed();

    private Hoe(ToolMaterial material, Item.Settings settings){
        super(material, speed, settings);
    }

    public static Item create(ToolMaterial material){
        return new Hoe(material, new Item.Settings());
    }

    public static void register(Item item, String name, ToolMaterial material, String group){
        item = create(material);
        Registry.register(Registry.ITEM, ToolBuilder.makeID(name), item);
        Groups.addTo(item, group);
    }
}
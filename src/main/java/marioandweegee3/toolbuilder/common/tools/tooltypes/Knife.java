package marioandweegee3.toolbuilder.common.tools.tooltypes;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.registry.Registry;

public class Knife extends SwordItem{
    private static float speed = ToolValues.KNIFE.getSpeed();
    private static float damage = ToolValues.KNIFE.getDamage();
    private Knife(ToolMaterial material, Item.Settings settings){
        super(material, (int) damage, speed, settings);
    }

    public static Item create(ToolMaterial material){
        return new Knife(material, new Item.Settings());
    }

    public static void register(Item item, String name, ToolMaterial material, String group){
        item = create(material);
        Registry.register(Registry.ITEM, ToolBuilder.makeID(name), item);
        Groups.addTo(item, group);
    }
}
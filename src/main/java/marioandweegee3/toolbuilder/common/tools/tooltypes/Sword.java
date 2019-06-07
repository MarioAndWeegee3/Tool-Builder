package marioandweegee3.toolbuilder.common.tools.tooltypes;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.registry.Registry;

public class Sword extends SwordItem{
    private static float speed = ToolValues.SWORD.getSpeed();
    private static float damage = ToolValues.SWORD.getDamage();
    private Sword(ToolMaterial material, Item.Settings settings){
        super(material, (int) damage, speed, settings);
    }

    public static Item create(ToolMaterial material){
        return new Sword(material, new Item.Settings());
    }

    public static void register(Item item, String name, ToolMaterial material, String group){
        item = create(material);
        Registry.register(Registry.ITEM, ToolBuilder.makeID(name), item);
        Groups.addTo(item, group);
    }
}
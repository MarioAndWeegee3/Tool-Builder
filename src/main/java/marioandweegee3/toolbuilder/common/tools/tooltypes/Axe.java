package marioandweegee3.toolbuilder.common.tools.tooltypes;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

public class Axe extends AxeItem{
    private static float speed = ToolValues.AXE.getSpeed();
    private static float damage = ToolValues.AXE.getDamage();

    public String name;

    private Axe(ToolMaterial material, Item.Settings settings, String name){
        super(material, damage, speed, settings);
        this.name = name;
    }

    public static Item create(ToolMaterial material, String name){
        return new Axe(material, new Item.Settings(), name);
    }
}
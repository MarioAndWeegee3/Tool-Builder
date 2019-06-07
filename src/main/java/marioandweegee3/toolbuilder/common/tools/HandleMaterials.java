package marioandweegee3.toolbuilder.common.tools;

import net.minecraft.item.Item;
import marioandweegee3.toolbuilder.common.Handles;

public enum HandleMaterials {
    WOOD(0, 1, 0, "wood", Handles.wood_handle),
    STONE(20, 0.95f, -10, "stone", Handles.stone_handle),
    GOLD(-10, 1.1f, 10, "gold", Handles.gold_handle);

    private float durability, extra;
    private int enchant;
    private String name;

    public final Item handle;

    private HandleMaterials(float durability, float extra, int enchantModifier, String name, Item handle){
        this.durability = durability;
        this.extra = extra;
        this.name = name;
        this.handle = handle;
    }

    public float getDurabilityModifier() {
        return durability;
    }

    public float getExtraDurability(){
        return extra;
    }

    public int getEnchantabilityModifier(){
        return enchant;
    }

    public String getName(){
        return name;
    }
}
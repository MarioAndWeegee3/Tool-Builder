package marioandweegee3.toolbuilder.common.tools;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.Effects;

public enum HandleMaterials implements HandleMaterial {
    WOOD(0, 1, 1, 0, "wood", Effects.GROWING), 
    STONE(20, 0.95f, 0.9f, -8, "stone", Effects.RESILIENT), 
    GOLD(-10, 1.2f, 1.18f, 10, "gold"),
    BONE(30, 0.90f, 1.08f, -5, "bone"), 
    DIAMOND(35, 1.25f, 1.2f, -10, "diamond");

    private float durability, extra, speedMod;
    private int enchant;
    private String name;
    private Set<Effect> effects = new HashSet<>(0);

    private HandleMaterials(float durability, float extra, float speedMod, int enchantModifier, String name,
            Effect... effects) {
        this.durability = durability;
        this.extra = extra;
        this.enchant = enchantModifier;
        this.speedMod = speedMod;
        for (Effect effect : effects) {
            this.effects.add(effect);
        }
        this.name = name;
    }

    public float getDurabilityModifier() {
        return durability;
    }

    public ArrayList<Item> getRepairItems(boolean grip) {
        Item[] items;
        switch (name) {
        case "wood": {
            if (grip) {
                items = toArray(getTBItem("wood_gripped_handle"));
            } else {
                if (ConfigHandler.INSTANCE.canCraftWithSticks()) {
                    items = toArray(getTBItem("wood_handle"), getItem(new Identifier("stick")));
                } else {
                    items = toArray(getTBItem("wood_handle"));
                }
            }
            break;
        }
        case "stone":
            items = grip ? toArray(getTBItem("stone_gripped_handle")) : toArray(getTBItem("stone_handle"));
            break;
        case "gold":
            items = grip ? toArray(getTBItem("gold_gripped_handle")) : toArray(getTBItem("gold_handle"));
            break;
        case "bone":
            items = grip ? toArray(getTBItem("bone_gripped_handle")) : toArray(getTBItem("bone_handle"));
            break;
        case "diamond":
            items = grip ? toArray(getTBItem("diamond_gripped_handle")) : toArray(getTBItem("diamond_handle"));
            break;
        default:
            items = new Item[0];
        }

        return new ArrayList<>(Arrays.asList(items));
    }

    private Item[] toArray(Item... items) {
        return items;
    }

    private Item getItem(Identifier id) {
        return Registry.ITEM.get(id);
    }

    private Item getTBItem(String id) {
        return getItem(ToolBuilder.makeID(id));
    }

    public float getMiningSpeedMultiplier() {
        return speedMod;
    }

    public float getDrawSpeedMultiplier() {
        if (speedMod > 1) {
            return speedMod + 0.15f;
        } else if (speedMod < 1) {
            return speedMod - 0.15f;
        } else {
            return speedMod;
        }
    }

    public float getExtraDurability() {
        return extra;
    }

    public int getEnchantabilityModifier() {
        return enchant;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMod() {
        return ToolBuilder.modID;
    }

    @Override
    public ArrayList<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
}
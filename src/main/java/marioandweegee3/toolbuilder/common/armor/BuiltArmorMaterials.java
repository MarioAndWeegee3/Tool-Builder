package marioandweegee3.toolbuilder.common.armor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import marioandweegee3.toolbuilder.common.tools.HeadMaterials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum BuiltArmorMaterials implements BuiltArmorMaterial {
    WOOD(HeadMaterials.WOOD, 3, new int[]{1,2,2,1}, 0, Effects.FLAMMABLE, Effects.GROWING),
    STONE(HeadMaterials.STONE, 4, new int[]{1,2,2,1}, 1, Effects.RESILIENT),
    COPPER(HeadMaterials.COPPER, 10, new int[]{1,4,5,2}, 0, Effects.EXPERIENCE),
    LEAD(HeadMaterials.LEAD, 17, new int[]{2,5,6,2}, 0.5f, Effects.MAGICAL),
    SILVER(HeadMaterials.SILVER, 12, new int[]{2,3,4,2}, 0, Effects.HOLY),
    STEEL(HeadMaterials.STEEL, 18, new int[]{3,5,7,3}, 0.75f, Effects.MAGNETIC),
    OBSIDIAN(HeadMaterials.OBSIDIAN, 42, new int[]{5,8,10,5}, 3),
    COBALT(HeadMaterials.COBALT, 20, new int[]{3,5,6,3}, 0, Effects.LIGHT),
    ALUMINUM(HeadMaterials.ALUMINUM, 13, new int[]{2,5,6,2}, 0),
    TUNGSTEN(HeadMaterials.TUNGSTEN, 21, new int[]{3,5,7,3}, 0.3f, Effects.RESILIENT),
    SLIME(HeadMaterials.SLIME, 16, new int[]{1,3,2,1}, 0, Effects.BOUNCY);

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private HeadMaterial baseMaterial;
    private int durabilityMultiplier;
    private int[] protectionAmounts;
    private float toughness;
    private Set<Effect> effects = new HashSet<>(0);

    private BuiltArmorMaterials(HeadMaterial material, int durabilityMultiplier, int[] protection, float toughness, Effect... effects){
        baseMaterial = material;
        this.durabilityMultiplier = durabilityMultiplier;
        protectionAmounts = protection;
        this.toughness = toughness;
        for(Effect effect : effects){
            this.effects.add(effect);
        }
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        int slotNum = slot.getEntitySlotId();
        return BASE_DURABILITY[slotNum] * durabilityMultiplier;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        int slotNum = slot.getEntitySlotId();
        return protectionAmounts[slotNum];
    }

    @Override
    public int getEnchantability() {
        return baseMaterial.getEnchantability();
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return baseMaterial.getRepairIngredient();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public String getName() {
        return "toolbuilder/"+baseMaterial.getName();
    }

    @Override
    public String getMaterialName(){
        return baseMaterial.getName();
    }

    @Override
    public String getMod() {
        return ToolBuilder.modID;
    }

    @Override
	public float getToughness() {
        return toughness;
    }
    
    @Override
    public ArrayList<Effect> getEffects(){
        return new ArrayList<>(effects);
    }

    @Override
    public String getRepairString() {
        return baseMaterial.getRepairString();
    }

    @Override
    public boolean isCotton() {
        return baseMaterial.isCotton();
    }

}
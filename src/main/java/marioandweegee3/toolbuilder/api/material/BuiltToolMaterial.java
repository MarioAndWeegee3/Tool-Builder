package marioandweegee3.toolbuilder.api.material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class BuiltToolMaterial implements ToolMaterial{
    private static final float gripModifier = 1.2f;
    public final HandleMaterial handle;
    public final HeadMaterial head;
    public final Boolean isGripped;
    private String name;

    protected BuiltToolMaterial(HandleMaterial handle, HeadMaterial head, String name, Boolean isGripped) {
        this.handle = handle;
        this.isGripped = isGripped;
        this.head = head;
        this.name = name;
    }

    public static BuiltToolMaterial of(HandleMaterial handle, HeadMaterial head, String name, Boolean isGripped){
        return new BuiltToolMaterial(handle, head, name, isGripped);
    }

    public Set<Effect> getEffects(){
        Set<Effect> effects = new HashSet<>(0);
        effects.addAll(Arrays.asList(head.getEffects()));
        effects.addAll(handle.getEffects());
        return effects;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.head.getRepairIngredient();
    }

    @Override
    public float getMiningSpeed() {
        return head.getMiningSpeed() * getMineSpeedModifier();
    }

    public float getMineSpeedModifier(){
        float speedMod = 1;
        if(this.isGripped) speedMod *= gripModifier;
        speedMod *= handle.getMiningSpeedMultiplier();
        return speedMod;
    }

    @Override
    public int getMiningLevel() {
        return this.head.getMiningLevel();
    }

    @Override
    public int getEnchantability() {
        int newEnchant = this.head.getEnchantability();
        newEnchant += this.handle.getEnchantabilityModifier();
        if(newEnchant <= 0) newEnchant = 1;
        return newEnchant;
    }

    @Override
    public int getDurability() {
        int newDurability = this.head.getDurability();
        newDurability += this.handle.getExtraDurability();
        newDurability *= this.handle.getDurabilityMultiplier();
        return newDurability;
    }

    @Override
    public float getAttackDamage() {
        float damage = this.head.getAttackDamage();
        return damage;
    }

    public String getName(){
        return this.name;
    }
}

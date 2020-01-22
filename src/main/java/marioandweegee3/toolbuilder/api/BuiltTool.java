package marioandweegee3.toolbuilder.api;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.Modifiable;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BuiltTool extends Modifiable {
    public default String getTranslationName(BuiltToolMaterial material) {
        return getType() + "." + getModName() + "." + material.head.getName();
    }

    public String getType();

    public BuiltToolMaterial getMaterial();

    public default String getModName() {
        return "toolbuilder";
    }

    public default int getNumModifiers(ItemStack stack){
        for(EffectInstance instance : getEffects(stack)){
            if(instance.getEffect() == Effects.EXTRA_MODS){
                return 3;
            }
        }
        return 2;
    }

    @Override
    default Set<EffectInstance> getEffects() {
        return getMaterial().getEffects();
    }

    public static float getAttackSpeed(BuiltToolMaterial material, float speed){
        float newSpeed = speed;

        if(material.isGripped){
            newSpeed += 0.1f;
        }
        
        for(EffectInstance instance : material.getEffects()){
            newSpeed += instance.getEffect().getAttackSpeedModifier(instance.getLevel());
        }

        return newSpeed;
    }

    public static ItemStack increaseCountForFortune(ItemStack tool, ItemStack drop){
        int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
        ItemStack newDrop = drop.copy();

        for(int i = 0; i < fortuneLevel; i++){
            boolean shouldIncreaseCount = (new Random().nextInt(3) == 0);
            if(shouldIncreaseCount){
                newDrop.setCount(newDrop.getCount()+1);
            }
        }

        return newDrop;
    }

    public default void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker){
        if(!target.world.isClient){
            for(EffectInstance instance : getEffects(stack)){
                instance.getEffect().onHit(stack, target, attacker, instance.getLevel());
            }
        }
    }

    public default void addTooltips(List<Text> tooltip, ItemStack stack, BuiltToolMaterial material, Style mainStyle, Style effectStyle, Style modifierStyle){
        tooltip.add(new TranslatableText(material.handle.getTranslationKey()).setStyle(mainStyle));

        tooltip.add(new TranslatableText("text.toolbuilder.durability").append((stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage()).setStyle(mainStyle));
        DecimalFormat dec = new DecimalFormat("#.##");
        dec.setRoundingMode(RoundingMode.HALF_UP);
        tooltip.add(new TranslatableText("text.toolbuilder.mining_speed").append(dec.format(material.getMiningSpeed())).setStyle(mainStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.enchantability").append(material.getEnchantability()+"").setStyle(mainStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.mining_level").append(material.getMiningLevel()+"").setStyle(mainStyle));

        if(material.isGripped){
            tooltip.add(new TranslatableText("text.toolbuilder.grip").setStyle(mainStyle));
        }

        addModifierTooltip(tooltip, stack, effectStyle, modifierStyle);
    }

    public default void onMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner, XpDropCheck dropCheck){
        for(EffectInstance effect : getEffects(stack)){
            effect.getEffect().postMine(material, stack, state, world, pos, miner, dropCheck, effect.getLevel());
        }
    }
}
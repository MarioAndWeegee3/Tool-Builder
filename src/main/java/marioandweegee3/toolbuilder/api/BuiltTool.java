package marioandweegee3.toolbuilder.api;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BuiltTool {
    public default String getTranslationName(BuiltToolMaterial material) {
        return getType() + "." + getModName() + "." + material.head.getName();
    }

    public String getType();

    public BuiltToolMaterial getMaterial();

    public default String getModName() {
        return "toolbuilder";
    }

    public default Set<EffectInstance> getEffects(ItemStack stack) {
        return EffectInstance.mergeSets(getMaterial().getEffects(), getModifierEffects(stack));
    }

    public default Set<EffectInstance> getModifierEffects(ItemStack stack) {
        Set<EffectInstance> effects = new HashSet<>(0);
        ListTag effectsTag = getModifierListTag(stack);
        for (Tag tag2 : effectsTag) {
            if (!(tag2 instanceof CompoundTag))
                continue;

            CompoundTag effectTag = (CompoundTag) tag2;
            try {
                effects.add(EffectInstance.fromTag(effectTag));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return effects;
    }

    public default ListTag getModifierListTag(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getList(Effects.effectNBTtag, 10);
    }

    public default void removeEffects(ItemStack stack, Set<EffectInstance> effects){
        CompoundTag tag = stack.getOrCreateTag();
        ListTag effectsTag = getModifierListTag(stack);

        int numMods = tag.getInt("num_modifiers");
        
        for(EffectInstance effect : effects){
            effectsTag.removeIf(t -> {
                if(!(t instanceof CompoundTag)) return false;

                CompoundTag effectTag = (CompoundTag) t;

                Effect e = TBRegistries.EFFECTS.get(new Identifier(effectTag.getString("id")));
                if(effect.getEffect().equals(e)){
                    return true;
                }

                return false;
            });

            numMods--;
        }
        tag.put(Effects.effectNBTtag, effectsTag);
        tag.putInt("num_modifiers", numMods);
        stack.setTag(tag);
    }

    public default int getNumModifiers(ItemStack stack){
        for(EffectInstance instance : getEffects(stack)){
            if(instance.getEffect() == Effects.EXTRA_MODS){
                return 3;
            }
        }
        return 2;
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

    @SuppressWarnings("unused")
    public default void addTooltips(List<Text> tooltip, ItemStack stack, BuiltToolMaterial material, Style mainStyle, Style effectStyle, Style modifierStyle){
        tooltip.add(new TranslatableText(material.handle.getTranslationKey()).setStyle(mainStyle));

        tooltip.add(new TranslatableText("text.toolbuilder.durability").append((stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage()).setStyle(mainStyle));
        DecimalFormat dec = new DecimalFormat("#.##");
        dec.setRoundingMode(RoundingMode.HALF_UP);
        tooltip.add(new TranslatableText("text.toolbuilder.mining_speed").append(dec.format(material.getMiningSpeed())).setStyle(mainStyle));

        if(material.isGripped){
            tooltip.add(new TranslatableText("text.toolbuilder.grip").setStyle(mainStyle));
        }

        List<EffectInstance> effects = new ArrayList<>(getEffects(stack));

        Collections.sort(effects);

        for(EffectInstance effect : effects){
            tooltip.add(effect.getTooltip().setStyle(effectStyle));
        }

        Integer numMods = 0;

        for(EffectInstance effect : getModifierEffects(stack)){
            numMods++;
        }

        tooltip.add(new TranslatableText("text.toolbuilder.modifiers").append(numMods.toString()).setStyle(modifierStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.max_modifiers").append(Integer.toString(getNumModifiers(stack))).setStyle(modifierStyle));
    }

    public default void onMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner, XpDropCheck dropCheck){
        for(EffectInstance effect : getEffects(stack)){
            effect.getEffect().postMine(material, stack, state, world, pos, miner, dropCheck, effect.getLevel());
        }
    }
}
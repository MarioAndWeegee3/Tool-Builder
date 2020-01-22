package marioandweegee3.toolbuilder.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public interface Modifiable {
    public Set<EffectInstance> getEffects();

    public default Set<EffectInstance> getEffects(ItemStack stack) {
        return EffectInstance.mergeSets(getEffects(), getModifierEffects(stack));
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

    public int getNumModifiers(ItemStack stack);

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

    @SuppressWarnings("unused")
    public default void addModifierTooltip(List<Text> tooltip, ItemStack stack, Style effectStyle, Style modifierStyle){
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
}
package marioandweegee3.toolbuilder.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class BuiltArmorItem extends ArmorItem {

    public BuiltArmorItem(BuiltArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public BuiltArmorMaterial getMaterial() {
        return (BuiltArmorMaterial) super.getMaterial();
    }

    public Set<EffectInstance> getEffects(ItemStack stack){
        return EffectInstance.mergeSets(getModifierEffects(stack), EffectInstance.fromEffects(getMaterial().getEffects()));
    }

    public Set<EffectInstance> getModifierEffects(ItemStack stack) {
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

    public ListTag getModifierListTag(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getList(Effects.effectNBTtag, 10);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        List<EffectInstance> effects = new ArrayList<>(getEffects(stack));

        Collections.sort(effects);

        for(EffectInstance effect : effects){
            tooltip.add(effect.getTooltip().setStyle(ToolBuilder.effectStyle));
        }

        int numModifiers = Optional.of(stack.getOrCreateTag().getInt("num_modifiers")).or(0);
        tooltip.add(new TranslatableText("text.toolbuilder.modifiers").append(numModifiers+"").setStyle(ToolBuilder.modifierStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.max_modifiers").append(Integer.toString(getNumModifiers(stack))).setStyle(ToolBuilder.modifierStyle));
    }

    public int getNumModifiers(ItemStack stack){
        for(EffectInstance instance : getEffects(stack)){
            if(instance.getEffect() == Effects.EXTRA_MODS){
                return 3;
            }
        }
        return 2;
    }

}
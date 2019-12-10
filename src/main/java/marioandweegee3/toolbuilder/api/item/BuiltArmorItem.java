package marioandweegee3.toolbuilder.api.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BuiltArmorItem extends ArmorItem {

    public BuiltArmorItem(BuiltArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public BuiltArmorMaterial getMaterial() {
        return (BuiltArmorMaterial) super.getMaterial();
    }

    public Set<Effect> getEffects(ItemStack stack){
        Set<Effect> effects = new HashSet<>(0);
        effects.addAll(getMaterial().getEffects());
        effects.addAll(getModifierEffects(stack));
        return effects;
    }

    public Set<Effect> getModifierEffects(ItemStack stack){
        Set<Effect> effects = new HashSet<>(0);
        CompoundTag tag = stack.getOrCreateTag();
        ListTag effectsTag = tag.getList(Effects.effectNBTtag, 8);
        for(Tag tag2 : effectsTag){
            if(!(tag2 instanceof StringTag)) continue;
            
            StringTag effectTag = (StringTag)tag2;
            Effect effect = TBRegistries.EFFECTS.get(new Identifier(effectTag.asString()));
            if(effect != null){
                effects.add(effect);
            }
        }
        return effects;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        for (Effect effect : getEffects(stack)) {
            tooltip.add(effect.getTranslationName().setStyle(ToolBuilder.effectStyle));
        }
    }

}
package marioandweegee3.toolbuilder.api.item;

import java.util.List;
import java.util.Set;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.material.BuiltArmorMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class BuiltArmorItem extends ArmorItem implements Modifiable {

    public BuiltArmorItem(BuiltArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public BuiltArmorMaterial getMaterial() {
        return (BuiltArmorMaterial) super.getMaterial();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        addModifierTooltip(tooltip, stack, ToolBuilder.effectStyle, ToolBuilder.modifierStyle);
    }

    public int getNumModifiers(ItemStack stack){
        for(EffectInstance instance : getEffects(stack)){
            if(instance.getEffect() == Effects.EXTRA_MODS){
                return 3;
            }
        }
        return 2;
    }

    @Override
    public Set<EffectInstance> getEffects() {
        return EffectInstance.fromEffects(getMaterial().getEffects());
    }

}
package marioandweegee3.toolbuilder.common.items;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class Handle extends Item{
    private String material;
    private boolean grip;

    public Handle(Settings settings, String material, boolean grip) {
        super(settings);
        this.grip = grip;
        this.material = material;
    }

    @Override
    public String getTranslationKey() {
        return "handle.toolbuilder."+material;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        HandleMaterial handle = TBRegistries.HANDLE_MATERIALS.get(ToolBuilder.makeID(material));
        DecimalFormat dec = new DecimalFormat("#.##");
        dec.setRoundingMode(RoundingMode.HALF_UP);
        
        tooltip.add(new TranslatableText("text.toolbuilder.extra_durability").append(handle.getExtraDurability()+"").setStyle(ToolBuilder.toolStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.durability_modifier").append(dec.format(handle.getDurabilityMultiplier())).setStyle(ToolBuilder.toolStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.mining_speed").append(handle.getMiningSpeedMultiplier()+"").setStyle(ToolBuilder.toolStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.enchantability").append(handle.getEnchantabilityModifier()+"").setStyle(ToolBuilder.toolStyle));
        if(grip){
            tooltip.add(new TranslatableText("text.toolbuilder.grip").setStyle(ToolBuilder.toolStyle));
        }
        for(EffectInstance instance : handle.getEffects()){
            tooltip.add(instance.getTooltip().setStyle(ToolBuilder.effectStyle));
        }
    }

    public boolean isGripped(){
        return grip;
    }

}
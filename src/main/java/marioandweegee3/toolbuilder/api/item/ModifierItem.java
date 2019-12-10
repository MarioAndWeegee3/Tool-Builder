package marioandweegee3.toolbuilder.api.item;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class ModifierItem extends Item implements Modifier{
    private Effect effect;

    public ModifierItem(Effect effect){
        super(new Settings().group(ItemGroup.MISC));
        this.effect = effect;
    }

    @Override
    public String getTranslationKey() {
        return "modifier.toolbuilder."+effect.getName();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("text.toolbuilder.effects."+effect.getName()).setStyle(ToolBuilder.modifierStyle));
    }

    @Override
    public Effect getEffect() {
        return effect;
    }
}
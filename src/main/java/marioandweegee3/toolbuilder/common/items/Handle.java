package marioandweegee3.toolbuilder.common.items;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
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
        if(grip){
            tooltip.add(new TranslatableText("text.toolbuilder.grip").setStyle(ToolBuilder.toolStyle));
        }
    }

    public boolean isGripped(){
        return grip;
    }

}
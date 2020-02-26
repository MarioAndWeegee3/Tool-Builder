package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.material.*;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Hoe extends HoeItem implements BuiltTool{
    private static float speed = ToolValues.HOE.getSpeed();

    private BuiltToolMaterial material;

    private Hoe(BuiltToolMaterial material, Item.Settings settings){
        super(material, BuiltTool.getAttackSpeed(material, speed), settings);
        this.material = material;
    }

    public static Item create(BuiltToolMaterial material){
        return new Hoe(material, new Item.Settings());
    }

    @Override
    public String getType() {
        return "hoe";
    }

    @Override
    public BuiltToolMaterial getMaterial() {
        return material;
    }

    @Override
    public String getTranslationKey() {
        return getTranslationName(material);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        addTooltips(tooltip, stack, material, ToolBuilder.toolStyle, ToolBuilder.effectStyle, ToolBuilder.modifierStyle);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        onHit(stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }
}
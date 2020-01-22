package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.Arrays;
import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.material.*;
import marioandweegee3.toolbuilder.common.itemgroups.Groups;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Shovel extends ShovelItem implements BuiltTool{
    private static float speed = ToolValues.SHOVEL.getSpeed();
    private static float damage = ToolValues.SHOVEL.getDamage();

    private BuiltToolMaterial material;

    private Shovel(BuiltToolMaterial material, Item.Settings settings){
        super(material, damage, BuiltTool.getAttackSpeed(material, speed), settings);
        this.material = material;
    }

    public static Item create(BuiltToolMaterial material){
        return new Shovel(material, new Item.Settings());
    }

    public static void register(Item item, String name, BuiltToolMaterial material, String group){
        item = create(material);
        Registry.register(Registry.ITEM, ToolBuilder.makeID(name), item);
        Groups.addTo(item, group);
    }

    @Override
    public boolean isEffectiveOn(BlockState state) {
        return super.isEffectiveOn(state)
                || Arrays.asList(Material.CLAY, Material.EARTH, Material.SAND, Material.SNOW, Material.SNOW_BLOCK, Material.ORGANIC)
                        .contains(state.getMaterial());
    }

    @Override
    public String getType() {
        return "shovel";
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

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        onMine(material, stack, state, world, pos, miner, this::shouldDropXp);

        return super.postMine(stack, world, state, pos, miner);
    }

    public boolean shouldDropXp(BlockState state, ItemStack stack){
        if(getMiningSpeed(stack, state) == this.miningSpeed) return true;
        else return false;
    }
}
package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.Arrays;
import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.XpDropCheck;
import marioandweegee3.toolbuilder.api.item.BigTool;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Excavator extends ShovelItem implements BigTool {
    private static float speed = ToolValues.EXCAVATOR.getSpeed();
    private static float damage = ToolValues.EXCAVATOR.getDamage();

    private BuiltToolMaterial material;

    private Excavator(BuiltToolMaterial material, Settings settings) {
        super(material, (int) damage, BuiltTool.getAttackSpeed(material, speed), settings);
        this.material = material;
    }

    public static Item create(BuiltToolMaterial material) {
        return new Excavator(ExcavatorMaterial.of(material), new Settings());
    }

    @Override
    public String getType() {
        return "excavator";
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
    public boolean isEffectiveOn(BlockState state) {
        return super.isEffectiveOn(state)
                || Arrays.asList(Material.CLAY, Material.EARTH, Material.SAND, Material.SNOW, Material.SNOW_BLOCK, Material.ORGANIC)
                        .contains(state.getMaterial());
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        addTooltips(tooltip, stack, material, ToolBuilder.toolStyle, ToolBuilder.effectStyle,
                ToolBuilder.modifierStyle);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        onHit(stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        onMine(material, stack, state, world, pos, miner, this::shouldDropXp);
        if(miner instanceof PlayerEntity && !miner.isSneaking()){
            this.attemptBreakNeighbors(world, pos, (PlayerEntity)miner, true);
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    public boolean shouldDropXp(BlockState state, ItemStack stack) {
        return getMiningSpeed(stack, state) == this.miningSpeed;
    }

    @Override
    public XpDropCheck getDropCheck() {
        return this::shouldDropXp;
    }

    public static class ExcavatorMaterial extends BuiltToolMaterial {

        public ExcavatorMaterial(HandleMaterial handle, HeadMaterial head, String name, Boolean grip) {
            super(handle, head, name, grip);
        }

        public static ExcavatorMaterial of(BuiltToolMaterial material) {
            return new ExcavatorMaterial(material.handle, material.head, material.getName(), material.isGripped);
        }

        @Override
        public int getDurability() {
            return super.getDurability() * 5;
        }

        @Override
        public float getMiningSpeed() {
            return super.getMiningSpeed() / 3.5f;
        }

    }

}
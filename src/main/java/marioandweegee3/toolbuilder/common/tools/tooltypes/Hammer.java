package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.XpDropCheck;
import marioandweegee3.toolbuilder.api.item.BigTool;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import marioandweegee3.toolbuilder.api.material.HeadMaterial;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Hammer extends PickaxeItem implements BigTool {
    private static float speed = ToolValues.HAMMER.getSpeed();
    private static float damage = ToolValues.HAMMER.getDamage();

    private BuiltToolMaterial material;

    private Hammer(HammerMaterial material, Settings settings) {
        super(material, (int) damage, BuiltTool.getAttackSpeed(material, speed), settings);
        this.material = material;
    }

    public static Hammer create(BuiltToolMaterial material) {
        return new Hammer(HammerMaterial.of(material), new Settings());
    }

    @Override
    public String getTranslationKey() {
        return getTranslationName(material);
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
    public String getType() {
        return "hammer";
    }

    @Override
    public BuiltToolMaterial getMaterial() {
        return material;
    }

    @Override
    public XpDropCheck getDropCheck() {
        return this::shouldDropXp;
    }

    @Override
    public boolean isEffectiveOn(BlockState blockState_1) {
        return super.isEffectiveOn(blockState_1);
    }

    public static class HammerMaterial extends BuiltToolMaterial {

        public HammerMaterial(HandleMaterial handle, HeadMaterial head, String name, Boolean grip) {
            super(handle, head, name, grip);
        }

        public static HammerMaterial of(BuiltToolMaterial material) {
            return new HammerMaterial(material.handle, material.head, material.getName(), material.isGripped);
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
package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.BowMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class Bow extends BowItem {
    public final BowMaterial material;

    public static final int baseDurability = 384;

    private Bow(BowMaterial material, Settings settings) {
        super(settings);
        this.material = material;
        this.addPropertyGetter(new Identifier("pull"), (stack, world, entity)->{
            if(entity == null) {
                return 0.0f;
            } else if(!(entity.getActiveItem().getItem() instanceof Bow)){
                return 0.0f;
            } else {
                return (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (20.0f * material.getDrawSpeedMultiplier());
            }
        });
        this.addPropertyGetter(new Identifier("pulling"), (stack, world, entity) -> (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) ? 1.0f : 0.0f);
    }

    public static Bow create(BowMaterial material){
        return new Bow(material, new Settings().maxCount(1).maxDamage((int)((baseDurability + material.handle.getExtraDurability()) * material.handle.getDurabilityMultiplier())));
    }

    @Override
    public String getTranslationKey() {
        return "bow.toolbuilder."+material.handle.getName();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("string.toolbuilder."+material.string.getName()).setStyle(ToolBuilder.toolStyle));
        if(material.grip) {
            tooltip.add(new TranslatableText("text.toolbuilder.grip").setStyle(ToolBuilder.toolStyle));
        }
        
        for(Effect effect : getEffects()){
            tooltip.add(effect.getTranslationName().setStyle(ToolBuilder.effectStyle));
        }
    }

    @Override
    public boolean canRepair(ItemStack stack0, ItemStack stack) {
        if(stack == null || stack.isEmpty()) return false;
        if(material.getRepairItems().contains(stack.getItem())) return true;
        return false;
    }
    
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity)user;
        boolean doesntConsumeArrow = player.abilities.creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack arrow = player.getArrowType(stack);
        if (arrow.isEmpty() && !doesntConsumeArrow) {
            return;
        }
        if (arrow.isEmpty()) {
            arrow = new ItemStack(Items.ARROW);
        }
        int integer9 = this.getMaxUseTime(stack) - remainingUseTicks;
        float velocityMult = tbgetPullProgress(integer9);
        if (velocityMult < 0.1) {
            return;
        }
        boolean infiniteArrow = doesntConsumeArrow && arrow.getItem() == Items.ARROW;
        if (!world.isClient) {
            ArrowItem arrowItem = (ArrowItem)((arrow.getItem() instanceof ArrowItem) ? arrow.getItem() : Items.ARROW);
            ProjectileEntity projectile = arrowItem.createArrow(world, arrow, player);
            if(getEffects().contains(Effects.ENDER)){
                velocityMult += 0.6f;
            }
            projectile.setProperties(player, player.pitch, player.yaw, 0.0f, velocityMult * 3.0f, 1.0f);
            if (velocityMult >= 1.0f) {
                projectile.setCritical(true);
            }
            int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (powerLevel > 0) {
                projectile.setDamage(projectile.getDamage() + powerLevel * 0.5 + 0.5);
            }
            int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            if (punchLevel > 0) {
                projectile.setPunch(punchLevel);
            }
            int flameTime = getFlameTime(stack);
            if (flameTime > 0) {
                projectile.setOnFireFor(flameTime);
            }
            stack.<PlayerEntity>damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            if (infiniteArrow || (player.abilities.creativeMode && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW))) {
                projectile.pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            world.spawnEntity(projectile);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (BowItem.RANDOM.nextFloat() * 0.4f + 1.2f) + velocityMult * 0.5f);
        if (!infiniteArrow && !player.abilities.creativeMode) {
            arrow.decrement(1);
            if (arrow.isEmpty()) {
                player.inventory.removeOne(arrow);
            }
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    public int getFlameTime(ItemStack stack){
        int time = 0;

        if(EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) time += 100;

        if(getEffects().contains(Effects.FLAMING)) time += 100;

        return time;
    }
    
    public float tbgetPullProgress(int useTicks) {
        float float2 = useTicks / (20.0f * material.getDrawSpeedMultiplier());
        float2 = (float2 * float2 + float2 * 2.0f) / 3.0f;
        if (float2 > 1.0f) {
            float2 = 1.0f;
        }
        return float2;
    }
    
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack5 = user.getStackInHand(hand);
        boolean boolean6 = !user.getArrowType(itemStack5).isEmpty();
        if (user.abilities.creativeMode || boolean6) {
            user.setCurrentHand(hand);
            return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, itemStack5);
        }
        if (boolean6) {
            return new TypedActionResult<ItemStack>(ActionResult.PASS, itemStack5);
        }
        return new TypedActionResult<ItemStack>(ActionResult.FAIL, itemStack5);
    }
    
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BowItem.BOW_PROJECTILES;
    }

    public Set<Effect> getEffects(){
        return material.getEffects();
    }
}
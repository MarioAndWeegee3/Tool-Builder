package marioandweegee3.toolbuilder.common.effect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.XpDropCheck;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.effect.EffectBase;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Bow;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public enum Effects implements Effect {
    EXPERIENCE("xp", 3, new EffectBase() {
        @Override
        public void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos,
                LivingEntity miner, XpDropCheck dropCheck, int level) {
            if (dropCheck.check(state, stack) && world.random.nextInt(4 - level) == 0) {
                world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        ConfigHandler.INSTANCE.getExtraXp()));
            }
        }
    }), 
    POISONOUS("poison", 3, new EffectBase() {
        @Override
        public void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, ConfigHandler.INSTANCE.getPoisonTime(), level - 1));
        }
    }), 
    HOLY("holy", 3, new EffectBase(){
        @Override
        public float getAdditonalAttackDamage(ItemStack stack, EntityGroup group, int level) {
            return ConfigHandler.INSTANCE.getHolyDamage().floatValue() * level;
        }

        @Override
        public float modifyDamageRecieved(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack,
                int level) {
            return (float) (baseDamage - ConfigHandler.INSTANCE.getHolyDamage() * 0.25 * level);
        }
    }), 
    GROWING("growing", 6, new EffectBase(){
        @Override
        public void onInventoryTick(ItemStack stack, LivingEntity holder, int level) {
            if(stack.getItem() != holder.getActiveItem().getItem() && stack.getDamage() - 1 >= 0){
                if(holder.world.random.nextInt(600 / level) == 0){
                    stack.setDamage(stack.getDamage() - 1);
                }
            }
        }

        @Override
        public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level) {
            onInventoryTick(stack, holder, level);
        }
    }), 
    FLAMING("flaming", 2, new EffectBase(){
        @Override
        public int arrowFireTimeModifier(ItemStack stack, Bow bow, int level) {
            return 100 * level;
        }

        @Override
        public void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level) {
            target.setOnFireFor(ConfigHandler.INSTANCE.getFlamingTime() * level);
        }

        @Override
        public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level) {
            holder.setOnFireFor(ConfigHandler.INSTANCE.getFlamingTime() * level);
        }

        @Override
        public List<ItemStack> modifyBlockDrops(BlockState state, BuiltTool tool, ItemStack toolStack, Builder builder, List<ItemStack> drops, int level) {
            List<SmeltingRecipe> smeltingRecipes = new ArrayList<>(0);
            for (Recipe<?> recipe : builder.getWorld().getRecipeManager().values()) {
                if (recipe instanceof SmeltingRecipe) {
                    smeltingRecipes.add((SmeltingRecipe) recipe);
                }
            }

            Set<ItemStack> smelted = new HashSet<>(0);

            for (ItemStack stack : drops) {
                for (SmeltingRecipe recipe : smeltingRecipes) {
                    DefaultedList<Ingredient> inputs = recipe.getPreviewInputs();
                    for (Ingredient input : inputs) {
                        for (int id : input.getIds()) {
                            Item inputItem = Registry.ITEM.get(id);
                            if (stack.getItem() == inputItem) {
                                smelted.add(BuiltTool.increaseCountForFortune(toolStack, recipe.getOutput()));
                                break;
                            }
                        }
                    }
                }
            }

            if (smelted.size() == 0) {
                return drops;
            } else {
                drops.clear();
            }

            for (ItemStack stack : smelted) {
                drops.add(stack);
            }

            return drops;
        }
    }), 
    LIGHT("light", 1, new EffectBase(){
        @Override
        public float modifyFallDamage(float fallDamage, BuiltArmorItem armor, ItemStack stack, int level) {
            fallDamage *= ConfigHandler.INSTANCE.getLightFallDamageMultiplier().floatValue();
            if (fallDamage > 0) {
                stack.damage(1, new Random(), null);
            }
            return fallDamage;
        }

        @Override
        public float getAttackSpeedModifier(int level) {
            return 0.05f;
        }
    }), 
    FLAMMABLE("flammable", 1, new EffectBase(){
        @Override
        public int modifyFireDuration(int fireDuration, LivingEntity entity, BuiltArmorItem armor, ItemStack stack,
                int level) {
            return (int) (fireDuration * ConfigHandler.INSTANCE.getFlammableTimeMultiplier());
        }
    }), 
    DURABLE("durable", 1, new EffectBase(){
        @Override
        public int modifyDurability(int durability) {
            return (int) (durability * ConfigHandler.INSTANCE.getDurableMultiplier());
        }
    }), 
    EXTRA_MODS("extra_modifiers", 1, new EffectBase()), 
    MAGICAL("magical", 1, new EffectBase(){
        @Override
        public float modifyDamageRecieved(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack,
                int level) {
            if(source.getMagic()){
                return (float) (baseDamage * 0.9);
            } else {
                return baseDamage;
            }
        }

        @Override
        public boolean bypassesArmor(String name, LivingEntity source, int level) {
            return true;
        }
    }), 
    BOUNCY("bouncy", 1, new EffectBase(){
        @Override
        public float modifyFallDamage(float fallDamage, BuiltArmorItem armor, ItemStack stack, int level) {
            if(armor.getSlotType() == EquipmentSlot.FEET){
                if (fallDamage > 0 && ConfigHandler.INSTANCE.bouncyDamagesArmor()) {
                    stack.damage(1, new Random(), null);
                }
                fallDamage = 0f;
            }
            return fallDamage;
        }

        @Override
        public float modifyDamageRecieved(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack,
                int level) {
            if(armor.getSlotType() == EquipmentSlot.HEAD && source == DamageSource.FLY_INTO_WALL) {
                if (baseDamage > 0 && ConfigHandler.INSTANCE.bouncyDamagesArmor()) {
                    stack.damage(1, new Random(), null);
                }
                baseDamage = 0;
            }
            return baseDamage;
        }

        @Override
        public List<ItemStack> modifyBlockDrops(BlockState state, BuiltTool tool, ItemStack toolStack, Builder builder,
                List<ItemStack> drops, int level) {
            if(new Random().nextInt(10) == 0){
                drops.add(new ItemStack(Items.SLIME_BALL));
            }
            return drops;
        }

        @Override
        public void onEntityLandOnBlock(BlockView view, BuiltArmorItem armor, LivingEntity entity, int level) {
            if(!(armor.getSlotType() == EquipmentSlot.FEET)) return;

            double speedMult = -ConfigHandler.INSTANCE.getBounceSpeedMultiplier(),
                    sneakSpeedMult = -ConfigHandler.INSTANCE.getSneakBounceSpeedMultiplier();
            Vec3d vel = new Vec3d(entity.getVelocity().x, entity.getVelocity().y, entity.getVelocity().z).multiply(2,
                    entity.isSneaking() ? sneakSpeedMult : speedMult, 2);
            // This prevents the player from building up insane speed an launching 1,000,000
            // blocks into the air.
            // Yes, that did happen. If the bounce speed limit is removed, you will go very
            // fast.
            double maxVelocity = ConfigHandler.INSTANCE.getMaxBounceVelocity();
            if (vel.y >= maxVelocity && ConfigHandler.INSTANCE.limitBounceHeight()) {
                entity.setVelocity(vel.x, maxVelocity, vel.z);
            } else {
                entity.setVelocity(vel);
            }

            entity.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.PLAYERS, 1, 0.5f);
        }
    }), 
    RESILIENT("resilient", 2, new EffectBase(){
        @Override
        public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel) {
            if(enchantment == Enchantments.UNBREAKING){
                return effectLevel;
            } else {
                return 0;
            }
        }
    }), 
    MAGNETIC("magnetic", 3, new EffectBase(){
        @Override
        public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level) {
            CompoundTag tag = stack.getOrCreateTag();
            int tick = MagneticEffect.readTicks(tag);
            if(tick <= 2){
                tick++;
            } else {
                MagneticEffect.run(holder, holder.world, level);
                tick = 0;
            }
            MagneticEffect.writeTicks(tick, tag);
        }

        @Override
        public void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level) {
            MagneticEffect.run(attacker, target.world, level);
        }

        @Override
        public void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos,
                LivingEntity miner, XpDropCheck dropCheck, int level) {
            MagneticEffect.run(miner, world, level);
        }
    }), 
    GLIMMERING("glimmering", 3, new EffectBase(){
        @Override
        public void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos,
                LivingEntity miner, XpDropCheck dropCheck, int level) {
            CompoundTag tag = stack.getOrCreateTag();
            int glimmers = 0;
            if (tag.contains(Effects.glimmerNBTtag)) {
                glimmers = tag.getInt(Effects.glimmerNBTtag);
            }

            boolean glimmerChance = world.random.nextInt(4 - level) == 0;

            if (glimmerChance) {
                glimmers = 1;
            } else {
                glimmers = 0;
            }

            tag.putInt(Effects.glimmerNBTtag, glimmers);
        }

        @Override
        public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel) {
            CompoundTag tag = stack.getOrCreateTag();
            int glimmers = 0;
            if (tag.contains(Effects.glimmerNBTtag)) {
                glimmers = tag.getInt(Effects.glimmerNBTtag);
            }

            if(glimmers == 1){
                return effectLevel;
            } else {
                return 0;
            }
        }
    }), 
    ROYAL("royal", 2, new EffectBase(){
        @Override
        public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel) {
            if(enchantment == Enchantments.FORTUNE || enchantment == Enchantments.LOOTING) {
                return effectLevel;
            } else {
                return 0;
            }
        }
    }), 
    AQUATIC("aquatic", 1, new EffectBase(){
        @Override
        public float getAdditonalAttackDamage(ItemStack stack, EntityGroup group, int level) {
            if(group == EntityGroup.AQUATIC){
                return 1.5f;
            } else {
                return 0;
            }
        }

        @Override
        public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level) {
            LivingEntity entity = (LivingEntity) holder;
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
            if(entity.isInFluid(FluidTags.WATER)){
                if(armor.getSlotType() == EquipmentSlot.HEAD){
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 20, 0, false, false));
                } else {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 20, 1, false, false));
                }
            }
        }

        @Override
        public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel) {
            if(enchantment == Enchantments.AQUA_AFFINITY) {
                return effectLevel;
            } else {
                return 0;
            }
        }
    }), 
    ENDER("ender", 2, new EffectBase(){
        @Override
        public float modifyArrowVelocity(float velocity, int level) {
            return velocity + (0.6f * level);
        }
    });

    private Effects(String name, int maxLevel, EffectBase base) {
        this.name = name;
        this.base = base;
        this.maxLevel = maxLevel;
    }

    public final String name;

    private final EffectBase base;

    private final int maxLevel;

    public static final String effectNBTtag = "toolbuilder_effects";
    public static final String glimmerNBTtag = "toolbuilder_glimmers";
    public static final String magneticTickNBTtag = "toolbuilder_magnetic_tick";

    @Override
    public TranslatableText getTranslationName() {
        return new TranslatableText("text.toolbuilder.effects." + name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Identifier getID() {
        return new Identifier(ToolBuilder.modID, name);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public float getAdditonalAttackDamage(ItemStack stack, EntityGroup group, int level) {
        return base.getAdditonalAttackDamage(stack, group, level);
    }

    @Override
    public List<ItemStack> modifyBlockDrops(BlockState state, BuiltTool tool, ItemStack toolStack, Builder builder, List<ItemStack> drops, int level) {
        return base.modifyBlockDrops(state, tool, toolStack, builder, drops, level);
    }

    @Override
    public void onEntityLandOnBlock(BlockView view, BuiltArmorItem armor, LivingEntity entity, int level) {
        base.onEntityLandOnBlock(view, armor, entity, level);
    }

    @Override
    public boolean bypassesArmor(String name, LivingEntity source, int level) {
        return base.bypassesArmor(name, source, level);
    }

    @Override
    public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel) {
        return base.enchantmentLevelIncrement(enchantment, stack, effectLevel);
    }

    @Override
    public float modifyFallDamage(float fallDamage, BuiltArmorItem armor, ItemStack stack, int level) {
        return base.modifyFallDamage(fallDamage, armor, stack, level);
    }

    @Override
    public float modifyDamageRecieved(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack,
            int level) {
        return base.modifyDamageRecieved(baseDamage, source, armor, stack, level);
    }

    @Override
    public int modifyFireDuration(int fireDuration, LivingEntity entity, BuiltArmorItem armor, ItemStack stack,
            int level) {
        return base.modifyFireDuration(fireDuration, entity, armor, stack, level);
    }

    @Override
    public void onInventoryTick(ItemStack stack, LivingEntity holder, int level) {
        base.onInventoryTick(stack, holder, level);
    }

    @Override
    public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level) {
        base.onArmorInventoryTick(stack, holder, level);
    }

    @Override
    public void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level) {
        base.onHit(stack, target, attacker, level);
    }

    @Override
    public void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos,
            LivingEntity miner, XpDropCheck dropCheck, int level) {
        base.postMine(material, stack, state, world, pos, miner, dropCheck, level);
    }

    @Override
    public int arrowFireTimeModifier(ItemStack stack, Bow bow, int level) {
        return base.arrowFireTimeModifier(stack, bow, level);
    }

    @Override
    public int modifyDurability(int durability) {
        return base.modifyDurability(durability);
    }

    @Override
    public float modifyArrowVelocity(float velocity, int level) {
        return base.modifyArrowVelocity(velocity, level);
    }

    @Override
    public float getAttackSpeedModifier(int level) {
        return base.getAttackSpeedModifier(level);
    }

    public static SuggestionProvider<ServerCommandSource> effectSuggestions(){
        return (ctx, builder) -> getSuggestionBuilder(builder);
    }

    private static CompletableFuture<Suggestions> getSuggestionBuilder(SuggestionsBuilder builder){
        String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

        for(Identifier effect : TBRegistries.EFFECTS.keySet()){
            String name = effect.toString();
            //name = "\""+name+"\"";
            if(name.toLowerCase(Locale.ROOT).startsWith(remaining)){
                builder.suggest(name);
            }
        }

        return builder.buildFuture();
    }
    
}
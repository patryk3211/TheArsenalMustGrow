/*
 * Copyright 2025 patryk3211
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.patryk3211.tamg.gun;

import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.tamg.Lang;
import org.patryk3211.tamg.Networking;
import org.patryk3211.tamg.TamgClient;
import org.patryk3211.tamg.config.CGuns;
import org.patryk3211.tamg.config.TamgConfigs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.patryk3211.tamg.config.CGuns.GunProperties.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GunItem extends ProjectileWeaponItem implements CustomArmPoseItem {
    // Remember to set this for every new gun instance.
    protected TagKey<Item> bulletTag = null;

    protected Vec3 flashOffset = Vec3.ZERO;
    protected Vec3 barrel = Vec3.ZERO;
    protected Vec3 correction = Vec3.ZERO;

    public static final int COLOR_BAR_EMPTY = 0xfffc6703;
    public static final int COLOR_BAR_FULL = 0xfffc2003;

    private final boolean automatic;

    public GunItem(Properties settings) {
        this(settings, false);
    }

    public GunItem(Properties settings, boolean automatic) {
        super(settings.stacksTo(1));
        this.automatic = automatic;
    }

    public float configF(CGuns.GunProperties property) {
        return (float) TamgConfigs.server().guns.getDouble(this, property);
    }

    public int configI(CGuns.GunProperties property) {
        return TamgConfigs.server().guns.getInt(this, property);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.is(bulletTag);
    }

    public boolean isGun(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
        return false;
    }

    public static float getHeatAmount(ItemStack stack) {
        if(!stack.hasTag())
            return 0;
        return stack.getTag().getFloat("Heat");
    }

    public static float getHeatPercent(ItemStack stack) {
        if(!(stack.getItem() instanceof GunItem gun))
            return 0;
        return getHeatAmount(stack) / gun.configF(HEAT_CAPACITY);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getHeatAmount(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Mth.floor(getHeatPercent(stack) * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.mixColors(COLOR_BAR_EMPTY, COLOR_BAR_FULL, getHeatPercent(stack));
    }

    public Vec3 getBarrel(ItemStack stack) {
        return barrel;
    }

    public Vec3 getCorrection(ItemStack stack) {
        return correction;
    }

    public boolean applyHeat(ItemStack stack) {
        if(stack.getItem() != this)
            throw new IllegalArgumentException("Stack must be of the correct item");
        var current = getHeatAmount(stack);
        current += configF(HEAT_PER_SHOT);
        if(current >= configF(HEAT_CAPACITY)) {
            current = configF(HEAT_CAPACITY);
        }
        stack.getOrCreateTag().putFloat("Heat", current);
        return current >= configF(HEAT_CAPACITY);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        var tag = stack.getOrCreateTag();
        var current = getHeatAmount(stack);
        if(current <= 0)
            return;
        tag.putFloat("Heat", Math.max(current - configF(HEAT_DISSIPATION), 0));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() == null)
            return InteractionResult.FAIL;
        return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    public void cooldownIfNotCoolingDown(Player player, int ticks) {
        if(player.getCooldowns().isOnCooldown(this))
            return;
        player.getCooldowns().addCooldown(this, ticks);
    }

    protected void spawnProjectile(Level world, Vec3 barrelPos, Vec3 motion, Player user, ItemStack stack) {
        var projectileEntity = BulletEntity.create(world, barrelPos, motion, this);
        projectileEntity.setOwner(user);
        world.addFreshEntity(projectileEntity);
    }

    protected InteractionResult shoot(Level world, Player user, ItemStack stack, InteractionHand hand) {
        var projectile = user.getProjectile(stack);
        if(projectile.isEmpty())
            return InteractionResult.FAIL;
        if(!user.isCreative())
            projectile.shrink(1);

        var barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(user, hand == InteractionHand.MAIN_HAND,
                getBarrel(stack)
//                new Vec3(.375f, -.20f, 1.25f)
        );
        var correction = ShootableGadgetItemMethods.getGunBarrelVec(user, hand == InteractionHand.MAIN_HAND,
                getCorrection(stack)
//                new Vec3(-0.025f, 0.0125f, 0)
        ).subtract(user.position().add(0, user.getEyeHeight(), 0));

        var lookVec = user.getLookAngle();
        var motion = lookVec.add(correction)
                .normalize()
                .scale(4);

        spawnProjectile(world, barrelPos, motion, user, stack);

        if(applyHeat(stack)) {
            // Overheated
            ShootableGadgetItemMethods.applyCooldown(user, stack, hand, this::isGun, 20 * 5);
            if(automatic)
                user.stopUsingItem();
        }
        Function<Boolean, GunS2CPacket> factory = b -> new GunS2CPacket(barrelPos, hand, b, user);

        var selfKnockback = configF(SELF_KNOCKBACK);
        if(selfKnockback > 0) {
            // Knockback user
            var selfMotion = lookVec.normalize().scale(-selfKnockback);
            user.addDeltaMovement(selfMotion);
            user.hurtMarked = true;
        }

        int cooldown = configI(USE_COOLDOWN);
        if(cooldown > 0)
            cooldownIfNotCoolingDown(user, cooldown);

        var trackingUser = PacketDistributor.TRACKING_ENTITY.with(() -> user);
        var userDist = PacketDistributor.PLAYER.with(() -> (ServerPlayer) user);
        Networking.getChannel().send(trackingUser, factory.apply(false));
        Networking.getChannel().send(userDist, factory.apply(true));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        var stack = user.getItemInHand(hand);
        if(world.isClientSide) {
            TamgClient.GUN_RENDER_HANDLER.dontAnimateItem(hand);
            return InteractionResultHolder.success(stack);
        }

        if(automatic) {
            user.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        return new InteractionResultHolder<>(shoot(world, user, stack, hand), stack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int pRemainingUseDuration) {
        if(world.isClientSide || !automatic)
            return;
        if(!(user instanceof Player player))
            return;
        if(pRemainingUseDuration % 2 == 0) {
            var hand = user.getMainHandItem() == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            shoot(world, player, stack, hand);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tamg.gun.bullet").append(Component.literal(":"))
                .withStyle(ChatFormatting.GRAY));
        var spacing = Component.literal(" ");

        float damageF = configF(DAMAGE);//type.getDamage() * additionalDamageMult;
        if(damageF > 0) {
            var damage = Component.literal(damageF == Mth.floor(damageF) ? "" + Mth.floor(damageF) : "" + damageF);
//            damage = damage.formatted(Formatting.DARK_GREEN);
            tooltip.add(spacing.plainCopy().append(Lang.translateDirect("gun.bullet.damage", damage).withStyle(ChatFormatting.DARK_GREEN)));
        }
        float knockbackF = configF(KNOCKBACK);//type.getDamage() * additionalDamageMult;
        if(knockbackF > 0) {
            var knockback = Component.literal(knockbackF == Mth.floor(knockbackF) ? "" + Mth.floor(knockbackF) : "" + knockbackF);
            tooltip.add(spacing.plainCopy().append(Lang.translateDirect("gun.bullet.knockback", knockback).withStyle(ChatFormatting.DARK_GREEN)));
        }
        super.appendHoverText(stack, world, tooltip, context);
    }

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if(!player.swinging) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return null;
    }

    public boolean onEntitySwing(ItemStack itemStack, LivingEntity livingEntity) {
        return true;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    public boolean hasZoom() {
        return false;
    }
}

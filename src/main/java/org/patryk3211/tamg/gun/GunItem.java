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
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.tamg.Lang;
import org.patryk3211.tamg.Networking;
import org.patryk3211.tamg.TamgClient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GunItem extends ProjectileWeaponItem implements CustomArmPoseItem {
    protected float damage = 2;
    protected float knockback = 0.1f;
    // Remember to set this for every new gun instance.
    protected TagKey<Item> bulletTag = null;
    protected float heatCapacity = 1;
    protected float heatPerShot = 0.5f;
    protected float heatDissipation = 0.1f;

    protected Vec3 flashOffset = Vec3.ZERO;
    protected Vec3 barrel = Vec3.ZERO;
    protected Vec3 correction = Vec3.ZERO;

    public static final int COLOR_BAR_EMPTY = 0xfffc6703;
    public static final int COLOR_BAR_FULL = 0xfffc2003;

    public GunItem(Properties settings) {
        super(settings.stacksTo(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new GunItemRenderer()));
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
        return getHeatAmount(stack) / gun.heatCapacity;
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

    public boolean applyHeat(ItemStack stack) {
        if(stack.getItem() != this)
            throw new IllegalArgumentException("Stack must be of the correct item");
        var current = getHeatAmount(stack);
        current += heatPerShot;
        if(current >= heatCapacity) {
            current = heatCapacity;
        }
        stack.getOrCreateTag().putFloat("Heat", current);
        return current >= heatCapacity;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        var current = getHeatAmount(stack);
        if(current <= 0)
            return;
        stack.getOrCreateTag().putFloat("Heat", Math.max(current - heatDissipation, 0));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() == null)
            return InteractionResult.FAIL;
        return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        var stack = user.getItemInHand(hand);
        if(world.isClientSide) {
            TamgClient.GUN_RENDER_HANDLER.dontAnimateItem(hand);
            return InteractionResultHolder.success(stack);
        }

        var projectile = user.getProjectile(stack);
        if(projectile.isEmpty())
            return InteractionResultHolder.fail(stack);
        if(!user.isCreative())
            projectile.shrink(1);

        var barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(user, hand == InteractionHand.MAIN_HAND,
                new Vec3(.375f, -0.15f, 1.0f));
        var correction = ShootableGadgetItemMethods.getGunBarrelVec(user, hand == InteractionHand.MAIN_HAND,
                new Vec3(0, 0.1f, 0)).subtract(user.position().add(0, user.getEyeHeight(), 0));

        var lookVec = user.getLookAngle();
        var motion = lookVec.add(correction)
                .normalize()
                .scale(4);

        var projectileEntity = BulletEntity.create(world, barrelPos, motion, this);
        projectileEntity.setOwner(user);
        world.addFreshEntity(projectileEntity);

        if(applyHeat(stack)) {
            // Overheated
            ShootableGadgetItemMethods.applyCooldown(user, stack, hand, this::isGun, 20 * 5);
        }
        Function<Boolean, GunS2CPacket> factory = b -> new GunS2CPacket(barrelPos, hand, b, user);

        var trackingUser = PacketDistributor.TRACKING_ENTITY.with(() -> user);
        var userDist = PacketDistributor.PLAYER.with(() -> (ServerPlayer) user);
        Networking.getChannel().send(trackingUser, factory.apply(false));
        Networking.getChannel().send(userDist, factory.apply(true));
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tamg.gun.bullet").append(Component.literal(":"))
                .withStyle(ChatFormatting.GRAY));
        var spacing = Component.literal(" ");

        float damageF = this.damage;//type.getDamage() * additionalDamageMult;
        var damage = Component.literal(damageF == Mth.floor(damageF) ? "" + Mth.floor(damageF) : "" + damageF);

        float knockbackF = this.knockback;//type.getDamage() * additionalDamageMult;
        var knockback = Component.literal(damageF == Mth.floor(damageF) ? "" + Mth.floor(damageF) : "" + damageF);

//        damage = damage.formatted(Formatting.DARK_GREEN);

        tooltip.add(spacing.plainCopy().append(Lang.translateDirect("gun.bullet.damage", damage).withStyle(ChatFormatting.DARK_GREEN)));
        tooltip.add(spacing.plainCopy().append(Lang.translateDirect("gun.bullet.knockback", knockback).withStyle(ChatFormatting.DARK_GREEN)));
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
}

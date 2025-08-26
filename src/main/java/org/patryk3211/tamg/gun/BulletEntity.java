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

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.patryk3211.tamg.collections.TamgDamageTypes;
import org.patryk3211.tamg.collections.TamgEntities;
import org.patryk3211.tamg.collections.TamgSoundEvents;
import org.patryk3211.tamg.config.CGuns;

public class BulletEntity extends Projectile {
    private float damage;
    private float knockback;

    public BulletEntity(EntityType<? extends Projectile> type, Level world) {
        super(type, world);
    }

    public static BulletEntity create(Level world, Vec3 position, Vec3 velocity, GunItem source) {
        var entity = new BulletEntity(TamgEntities.BULLET.get(), world);
        entity.setPosRaw(position.x, position.y, position.z);
        entity.setDeltaMovement(velocity);
        ProjectileUtil.rotateTowardsMovement(entity, 1.0f);
        entity.setOldPosAndRot();
        entity.reapplyPosition();
        entity.damage = source.configF(CGuns.GunProperties.DAMAGE);
        entity.knockback = source.configF(CGuns.GunProperties.KNOCKBACK);
        return entity;
    }

    @Override
    protected void defineSynchedData() {

    }

    public static void playLaunchSound(Level world, Vec3 location, float pitch) {
        TamgSoundEvents.GUN_SHOT.playAt(world, location, 1, pitch, true);
    }

    @Override
    public void tick() {
        super.tick();
        var hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if(hit.getType() != HitResult.Type.MISS) {
            this.onHit(hit);
        }

        checkInsideBlocks();
        var velocity = this.getDeltaMovement();
        var x = this.getX() + velocity.x;
        var y = this.getY() + velocity.y;
        var z = this.getZ() + velocity.z;
        ProjectileUtil.rotateTowardsMovement(this, 1.0f);
        if(this.isInWater()) {
            kill();
            return;
        }

        setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
        setPos(x, y, z);
    }

    private DamageSource causeDamage() {
        Registry<DamageType> registry = level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolder(TamgDamageTypes.GUN_SHOT).get(), this, getOwner());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.xRotO = getXRot();
        this.yRotO = getYRot();
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        var owner = getOwner();
        var target = hit.getEntity();
        if(!target.isAlive())
            return;
        if(owner instanceof LivingEntity living)
            living.setLastHurtMob(target);

        if(target instanceof WitherBoss wither && wither.isPowered())
            return;

        float damage = this.damage;
        float knockback = this.knockback;

        var world = level();
        var source = causeDamage();

        var onServer = !world.isClientSide;
        if(onServer && !target.hurt(source, damage)) {
            kill();
            return;
        }

        if(target.getType() == EntityType.ENDERMAN)
            return;

        if(!(target instanceof LivingEntity livingTarget)) {
//            playHitSound(getWorld(), getPos());
            kill();
            return;
        }

        if (onServer && knockback > 0) {
            Vec3 appliedMotion = this.getDeltaMovement()
                    .multiply(1.0D, 0.0D, 1.0D)
                    .normalize()
                    .scale(knockback * 0.6);
            if (appliedMotion.lengthSqr() > 0.0D)
                livingTarget.addDeltaMovement(new Vec3(appliedMotion.x, 0.05D, appliedMotion.z));
        }

        if(onServer && owner instanceof LivingEntity livingOwner) {
            EnchantmentHelper.doPostHurtEffects(livingTarget, livingOwner);
            EnchantmentHelper.doPostDamageEffects(livingOwner, livingTarget);
        }

        if(livingTarget != owner && livingTarget instanceof Player && owner instanceof ServerPlayer ownerPlayer && !isSilent()) {
            ownerPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
        }

        if(onServer && owner instanceof ServerPlayer serverPlayer) {
//            if (!target.isAlive() && target.getType()
//                    .getSpawnGroup() == SpawnGroup.MONSTER || (target instanceof PlayerEntity && target != owner))
//                AllAdvancements.POTATO_CANNON.awardTo(serverplayerentity);
        }

        kill();
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        var level = level();
        if(!level.isClientSide) {
            var state = level.getBlockState(hit.getBlockPos());
            var sound = state.getSoundType().getHitSound();
            level.playSound(null, blockPosition(), sound, SoundSource.BLOCKS, 1, 1.5f + random.nextFloat() * 0.1f);
        }
        kill();
    }
}

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

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class GunRenderHandler extends ShootableGadgetRenderHandler {
    private final List<AnimationData> animationProgress = new ArrayList<>();

    protected void beforeShoot(Player shooter, InteractionHand hand) {
        var reset = false;
        for(var data : animationProgress) {
            if(data.player == shooter && data.hand == hand) {
                data.reset();
                reset = true;
                break;
            }
        }
        if(!reset)
            animationProgress.add(new AnimationData(shooter, hand));
    }

    @Override
    public void tick() {
        super.tick();
        animationProgress.removeIf(data -> {
            data.tick();
            return data.isDone();
        });
    }

    @Override
    protected void playSound(InteractionHand hand, Vec3 position) {
        BulletEntity.playLaunchSound(Minecraft.getInstance().level, position, 1);
    }

    @Override
    protected boolean appliesTo(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }

    @Override
    protected void transformTool(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
        ms.translate(flip * -.1f, 0, .14f);
        ms.scale(.75f, .75f, .75f);
        TransformStack.of(ms)
                .rotateXDegrees(recoil * 80);
    }

    @Override
    protected void transformHand(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
        ms.translate(flip * -.09, -.275, -.25);
        TransformStack.of(ms)
                .rotateZDegrees(flip * -10);
    }

    @Nullable
    public AnimationData getAnimation(ItemStack stack) {
        for(var data : animationProgress) {
            if(data.isFor(stack))
                return data;
        }
        return null;
    }

    public static class AnimationData {
        private static final RandomSource r = RandomSource.create();

        public final Player player;
        public final InteractionHand hand;
        private ItemStack old;

        private float prevFlash;
        private float flash;
        public float flashAngle;

        private float prevTop;
        private float topOffset;

        public AnimationData(Player player, InteractionHand hand) {
            this.player = player;
            this.hand = hand;
            this.old = player.getItemInHand(hand);
            reset();
        }

        public boolean isFor(ItemStack stack) {
            if(old == stack)
                return true;
            var newStack = player.getItemInHand(hand);
            old = newStack;
            return newStack == stack;
        }

        public void reset() {
            flash = 1.0f;
            prevFlash = 1.0f;
            flashAngle = (float) (r.nextFloat() * Math.PI * 2f);
            prevTop = 0.0f;
            topOffset = 0.0f;
        }

        public void tick() {
            prevFlash = flash;
            flash *= 0.9f;

            prevTop = topOffset;
            if(topOffset < 1.0f) {
                topOffset += 0.4f;
            }
        }

        public float flash(float pt) {
            return Mth.lerp(pt, prevFlash, flash);
        }

        public float top(float pt) {
            var value = Mth.clamp(Mth.lerp(pt, prevTop, topOffset), 0, 1);
            if(value < 0.125f) {
                value = 8 * value;
            } else {
                value = -8 * (value - 1) / 7;
            }
            return value * 2 / 16f;
        }

        public boolean isDone() {
            return flash < 0.05f && topOffset >= 1.0f;
        }
    }
}

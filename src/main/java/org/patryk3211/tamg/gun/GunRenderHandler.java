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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

@OnlyIn(Dist.CLIENT)
public class GunRenderHandler extends ShootableGadgetRenderHandler {
    private final List<GunAnimationData> animationProgress = new ArrayList<>();
    public static final Map<GunItem, BiFunction<Player, InteractionHand, GunAnimationData>> animationConstructors = new HashMap<>();

    protected void beforeShoot(Player shooter, InteractionHand hand) {
        var reset = false;
        for(var data : animationProgress) {
            if(data.player == shooter && data.hand == hand) {
                data.reset();
                reset = true;
                break;
            }
        }
        if(!reset) {
            var constructor = animationConstructors.get(shooter.getItemInHand(hand).getItem());
            if(constructor != null) {
                animationProgress.add(constructor.apply(shooter, hand));
            }
        }
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
    public GunAnimationData getAnimation(ItemStack stack) {
        for(var data : animationProgress) {
            if(data.isFor(stack))
                return data;
        }
        return null;
    }
}

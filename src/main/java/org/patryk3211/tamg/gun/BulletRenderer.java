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
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BulletRenderer extends EntityRenderer<BulletEntity> {
//    public static final ResourceLocation TEXTURE = Tamg.texture("entity/zap_projectile");

    public BulletRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource consumers, int light) {
        var buffer = consumers.getBuffer(RenderType.entitySolid(getTextureLocation(entity)));

        var normalMatrix = matrices.last().normal();

        var stack = TransformStack.of(matrices);
        stack.pushPose();
        stack.translate(0, 0.125f, 0);

        stack.rotateY(-yaw);
        stack.rotateX(-entity.getViewXRot(tickDelta));

        final float UNIT = 1 / 16f;
        final float HALF_UNIT = UNIT / 2f;

        for(int i = 0; i < 4; ++i) {
            stack.rotateZ(90);

            var positionMatrix = matrices.last().pose();
            light = LightTexture.FULL_BRIGHT;
            vertex(positionMatrix, normalMatrix, buffer, -HALF_UNIT, -HALF_UNIT, -HALF_UNIT * 5, 0, 0, 0, 0, 1, light);
            vertex(positionMatrix, normalMatrix, buffer, -HALF_UNIT, -HALF_UNIT, HALF_UNIT * 5, UNIT * 5, 0, 0, 0, 1, light);
            vertex(positionMatrix, normalMatrix, buffer, HALF_UNIT, HALF_UNIT, HALF_UNIT * 5, UNIT * 5, UNIT, 0, 0, 1, light);
            vertex(positionMatrix, normalMatrix, buffer, HALF_UNIT, HALF_UNIT, -HALF_UNIT * 5, 0, UNIT, 0, 0, 1, light);
        }

        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return null;
//        return TEXTURE;
    }

    public void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, float normalX, float normalZ, float normalY, int light) {
        vertexConsumer
                .vertex(positionMatrix, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normalMatrix, normalX, normalY, normalZ)
                .endVertex();
    }
}

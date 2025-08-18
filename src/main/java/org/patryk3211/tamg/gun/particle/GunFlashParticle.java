package org.patryk3211.tamg.gun.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.patryk3211.tamg.Tamg;

public class GunFlashParticle extends Particle {
    private static final float[] vertices = new float[] {
            -2.5f, 0, -3,
            -2.5f, 0,  3,
             2.5f, 0,  3,
             2.5f, 0,  -3,

            0, -2.5f, -3,
            0, -2.5f,  3,
            0,  2.5f,  3,
            0,  2.5f, -3
    };

    private static final float[] uvs = new float[] {
            0, 0.3125f,
            0.375f, 0.3125f,
            0.375f, 0,
            0, 0,

            0.375f, 0.625f,
            0, 0.625f,
            0, 0.3125f,
            0.375f, 0.3125f,
    };

    private static final ResourceLocation TEXTURE = Tamg.texture("item/flash");
    private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.disableCull();

            // transparent, additive blending
//            RenderSystem.depthMask(false);
//            RenderSystem.enableBlend();
//            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

            // opaque
//			RenderSystem.depthMask(true);
//			RenderSystem.disableBlend();
//			RenderSystem.enableLighting();

            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            RenderSystem.enableCull();
//            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
//                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }
    };

    protected GunFlashParticle(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
        super(level, x, y, z);
        this.xd = mx;
        this.yd = my;
        this.zd = mz;
    }

    @Override
    public void tick() {
        // Flashes are very short-lived.
        remove();
    }

    @Override
    public void render(VertexConsumer builder, Camera camera, float partialTicks) {
        var camPos = camera.getPosition();
        var rotation = new Quaternionf();
        var mag = Math.sqrt(xd * xd + zd * zd + yd * yd);
        rotation.rotateY((float) Math.atan2(xd / mag, zd / mag));
        rotation.rotateX((float) -Math.asin(yd / mag));
        for(int i = 0; i < vertices.length / 3; ++i) {
            var vector = new Vector3f(vertices[i * 3] / 16f, vertices[i * 3 + 1] / 16f, vertices[i * 3 + 2] / 16f);
            vector.rotate(rotation);
            vector.add(
                    (float) (this.x - camPos.x),
                    (float) (this.y - camPos.y) + 2 / 16f,
                    (float) (this.z - camPos.z)
            );
            builder.vertex(vector.x, vector.y, vector.z)
                    .uv(uvs[i * 2], uvs[i * 2 + 1])
                    .color(rCol, gCol, bCol, alpha)
                    .uv2(LightTexture.FULL_BRIGHT)
                    .endVertex();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }

    public static class Factory implements ParticleEngine.SpriteParticleRegistration<GunFlashParticleOptions> {
        @Override
        public ParticleProvider<GunFlashParticleOptions> create(SpriteSet pSprites) {
            return (type, level, x, y, z, vx, vy, vz) ->
                    new GunFlashParticle(level, x, y, z, vx, vy, vz);
        }
    }
}

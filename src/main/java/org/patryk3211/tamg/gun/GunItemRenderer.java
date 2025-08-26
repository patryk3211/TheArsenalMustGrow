package org.patryk3211.tamg.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.tamg.TamgClient;
import org.patryk3211.tamg.collections.TamgRenderTypes;

@OnlyIn(Dist.CLIENT)
public abstract class GunItemRenderer<A extends GunAnimationData> extends CustomRenderedItemModelRenderer {
    private final Class<A> clazz;

    protected GunItemRenderer(Class<A> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderer.render(model.getOriginalModel(), light);

        var animation = TamgClient.GUN_RENDER_HANDLER.getAnimation(stack);
        A castAnimation = null;
        if(clazz.isInstance(animation)) {
            castAnimation = (A) animation;
        }
        var pt = AnimationTickHolder.getPartialTicks();
        renderAnimatedPart((GunItem) stack.getItem(), castAnimation, pt, renderer, ms, buffer, light);
    }

    protected abstract PartialModel flash();

    protected void renderAnimatedPart(GunItem gun, @Nullable A animation, float pt, PartialItemModelRenderer renderer, PoseStack ms, MultiBufferSource buffer, int light) {
        if(animation != null) {
            var consumer = buffer.getBuffer(TamgRenderTypes.SOLID_GLOWING);
            var flash = CachedBuffers.partial(flash(), Blocks.AIR.defaultBlockState());

            var offset = gun.flashOffset;
            flash.light(LightTexture.FULL_BRIGHT)
                    .disableDiffuse()
                    .translate(offset.scale(1 / 16f))
                    .rotateZ(animation.flashAngle)
                    .scale(animation.flash(pt))
                    .renderInto(ms, consumer);
        }
    }
}

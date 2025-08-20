package org.patryk3211.tamg.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.patryk3211.tamg.TamgClient;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.collections.TamgRenderTypes;

@OnlyIn(Dist.CLIENT)
public class GunItemRenderer extends CustomRenderedItemModelRenderer {
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        renderer.render(model.getOriginalModel(), light);

        ms.pushPose();
        var data = TamgClient.GUN_RENDER_HANDLER.getAnimation(stack);
        if(data != null && stack.getItem() instanceof GunItem gun) {
            var consumer = buffer.getBuffer(TamgRenderTypes.SOLID_GLOWING);
            var flash = CachedBuffers.partial(TamgPartialModels.FLASH, Blocks.AIR.defaultBlockState());

            var pt = AnimationTickHolder.getPartialTicks();

            var offset = gun.flashOffset; //new Vec3(0, -2.5, -4);
            flash.light(LightTexture.FULL_BRIGHT)
                    .disableDiffuse()
                    .translate(offset.scale(1 / 16f))
                    .rotateZ(data.flashAngle)
                    .scale(data.flash(pt))
                    .renderInto(ms, consumer);

            ms.translate(0, 0, data.top(pt));
        }

        renderer.render(TamgPartialModels.PISTOL_TOP.get(), light);
        ms.popPose();
    }
}

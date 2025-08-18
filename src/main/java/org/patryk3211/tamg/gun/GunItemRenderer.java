package org.patryk3211.tamg.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GunItemRenderer extends CustomRenderedItemModelRenderer {
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        renderer.render(model.getOriginalModel(), light);

//        var flash = TamgClient.GUN_RENDER_HANDLER.getFlash(AnimationTickHolder.getPartialTicks());

//        if(flash > 0.05 && stack.getItem() instanceof GunItem gun) {
//            ms.pushPose();
//            var offset = new Vec3(5.5, 3, 4);
//            ms.translate(0, -0.1875, -0.25);
//            ms.scale(flash, flash, flash);
//            ms.translate(offset.x / 16, offset.y / 16 + 0.1875, offset.z / 16 + 0.25);
//            renderer.render(TamgPartialModels.FLASH.get(), LightTexture.FULL_BRIGHT);
//            ms.popPose();
//        }
    }
}

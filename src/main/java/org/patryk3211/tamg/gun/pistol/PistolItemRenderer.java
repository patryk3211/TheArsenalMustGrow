package org.patryk3211.tamg.gun.pistol;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.GunItemRenderer;

public class PistolItemRenderer extends GunItemRenderer<PistolAnimationData> {
    public PistolItemRenderer() {
        super(PistolAnimationData.class);
    }

    @Override
    protected PartialModel flash() {
        return TamgPartialModels.PISTOL_FLASH;
    }

    @Override
    protected void renderAnimatedPart(GunItem gun, @Nullable PistolAnimationData animation, float pt, PartialItemModelRenderer renderer, PoseStack ms, MultiBufferSource buffer, int light) {
        super.renderAnimatedPart(gun, animation, pt, renderer, ms, buffer, light);
        ms.pushPose();
        if(animation != null) {
            ms.translate(0, 0, animation.top(pt));
        }

        renderer.render(TamgPartialModels.PISTOL_TOP.get(), light);
        ms.popPose();
    }
}

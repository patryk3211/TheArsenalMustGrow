package org.patryk3211.tamg.gun.revolver;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.GunItemRenderer;

public class RevolverItemRenderer extends GunItemRenderer<RevolverAnimationData> {
    public RevolverItemRenderer() {
        super(RevolverAnimationData.class);
    }

    @Override
    protected PartialModel flash() {
        return TamgPartialModels.REVOLVER_FLASH;
    }

    @Override
    protected void renderAnimatedPart(GunItem gun, @Nullable RevolverAnimationData animation, float pt, PartialItemModelRenderer renderer, PoseStack ms, MultiBufferSource buffer, int light) {
        super.renderAnimatedPart(gun, animation, pt, renderer, ms, buffer, light);
        ms.pushPose();
        if(animation != null) {
            var rot = new Quaternionf();
            rot.rotateX(animation.hammer(pt));
            ms.rotateAround(rot, 0.0f, -0.125f, 0.3125f);
        }
        renderer.render(TamgPartialModels.REVOLVER_HAMMER.get(), light);
        ms.popPose();

        ms.pushPose();
        if(animation != null) {
            var rot = new Quaternionf();
            rot.rotateZ(animation.drum(pt));
            ms.rotateAround(rot, 0, -0.125f, 0);
        }
        renderer.render(TamgPartialModels.REVOLVER_DRUM.get(), light);
        ms.popPose();
    }
}

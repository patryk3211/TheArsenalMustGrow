package org.patryk3211.tamg.gun.sniper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.gun.EmptyAnimationData;
import org.patryk3211.tamg.gun.GunItemRenderer;

public class SniperRifleItemRenderer extends GunItemRenderer<EmptyAnimationData> {
    public SniperRifleItemRenderer() {
        super(EmptyAnimationData.class);
    }

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        var player = Minecraft.getInstance().player;
        if(player != null && SniperRifleItem.isZooming(player))
            return;
        super.render(stack, model, renderer, transformType, ms, buffer, light, overlay);
    }

    @Override
    protected PartialModel flash() {
        return TamgPartialModels.REVOLVER_FLASH;
    }
}

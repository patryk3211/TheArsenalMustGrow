package org.patryk3211.tamg.gun.sniper;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.patryk3211.tamg.Tamg;

public class SniperOverlay extends ForgeGui implements IGuiOverlay {
    protected static final ResourceLocation SNIPER_SCOPE_LOCATION = Tamg.texture("misc/sniper_scope");

    public SniperOverlay(Minecraft mc) {
        super(mc);
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if(minecraft.player == null)
            return;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        float deltaFrame = minecraft.getDeltaFrameTime();
        scopeScale = Mth.lerp(0.5F * deltaFrame, scopeScale, 1.125F);
        if (minecraft.options.getCameraType().isFirstPerson()) {
            if(SniperRifleItem.isZooming(minecraft.player)) {
                float f = (float)Math.min(screenWidth, screenHeight);
                float f1 = Math.min((float) screenWidth / f, (float) screenHeight / f) * scopeScale;
                int i = Mth.floor(f * f1);
                int j = Mth.floor(f * f1);
                int k = (screenWidth - i) / 2;
                int l = (screenHeight - j) / 2;
                int i1 = k + i;
                int j1 = l + j;
                guiGraphics.blit(SNIPER_SCOPE_LOCATION, k, l, -90, 0.0F, 0.0F, i, j, i, j);
                guiGraphics.fill(RenderType.guiOverlay(), 0, j1, screenWidth, screenHeight, -90, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), 0, 0, screenWidth, l, -90, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), i1, l, screenWidth, j1, -90, -16777216);
            } else {
                scopeScale = 0.5F;
            }
        }
    }
}

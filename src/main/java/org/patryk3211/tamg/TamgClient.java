package org.patryk3211.tamg;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.collections.TamgParticles;
import org.patryk3211.tamg.collections.TamgRenderTypes;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.GunRenderHandler;
import org.patryk3211.tamg.gun.ZoomC2SPacket;
import org.patryk3211.tamg.gun.sniper.SniperOverlay;
import org.patryk3211.tamg.gun.sniper.SniperRifleItem;

@OnlyIn(Dist.CLIENT)
public class TamgClient {
    public static final GunRenderHandler GUN_RENDER_HANDLER = new GunRenderHandler();
    private static int leftUseTicks = 0;

    public static void init() {
        TamgPartialModels.load();
        TamgRenderTypes.init();

        GUN_RENDER_HANDLER.registerListeners(MinecraftForge.EVENT_BUS);
        Tamg.modEventBus.addListener(TamgClient::particleManagerRegistration);
        Tamg.modEventBus.addListener(TamgClient::overlayRegistration);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::clientTick);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::mouseButton);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::fovModifier);
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            GUN_RENDER_HANDLER.tick();
            var player = Minecraft.getInstance().player;
            if(player != null && SniperRifleItem.isZooming(player)) {
                if(!Minecraft.getInstance().options.keyAttack.isDown()) {
                    Networking.getChannel().sendToServer(new ZoomC2SPacket(false));
                }
            }
        }
    }

    public static void mouseButton(InputEvent.MouseButton.Pre event) {
        var player = Minecraft.getInstance().player;
        if(player == null)
            return;
        var stack = player.getMainHandItem();
        if(stack.getItem() instanceof GunItem gun) {
            if(gun.hasZoom() && event.getAction() == InputConstants.PRESS
                && Minecraft.getInstance().options.keyAttack.matchesMouse(event.getButton())) {
                Networking.getChannel().sendToServer(new ZoomC2SPacket(true));
            }
        }
    }

    public static void fovModifier(ComputeFovModifierEvent event) {
        if(SniperRifleItem.isZooming(event.getPlayer()))
            event.setNewFovModifier(0.1f);
    }

    public static void particleManagerRegistration(RegisterParticleProvidersEvent event) {
        TamgParticles.registerFactories(event);
    }

    public static void overlayRegistration(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.SPYGLASS.id(), "tamg_sniper_scope", new SniperOverlay(Minecraft.getInstance()));
    }
}

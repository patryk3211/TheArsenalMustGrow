package org.patryk3211.tamg;

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
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.collections.TamgParticles;
import org.patryk3211.tamg.collections.TamgRenderTypes;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.GunRenderHandler;
import org.patryk3211.tamg.gun.sniper.SniperOverlay;
import org.patryk3211.tamg.gun.sniper.SniperRifleItem;

@OnlyIn(Dist.CLIENT)
public class TamgClient {
    public static final GunRenderHandler GUN_RENDER_HANDLER = new GunRenderHandler();
    private static int leftUseCooldown = 0;

    public static void init() {
        TamgPartialModels.load();
        TamgRenderTypes.init();

        GUN_RENDER_HANDLER.registerListeners(MinecraftForge.EVENT_BUS);
        Tamg.modEventBus.addListener(TamgClient::particleManagerRegistration);
        Tamg.modEventBus.addListener(TamgClient::overlayRegistration);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::clientTick);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::clientInput);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::fovModifier);
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            GUN_RENDER_HANDLER.tick();
            if(leftUseCooldown > 0)
                --leftUseCooldown;
        }
    }

    public static void clientInput(InputEvent.InteractionKeyMappingTriggered event) {
        var player = Minecraft.getInstance().player;
        var stack = player.getItemInHand(event.getHand());
        if(event.isAttack() && stack.getItem() instanceof GunItem gun && leftUseCooldown == 0) {
            InteractionResult result = gun.onLeftClick(player, stack, event.getHand());
            if(result.consumesAction()) {
                leftUseCooldown = 10;
            }
            event.setCanceled(result.consumesAction());
            event.setSwingHand(result.shouldSwing());
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

package org.patryk3211.tamg;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.patryk3211.tamg.collections.TamgPartialModels;
import org.patryk3211.tamg.collections.TamgParticles;
import org.patryk3211.tamg.collections.TamgRenderTypes;
import org.patryk3211.tamg.gun.GunRenderHandler;

@OnlyIn(Dist.CLIENT)
public class TamgClient {
    public static final GunRenderHandler GUN_RENDER_HANDLER = new GunRenderHandler();

    public static void init() {
        TamgPartialModels.load();
        TamgRenderTypes.init();

        GUN_RENDER_HANDLER.registerListeners(MinecraftForge.EVENT_BUS);
        Tamg.modEventBus.addListener(TamgClient::particleManagerRegistration);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::clientTick);
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            GUN_RENDER_HANDLER.tick();
//            RiotArmorFirstPersonRenderer.clientTick();
        }
    }

    public static void particleManagerRegistration(RegisterParticleProvidersEvent event) {
        TamgParticles.registerFactories(event);
    }
}

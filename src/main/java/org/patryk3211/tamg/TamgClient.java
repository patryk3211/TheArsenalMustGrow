package org.patryk3211.tamg;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.patryk3211.tamg.gun.GunRenderHandler;

public class TamgClient {
    public static final GunRenderHandler GUN_RENDER_HANDLER = new GunRenderHandler();

    public static void init() {
        GUN_RENDER_HANDLER.registerListeners(MinecraftForge.EVENT_BUS);
        MinecraftForge.EVENT_BUS.addListener(TamgClient::clientTick);
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            GUN_RENDER_HANDLER.tick();
        }
    }
}

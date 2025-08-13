package org.patryk3211.tamg;

import net.minecraftforge.common.MinecraftForge;
import org.patryk3211.tamg.gun.GunRenderHandler;

public class TamgClient {
    public static final GunRenderHandler GUN_RENDER_HANDLER = new GunRenderHandler();

    public static void init() {
        GUN_RENDER_HANDLER.registerListeners(MinecraftForge.EVENT_BUS);
    }
}

package org.patryk3211.tamg;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.MutableComponent;

public class Lang extends net.createmod.catnip.lang.Lang {
    public static LangBuilder builder() {
        return builder(Tamg.MOD_ID);
    }

    public static MutableComponent translateDirect(String key, Object... args) {
        return builder().translate(key, args).component();
    }
}

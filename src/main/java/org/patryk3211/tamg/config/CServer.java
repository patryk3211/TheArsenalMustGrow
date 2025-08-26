package org.patryk3211.tamg.config;

import net.createmod.catnip.config.ConfigBase;

public class CServer extends ConfigBase {
    public final CGuns guns = nested(0, CGuns::new);

    @Override
    public String getName() {
        return "server";
    }
}

package org.patryk3211.tamg.collections;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import org.patryk3211.tamg.Tamg;

public class TamgPartialModels {
    public static final PartialModel FLASH = model("flash");

    private static PartialModel model(String path) {
        return PartialModel.of(Tamg.asResource(path));
    }
}

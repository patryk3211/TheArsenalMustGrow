package org.patryk3211.tamg.collections;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import org.patryk3211.tamg.Tamg;

public class TamgPartialModels {
    public static final PartialModel PISTOL_FLASH = model("item/pistol/flash");
    public static final PartialModel PISTOL_TOP = model("item/pistol/top");

    public static final PartialModel REVOLVER_FLASH = model("item/revolver/flash");
    public static final PartialModel REVOLVER_HAMMER = model("item/revolver/hammer");
    public static final PartialModel REVOLVER_DRUM = model("item/revolver/drum");

    private static PartialModel model(String path) {
        return PartialModel.of(Tamg.asResource(path));
    }

    public static void load() { }
}

package org.patryk3211.tamg.collections;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static org.patryk3211.tamg.Tamg.REGISTRATE;

public class ModdedItems {
    public static final ItemEntry<Item> TEST = REGISTRATE.item("test", Item::new).register();

    public static void register() { /* Initialize static fields */ }
}

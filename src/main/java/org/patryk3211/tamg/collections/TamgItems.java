package org.patryk3211.tamg.collections;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.GunProperties;

import static org.patryk3211.tamg.Tamg.REGISTRATE;

public class TamgItems {
    public static final ItemEntry<Item>
            SMALL_BULLET_CASING = ingredient("small_casing"),
            MEDIUM_BULLET_CASING = ingredient("medium_casing"),
            HEAVY_BULLET_CASING = ingredient("heavy_casing"),
            SHOTGUN_SLUG_CASING = ingredient("slug_casing"),

            SMALL_BULLET = ingredient("small_bullet", TamgItemTags.PISTOL_BULLETS.tag),
            MEDIUM_BULLET = ingredient("medium_bullet"),
            HEAVY_BULLET = ingredient("heavy_bullet"),
            SHOTGUN_SLUG = ingredient("shotgun_slug");

    public static final ItemEntry<Item> CARBON_FIBRE = ingredient("carbon_fibre");

    public static final ItemEntry<GunItem> PISTOL = REGISTRATE.item("pistol", GunItem::new)
            .transform(GunProperties.setDamage(4))
            .model((ctx, prov) -> {})
            .register();

    public static final ItemEntry<SequencedAssemblyItem>
            INCOMPLETE_PISTOL = sequenced(PISTOL),
            INCOMPLETE_SMALL_BULLET = sequenced(SMALL_BULLET),
            INCOMPLETE_MEDIUM_BULLET = sequenced(MEDIUM_BULLET),
            INCOMPLETE_HEAVY_BULLET = sequenced(HEAVY_BULLET),
            INCOMPLETE_SHOTGUN_SLUG = sequenced(SHOTGUN_SLUG);

    @SafeVarargs
    private static ItemEntry<Item> ingredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new).tag(tags).register();
    }

    private static ItemEntry<SequencedAssemblyItem> sequenced(ItemEntry<?> complete) {
        return sequenced(complete.getId().getPath());
    }

    private static ItemEntry<SequencedAssemblyItem> sequenced(String name) {
        return REGISTRATE.item("incomplete_" + name, SequencedAssemblyItem::new).register();
    }

    public static void register() { /* Initialize static fields */ }
}

package org.patryk3211.tamg.collections;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import org.patryk3211.tamg.armor.Armor;
import org.patryk3211.tamg.config.CGuns;
import org.patryk3211.tamg.gun.GunItem;
import org.patryk3211.tamg.gun.pistol.PistolAnimationData;
import org.patryk3211.tamg.gun.pistol.PistolItemRenderer;
import org.patryk3211.tamg.gun.revolver.RevolverAnimationData;
import org.patryk3211.tamg.gun.revolver.RevolverItemRenderer;

import static org.patryk3211.tamg.Tamg.REGISTRATE;
import static org.patryk3211.tamg.gun.GunProperties.*;

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
            .transform(CGuns.base(4, 0, 1.0, 0.5, 0.1, 0, 0))
            .transform(setBulletTag(TamgItemTags.PISTOL_BULLETS.tag))
            .transform(setFlashOffset(0, -2.5, -4))
            .transform(setShootVectors(0.375, -0.15, 1.0, -0.025, 0.0125, 0))
            .transform(withAnimationData(() -> PistolAnimationData::new))
            .transform(withRenderer(() -> PistolItemRenderer::new))
            .model(gunBaseModel())
            .register();

    public static final ItemEntry<GunItem> REVOLVER = REGISTRATE.item("revolver", GunItem::new)
            .transform(CGuns.base(8, 0.5, 1.0, 0.3, 0.025, 0.7, 6))
            .transform(setBulletTag(TamgItemTags.PISTOL_BULLETS.tag))
            .transform(setFlashOffset(0, -0.5, -11))
            .transform(setShootVectors(0.375, 0, 1.25, -0.025, 0.0125, 0))
            .transform(withAnimationData(() -> RevolverAnimationData::new))
            .transform(withRenderer(() -> RevolverItemRenderer::new))
            .model(gunBaseModel())
            .register();

    public static final ItemEntry<SequencedAssemblyItem>
            INCOMPLETE_PISTOL = sequenced(PISTOL),
            INCOMPLETE_REVOLVER = sequenced(REVOLVER),
            INCOMPLETE_SMALL_BULLET = sequenced(SMALL_BULLET),
            INCOMPLETE_MEDIUM_BULLET = sequenced(MEDIUM_BULLET),
            INCOMPLETE_HEAVY_BULLET = sequenced(HEAVY_BULLET),
            INCOMPLETE_SHOTGUN_SLUG = sequenced(SHOTGUN_SLUG);

    public static final ItemEntry<Armor>
            ARMOR_HELMET = REGISTRATE.item("armor_helmet", Armor.of(ArmorItem.Type.HELMET)).register(),
            ARMOR_CHESTPLATE = REGISTRATE.item("armor_chestplate", Armor.of(ArmorItem.Type.CHESTPLATE)).register(),
            ARMOR_LEGGINGS = REGISTRATE.item("armor_leggings", Armor.of(ArmorItem.Type.LEGGINGS)).register(),
            ARMOR_BOOTS = REGISTRATE.item("armor_boots", Armor.of(ArmorItem.Type.BOOTS)).register();

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

    public static <T extends GunItem> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> gunBaseModel() {
        return (ctx, prov) ->
                prov.withExistingParent(ctx.getName(), prov.modLoc("item/" + ctx.getName() + "/base"));
    }
}

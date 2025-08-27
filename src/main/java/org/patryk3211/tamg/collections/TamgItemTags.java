package org.patryk3211.tamg.collections;

import com.simibubi.create.AllTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.patryk3211.tamg.Tamg;

public enum TamgItemTags {
    PISTOL_BULLETS,
    REVOLVER_BULLETS,
    ASSAULT_RIFLE_BULLETS,
    ;

    public final TagKey<Item> tag;

    TamgItemTags() {
        tag = AllTags.optionalTag(ForgeRegistries.ITEMS, new ResourceLocation(Tamg.MOD_ID, name().toLowerCase()));
    }

    TamgItemTags(String path) {
        this(Tamg.MOD_ID, path);
    }

    TamgItemTags(String namespace, String path) {
        tag = AllTags.optionalTag(ForgeRegistries.ITEMS, new ResourceLocation(namespace, path));
    }
}

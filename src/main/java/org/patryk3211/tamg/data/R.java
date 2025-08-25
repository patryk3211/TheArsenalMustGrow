package org.patryk3211.tamg.data;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.patryk3211.tamg.collections.TamgItems;

class R {
    public static ItemLike copperNugget() {
        return AllItems.COPPER_NUGGET;
    }

    public static ItemLike leadNugget() {
        return TFMGItems.LEAD_NUGGET;
    }

    public static ItemLike ironNugget() {
        return Items.IRON_NUGGET;
    }

    public static ItemLike brassSheet() {
        return AllItems.BRASS_SHEET;
    }

    public static ItemLike gunpowder() {
        return Items.GUNPOWDER;
    }

    public static TagKey<Item> coal() {
        return ItemTags.COALS;
    }

    public static ItemLike pulp() {
        return AllItems.PULP;
    }

    public static ItemLike screws() {
        return TFMGItems.SCREW;
    }

    public static ItemLike screwDriver() {
        return TFMGItems.SCREWDRIVER;
    }

    public static ItemLike carbonFibre() {
        return TamgItems.CARBON_FIBRE;
    }

    public static ItemLike steelMechanism() {
        return TFMGItems.STEEL_MECHANISM;
    }

    public static TagKey<Item> ironSheet() {
        return AllTags.forgeItemTag("plates/iron");
    }

    public static ItemLike precisionMechanism() {
        return AllItems.PRECISION_MECHANISM;
    }
}

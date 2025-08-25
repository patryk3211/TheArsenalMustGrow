package org.patryk3211.tamg.armor;

import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import org.patryk3211.tamg.Tamg;

public class Armor extends BaseArmorItem {
    public static final ResourceLocation TEXTURE = Tamg.asResource("armor");

    public static NonNullFunction<Properties, Armor> of(Type type) {
        return p -> new Armor(type, p);
    }

    public Armor(Type type, Properties properties) {
        super(ArmorMaterial.INSTANCE, type, properties, TEXTURE);
    }
}

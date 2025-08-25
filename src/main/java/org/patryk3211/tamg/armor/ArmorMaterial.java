package org.patryk3211.tamg.armor;

import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.patryk3211.tamg.Tamg;

public class ArmorMaterial implements net.minecraft.world.item.ArmorMaterial {
    public static final ArmorMaterial INSTANCE = new ArmorMaterial();

    private ArmorMaterial() {

    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch(type) {
            case HELMET -> 90;
            case CHESTPLATE -> 150;
            case LEGGINGS -> 120;
            case BOOTS -> 90;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch(type) {
            case HELMET -> 4;
            case CHESTPLATE -> 7;
            case LEGGINGS -> 5;
            case BOOTS -> 3;
        };
    }

    @Override
    public int getEnchantmentValue() {
        return 2;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(TFMGItems.STEEL_INGOT);
    }

    @Override
    public String getName() {
        return Tamg.asResource("armor").toString();
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}

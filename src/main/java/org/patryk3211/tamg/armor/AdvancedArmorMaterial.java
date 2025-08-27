package org.patryk3211.tamg.armor;

import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.patryk3211.tamg.Tamg;

public class AdvancedArmorMaterial implements ArmorMaterial {
    public static final AdvancedArmorMaterial INSTANCE = new AdvancedArmorMaterial();

    private AdvancedArmorMaterial() {

    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch(type) {
            case HELMET -> 330;
            case CHESTPLATE -> 480;
            case LEGGINGS -> 450;
            case BOOTS -> 390;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch(type) {
            case HELMET -> 3;
            case CHESTPLATE -> 8;
            case LEGGINGS -> 6;
            case BOOTS -> 3;
        };
    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(TFMGItems.STEEL_INGOT);
    }

    @Override
    public String getName() {
        return Tamg.asResource("advanced_armor").toString();
    }

    @Override
    public float getToughness() {
        return 2.0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.1f;
    }
}

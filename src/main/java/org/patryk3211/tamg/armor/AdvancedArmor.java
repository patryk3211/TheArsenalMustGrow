package org.patryk3211.tamg.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import com.simibubi.create.foundation.item.LayeredArmorItem;
import com.simibubi.create.foundation.mixin.accessor.HumanoidArmorLayerAccessor;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.collections.TamgRenderTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;
import java.util.Map;

@ParametersAreNonnullByDefault
public class AdvancedArmor extends BaseArmorItem implements LayeredArmorItem {
    public static final ResourceLocation TEXTURE = Tamg.asResource("advanced_armor");

    public static NonNullFunction<Properties, AdvancedArmor> of(Type type) {
        return p -> new AdvancedArmor(type, p);
    }

    public AdvancedArmor(Type type, Properties properties) {
        super(AdvancedArmorMaterial.INSTANCE, type, properties, TEXTURE);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int base = super.getEnchantmentLevel(stack, enchantment);
        if(enchantment == Enchantments.PROJECTILE_PROTECTION || enchantment == Enchantments.ALL_DAMAGE_PROTECTION) {
            return Math.max(base, 1);
        }
        return base;
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        var map = super.getAllEnchantments(stack);
        map.compute(Enchantments.PROJECTILE_PROTECTION, (e, c) -> c == null ? 1 : Math.max(c, 1));
        map.compute(Enchantments.ALL_DAMAGE_PROTECTION, (e, c) -> c == null ? 1 : Math.max(c, 1));
        return map;
    }

    @Override
    public String getArmorTextureLocation(LivingEntity entity, EquipmentSlot slot, ItemStack stack, int layer) {
        return String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d.png", TEXTURE.getNamespace(), TEXTURE.getPath(), layer);
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void renderArmorPiece(HumanoidArmorLayer<?, ?, ?> layer, PoseStack poseStack,
                                  MultiBufferSource bufferSource, LivingEntity entity, EquipmentSlot slot, int light,
                                  HumanoidModel<?> originalModel, ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem item)) {
            return;
        }
        if (!item.canEquip(stack, slot, entity)) {
            return;
        }

        HumanoidArmorLayerAccessor accessor = (HumanoidArmorLayerAccessor) layer;
        Map<String, ResourceLocation> locationCache = HumanoidArmorLayerAccessor.create$getArmorLocationCache();
        boolean glint = stack.hasFoil();

        HumanoidModel<?> innerModel = accessor.create$getInnerModel();
        layer.getParentModel().copyPropertiesTo((HumanoidModel) innerModel);
        accessor.create$callSetPartVisibility(innerModel, slot);
        String locationStr2 = getArmorTextureLocation(entity, slot, stack, 2);
        ResourceLocation location2 = locationCache.computeIfAbsent(locationStr2, ResourceLocation::new);
        renderModel(poseStack, bufferSource, light, item, innerModel, glint, 1.0F, 1.0F, 1.0F, location2);

        HumanoidModel<?> outerModel = accessor.create$getOuterModel();
        layer.getParentModel().copyPropertiesTo((HumanoidModel) outerModel);
        accessor.create$callSetPartVisibility(outerModel, slot);
        String locationStr1 = getArmorTextureLocation(entity, slot, stack, 1);
        ResourceLocation location1 = locationCache.computeIfAbsent(locationStr1, ResourceLocation::new);
        renderModel(poseStack, bufferSource, light, item, outerModel, glint, 1.0F, 1.0F, 1.0F, location1);
    }

    // from HumanoidArmorLayer.renderModel
    private void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int light, ArmorItem item,
                             Model model, boolean glint, float red, float green, float blue, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(TamgRenderTypes.ARMOR_TRANSLUCENT_NO_CULL.apply(armorResource));
        //armorCutoutNoCull(armorResource));
        model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}

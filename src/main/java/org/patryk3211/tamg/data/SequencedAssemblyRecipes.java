package org.patryk3211.tamg.data;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.collections.TamgItems;

@SuppressWarnings("unused")
public class SequencedAssemblyRecipes extends SequencedAssemblyRecipeGen {
    GeneratedRecipe

    PISTOL = create("pistol", b -> b
            .require(AllItems.POTATO_CANNON)
            .addOutput(TamgItems.PISTOL, 100)
            .addOutput(TFMGItems.STEEL_MECHANISM, 5)
            .addOutput(TFMGItems.STEEL_INGOT, 3)
            .addOutput(TFMGItems.SCREW, 2)
            .transitionTo(TamgItems.INCOMPLETE_PISTOL)
            .loops(2)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.steelMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.carbonFibre()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.screws()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.screwDriver()).toolNotConsumed())
            .addStep(FillingRecipe::new, rb -> rb.require(TFMGFluids.LUBRICATION_OIL.get(), 250))
    ),

    REVOLVER = create("revolver", b -> b
            .require(AllItems.POTATO_CANNON)
            .addOutput(TamgItems.REVOLVER, 100)
            .addOutput(TFMGItems.STEEL_MECHANISM, 5)
            .addOutput(Items.IRON_INGOT, 3)
            .addOutput(TFMGItems.SCREW, 2)
            .transitionTo(TamgItems.INCOMPLETE_REVOLVER)
            .loops(2)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.ironSheet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.steelMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.precisionMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.carbonFibre()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.screws()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.screwDriver()).toolNotConsumed())
            .addStep(FillingRecipe::new, rb -> rb.require(TFMGFluids.LUBRICATION_OIL.get(), 250))
    ),

    ASSAULT_RIFLE = create("assault_rifle", b -> b
            .require(TamgItems.PISTOL)
            .addOutput(TamgItems.ASSAULT_RIFLE, 100)
            .addOutput(TFMGItems.STEEL_MECHANISM, 5)
            .addOutput(TFMGItems.STEEL_INGOT, 5)
            .addOutput(TFMGItems.SCREW, 3)
            .transitionTo(TamgItems.INCOMPLETE_ASSAULT_RIFLE)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.heavyPlate()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.steelMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.steelMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.carbonFibre()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.screws()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.screwDriver()).toolNotConsumed())
            .addStep(FillingRecipe::new, rb -> rb.require(TFMGFluids.LUBRICATION_OIL.get(), 250))
    ),

    SMALL_BULLET = create("small_bullet", b -> b
            .require(TamgItems.SMALL_BULLET_CASING.get())
            .addOutput(TamgItems.SMALL_BULLET, 100)
            .addOutput(AllItems.BRASS_SHEET, 5)
            .addOutput(Items.GUNPOWDER, 2)
            .addOutput(AllItems.COPPER_NUGGET, 2)
            .transitionTo(TamgItems.INCOMPLETE_SMALL_BULLET)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.gunpowder()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.leadNugget()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.copperNugget()))
            .addStep(PressingRecipe::new, rb -> rb)
    ),

    MEDIUM_BULLET = create("medium_bullet", b -> b
            .require(TamgItems.MEDIUM_BULLET_CASING.get())
            .addOutput(TamgItems.MEDIUM_BULLET, 100)
            .addOutput(AllItems.BRASS_SHEET, 6)
            .addOutput(Items.GUNPOWDER, 3)
            .addOutput(AllItems.COPPER_NUGGET, 3)
            .transitionTo(TamgItems.INCOMPLETE_MEDIUM_BULLET)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.gunpowder()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.gunpowder()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.leadNugget()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.copperNugget()))
            .addStep(PressingRecipe::new, rb -> rb)
    ),

    HEAVY_BULLET = create("heavy_bullet", b -> b
            .require(TamgItems.HEAVY_BULLET_CASING.get())
            .addOutput(TamgItems.HEAVY_BULLET, 100)
            .addOutput(AllItems.BRASS_SHEET, 6)
            .addOutput(Items.GUNPOWDER, 3)
            .addOutput(AllItems.COPPER_NUGGET, 3)
            .transitionTo(TamgItems.INCOMPLETE_HEAVY_BULLET)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.gunpowder()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.gunpowder()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.leadNugget()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.ironNugget()))
            .addStep(PressingRecipe::new, rb -> rb)
    ),

    SHOTGUN_SLUG = create("shotgun_slug", b -> b
            .require(TamgItems.SHOTGUN_SLUG_CASING.get())
            .addOutput(TamgItems.SHOTGUN_SLUG, 100)
            .addOutput(AllItems.BRASS_SHEET, 6)
            .addOutput(Items.GUNPOWDER, 3)
            .addOutput(TFMGItems.LEAD_NUGGET, 3)
            .transitionTo(TamgItems.INCOMPLETE_SHOTGUN_SLUG)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.redCandle()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.gunpowder()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.leadNugget()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.leadNugget()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(R.leadNugget()))
    )
            ;

    public SequencedAssemblyRecipes(PackOutput output) {
        super(output, Tamg.MOD_ID);
    }
}

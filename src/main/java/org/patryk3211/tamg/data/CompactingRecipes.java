package org.patryk3211.tamg.data;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.collections.TamgItems;

@SuppressWarnings("unused")
public class CompactingRecipes extends ProcessingRecipeGen {
    GeneratedRecipe

    CARBON_FIBRE = create("carbon_fibre", b -> b
            .require(R.coal())
            .require(R.coal())
            .require(R.coal())
            .require(R.coal())
            .require(R.coal())
            .require(R.pulp())
            .requiresHeat(HeatCondition.SUPERHEATED)
            .output(TamgItems.CARBON_FIBRE))
    ;

    public CompactingRecipes(PackOutput generator) {
        super(generator, Tamg.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}

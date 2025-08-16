package org.patryk3211.tamg.data;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.patryk3211.tamg.Tamg;
import org.patryk3211.tamg.collections.TamgItems;

@SuppressWarnings("unused")
public class CuttingRecipes extends ProcessingRecipeGen {
    GeneratedRecipe

    SMALL_BULLET_CASING = create("small_casing", b -> b
            .require(R.brassSheet())
            .output(TamgItems.SMALL_BULLET_CASING, 2)
            .duration(50)
    ),
    MEDIUM_BULLET_CASING = create("medium_casing", b -> b
            .require(R.brassSheet())
            .output(TamgItems.MEDIUM_BULLET_CASING, 2)
            .duration(50)
    ),
    HEAVY_BULLET_CASING = create("heavy_casing", b -> b
            .require(R.brassSheet())
            .output(TamgItems.HEAVY_BULLET_CASING, 2)
            .duration(50)
    ),
    SHOTGUN_SLUG_CASING = create("slug_casing", b -> b
            .require(R.brassSheet())
            .output(TamgItems.SHOTGUN_SLUG_CASING, 2)
            .duration(50)
    )
            ;

    public CuttingRecipes(PackOutput generator) {
        super(generator, Tamg.MOD_ID);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }
}

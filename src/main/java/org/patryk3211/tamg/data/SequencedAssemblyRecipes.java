package org.patryk3211.tamg.data;

import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import net.minecraft.data.PackOutput;
import org.patryk3211.tamg.Tamg;

public class SequencedAssemblyRecipes extends SequencedAssemblyRecipeGen {
    public SequencedAssemblyRecipes(PackOutput output) {
        super(output, Tamg.MOD_ID);
    }
}

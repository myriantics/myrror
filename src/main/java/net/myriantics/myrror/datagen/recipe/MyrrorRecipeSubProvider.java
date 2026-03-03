package net.myriantics.myrror.datagen.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public abstract class MyrrorRecipeSubProvider {
    protected final MyrrorRecipeProvider provider;
    protected final RecipeOutput output;

    public MyrrorRecipeSubProvider(MyrrorRecipeProvider provider, RecipeOutput output) {
        this.provider = provider;
        this.output = output;
        this.generateRecipes();
    }

    protected abstract void generateRecipes();
}

package net.myriantics.myrror.datagen.template.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public abstract class MyrrorRecipeSubProvider {
    protected final MyrrorRecipeProvider provider;
    protected final RecipeOutput output;

    public MyrrorRecipeSubProvider(MyrrorRecipeProvider provider, RecipeOutput output) {
        this.provider = provider;
        this.output = output;
        this.generateRecipes();
    }

    protected abstract void generateRecipes();

    protected void acceptRecipe(ResourceLocation recipeId, Recipe<?> recipe) {
        this.provider.acceptRecipe(this.output, recipeId, recipe);
    }
}

package net.myriantics.myrror.datagen.template.recipe.providers;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeProvider;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeSubProvider;
import net.myriantics.myrror.util.NamedIngredient;
import org.jetbrains.annotations.Nullable;

public abstract class StonecuttingRecipeProvider extends MyrrorRecipeSubProvider {
    public StonecuttingRecipeProvider(MyrrorRecipeProvider provider, RecipeOutput output) {
        super(provider, output);
    }

    public void addStonecuttingRecipe(NamedIngredient ingredient, ItemStack result, @Nullable String group) {
        String resultName = RecipeProvider.getItemName(result.getItem());
        ResourceLocation recipeId = this.provider.computeRecipeIdentifier("stonecutting", resultName + "_from_" + ingredient.getName());
        this.acceptRecipe(recipeId, new StonecutterRecipe(group, ingredient.toIngredient(), result));
    }
}

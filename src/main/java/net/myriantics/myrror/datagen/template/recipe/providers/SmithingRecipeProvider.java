package net.myriantics.myrror.datagen.template.recipe.providers;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeProvider;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeSubProvider;
import net.myriantics.myrror.util.NamedIngredient;

public abstract class SmithingRecipeProvider extends MyrrorRecipeSubProvider {
    public SmithingRecipeProvider(MyrrorRecipeProvider provider, RecipeOutput output) {
        super(provider, output);
    }

    public void addSmithingTransformRecipe(NamedIngredient template, NamedIngredient base, NamedIngredient addition, ItemStack result) {
        String resultName = RecipeProvider.getItemName(result.getItem());

        ResourceLocation recipeId = this.provider.computeRecipeIdentifier("smithing_transform", template.getName() + "/" + resultName + "_from_" + base.getName() + "_and_" + addition);

        this.acceptRecipe(recipeId, new SmithingTransformRecipe(template.toIngredient(), base.toIngredient(), addition.toIngredient(), result));
    }
}

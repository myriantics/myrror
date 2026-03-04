package net.myriantics.myrror.datagen.template.recipe.providers;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeProvider;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeSubProvider;
import net.myriantics.myrror.util.NamedIngredient;

import java.util.function.UnaryOperator;

public abstract class CookingRecipeProvider extends MyrrorRecipeSubProvider {

    public CookingRecipeProvider(MyrrorRecipeProvider provider, RecipeOutput output) {
        super(provider, output);
    }

    public void addOreSmeltingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator) {
        addSmeltingRecipe(ingredient, result, operator);
        addBlastingRecipe(ingredient, result, builder -> operator.apply(builder).mulCookingTime(0.5f));
    }

    public void addFoodCookingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator) {
        addSmeltingRecipe(ingredient, result, operator);
        addSmokingRecipe(ingredient, result, builder -> operator.apply(builder).mulCookingTime(0.5f));
        addCampfireCookingRecipe(ingredient, result, builder -> operator.apply(builder).mulCookingTime(3.0f));
    }

    public void addSmeltingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator) {
        addCookingRecipe(ingredient, result, operator, SmeltingRecipe::new, "smelting");
    }

    public void addBlastingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator) {
        addCookingRecipe(ingredient, result, operator, BlastingRecipe::new, "blasting");
    }

    public void addSmokingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator) {
        addCookingRecipe(ingredient, result, operator, SmokingRecipe::new, "smoking");
    }

    public void addCampfireCookingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator) {
        addCookingRecipe(ingredient, result, operator, CampfireCookingRecipe::new, "campfire_cooking");
    }

    public void addCookingRecipe(NamedIngredient ingredient, ItemStack result, UnaryOperator<CookingBuilder> operator, AbstractCookingRecipe.Factory<?> factory, String type) {
        CookingBuilder builder = operator.apply(new CookingBuilder(ingredient.toIngredient(), result));

        String resultName = RecipeProvider.getItemName(result.getItem());

        ResourceLocation recipeId = this.provider.computeRecipeIdentifier("cooking/" + type, resultName + "_from_" + ingredient.getName());
        this.acceptRecipe(recipeId, builder.build(factory));
    }

    public static final class CookingBuilder {
        private final Ingredient ingredient;
        private final ItemStack result;
        private float experience = 0.1f;
        private int cookingTime = 200;
        private String group = null;
        private CookingBookCategory category = CookingBookCategory.MISC;

        private CookingBuilder(Ingredient ingredient, ItemStack result) {
            this.ingredient = ingredient;
            this.result = result;
        }

        public static CookingBuilder of(Ingredient ingredient, ItemStack result) {
            return new CookingBuilder(ingredient, result);
        }

        public CookingBuilder experience(float experience) {
            this.experience = experience;
            return this;
        }

        public CookingBuilder mulExperience(float multiplier) {
            this.experience *= multiplier;
            return this;
        }

        public CookingBuilder cookingTime(int cookingTime) {
            this.cookingTime = cookingTime;
            return this;
        }

        public CookingBuilder mulCookingTime(float multiplier) {
            this.cookingTime = (int) (multiplier * this.cookingTime);
            return this;
        }

        public CookingBuilder group(String group) {
            this.group = group;
            return this;
        }

        public CookingBuilder category(CookingBookCategory category) {
            this.category = category;
            return this;
        }

        public <T extends AbstractCookingRecipe> T build(AbstractCookingRecipe.Factory<T> factory) {
            return factory.create(this.group, this.category, this.ingredient, this.result, this.experience, this.cookingTime);
        }
    }
}

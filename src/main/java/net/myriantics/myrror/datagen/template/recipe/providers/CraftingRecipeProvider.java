package net.myriantics.myrror.datagen.template.recipe.providers;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeProvider;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeSubProvider;
import net.myriantics.myrror.util.MyrrorPatterns;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.UnaryOperator;

public abstract class CraftingRecipeProvider extends MyrrorRecipeSubProvider {
    public CraftingRecipeProvider(MyrrorRecipeProvider provider, RecipeOutput output) {
        super(provider, output);
    }

    public void add2x2PackingUnpackingRecipes(ItemLike large, ItemLike small, UnaryOperator<CraftingBuilder> operator) {
        add2x2PackingRecipe(Ingredient.of(small), new ItemStack(large), builder -> {
            operator.apply(builder);
            return builder;
        });
        add2x2UnpackingRecipe(Ingredient.of(large), small, builder -> {
            operator.apply(builder);
            return builder;
        });
    }

    public void add3x3PackingUnpackingRecipes(ItemLike large, ItemLike small, UnaryOperator<CraftingBuilder> operator) {
        add3x3PackingRecipe(Ingredient.of(small), new ItemStack(large), builder -> {
            operator.apply(builder);
            return builder;
        });
        add3x3UnpackingRecipe(Ingredient.of(large), small, builder -> {
            operator.apply(builder);
            return builder;
        });
    }

    public void add2x2PackingRecipe(Ingredient input, ItemStack output, UnaryOperator<ShapedBuilder> operator) {
        addShapedCraftingRecipe(MyrrorPatterns.SQUARE_2, output, builder -> operator.apply(builder.associate('x', input)));
    }

    public void add2x2UnpackingRecipe(Ingredient ingredient, ItemLike output, UnaryOperator<ShapelessBuilder> operator) {
        add1ToXUnpackingRecipe(ingredient, new ItemStack(output, 4), operator);
    }

    public void add3x3PackingRecipe(Ingredient input, ItemStack output, UnaryOperator<ShapedBuilder> operator) {
        addShapedCraftingRecipe(MyrrorPatterns.SQUARE_3, output, builder -> operator.apply(builder.associate('x', input)));
    }

    public void add3x3UnpackingRecipe(Ingredient ingredient, ItemLike output, UnaryOperator<ShapelessBuilder> operator) {
        add1ToXUnpackingRecipe(ingredient, new ItemStack(output, 9), operator);
    }

    public void add1ToXUnpackingRecipe(Ingredient ingredient, ItemStack output, UnaryOperator<ShapelessBuilder> operator) {
        addShapelessCraftingRecipe(output, builder -> operator.apply(builder.requires(ingredient)));
    }

    public void addShapedCraftingRecipe(String[] pattern, ItemStack output, UnaryOperator<ShapedBuilder> operator) {
        ShapedBuilder builder = operator.apply(ShapedBuilder.of(pattern, output));

        String resultName = RecipeProvider.getItemName(output.getItem());

        ResourceLocation recipeId = this.provider.computeRecipeIdentifier("crafting/shaped", resultName);
        this.acceptRecipe(recipeId, builder.build());
    }

    public void addShapelessCraftingRecipe(ItemStack output, UnaryOperator<ShapelessBuilder> operator) {
        ShapelessBuilder builder = operator.apply(ShapelessBuilder.of(output));

        String resultName = RecipeProvider.getItemName(output.getItem());

        ResourceLocation recipeId = this.provider.computeRecipeIdentifier("crafting/shapeless", resultName);

        this.acceptRecipe(recipeId, builder.build());
    }

    public interface CraftingBuilder {
        CraftingBuilder category(CraftingBookCategory category);

        CraftingBuilder group(String group);
    }

    public static final class ShapedBuilder implements CraftingBuilder {
        private final HashMap<Character, Ingredient> map = new HashMap<>(9);
        private final String[] pattern;
        private final ItemStack result;
        private CraftingBookCategory category = CraftingBookCategory.MISC;
        private String group = null;

        private ShapedBuilder(String[] pattern, ItemStack result) {
            this.pattern = pattern;
            this.result = result;
        }

        public static ShapedBuilder of(String[] pattern, ItemStack result) {
            return new ShapedBuilder(pattern, result);
        }

        public ShapedBuilder associate(char key, ItemLike item) {
            return this.associate(key, Ingredient.of(item));
        }

        public ShapedBuilder associate(char key, TagKey<Item> tagKey) {
            return this.associate(key, Ingredient.of(tagKey));
        }

        public ShapedBuilder associate(char key, Ingredient ingredient) {
            this.map.put(key, ingredient);
            return this;
        }

        public ShapedBuilder group(String group) {
            this.group = group;
            return this;
        }

        public ShapedBuilder category(@NotNull CraftingBookCategory category) {
            this.category = Objects.requireNonNull(category);
            return this;
        }

        public ShapedRecipe build() {
            return new ShapedRecipe(this.group, this.category, ShapedRecipePattern.of(this.map, this.pattern), this.result);
        }
    }

    public static final class ShapelessBuilder implements CraftingBuilder {
        private final NonNullList<Ingredient> inputs = NonNullList.withSize(9, Ingredient.EMPTY);
        private final ItemStack result;
        private CraftingBookCategory category = CraftingBookCategory.MISC;
        private String group = null;

        private ShapelessBuilder(ItemStack result) {
            this.result = result;
        }

        public static ShapelessBuilder of(ItemLike result, int count) {
            return of(new ItemStack(result, count));
        }

        public static ShapelessBuilder of(ItemStack stack) {
            return new ShapelessBuilder(stack);
        }

        @Override
        public ShapelessBuilder category(@NotNull CraftingBookCategory category) {
            this.category = Objects.requireNonNull(category);
            return this;
        }

        @Override
        public ShapelessBuilder group(String group) {
            this.group = group;
            return this;
        }

        public ShapelessBuilder requires(TagKey<Item> tag) {
            return this.requires(Ingredient.of(tag));
        }

        public ShapelessBuilder requires(ItemLike item) {
            return this.requires(item, 1);
        }

        public ShapelessBuilder requires(ItemLike item, int quantity) {
            for (int i = 0; i < quantity; i++) {
                this.requires(Ingredient.of(item));
            }

            return this;
        }

        public ShapelessBuilder requires(Ingredient ingredient) {
            return this.requires(ingredient, 1);
        }

        public ShapelessBuilder requires(Ingredient ingredient, int quantity) {
            for (int i = 0; i < quantity; i++) {
                this.inputs.add(ingredient);
            }

            return this;
        }

        public ShapelessRecipe build() {
            return new ShapelessRecipe(this.group, this.category, result, inputs);
        }
    }
}

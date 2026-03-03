package net.myriantics.myrror.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public final class NamedIngredient {
    private final Ingredient ingredient;
    private final String name;

    private NamedIngredient(Ingredient ingredient, String name) {
        this.ingredient = ingredient;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Ingredient toIngredient() {
        return this.ingredient;
    }

    public NamedIngredient of(Ingredient ingredient, String name) {
        return new NamedIngredient(ingredient, name);
    }

    public static NamedIngredient fromTag(TagKey<Item> tag) {
        return new NamedIngredient(Ingredient.of(tag), tag.location().getPath());
    }

    public static NamedIngredient ofStacks(ItemStack... stacks) throws IllegalArgumentException {
        if (stacks.length == 0) {
            throw new IllegalArgumentException("Named Ingredient must have at least one stack defined.");
        } else {
            return new NamedIngredient(
                    Ingredient.of(stacks),
                    BuiltInRegistries.ITEM.getKey(stacks[0].getItem()).getPath()
            );
        }
    }

    public static NamedIngredient ofItems(ItemLike... items) throws IllegalArgumentException {
        if (items.length == 0) {
            throw new IllegalArgumentException("Named Ingredient must have at least one item defined.");
        } else {
            return new NamedIngredient(
                    Ingredient.of(items),
                    BuiltInRegistries.ITEM.getKey(items[0].asItem()).getPath()
            );
        }
    }
}

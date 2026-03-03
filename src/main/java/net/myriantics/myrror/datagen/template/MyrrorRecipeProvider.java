package net.myriantics.myrror.datagen.template;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.myriantics.myrror.MyrrorCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public final class MyrrorRecipeProvider extends RecipeProvider {
    private final HashMap<ResourceLocation, Integer> recipeIdOccurrencesMap = new HashMap<>();
    private final ArrayList<RecipeGenerator> generators = new ArrayList<>();
    private final String namespace;

    public MyrrorRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String namespace) {
        super(output, registriesFuture);
        this.namespace = namespace;
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        for (RecipeGenerator generator : this.generators) {
            generator.generate(this, exporter);
        }
    }

    public MyrrorRecipeProvider addProvider(RecipeGenerator generator) {
        this.generators.add(generator);
        return this;
    }

    void acceptRecipe(RecipeOutput exporter, ResourceLocation recipeId, Recipe<?> recipe) {

        ResourceLocation proposedId = null;

        // iterate through them all to check if theyre the same as the active recipe's id
        for (ResourceLocation potentiallySpentResourceLocation : recipeIdOccurrencesMap.keySet()) {
            // if there is a match, attach a discriminator to the end of the recipe id
            if (potentiallySpentResourceLocation.equals(recipeId)) {
                proposedId = recipeId.withPath(recipeId.getPath() + "_" + recipeIdOccurrencesMap.get(potentiallySpentResourceLocation));
                break;
            }
        }

        if (proposedId == null) {
            // if no duplicate recipe was found, add a new entry with the associated number of 1
            recipeIdOccurrencesMap.put(recipeId, 1);
        } else {
            // notify the dev of the recipe accomodation :)
            MyrrorCommon.LOGGER.info("Accommodated for duplicate recipe: " + recipeId);
            // if a duplicate recipe was found, increment the counter in the map
            recipeIdOccurrencesMap.put(recipeId, recipeIdOccurrencesMap.get(recipeId) + 1);
            // make sure to update the recipe id to include the discriminator
            recipeId = proposedId;
        }

        exporter.accept(recipeId, recipe, null);
    }

    public ResourceLocation computeRecipeIdentifier(String typeId, String path) {
        return ResourceLocation.fromNamespaceAndPath(this.namespace,typeId + "/" + path);
    }

    public interface RecipeGenerator {
        void generate(MyrrorRecipeProvider provider, RecipeOutput output);
    }
}

package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream

class CustomRecipeProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricRecipeProvider(packOutput, registryLookup) {
    override fun createRecipeProvider(
        registryLookup: HolderLookup.Provider,
        recipes: BootstrapContext<Recipe<*>>,
        advancements: BootstrapContext<Advancement>,
    ) = object : RecipeProvider(recipes, advancements) {
        private val exporter: RecipeOutput = BootstrapRecipeOutput(recipes, advancements)

        override fun buildRecipes() {
            shaped(RecipeCategory.MISC, CustomBlocks.DUNGEON_DEVICE.asItem())
                .pattern("ono")
                .pattern("nsn")
                .pattern("ono")
                .define('o', Items.OBSIDIAN)
                .define('n', Items.NETHERITE_INGOT)
                .define('s', Items.NETHER_STAR)
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(exporter)

            shaped(RecipeCategory.MISC, CustomBlocks.CRYSTAL_FORGE.asItem())
                .pattern("c")
                .pattern("w")
                .pattern("a")
                .define('c', CustomTags.CRYSTAL)
                .define('w', ItemTags.WOOL_CARPETS)
                .define('a', ItemTags.ANVIL)
                .unlockedBy("has_crystal", has(CustomTags.CRYSTAL))
                .save(exporter)

            dyedItem(CustomArmorItems.SUEDE_HELMET, "dyed_armor");
            dyedItem(CustomArmorItems.SUEDE_CHESTPLATE, "dyed_armor");
            dyedItem(CustomArmorItems.SUEDE_LEGGINGS, "dyed_armor");
            dyedItem(CustomArmorItems.SUEDE_BOOTS, "dyed_armor");
        }
    }

    override fun getName() = "MCEndgameRecipeProvider"

    /**
     * 26.3 collects recipes and advancements through bootstrap contexts instead of a RecipeOutput,
     * so this adapts the two contexts back to the RecipeOutput the recipe builders expect.
     */
    private class BootstrapRecipeOutput(
        private val recipes: BootstrapContext<Recipe<*>>,
        private val advancements: BootstrapContext<Advancement>,
    ) : RecipeOutput {
        override fun accept(key: ResourceKey<Recipe<*>>, recipe: Recipe<*>, advancement: AdvancementHolder?) {
            recipes.register(key, recipe)
            advancement?.register(advancements)
        }

        override fun advancement() = Advancement.Builder.recipeAdvancement()
            .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
            .requirements(AdvancementRequirements.Strategy.OR)

        override fun <S : Any> lookup(key: ResourceKey<out Registry<out S>>): HolderGetter<S> = recipes.lookup(key)

        override fun <S : Any> listContextElements(key: ResourceKey<out Registry<out S>>): Stream<Holder.Reference<S>> =
            recipes.listContextElements(key)
    }
}

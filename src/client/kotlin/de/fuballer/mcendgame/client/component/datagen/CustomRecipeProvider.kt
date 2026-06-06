package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class CustomRecipeProvider(
    packOutput: FabricPackOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
) : FabricRecipeProvider(packOutput, registryLookup) {
    override fun createRecipeProvider(
        registryLookup: HolderLookup.Provider,
        exporter: RecipeOutput,
    ) = object : RecipeProvider(registryLookup, exporter) {
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
}
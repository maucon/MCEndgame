package de.fuballer.mcendgame.client

import de.fuballer.mcendgame.client.component.datagen.*
import de.fuballer.mcendgame.main.component.biome.CustomBiomes
import de.fuballer.mcendgame.main.component.dimension.CustomDimensions
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries

object MCEndgameDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()

        pack.addProvider { dataOutput, registryLookup -> CustomBlockTagProvider(dataOutput, registryLookup) }
        pack.addProvider { dataOutput, registryLookup -> CustomItemTagProvider(dataOutput, registryLookup) }
        pack.addProvider { dataOutput, registryLookup -> CustomEntityTagProvider(dataOutput, registryLookup) }
        pack.addProvider { dataOutput, registryLookup -> CustomLootTableProvider(dataOutput, registryLookup) }
        pack.addProvider { dataOutput, _ -> CustomModelProvider(dataOutput) }
        pack.addProvider { dataOutput, _ -> CustomDamageTypeProvider(dataOutput) }
        pack.addProvider { dataOutput, registryLookUp -> CustomRecipeProvider(dataOutput, registryLookUp) }
        pack.addProvider { dataOutput, registryLookUp -> CustomDamageTypeTagProvider(dataOutput, registryLookUp) }
        pack.addProvider { dataOutput, registryLookUp -> CustomWorldGeneratorProvider(dataOutput, registryLookUp) }
        pack.addProvider { dataOutput, registryLookup -> CustomRegistryProvider(dataOutput, registryLookup) }
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder
            .add(Registries.BIOME, CustomBiomes::bootstrap)
            .add(Registries.DIMENSION_TYPE, CustomDimensions::bootstrap)
    }
}
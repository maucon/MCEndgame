package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.tags.EntityTypeTags
import java.util.concurrent.CompletableFuture

class CustomEntityTagProvider(
    dataOutput: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>,
) : FabricTagProvider.EntityTypeTagProvider(dataOutput, registriesFuture) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        valueLookupBuilder(EntityTypeTags.ARTHROPOD)
            .add(CustomEntities.ARACHNE)

        valueLookupBuilder(EntityTypeTags.ZOMBIES)
            .add(CustomEntities.BONECRUSHER)
    }
}
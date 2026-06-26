package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.tags.EntityTypeTags
import java.util.concurrent.CompletableFuture

class CustomEntityTagProvider(
    packOutput: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>,
) : FabricTagsProvider.EntityTypeTagsProvider(packOutput, registriesFuture) {
    override fun addTags(wrapperLookup: HolderLookup.Provider) {
        valueLookupBuilder(EntityTypeTags.ARTHROPOD)
            .add(CustomEntities.ARACHNE)

        valueLookupBuilder(EntityTypeTags.ZOMBIES)
            .add(CustomEntities.BONECRUSHER)

        valueLookupBuilder(EntityTypeTags.SKELETONS)
            .add(CustomEntities.SKELETON_MAGE)
    }
}
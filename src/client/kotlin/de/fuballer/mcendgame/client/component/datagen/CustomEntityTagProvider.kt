package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntityIds
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
        builder(EntityTypeTags.ARTHROPOD)
            .add(CustomEntityIds.ARACHNE)

        builder(EntityTypeTags.ZOMBIES)
            .add(CustomEntityIds.BONECRUSHER)

        builder(EntityTypeTags.SKELETONS)
            .add(CustomEntityIds.SKELETON_MAGE)
    }
}
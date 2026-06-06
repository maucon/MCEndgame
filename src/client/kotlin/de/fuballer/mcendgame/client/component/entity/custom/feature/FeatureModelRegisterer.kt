package de.fuballer.mcendgame.client.component.entity.custom.feature

import de.fuballer.mcendgame.client.component.entity.custom.feature.webbed.WebbedModel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry

@Injectable
object FeatureModelRegisterer {
    @Initializer
    fun register() {
        ModelLayerRegistry.registerModelLayer(
            WebbedModel.WEBBED_LAYER,
            WebbedModel::getTexturedModelData
        )
    }
}
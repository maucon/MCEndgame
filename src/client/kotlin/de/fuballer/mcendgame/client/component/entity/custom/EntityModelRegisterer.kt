package de.fuballer.mcendgame.client.component.entity.custom

import de.fuballer.mcendgame.client.component.entity.custom.entities.arachne.ArachneEntityModel
import de.fuballer.mcendgame.client.component.entity.custom.entities.arachne.ArachneRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.beakburn.BeakburnRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.beakburn.BeakburnRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.bonecrusher.BonecrusherRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.bonecrusher.BonecrusherRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.elf_duelist.ElfDuelistRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.elf_duelist.ElfDuelistRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.PortalRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.default_.DefaultPortalEntityModel
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.legacy.LegacyPortalEntityModel
import de.fuballer.mcendgame.client.component.entity.custom.entities.scarred_one.ScarredOneRenderState
import de.fuballer.mcendgame.client.component.entity.custom.entities.scarred_one.ScarredOneRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.spiderling.SpiderlingRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.swamp_golem.SwampGolemEntityModel
import de.fuballer.mcendgame.client.component.entity.custom.entities.swamp_golem.SwampGolemRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy.TrainingDummyEntityModel
import de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy.TrainingDummyRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.webhook.WebhookRenderer
import de.fuballer.mcendgame.client.component.entity.custom.entities.webshot.WebshotEntityModel
import de.fuballer.mcendgame.client.component.entity.custom.entities.webshot.WebshotRenderer
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.portal.Portals
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.EntityRenderers

@Injectable
object EntityModelRegisterer {
    @Initializer
    fun register() {
        EntityModelLayerRegistry.registerModelLayer(
            SwampGolemEntityModel.SWAMP_GOLEM,
            SwampGolemEntityModel::getTexturedModelData
        )
        EntityRenderers.register(CustomEntities.SWAMP_GOLEM, ::SwampGolemRenderer)

        EntityModelLayerRegistry.registerModelLayer(ArachneEntityModel.ARACHNE, ArachneEntityModel::getTexturedModelData)
        EntityRenderers.register(CustomEntities.ARACHNE, ::ArachneRenderer)

        EntityModelLayerRegistry.registerModelLayer(
            WebshotEntityModel.WEBSHOT,
            WebshotEntityModel::getTexturedModelData
        )
        EntityRenderers.register(CustomEntities.WEBSHOT, ::WebshotRenderer)

        EntityRenderers.register(CustomEntities.WEBHOOK, ::WebhookRenderer)

        EntityRenderers.register(CustomEntities.SPIDERLING, ::SpiderlingRenderer)

        EntityModelLayerRegistry.registerModelLayer(TrainingDummyEntityModel.TRAINING_DUMMY, TrainingDummyEntityModel::getTexturedModelData)
        EntityRenderers.register(CustomEntities.TRAINING_DUMMY) { context -> TrainingDummyRenderer(context, ModelLayers.ARMOR_STAND_ARMOR) }

        EntityRenderers.register(CustomEntities.BONECRUSHER) { state -> BonecrusherRenderer<BonecrusherRenderState>(state) }
        EntityRenderers.register(CustomEntities.ELF_DUELIST) { state -> ElfDuelistRenderer<ElfDuelistRenderState>(state) }
        EntityRenderers.register(CustomEntities.BEAKBURN) { state -> BeakburnRenderer<BeakburnRenderState>(state) }

        EntityRenderers.register(CustomEntities.SCARRED_ONE) { state -> ScarredOneRenderer<ScarredOneRenderState>(state) }

        EntityModelLayerRegistry.registerModelLayer(
            DefaultPortalEntityModel.PORTAL,
            DefaultPortalEntityModel::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            LegacyPortalEntityModel.PORTAL,
            LegacyPortalEntityModel::getTexturedModelData
        )
        EntityRenderers.register(Portals.ENTITY_TYPE, ::PortalRenderer)
    }
}
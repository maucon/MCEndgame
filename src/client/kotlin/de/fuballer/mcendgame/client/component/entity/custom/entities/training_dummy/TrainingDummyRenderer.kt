package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EquipmentModelData
import net.minecraft.util.Identifier

class TrainingDummyRenderer(
    context: EntityRendererFactory.Context,
    equipmentModelData: EquipmentModelData<EntityModelLayer>,
) : BipedEntityRenderer<TrainingDummyEntity, TrainingDummyRenderState, TrainingDummyEntityModel>(
    context,
    TrainingDummyEntityModel(context.getPart(TrainingDummyEntityModel.TRAINING_DUMMY)),
    0.0f,
) {
    init {
        addFeature(
            ArmorFeatureRenderer(
                this,
                EquipmentModelData.mapToEntityModel(equipmentModelData, context.entityModels, ::TrainingDummyEntityModel),
                context.equipmentRenderer,
            )
        )
    }

    companion object {
        val TEXTURE: Identifier = IdentifierUtil.default("textures/entity/training_dummy/training_dummy.png")
    }

    override fun getTexture(state: TrainingDummyRenderState) = TEXTURE

    override fun createRenderState() = TrainingDummyRenderState()
}
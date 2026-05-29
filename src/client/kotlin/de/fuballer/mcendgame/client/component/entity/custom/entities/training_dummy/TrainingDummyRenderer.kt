package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.render.command.OrderedRenderCommandQueue
import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EquipmentModelData
import net.minecraft.client.render.state.CameraRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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

        private const val TEXT_OFFSET = -0.3
        private const val TEXT_SPACING = 0.28
        private val FORMAT = DecimalFormat().apply {
            minimumFractionDigits = 1
            maximumFractionDigits = 1
            decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
            isGroupingUsed = false
        }
    }

    override fun getTexture(state: TrainingDummyRenderState) = TEXTURE

    override fun createRenderState() = TrainingDummyRenderState()

    override fun updateRenderState(dummy: TrainingDummyEntity, state: TrainingDummyRenderState, tickDelta: Float) {
        super.updateRenderState(dummy, state, tickDelta)

        if (!dummy.dataTracker.get(TrainingDummyEntity.DAMAGE_ACTIVE)) return
        state.damageActive = true
        state.lastDamage = dummy.dataTracker.get(TrainingDummyEntity.LAST_DAMAGE)
        state.highestDamage = dummy.dataTracker.get(TrainingDummyEntity.HIGHEST_DAMAGE)
        state.damageSum = dummy.dataTracker.get(TrainingDummyEntity.DAMAGE_SUM)
        state.damagePerSecond = dummy.dataTracker.get(TrainingDummyEntity.DAMAGE_PER_SECOND)
        state.damageDuration = dummy.dataTracker.get(TrainingDummyEntity.DAMAGE_DURATION) + tickDelta
    }

    override fun render(
        state: TrainingDummyRenderState,
        matrices: MatrixStack,
        queue: OrderedRenderCommandQueue,
        cameraRenderState: CameraRenderState
    ) {
        super.render(state, matrices, queue, cameraRenderState)

        if (!state.damageActive) return

        val key = "training_dummy."
        val texts = listOf(
            Text.empty()
                .append(Text.translatable("${key}damage_per_second").formatted(Formatting.DARK_RED, Formatting.BOLD))
                .append(
                    Text.literal(FORMAT.format(state.damagePerSecond))
                        .formatted(Formatting.RED, Formatting.BOLD)
                ),

            Text.empty()
                .append(Text.translatable("${key}last_damage").formatted(Formatting.GRAY))
                .append(
                    Text.literal(FORMAT.format(state.lastDamage))
                        .formatted(Formatting.WHITE)
                ),

            Text.empty()
                .append(Text.translatable("${key}highest_damage").formatted(Formatting.GRAY))
                .append(
                    Text.literal(FORMAT.format(state.highestDamage))
                        .formatted(Formatting.GOLD)
                ),

            Text.empty()
                .append(Text.translatable("${key}damage_sum").formatted(Formatting.GRAY))
                .append(
                    Text.literal(FORMAT.format(state.damageSum))
                        .formatted(Formatting.WHITE)
                ),

            Text.empty()
                .append(Text.translatable("${key}damage_duration").formatted(Formatting.GRAY))
                .append(
                    Text.literal("${FORMAT.format(state.damageDuration / 20f)}s")
                        .formatted(Formatting.AQUA)
                ),
        )

        texts.forEachIndexed { index, text ->
            val labelPos = Vec3d(0.0, state.height + TEXT_OFFSET + (texts.size - index) * TEXT_SPACING, 0.0)
            queue.submitLabel(matrices, labelPos, 0, text, true, state.light, state.squaredDistanceToCamera, cameraRenderState)
        }
    }
}
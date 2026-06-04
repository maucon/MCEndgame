package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import com.mojang.blaze3d.vertex.PoseStack
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.ChatFormatting
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.ArmorModelSet
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HumanoidMobRenderer
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.phys.Vec3
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class TrainingDummyRenderer(
    context: EntityRendererProvider.Context,
    equipmentModelData: ArmorModelSet<ModelLayerLocation>,
) : HumanoidMobRenderer<TrainingDummyEntity, TrainingDummyRenderState, TrainingDummyEntityModel>(
    context,
    TrainingDummyEntityModel(context.bakeLayer(TrainingDummyEntityModel.TRAINING_DUMMY)),
    0.0f,
) {
    init {
        addLayer(
            HumanoidArmorLayer(
                this,
                ArmorModelSet.bake(equipmentModelData, context.modelSet, ::TrainingDummyEntityModel),
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

    override fun getTextureLocation(state: TrainingDummyRenderState) = TEXTURE

    override fun createRenderState() = TrainingDummyRenderState()

    override fun extractRenderState(dummy: TrainingDummyEntity, state: TrainingDummyRenderState, tickDelta: Float) {
        super.extractRenderState(dummy, state, tickDelta)

        if (!dummy.entityData.get(TrainingDummyEntity.DAMAGE_ACTIVE)) return
        state.damageActive = true
        state.lastDamage = dummy.entityData.get(TrainingDummyEntity.LAST_DAMAGE)
        state.highestDamage = dummy.entityData.get(TrainingDummyEntity.HIGHEST_DAMAGE)
        state.damageSum = dummy.entityData.get(TrainingDummyEntity.DAMAGE_SUM)
        state.damagePerSecond = dummy.entityData.get(TrainingDummyEntity.DAMAGE_PER_SECOND)
        state.damageDuration = dummy.entityData.get(TrainingDummyEntity.DAMAGE_DURATION) + tickDelta
    }

    override fun submit(
        state: TrainingDummyRenderState,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        cameraRenderState: CameraRenderState
    ) {
        super.submit(state, matrices, queue, cameraRenderState)

        if (!state.damageActive) return

        val key = "training_dummy."
        val texts = listOf(
            Component.empty()
                .append(Component.translatable("${key}damage_per_second").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD))
                .append(
                    Component.literal(FORMAT.format(state.damagePerSecond))
                        .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
                ),

            Component.empty()
                .append(Component.translatable("${key}last_damage").withStyle(ChatFormatting.GRAY))
                .append(
                    Component.literal(FORMAT.format(state.lastDamage))
                        .withStyle(ChatFormatting.WHITE)
                ),

            Component.empty()
                .append(Component.translatable("${key}highest_damage").withStyle(ChatFormatting.GRAY))
                .append(
                    Component.literal(FORMAT.format(state.highestDamage))
                        .withStyle(ChatFormatting.GOLD)
                ),

            Component.empty()
                .append(Component.translatable("${key}damage_sum").withStyle(ChatFormatting.GRAY))
                .append(
                    Component.literal(FORMAT.format(state.damageSum))
                        .withStyle(ChatFormatting.WHITE)
                ),

            Component.empty()
                .append(Component.translatable("${key}damage_duration").withStyle(ChatFormatting.GRAY))
                .append(
                    Component.literal("${FORMAT.format(state.damageDuration / 20f)}s")
                        .withStyle(ChatFormatting.AQUA)
                ),
        )

        texts.forEachIndexed { index, text ->
            val labelPos = Vec3(0.0, state.boundingBoxHeight + TEXT_OFFSET + (texts.size - index) * TEXT_SPACING, 0.0)
            queue.submitNameTag(matrices, labelPos, 0, text, true, state.lightCoords, state.distanceToCameraSq, cameraRenderState)
        }
    }
}
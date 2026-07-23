package de.fuballer.mcendgame.client.component.render.link

import com.mojang.blaze3d.vertex.VertexConsumer
import de.fuballer.mcendgame.client.component.entity.custom.data.EntityConnectionPointData
import de.fuballer.mcendgame.client.component.entity.custom.data.MultipleEntityConnectionData
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers
import de.fuballer.mcendgame.client.messaging.CollectRenderSubmitsCommand
import de.fuballer.mcendgame.main.accessor.LivingEntityLinkAttributeAccessor
import de.fuballer.mcendgame.main.component.custom_attribute.effects.link.LinkSettings
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.util.LightCoordsUtil
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Injectable
class LinkRenderService {
    @CommandHandler
    fun on(cmd: CollectRenderSubmitsCommand) {
        val context = cmd.context

        val client = Minecraft.getInstance()
        val cameraPos = context.gameRenderer().mainCamera().position()
        val tickDelta = client.deltaTracker.getGameTimeDeltaPartialTick(false)
        val player = client.player
        val firstPerson = client.options.cameraType.isFirstPerson

        val entities = client.level?.entitiesForRendering()?.filterIsInstance<LivingEntity>() ?: return
        entities.forEach { renderPotentialLinks(it, tickDelta, context, cameraPos, player, firstPerson) }
    }

    private fun renderPotentialLinks(
        entity: LivingEntity,
        tickDelta: Float,
        context: LevelRenderContext,
        cameraPos: Vec3,
        player: LocalPlayer?,
        firstPerson: Boolean,
    ) {
        val data = getLinkData(entity, tickDelta) ?: return

        if (firstPerson && entity == player) {
            val yawRadians = Math.toRadians(player.getYRot(tickDelta).toDouble())
            val yawVector = Vec3(-sin(yawRadians), 0.0, cos(yawRadians)).normalize()
            val offsetStrength = abs(player.getXRot(tickDelta)) / 90
            val linkOriginOffset = yawVector.scale(-0.5 * offsetStrength)
            data.originEntity.pos = data.originEntity.pos.add(linkOriginOffset)
            data.offset = data.offset.add(linkOriginOffset)
        }

        val cameraOffset = entity.getPosition(tickDelta).subtract(cameraPos)
        data.offset = data.offset.add(cameraOffset)

        val age = entity.tickCount + tickDelta

        renderLinks(context, data, age)
    }

    private fun getLinkData(entity: LivingEntity, tickDelta: Float): MultipleEntityConnectionData? {
        val linkedEntities = (entity as? LivingEntityLinkAttributeAccessor)?.`mcendgame$getLinkedEntities`() ?: return null
        if (linkedEntities.isEmpty()) return null

        val data = MultipleEntityConnectionData()
        data.offset = Vec3(0.0, entity.bbHeight * LinkSettings.LINK_CONNECTION_HEIGHT, 0.0)
        data.originEntity = getMainEntityConnectionPoint(entity, tickDelta, entity.level())
        data.connectedEntities = getLinkedEntitiesConnectionPoints(linkedEntities, tickDelta, entity.level())

        return data
    }

    private fun getMainEntityConnectionPoint(
        entity: Entity,
        tickDelta: Float,
        world: Level,
    ): EntityConnectionPointData {
        val entityData = EntityConnectionPointData()
        entityData.pos = entity.getPosition(tickDelta).add(0.0, entity.bbHeight * LinkSettings.LINK_CONNECTION_HEIGHT, 0.0)

        val blockPos = BlockPos.containing(entity.getEyePosition(tickDelta))
        entityData.blockLight = world.getBrightness(LightLayer.BLOCK, blockPos)
        entityData.skyLight = world.getBrightness(LightLayer.SKY, blockPos)

        return entityData
    }

    private fun getLinkedEntitiesConnectionPoints(
        entities: Map<UUID, Long>,
        tickDelta: Float,
        world: Level,
    ): List<EntityConnectionPointData> {
        val data = mutableListOf<EntityConnectionPointData>()

        val currentTime = world.gameTime
        for ((uuid, connectionTime) in entities) {
            val entity = world.getEntity(uuid) ?: continue

            val entityData = getMainEntityConnectionPoint(entity, tickDelta, world)
            entityData.connectionDuration = currentTime + tickDelta - connectionTime

            data.add(entityData)
        }

        return data
    }

    private fun renderLinks(
        context: LevelRenderContext,
        data: MultipleEntityConnectionData,
        age: Float,
    ) {
        val poseStack = context.poseStack()

        poseStack.pushPose()
        poseStack.translate(data.offset)

        context.submitNodeCollector().submitCustomGeometry(poseStack, CustomRenderLayers.LINK) { entry, vertexConsumer ->
            data.connectedEntities.forEach {
                renderLink(entry.pose(), vertexConsumer, data.originEntity, it, age)
            }
        }

        poseStack.popPose()
    }

    private fun renderLink(
        matrix: Matrix4f,
        vertexConsumer: VertexConsumer,
        origin: EntityConnectionPointData,
        linked: EntityConnectionPointData,
        age: Float,
    ) {
        val targetDistanceVector = linked.pos.subtract(origin.pos)
        val targetDistance = targetDistanceVector.length()
        val distancePercent = (linked.connectionDuration.toDouble() / LinkSettings.getLinkConnectingTime(targetDistance)).coerceAtMost(1.0)
        val linkDistance = targetDistanceVector.scale(distancePercent)
        val segmentCount = linkDistance.length() / LinkSettings.LINK_RENDER_SEGMENT_LENGTH

        val perpendicularVector = linkDistance.horizontal().yRot(Math.toRadians(90.0).toFloat()).normalize()

        val linkLength = linkDistance.length()

        val vertexData = mutableListOf<LinkVertexData>()
        for (i in 0..segmentCount.toInt() + 1) {
            var vertexDistance = i * LinkSettings.LINK_RENDER_SEGMENT_LENGTH
            if (i > segmentCount + 1) vertexDistance -= LinkSettings.LINK_RENDER_SEGMENT_LENGTH * (1 - segmentCount % 1)
            var vertexPos = linkDistance.scale(vertexDistance / linkLength)

            val vertexTargetDistancePercentage = vertexDistance / targetDistanceVector.length()
            val flatteningSineStrength = sin(Math.PI * vertexTargetDistancePercentage)
            val flattenedSine = sin(vertexDistance - age * LinkSettings.LINK_RENDER_SINE_SPEED) * flatteningSineStrength

            val verticalOffset = flattenedSine * LinkSettings.LINK_RENDER_SINE_VERTICAL_STRENGTH
            vertexPos = vertexPos.add(0.0, verticalOffset, 0.0)
            val horizontalOffset = flattenedSine * LinkSettings.LINK_RENDER_SINE_HORIZONTAL_STRENGTH
            vertexPos = vertexPos.add(perpendicularVector.scale(horizontalOffset))

            val thicknessFactor = 1 + (abs(flattenedSine) * LinkSettings.LINK_RENDER_MAX_THICKNESS_FACTOR)

            val distancePercentage = vertexDistance / targetDistance
            val color = LinkSettings.getColor(distancePercentage)

            val blockLight = Mth.lerpInt(distancePercentage.toFloat(), origin.blockLight, linked.blockLight)
            val skyLight = Mth.lerpInt(distancePercentage.toFloat(), origin.skyLight, linked.skyLight)
            val light = LightCoordsUtil.pack(blockLight, skyLight)

            vertexData.add(LinkVertexData(vertexPos, color, light, thicknessFactor))
        }

        val widthOffset = perpendicularVector.scale(LinkSettings.LINK_RENDER_SEGMENT_WIDTH)

        vertexData.forEach { data -> addVertices(vertexConsumer, matrix, data, widthOffset, false) }
        vertexData.reversed().forEach { data -> addVertices(vertexConsumer, matrix, data, widthOffset, true) }
    }

    private fun addVertices(
        vertexConsumer: VertexConsumer,
        matrix: Matrix4f,
        data: LinkVertexData,
        widthOffset: Vec3,
        reverse: Boolean,
    ) {
        val pos = data.pos
        val color = data.color
        val light = data.light

        val thickness = data.thicknessFactor
        val heightOffset = (if (reverse) LinkSettings.LINK_RENDER_SEGMENT_WIDTH else -LinkSettings.LINK_RENDER_SEGMENT_WIDTH) * thickness
        val scaledWidthOffset = widthOffset.scale(thickness)

        val vec1 = pos.add(scaledWidthOffset).add(0.0, heightOffset, 0.0)
        vertexConsumer.addVertex(matrix, vec1.x.toFloat(), vec1.y.toFloat(), vec1.z.toFloat())
            .setColor(color).setLight(light)

        val vec2 = pos.subtract(scaledWidthOffset).subtract(0.0, heightOffset, 0.0)
        vertexConsumer.addVertex(matrix, vec2.x.toFloat(), vec2.y.toFloat(), vec2.z.toFloat())
            .setColor(color).setLight(light)
    }
}
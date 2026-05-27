package de.fuballer.mcendgame.client.component.item.custom.armor

import de.fuballer.mcendgame.client.component.item.custom.armor.model.abyssal_mask.AbyssalMaskModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.bound_abyss.BoundAbyssModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.broodmother.BroodmotherModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsBootsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsChestplateModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsHelmetModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsLeggingsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.emberchant.EmberchantModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.emberreign.EmberreignModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.geistergaloschen.GeistergaloschenModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.gilded_tempest.GildedTempestModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.iceborne.IceborneModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.lamias_gift.LamiasGiftModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.moonshadow.MoonshadowModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.stoneward.StonewardModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeBootsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeChestplateModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeHelmetModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeLeggingsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.voidweaver.VoidweaverModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.windstrider.WindstriderModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseBootsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseChestplateModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseHelmetModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseLeggingsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.transformer.EntityArmorTransformer
import de.fuballer.mcendgame.client.component.item.custom.armor.transformer.PiglinArmorTransformer
import de.fuballer.mcendgame.client.util.BipedEntityRenderStateMixinExtension.getHiddenArmor
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.Model
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.command.OrderedRenderCommandQueue
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.state.BipedEntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class CustomHumanoidArmorFeatureRenderer<S : BipedEntityRenderState, M : BipedEntityModel<S>>(
    featureContext: FeatureRendererContext<S, M>,
    ctx: EntityRendererFactory.Context,
) : FeatureRenderer<S, M>(featureContext) {
    private val armorTransformers: Map<EntityType<out Entity>, EntityArmorTransformer> = mapOf(
        EntityType.PIGLIN to PiglinArmorTransformer(),
        EntityType.PIGLIN_BRUTE to PiglinArmorTransformer(),
        EntityType.ZOMBIFIED_PIGLIN to PiglinArmorTransformer(),
    )

    private val texturedArmorModels: MutableMap<Item, TexturedArmorModel<BipedEntityModel<S>>> = mutableMapOf()

    init {
        texturedArmorModels[CustomArmorItems.ICEBORNE] = TexturedArmorModel(
            { IceborneModel(ctx.getPart(IceborneModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/iceborne.png"),
        )
        texturedArmorModels[CustomArmorItems.BOUND_ABYSS] = TexturedArmorModel(
            { BoundAbyssModel(ctx.getPart(BoundAbyssModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/bound_abyss.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_HELMET] = TexturedArmorModel(
            { DruidsHelmetModel(ctx.getPart(DruidsHelmetModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_CHESTPLATE] = TexturedArmorModel(
            { DruidsChestplateModel(ctx.getPart(DruidsChestplateModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_LEGGINGS] = TexturedArmorModel(
            { DruidsLeggingsModel(ctx.getPart(DruidsLeggingsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_BOOTS] = TexturedArmorModel(
            { DruidsBootsModel(ctx.getPart(DruidsBootsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.EMBERCHANT] = TexturedArmorModel(
            { EmberchantModel(ctx.getPart(EmberchantModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/emberchant.png"),
        )
        texturedArmorModels[CustomArmorItems.LAMIAS_GIFT] = TexturedArmorModel(
            { LamiasGiftModel(ctx.getPart(LamiasGiftModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/lamias_gift.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_HELMET] = TexturedArmorModel(
            { WitherRoseHelmetModel(ctx.getPart(WitherRoseHelmetModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_CHESTPLATE] = TexturedArmorModel(
            { WitherRoseChestplateModel(ctx.getPart(WitherRoseChestplateModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_LEGGINGS] = TexturedArmorModel(
            { WitherRoseLeggingsModel(ctx.getPart(WitherRoseLeggingsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_BOOTS] = TexturedArmorModel(
            { WitherRoseBootsModel(ctx.getPart(WitherRoseBootsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.SUEDE_HELMET] = TexturedArmorModel(
            { SuedeHelmetModel(ctx.getPart(SuedeHelmetModel.MODEL_LAYER)) },
            colorAbleTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = ColorUtil.rgbaToInt(160, 101, 64, 255),
        )
        texturedArmorModels[CustomArmorItems.SUEDE_CHESTPLATE] = TexturedArmorModel(
            { SuedeChestplateModel(ctx.getPart(SuedeChestplateModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede.png"),
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = ColorUtil.rgbaToInt(160, 101, 64, 255),
        )
        texturedArmorModels[CustomArmorItems.SUEDE_LEGGINGS] = TexturedArmorModel(
            { SuedeLeggingsModel(ctx.getPart(SuedeLeggingsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede.png"),
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = ColorUtil.rgbaToInt(160, 101, 64, 255),
        )
        texturedArmorModels[CustomArmorItems.SUEDE_BOOTS] = TexturedArmorModel(
            { SuedeBootsModel(ctx.getPart(SuedeBootsModel.MODEL_LAYER)) },
            colorAbleTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = ColorUtil.rgbaToInt(160, 101, 64, 255),
        )
        texturedArmorModels[CustomArmorItems.STONEWARD] = TexturedArmorModel(
            { StonewardModel(ctx.getPart(StonewardModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/stoneward.png"),
        )
        texturedArmorModels[CustomArmorItems.MOONSHADOW] = TexturedArmorModel(
            { MoonshadowModel(ctx.getPart(MoonshadowModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/moonshadow.png"),
        )
        texturedArmorModels[CustomArmorItems.GEISTERGALOSCHEN] = TexturedArmorModel(
            { GeistergaloschenModel(ctx.getPart(GeistergaloschenModel.MODEL_LAYER)) },
            translucentTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/geistergaloschen.png"),
        )
        texturedArmorModels[CustomArmorItems.VOIDWEAVER] = TexturedArmorModel(
            { VoidweaverModel(ctx.getPart(VoidweaverModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/voidweaver.png"),
        )
        texturedArmorModels[CustomArmorItems.ABYSSAL_MASK] = TexturedArmorModel(
            { AbyssalMaskModel(ctx.getPart(AbyssalMaskModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/abyssal_mask.png"),
        )
        texturedArmorModels[CustomArmorItems.GILDED_TEMPEST] = TexturedArmorModel(
            { GildedTempestModel(ctx.getPart(GildedTempestModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/gilded_tempest.png"),
        )
        texturedArmorModels[CustomArmorItems.WINDSTRIDER] = TexturedArmorModel(
            { WindstriderModel(ctx.getPart(WindstriderModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/windstrider.png"),
        )
        texturedArmorModels[CustomArmorItems.BROODMOTHER] = TexturedArmorModel(
            { BroodmotherModel(ctx.getPart(BroodmotherModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/broodmother.png"),
            emissiveTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/broodmother_emissive.png"),
        )
        texturedArmorModels[CustomArmorItems.EMBERREIGN] = TexturedArmorModel(
            { EmberreignModel(ctx.getPart(EmberreignModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/emberreign.png"),
        )
    }

    private fun renderArmor(
        bipedEntityRenderState: S,
        matrices: MatrixStack,
        queue: OrderedRenderCommandQueue,
        itemStack: ItemStack,
        light: Int,
        slot: EquipmentSlot,
    ) {
        // Note: rendering leggings and boots on endermen is disabled
        if (bipedEntityRenderState.entityType == EntityType.ENDERMAN && (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET)) return

        val item = itemStack.item
        val texturedArmorModel = texturedArmorModels[item] ?: return

        val model = texturedArmorModel.modelProvider()
        model.copyTransforms(contextModel)

        matrices.push()
        armorTransformers[bipedEntityRenderState.entityType]?.transform(slot, matrices)

        if (texturedArmorModel.texture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderLayers.armorCutoutNoCull(texturedArmorModel.texture),
                matrices,
                queue,
                light,
                itemStack.hasGlint(),
            )
        }

        if (texturedArmorModel.colorAbleTexture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderLayers.armorCutoutNoCull(texturedArmorModel.colorAbleTexture),
                matrices,
                queue,
                light,
                itemStack.hasGlint(),
                color = DyedColorComponent.getColor(itemStack, texturedArmorModel.defaultColor),
            )
        }

        if (texturedArmorModel.translucentTexture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderLayers.entityTranslucent(texturedArmorModel.translucentTexture),
                matrices,
                queue,
                light,
                itemStack.hasGlint()
            )
        }

        if (texturedArmorModel.emissiveTexture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderLayers.eyes(texturedArmorModel.emissiveTexture),
                matrices,
                queue,
                LightmapTextureManager.MAX_LIGHT_COORDINATE,
                itemStack.hasGlint(),
            )
        }

        matrices.pop()
    }

    private fun renderModel(
        state: S,
        model: Model<S>,
        renderLayer: RenderLayer,
        matrices: MatrixStack,
        queue: OrderedRenderCommandQueue,
        light: Int,
        glint: Boolean,
        color: Int = -1,
    ) {
        /*
        if (model is CustomVertexConsumer) {
            vertexConsumer = model.getVertexConsumer(bipedEntityRenderState, vertexConsumerProvider, vertexConsumer)
        }
        */
        queue.submitModel(model, state, matrices, renderLayer, light, OverlayTexture.DEFAULT_UV, color, null, state.outlineColor, null)
        if (glint) queue.submitModel(model, state, matrices, RenderLayers.armorEntityGlint(), light, OverlayTexture.DEFAULT_UV, color, null, state.outlineColor, null)
    }

    override fun render(
        matrixStack: MatrixStack,
        queue: OrderedRenderCommandQueue,
        light: Int,
        bipedEntityRenderState: S,
        limbAngle: Float,
        limbDistance: Float,
    ) {
        val hiddenArmor = bipedEntityRenderState.getHiddenArmor()

        if (!hiddenArmor.contains(EquipmentSlot.HEAD)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.equippedHeadStack,
                light,
                EquipmentSlot.HEAD,
            )
        }
        if (!hiddenArmor.contains(EquipmentSlot.CHEST)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.equippedChestStack,
                light,
                EquipmentSlot.CHEST,
            )
        }
        if (!hiddenArmor.contains(EquipmentSlot.LEGS)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.equippedLegsStack,
                light,
                EquipmentSlot.LEGS,
            )
        }
        if (!hiddenArmor.contains(EquipmentSlot.FEET)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.equippedFeetStack,
                light,
                EquipmentSlot.FEET,
            )
        }
    }
}
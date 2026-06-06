package de.fuballer.mcendgame.client.component.item.custom.armor

import com.mojang.blaze3d.vertex.PoseStack
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
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers
import de.fuballer.mcendgame.client.util.BipedEntityRenderStateMixinExtension.getHiddenArmor
import de.fuballer.mcendgame.client.util.EntityRenderStateMixinExtension.getLowHealthTicks
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.LightCoordsUtil
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor

class CustomHumanoidArmorFeatureRenderer<S : HumanoidRenderState, M : HumanoidModel<S>>(
    featureContext: RenderLayerParent<S, M>,
    ctx: EntityRendererProvider.Context,
) : RenderLayer<S, M>(featureContext) {
    private val armorTransformers: Map<EntityType<out Entity>, EntityArmorTransformer> = mapOf(
        EntityType.PIGLIN to PiglinArmorTransformer(),
        EntityType.PIGLIN_BRUTE to PiglinArmorTransformer(),
        EntityType.ZOMBIFIED_PIGLIN to PiglinArmorTransformer(),
    )

    private val texturedArmorModels: MutableMap<Item, TexturedArmorModel<HumanoidModel<S>>> = mutableMapOf()

    init {
        texturedArmorModels[CustomArmorItems.ICEBORNE] = TexturedArmorModel(
            { IceborneModel(ctx.bakeLayer(IceborneModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/iceborne.png"),
        )
        texturedArmorModels[CustomArmorItems.BOUND_ABYSS] = TexturedArmorModel(
            { BoundAbyssModel(ctx.bakeLayer(BoundAbyssModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/bound_abyss.png"),
            specialTextures = listOf(
                TexturedArmorModel.SpecialRenderTypeArmorTexture(
                    IdentifierUtil.default("textures/entity/equipment/custom_humanoid/bound_abyss_gold.png"),
                    { CustomRenderLayers.boundAbyss(it) },
                    { state, _ ->
                        val strength = (state.getLowHealthTicks() / 20f * 255).toInt().coerceIn(0, 255)
                        ColorUtil.rgbaToInt(strength, 0, 0, 0)
                    }
                )
            ),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_HELMET] = TexturedArmorModel(
            { DruidsHelmetModel(ctx.bakeLayer(DruidsHelmetModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_CHESTPLATE] = TexturedArmorModel(
            { DruidsChestplateModel(ctx.bakeLayer(DruidsChestplateModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_LEGGINGS] = TexturedArmorModel(
            { DruidsLeggingsModel(ctx.bakeLayer(DruidsLeggingsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.DRUIDS_BOOTS] = TexturedArmorModel(
            { DruidsBootsModel(ctx.bakeLayer(DruidsBootsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/druids.png"),
        )
        texturedArmorModels[CustomArmorItems.EMBERCHANT] = TexturedArmorModel(
            { EmberchantModel(ctx.bakeLayer(EmberchantModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/emberchant.png"),
        )
        texturedArmorModels[CustomArmorItems.LAMIAS_GIFT] = TexturedArmorModel(
            { LamiasGiftModel(ctx.bakeLayer(LamiasGiftModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/lamias_gift.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_HELMET] = TexturedArmorModel(
            { WitherRoseHelmetModel(ctx.bakeLayer(WitherRoseHelmetModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_CHESTPLATE] = TexturedArmorModel(
            { WitherRoseChestplateModel(ctx.bakeLayer(WitherRoseChestplateModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_LEGGINGS] = TexturedArmorModel(
            { WitherRoseLeggingsModel(ctx.bakeLayer(WitherRoseLeggingsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.WITHER_ROSE_BOOTS] = TexturedArmorModel(
            { WitherRoseBootsModel(ctx.bakeLayer(WitherRoseBootsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/wither_rose.png"),
        )
        texturedArmorModels[CustomArmorItems.SUEDE_HELMET] = TexturedArmorModel(
            { SuedeHelmetModel(ctx.bakeLayer(SuedeHelmetModel.MODEL_LAYER)) },
            colorAbleTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = DyedItemColor.LEATHER_COLOR,
        )
        texturedArmorModels[CustomArmorItems.SUEDE_CHESTPLATE] = TexturedArmorModel(
            { SuedeChestplateModel(ctx.bakeLayer(SuedeChestplateModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede.png"),
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = DyedItemColor.LEATHER_COLOR,
        )
        texturedArmorModels[CustomArmorItems.SUEDE_LEGGINGS] = TexturedArmorModel(
            { SuedeLeggingsModel(ctx.bakeLayer(SuedeLeggingsModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede.png"),
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = DyedItemColor.LEATHER_COLOR,
        )
        texturedArmorModels[CustomArmorItems.SUEDE_BOOTS] = TexturedArmorModel(
            { SuedeBootsModel(ctx.bakeLayer(SuedeBootsModel.MODEL_LAYER)) },
            colorAbleTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/suede_color_able.png"),
            defaultColor = DyedItemColor.LEATHER_COLOR,
        )
        texturedArmorModels[CustomArmorItems.STONEWARD] = TexturedArmorModel(
            { StonewardModel(ctx.bakeLayer(StonewardModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/stoneward.png"),
        )
        texturedArmorModels[CustomArmorItems.MOONSHADOW] = TexturedArmorModel(
            { MoonshadowModel(ctx.bakeLayer(MoonshadowModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/moonshadow.png"),
        )
        texturedArmorModels[CustomArmorItems.GEISTERGALOSCHEN] = TexturedArmorModel(
            { GeistergaloschenModel(ctx.bakeLayer(GeistergaloschenModel.MODEL_LAYER)) },
            translucentTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/geistergaloschen.png"),
        )
        texturedArmorModels[CustomArmorItems.VOIDWEAVER] = TexturedArmorModel(
            { VoidweaverModel(ctx.bakeLayer(VoidweaverModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/voidweaver.png"),
        )
        texturedArmorModels[CustomArmorItems.ABYSSAL_MASK] = TexturedArmorModel(
            { AbyssalMaskModel(ctx.bakeLayer(AbyssalMaskModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/abyssal_mask.png"),
        )
        texturedArmorModels[CustomArmorItems.GILDED_TEMPEST] = TexturedArmorModel(
            { GildedTempestModel(ctx.bakeLayer(GildedTempestModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/gilded_tempest.png"),
        )
        texturedArmorModels[CustomArmorItems.WINDSTRIDER] = TexturedArmorModel(
            { WindstriderModel(ctx.bakeLayer(WindstriderModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/windstrider.png"),
        )
        texturedArmorModels[CustomArmorItems.BROODMOTHER] = TexturedArmorModel(
            { BroodmotherModel(ctx.bakeLayer(BroodmotherModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/broodmother.png"),
            emissiveTexture = IdentifierUtil.default("textures/entity/equipment/custom_humanoid/broodmother_emissive.png"),
        )
        texturedArmorModels[CustomArmorItems.EMBERREIGN] = TexturedArmorModel(
            { EmberreignModel(ctx.bakeLayer(EmberreignModel.MODEL_LAYER)) },
            IdentifierUtil.default("textures/entity/equipment/custom_humanoid/emberreign.png"),
        )
    }

    private fun renderArmor(
        bipedEntityRenderState: S,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        itemStack: ItemStack,
        light: Int,
        slot: EquipmentSlot,
    ) {
        // Note: rendering leggings and boots on endermen is disabled
        if (bipedEntityRenderState.entityType == EntityType.ENDERMAN && (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET)) return

        val item = itemStack.item
        val texturedArmorModel = texturedArmorModels[item] ?: return

        val model = texturedArmorModel.modelProvider()
        model.copyTransforms(parentModel)

        matrices.pushPose()
        armorTransformers[bipedEntityRenderState.entityType]?.transform(slot, matrices)

        if (texturedArmorModel.texture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderTypes.armorCutoutNoCull(texturedArmorModel.texture),
                matrices,
                queue,
                light,
                itemStack.hasFoil(),
            )
        }

        if (texturedArmorModel.colorAbleTexture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderTypes.armorCutoutNoCull(texturedArmorModel.colorAbleTexture),
                matrices,
                queue,
                light,
                itemStack.hasFoil(),
                color = DyedItemColor.getOrDefault(itemStack, texturedArmorModel.defaultColor),
            )
        }

        if (texturedArmorModel.translucentTexture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderTypes.entityTranslucent(texturedArmorModel.translucentTexture),
                matrices,
                queue,
                light,
                itemStack.hasFoil()
            )
        }

        if (texturedArmorModel.emissiveTexture != null) {
            renderModel(
                bipedEntityRenderState,
                model,
                RenderTypes.eyes(texturedArmorModel.emissiveTexture),
                matrices,
                queue,
                LightCoordsUtil.FULL_BRIGHT,
                itemStack.hasFoil(),
            )
        }

        texturedArmorModel.specialTextures.forEach { (identifier, renderLayer, color) ->
            renderModel(
                bipedEntityRenderState,
                model,
                renderLayer(identifier),
                matrices,
                queue,
                light,
                itemStack.hasFoil(),
                color(bipedEntityRenderState, itemStack)
            )
        }

        matrices.popPose()
    }

    private fun renderModel(
        state: S,
        model: Model<S>,
        renderLayer: RenderType,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        light: Int,
        glint: Boolean,
        color: Int = -1,
    ) {
        queue.submitModel(model, state, matrices, renderLayer, light, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null)
        if (glint) queue.submitModel(model, state, matrices, RenderTypes.armorEntityGlint(), light, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null)
    }

    override fun submit(
        matrixStack: PoseStack,
        queue: SubmitNodeCollector,
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
                bipedEntityRenderState.headEquipment,
                light,
                EquipmentSlot.HEAD,
            )
        }
        if (!hiddenArmor.contains(EquipmentSlot.CHEST)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.chestEquipment,
                light,
                EquipmentSlot.CHEST,
            )
        }
        if (!hiddenArmor.contains(EquipmentSlot.LEGS)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.legsEquipment,
                light,
                EquipmentSlot.LEGS,
            )
        }
        if (!hiddenArmor.contains(EquipmentSlot.FEET)) {
            renderArmor(
                bipedEntityRenderState,
                matrixStack,
                queue,
                bipedEntityRenderState.feetEquipment,
                light,
                EquipmentSlot.FEET,
            )
        }
    }
}
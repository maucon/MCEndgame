package de.fuballer.mcendgame.client.component.entity.custom.entities.arachne

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.animation.KeyframeAnimation
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition

class ArachneEntityModel(
    modelPart: ModelPart,
) : EntityModel<ArachneRenderState>(modelPart) {
    val arachne = root.getChild("arachne")
    val cephalothorax = arachne.getChild("cephalothorax")
    val legs = cephalothorax.getChild("legs")
    val legLeft1 = legs.getChild("legLeft1")
    val temurLeft1 = legLeft1.getChild("temurLeft1")
    val tibiaLeft1 = temurLeft1.getChild("tibiaLeft1")
    val metatarsusLeft1 = tibiaLeft1.getChild("metatarsusLeft1")
    val tarsusLeft1 = metatarsusLeft1.getChild("tarsusLeft1")
    val legLeft2 = legs.getChild("legLeft2")
    val temurLeft2 = legLeft2.getChild("temurLeft2")
    val tibiaLeft2 = temurLeft2.getChild("tibiaLeft2")
    val metatarsusLeft2 = tibiaLeft2.getChild("metatarsusLeft2")
    val tarsusLeft2 = metatarsusLeft2.getChild("tarsusLeft2")
    val legLeft3 = legs.getChild("legLeft3")
    val temurLeft3 = legLeft3.getChild("temurLeft3")
    val tibiaLeft3 = temurLeft3.getChild("tibiaLeft3")
    val metatarsusLeft3 = tibiaLeft3.getChild("metatarsusLeft3")
    val tarsusLeft3 = metatarsusLeft3.getChild("tarsusLeft3")
    val legLeft4 = legs.getChild("legLeft4")
    val temurLeft4 = legLeft4.getChild("temurLeft4")
    val tibiaLeft4 = temurLeft4.getChild("tibiaLeft4")
    val metatarsusLeft4 = tibiaLeft4.getChild("metatarsusLeft4")
    val tarsusLeft4 = metatarsusLeft4.getChild("tarsusLeft4")
    val legRight1 = legs.getChild("legRight1")
    val temurRight1 = legRight1.getChild("temurRight1")
    val tibiaRight1 = temurRight1.getChild("tibiaRight1")
    val metatarsusRight1 = tibiaRight1.getChild("metatarsusRight1")
    val tarsusRight1 = metatarsusRight1.getChild("tarsusRight1")
    val legRight2 = legs.getChild("legRight2")
    val temurRight2 = legRight2.getChild("temurRight2")
    val tibiaRight2 = temurRight2.getChild("tibiaRight2")
    val metatarsusRight2 = tibiaRight2.getChild("metatarsusRight2")
    val tarsusRight2 = metatarsusRight2.getChild("tarsusRight2")
    val legRight3 = legs.getChild("legRight3")
    val temurRight3 = legRight3.getChild("temurRight3")
    val tibiaRight3 = temurRight3.getChild("tibiaRight3")
    val metatarsusRight3 = tibiaRight3.getChild("metatarsusRight3")
    val tarsusRight3 = metatarsusRight3.getChild("tarsusRight3")
    val legRight4 = legs.getChild("legRight4")
    val temurRight4 = legRight4.getChild("temurRight4")
    val tibiaRight4 = temurRight4.getChild("tibiaRight4")
    val metatarsusRight4 = tibiaRight4.getChild("metatarsusRight4")
    val tarsusRight4 = metatarsusRight4.getChild("tarsusRight4")
    val upperbody = cephalothorax.getChild("upperbody")
    val chest = upperbody.getChild("chest")
    val breast = chest.getChild("breast")
    val chestDressLower = breast.getChild("chestDressLower")
    val breastTop = breast.getChild("breastTop")
    val neck = chest.getChild("neck")
    val head = neck.getChild("head")
    val armLeft = chest.getChild("armLeft")
    val armLeftLower = armLeft.getChild("armLeftLower")
    val armRight = chest.getChild("armRight")
    val armRightLower = armRight.getChild("armRightLower")
    val abdomen = cephalothorax.getChild("abdomen")

    val walkingAnimation: KeyframeAnimation = ArachneAnimations.WALKING.bake(modelPart)
    val walkingBackwardsAnimation: KeyframeAnimation = ArachneAnimations.WALKING_BACKWARDS.bake(modelPart)
    val idleAnimation: KeyframeAnimation = ArachneAnimations.IDLE.bake(modelPart)
    val spitAnimation: KeyframeAnimation = ArachneAnimations.SPIT.bake(modelPart)
    val attackAnimation: KeyframeAnimation = ArachneAnimations.ATTACK.bake(modelPart)

    companion object {
        val ARACHNE = ModelLayerLocation(IdentifierUtil.default("arachne"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val arachne =
                modelPartData.addOrReplaceChild("arachne", CubeListBuilder.create(), PartPose.offset(0.0f, 25.0f, -3.0f))

            val cephalothorax = arachne.addOrReplaceChild(
                "cephalothorax",
                CubeListBuilder.create().texOffs(110, 118).addBox(-4.0f, -12.0f, -2.0f, 8.0f, 6.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(114, 108).addBox(-3.0f, -13.0f, 0.0f, 6.0f, 1.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(120, 103).addBox(-2.5f, -14.0f, 1.0f, 5.0f, 1.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(122, 98).addBox(-2.0f, -16.0f, 0.5f, 4.0f, 2.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val legs = cephalothorax.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val legLeft1 = legs.addOrReplaceChild(
                "legLeft1",
                CubeListBuilder.create().texOffs(147, 105).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(147, 110).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(3.0f, -8.0f, -2.0f, 0.0011f, 1.149f, 0.087f)
            )

            val temurLeft1 = legLeft1.addOrReplaceChild(
                "temurLeft1",
                CubeListBuilder.create().texOffs(156, 105).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(156, 110).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(1.0f, 0.0f, 0.0f, 0.0085f, -0.0059f, -1.1775f)
            )

            val tibiaLeft1 = temurLeft1.addOrReplaceChild(
                "tibiaLeft1",
                CubeListBuilder.create().texOffs(186, 105)
                    .addBox(0.1455f, -0.1039f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(186, 110).addBox(0.1455f, -0.1039f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(12.65f, -1.0f, 0.0f, 0.0f, 0.0f, 1.4835f)
            )

            val metatarsusLeft1 = tibiaLeft1.addOrReplaceChild(
                "metatarsusLeft1",
                CubeListBuilder.create().texOffs(214, 105)
                    .addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(214, 110).addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(11.8955f, 0.1461f, 0.0f, 0.0048f, 0.0019f, 0.7851f)
            )

            val tarsusLeft1 = metatarsusLeft1.addOrReplaceChild(
                "tarsusLeft1",
                CubeListBuilder.create().texOffs(241, 107)
                    .addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(241, 112).addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, 0.2182f)
            )

            val legLeft2 = legs.addOrReplaceChild(
                "legLeft2",
                CubeListBuilder.create().texOffs(147, 115).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(147, 120).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(4.0f, -8.0f, 0.0f, 0.0005f, 0.4509f, 0.0863f)
            )

            val temurLeft2 = legLeft2.addOrReplaceChild(
                "temurLeft2",
                CubeListBuilder.create().texOffs(156, 115).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(156, 120).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(1.0f, 0.0f, 0.0f, 0.0085f, -0.0059f, -1.1775f)
            )

            val tibiaLeft2 = temurLeft2.addOrReplaceChild(
                "tibiaLeft2",
                CubeListBuilder.create().texOffs(186, 115)
                    .addBox(0.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(186, 120).addBox(0.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(12.65f, -1.0f, 0.0f, 0.0f, 0.0f, 1.4399f)
            )

            val metatarsusLeft2 = tibiaLeft2.addOrReplaceChild(
                "metatarsusLeft2",
                CubeListBuilder.create().texOffs(214, 115)
                    .addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(214, 120).addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(11.8871f, 0.222f, 0.0f, 0.0048f, 0.0019f, 0.7851f)
            )

            val tarsusLeft2 = metatarsusLeft2.addOrReplaceChild(
                "tarsusLeft2",
                CubeListBuilder.create().texOffs(241, 117)
                    .addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(241, 122).addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, 0.2182f)
            )

            val legLeft3 = legs.addOrReplaceChild(
                "legLeft3",
                CubeListBuilder.create().texOffs(147, 125).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(147, 130).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(4.0f, -8.0f, 3.0f, 0.0004f, -0.0727f, 0.086f)
            )

            val temurLeft3 = legLeft3.addOrReplaceChild(
                "temurLeft3",
                CubeListBuilder.create().texOffs(156, 125).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(156, 130).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(1.0f, 0.0f, 0.0f, 0.0085f, -0.0059f, -1.1775f)
            )

            val tibiaLeft3 = temurLeft3.addOrReplaceChild(
                "tibiaLeft3",
                CubeListBuilder.create().texOffs(186, 125)
                    .addBox(0.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(186, 130).addBox(0.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(12.65f, -1.0f, 0.0f, 0.0f, 0.0f, 1.4399f)
            )

            val metatarsusLeft3 = tibiaLeft3.addOrReplaceChild(
                "metatarsusLeft3",
                CubeListBuilder.create().texOffs(214, 125)
                    .addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(214, 130).addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(11.8871f, 0.222f, 0.0f, 0.0048f, 0.0019f, 0.7851f)
            )

            val tarsusLeft3 = metatarsusLeft3.addOrReplaceChild(
                "tarsusLeft3",
                CubeListBuilder.create().texOffs(241, 127)
                    .addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(241, 132).addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, 0.2182f)
            )

            val legLeft4 = legs.addOrReplaceChild(
                "legLeft4",
                CubeListBuilder.create().texOffs(147, 135).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(147, 140).addBox(-0.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(4.0f, -8.0f, 6.0f, 0.0005f, -0.5963f, 0.0857f)
            )

            val temurLeft4 = legLeft4.addOrReplaceChild(
                "temurLeft4",
                CubeListBuilder.create().texOffs(156, 135).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(156, 140).addBox(-0.25f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(1.0f, 0.0f, 0.0f, 0.0085f, -0.0059f, -1.1775f)
            )

            val tibiaLeft4 = temurLeft4.addOrReplaceChild(
                "tibiaLeft4",
                CubeListBuilder.create().texOffs(186, 135)
                    .addBox(0.1309f, -0.028f, -1.1427f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(186, 140).addBox(0.1309f, -0.028f, -1.1427f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(12.65f, -1.0f, 0.0f, 0.0f, -0.0873f, 1.4399f)
            )

            val metatarsusLeft4 = tibiaLeft4.addOrReplaceChild(
                "metatarsusLeft4",
                CubeListBuilder.create().texOffs(214, 135)
                    .addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(214, 140).addBox(-0.2502f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(11.8809f, 0.222f, -0.1427f, 0.0048f, 0.0019f, 0.7851f)
            )

            val tarsusLeft4 = metatarsusLeft4.addOrReplaceChild(
                "tarsusLeft4",
                CubeListBuilder.create().texOffs(241, 137)
                    .addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(241, 142).addBox(-0.6299f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, 0.2182f)
            )

            val legRight1 = legs.addOrReplaceChild(
                "legRight1",
                CubeListBuilder.create().texOffs(101, 105).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(101, 110).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(-3.0f, -8.0f, -2.0f, 0.0011f, -1.149f, -0.087f)
            )

            val temurRight1 = legRight1.addOrReplaceChild(
                "temurRight1",
                CubeListBuilder.create().texOffs(70, 105).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(70, 110).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0.0085f, 0.0059f, 1.1775f)
            )

            val tibiaRight1 = temurRight1.addOrReplaceChild(
                "tibiaRight1",
                CubeListBuilder.create().texOffs(42, 105)
                    .addBox(-12.1455f, -0.1039f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(42, 110).addBox(-12.1455f, -0.1039f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(-12.65f, -1.0f, 0.0f, 0.0f, 0.0f, -1.4835f)
            )

            val metatarsusRight1 = tibiaRight1.addOrReplaceChild(
                "metatarsusRight1",
                CubeListBuilder.create().texOffs(16, 105)
                    .addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(16, 110).addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-11.8955f, 0.1461f, 0.0f, 0.0048f, -0.0019f, -0.7851f)
            )

            val tarsusRight1 = metatarsusRight1.addOrReplaceChild(
                "tarsusRight1",
                CubeListBuilder.create().texOffs(1, 107).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(1, 112).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(-10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, -0.2182f)
            )

            val legRight2 = legs.addOrReplaceChild(
                "legRight2",
                CubeListBuilder.create().texOffs(101, 115).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(101, 120).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(-4.0f, -8.0f, 0.0f, 0.0005f, -0.4509f, -0.0863f)
            )

            val temurRight2 = legRight2.addOrReplaceChild(
                "temurRight2",
                CubeListBuilder.create().texOffs(70, 115).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(70, 120).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0.0085f, 0.0059f, 1.1775f)
            )

            val tibiaRight2 = temurRight2.addOrReplaceChild(
                "tibiaRight2",
                CubeListBuilder.create().texOffs(42, 115)
                    .addBox(-12.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(42, 120).addBox(-12.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(-12.65f, -1.0f, 0.0f, 0.0f, 0.0f, -1.4399f)
            )

            val metatarsusRight2 = tibiaRight2.addOrReplaceChild(
                "metatarsusRight2",
                CubeListBuilder.create().texOffs(16, 115)
                    .addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(16, 120).addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-11.8871f, 0.222f, 0.0f, 0.0048f, -0.0019f, -0.7851f)
            )

            val tarsusRight2 = metatarsusRight2.addOrReplaceChild(
                "tarsusRight2",
                CubeListBuilder.create().texOffs(1, 117).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(1, 122).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(-10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, -0.2182f)
            )

            val legRight3 = legs.addOrReplaceChild(
                "legRight3",
                CubeListBuilder.create().texOffs(101, 125).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(101, 130).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(-4.0f, -8.0f, 3.0f, 0.0004f, 0.0727f, -0.086f)
            )

            val temurRight3 = legRight3.addOrReplaceChild(
                "temurRight3",
                CubeListBuilder.create().texOffs(70, 125).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(70, 130).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0.0085f, 0.0059f, 1.1775f)
            )

            val tibiaRight3 = temurRight3.addOrReplaceChild(
                "tibiaRight3",
                CubeListBuilder.create().texOffs(42, 125)
                    .addBox(-12.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(42, 130).addBox(-12.1371f, -0.028f, -1.0f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(-12.65f, -1.0f, 0.0f, 0.0f, 0.0f, -1.4399f)
            )

            val metatarsusRight3 = tibiaRight3.addOrReplaceChild(
                "metatarsusRight3",
                CubeListBuilder.create().texOffs(16, 125)
                    .addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(16, 130).addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-11.8871f, 0.222f, 0.0f, 0.0048f, -0.0019f, -0.7851f)
            )

            val tarsusRight3 = metatarsusRight3.addOrReplaceChild(
                "tarsusRight3",
                CubeListBuilder.create().texOffs(1, 127).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(1, 132).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(-10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, -0.2182f)
            )

            val legRight4 = legs.addOrReplaceChild(
                "legRight4",
                CubeListBuilder.create().texOffs(101, 135).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.25f))
                    .texOffs(101, 140).addBox(-1.5f, -1.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.3f)),
                PartPose.offsetAndRotation(-4.0f, -8.0f, 6.0f, 0.0005f, 0.5963f, -0.0857f)
            )

            val temurRight4 = legRight4.addOrReplaceChild(
                "temurRight4",
                CubeListBuilder.create().texOffs(70, 135).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(70, 140).addBox(-12.75f, -1.0f, -1.0f, 13.0f, 2.0f, 2.0f, CubeDeformation(0.05f)),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0.0085f, 0.0059f, 1.1775f)
            )

            val tibiaRight4 = temurRight4.addOrReplaceChild(
                "tibiaRight4",
                CubeListBuilder.create().texOffs(42, 135)
                    .addBox(-12.1309f, -0.028f, -1.1427f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.25f))
                    .texOffs(42, 140).addBox(-12.1309f, -0.028f, -1.1427f, 12.0f, 2.0f, 2.0f, CubeDeformation(-0.2f)),
                PartPose.offsetAndRotation(-12.65f, -1.0f, 0.0f, 0.0f, 0.0873f, -1.4399f)
            )

            val metatarsusRight4 = tibiaRight4.addOrReplaceChild(
                "metatarsusRight4",
                CubeListBuilder.create().texOffs(16, 135)
                    .addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.3f))
                    .texOffs(16, 140).addBox(-10.7498f, -0.2929f, -1.0034f, 11.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-11.8809f, 0.222f, -0.1427f, 0.0048f, -0.0019f, -0.7851f)
            )

            val tarsusRight4 = metatarsusRight4.addOrReplaceChild(
                "tarsusRight4",
                CubeListBuilder.create().texOffs(1, 137).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.1f))
                    .texOffs(1, 142).addBox(-5.3701f, 0.0964f, -0.5f, 6.0f, 1.0f, 1.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(-10.4998f, 0.2071f, -0.0034f, 0.0f, 0.0f, -0.2182f)
            )

            val upperbody = cephalothorax.addOrReplaceChild(
                "upperbody",
                CubeListBuilder.create().texOffs(116, 80).addBox(-4.0f, -5.5f, -2.25f, 8.0f, 5.0f, 4.0f, CubeDeformation(-0.35f))
                    .texOffs(116, 90).addBox(-4.0f, -1.5f, -2.25f, 8.0f, 3.0f, 4.0f, CubeDeformation(0.1f)),
                PartPose.offset(0.0f, -12.5f, 0.25f)
            )

            val chest = upperbody.addOrReplaceChild(
                "chest",
                CubeListBuilder.create().texOffs(116, 49).addBox(-4.0f, -7.0f, -2.25f, 8.0f, 7.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -4.5f, -0.25f)
            )

            val breast = chest.addOrReplaceChild(
                "breast",
                CubeListBuilder.create().texOffs(117, 67).addBox(-4.0f, -0.5f, -2.0f, 8.0f, 2.0f, 3.0f, CubeDeformation(-0.25f)),
                PartPose.offset(0.0f, -4.0f, -2.25f)
            )

            val chestDressLower = breast.addOrReplaceChild(
                "chestDressLower",
                CubeListBuilder.create().texOffs(118, 73).addBox(-3.5f, -0.25f, 0.0f, 7.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 1.25f, -1.5f, 0.5236f, 0.0f, 0.0f)
            )

            val breastTop = breast.addOrReplaceChild(
                "breastTop",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, -0.25f, -1.7f, 0.48f, 0.0f, 0.0f)
            )

            val breastRight_r1 = breastTop.addOrReplaceChild(
                "breastRight_r1",
                CubeListBuilder.create().texOffs(115, 61).addBox(-1.0f, 0.0f, -0.2f, 3.0f, 2.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(129, 61).addBox(2.5f, 0.0f, -0.2f, 3.0f, 2.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-2.25f, 0.0f, 0.2f, 0.2618f, 0.0f, 0.0f)
            )

            val neck = chest.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(122, 42).addBox(-1.5f, -2.0f, -1.5f, 3.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -7.0f, 0.0f)
            )

            val head = neck.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(112, 25).addBox(-4.0f, -8.0f, -4.25f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(112, 0).addBox(-4.0f, -8.0f, -4.25f, 8.0f, 16.0f, 8.0f, CubeDeformation(0.15f)),
                PartPose.offset(0.0f, -1.0f, 0.0f)
            )

            val armLeft = chest.addOrReplaceChild(
                "armLeft",
                CubeListBuilder.create().texOffs(142, 51).addBox(0.0f, -0.5f, -1.5f, 3.0f, 6.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(155, 51).addBox(0.0f, -0.5f, -1.5f, 3.0f, 6.0f, 3.0f, CubeDeformation(0.1f)),
                PartPose.offset(3.75f, -6.25f, 0.0f)
            )

            val armLeftLower = armLeft.addOrReplaceChild(
                "armLeftLower",
                CubeListBuilder.create().texOffs(142, 61).addBox(4.0f, -18.75f, -1.5f, 3.0f, 7.0f, 3.0f, CubeDeformation(-0.25f))
                    .texOffs(155, 61).addBox(4.0f, -18.75f, -1.5f, 3.0f, 7.0f, 3.0f, CubeDeformation(-0.15f)),
                PartPose.offset(-4.0f, 23.25f, 0.0f)
            )

            val armRight = chest.addOrReplaceChild(
                "armRight",
                CubeListBuilder.create().texOffs(102, 50).addBox(-3.0f, -0.5f, -1.5f, 3.0f, 6.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(89, 50).addBox(-3.0f, -0.5f, -1.5f, 3.0f, 6.0f, 3.0f, CubeDeformation(0.1f)),
                PartPose.offset(-3.75f, -6.25f, 0.0f)
            )

            val armRightLower = armRight.addOrReplaceChild(
                "armRightLower",
                CubeListBuilder.create().texOffs(102, 60).addBox(-7.0f, -18.75f, -1.5f, 3.0f, 7.0f, 3.0f, CubeDeformation(-0.25f))
                    .texOffs(89, 60).addBox(-7.0f, -18.75f, -1.5f, 3.0f, 7.0f, 3.0f, CubeDeformation(-0.15f)),
                PartPose.offset(4.0f, 23.25f, 0.0f)
            )

            val abdomen = cephalothorax.addOrReplaceChild(
                "abdomen",
                CubeListBuilder.create().texOffs(104, 147).addBox(-6.0f, -5.0f, 0.0f, 12.0f, 9.0f, 12.0f, CubeDeformation(0.0f))
                    .texOffs(109, 135).addBox(-4.5f, -6.0f, 1.0f, 9.0f, 1.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(109, 169).addBox(-4.5f, 4.0f, 1.0f, 9.0f, 1.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(81, 151).addBox(-7.0f, -4.0f, 1.0f, 1.0f, 7.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(153, 151).addBox(6.0f, -4.0f, 1.0f, 1.0f, 7.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(117, 181).addBox(-5.0f, -3.75f, 12.0f, 10.0f, 7.0f, 1.0f, CubeDeformation(0.0f))
                    .texOffs(119, 190).addBox(-4.0f, -2.25f, 13.0f, 8.0f, 5.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -9.0f, 7.0f)
            )
            return LayerDefinition.create(modelData, 256, 256)
        }
    }

    override fun setupAnim(
        renderState: ArachneRenderState,
    ) {
        super.setupAnim(renderState)

        idleAnimation.apply(renderState.idleAnimationState, renderState.ageInTicks)

        val walkAnimSpeed = renderState.moveSpeed * 6F / renderState.scale
        walkingAnimation.apply(renderState.walkAnimationState, renderState.ageInTicks, walkAnimSpeed)
        walkingBackwardsAnimation.apply(renderState.walkBWAnimationState, renderState.ageInTicks, walkAnimSpeed)

        spitAnimation.apply(renderState.spitAnimationState, renderState.ageInTicks)
        attackAnimation.apply(renderState.meleeAttackAnimationState, renderState.ageInTicks)

        setHeadAngles(renderState)
    }

    private fun setHeadAngles(
        renderState: ArachneRenderState,
    ) {
        head.xRot += Math.toRadians(renderState.xRot.toDouble()).toFloat()
        head.yRot += Math.toRadians(renderState.yRot.toDouble()).toFloat()
        head.yRot = Math.clamp(head.yRot, -1F, 1F)
    }
}
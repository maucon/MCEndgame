package de.fuballer.mcendgame.client.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import net.minecraft.client.model.HumanoidModel.ArmPose
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.ArmorModelSet
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HumanoidMobRenderer
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.item.*

class BanditRenderer(
    context: EntityRendererProvider.Context,
    slim: Boolean,
) : HumanoidMobRenderer<BanditEntity, BanditRenderState, BanditModel>(
    context,
    BanditModel(context.bakeLayer(if (slim) ModelLayers.PLAYER_SLIM else ModelLayers.PLAYER), slim),
    0.5f,
) {
    companion object {
        private fun getArmPose(avatar: Avatar, arm: HumanoidArm): ArmPose {
            val mainHandItem = avatar.getItemInHand(InteractionHand.MAIN_HAND)
            val offHandItem = avatar.getItemInHand(InteractionHand.OFF_HAND)
            val mainHandPose = getArmPose(avatar, mainHandItem, InteractionHand.MAIN_HAND)
            var offHandPose = getArmPose(avatar, offHandItem, InteractionHand.OFF_HAND)
            if (mainHandPose.isTwoHanded) {
                offHandPose = if (offHandItem.isEmpty) ArmPose.EMPTY else ArmPose.ITEM
            }

            return if (avatar.mainArm == arm) mainHandPose else offHandPose
        }

        private fun getArmPose(avatar: Avatar, itemInHand: ItemStack, hand: InteractionHand?): ArmPose {
            if (itemInHand.isEmpty) return ArmPose.EMPTY

            if (!avatar.swinging && itemInHand.`is`(Items.CROSSBOW) && CrossbowItem.isCharged(itemInHand)) return ArmPose.CROSSBOW_HOLD

            if (avatar.usedItemHand == hand && avatar.useItemRemainingTicks > 0) {
                val anim = itemInHand.useAnimation
                if (anim == ItemUseAnimation.BLOCK) {
                    return ArmPose.BLOCK
                }

                if (anim == ItemUseAnimation.BOW) {
                    return ArmPose.BOW_AND_ARROW
                }

                if (anim == ItemUseAnimation.TRIDENT) {
                    return ArmPose.THROW_TRIDENT
                }

                if (anim == ItemUseAnimation.CROSSBOW) {
                    return ArmPose.CROSSBOW_CHARGE
                }

                if (anim == ItemUseAnimation.SPYGLASS) {
                    return ArmPose.SPYGLASS
                }

                if (anim == ItemUseAnimation.TOOT_HORN) {
                    return ArmPose.TOOT_HORN
                }

                if (anim == ItemUseAnimation.BRUSH) {
                    return ArmPose.BRUSH
                }

                if (anim == ItemUseAnimation.SPEAR) {
                    return ArmPose.SPEAR
                }
            }

            val attack = itemInHand.get(DataComponents.SWING_ANIMATION)
            return if (attack != null && attack.type() == SwingAnimationType.STAB && avatar.swinging) ArmPose.SPEAR
            else if (itemInHand.`is`(ItemTags.SPEARS)) ArmPose.SPEAR else ArmPose.ITEM
        }
    }

    init {
        addLayer(
            HumanoidArmorLayer(
                this,
                ArmorModelSet.bake(
                    if (slim) ModelLayers.PLAYER_SLIM_ARMOR else ModelLayers.PLAYER_ARMOR,
                    context.modelSet,
                ) { part -> BanditModel(part, slim) },
                context.equipmentRenderer,
            )
        )
    }

    override fun getTextureLocation(state: BanditRenderState) = state.banditType.texture

    override fun createRenderState() = BanditRenderState()

    override fun extractRenderState(
        entity: BanditEntity,
        state: BanditRenderState,
        partialTicks: Float,
    ) {
        super.extractRenderState(entity, state, partialTicks)
        state.banditType = entity.banditType
        state.leftArmPose = getArmPose(entity, HumanoidArm.LEFT)
        state.rightArmPose = getArmPose(entity, HumanoidArm.RIGHT)
        state.arrowCount = entity.arrowCount
        state.stingerCount = entity.stingerCount
    }
}
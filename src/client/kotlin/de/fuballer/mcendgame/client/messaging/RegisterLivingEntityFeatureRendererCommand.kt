package de.fuballer.mcendgame.client.messaging

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity

class RegisterLivingEntityFeatureRendererCommand(
    val entityType: EntityType<out LivingEntity>,
    val entityRenderer: LivingEntityRenderer<*, *, *>,
    val registrationHelper: LivingEntityRenderLayerRegistrationCallback.RegistrationHelper,
    val context: EntityRendererProvider.Context
)
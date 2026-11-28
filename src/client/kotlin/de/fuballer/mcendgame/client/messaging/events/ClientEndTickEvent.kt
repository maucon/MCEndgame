package de.fuballer.mcendgame.client.messaging.events

import net.minecraft.client.Minecraft

data class ClientEndTickEvent(
    val minecraft: Minecraft,
)
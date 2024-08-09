package de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType

data class ActiveSupportWolf(
    val type: SupportWolfType,
    val slot: SlotType
)
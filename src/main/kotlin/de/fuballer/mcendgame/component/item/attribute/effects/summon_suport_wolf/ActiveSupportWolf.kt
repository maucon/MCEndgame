package de.fuballer.mcendgame.component.item.attribute.effects.summon_suport_wolf

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType

data class ActiveSupportWolf(
    val type: SupportWolfType,
    val slot: SlotType
)
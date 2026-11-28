package de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.key.demonic

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.key.KeyItem
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.sigil.SigilItem.Companion.SIGIL_TRANSLATABLE_DESCRIPTION_KEY
import net.minecraft.network.chat.Component

class DemonicKey(
    settings: Properties,
) : KeyItem(settings) {
    override val dungeonType = DungeonType.DEMONIC_CRUCIBLE

    override val specificDescription = mutableListOf(
        Component.translatable(SIGIL_TRANSLATABLE_DESCRIPTION_KEY + "dungeon_type").append(dungeonType.translatable),
    )
}
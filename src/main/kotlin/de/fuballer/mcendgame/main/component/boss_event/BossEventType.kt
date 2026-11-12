package de.fuballer.mcendgame.main.component.boss_event

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.resources.Identifier

enum class BossEventType(
    val identifier: String,
    val backgroundSprite: Identifier = IdentifierUtil.default("boss_bar/${identifier}_background"),
    val progressSprite: Identifier = IdentifierUtil.default("boss_bar/${identifier}_progress"),
    val foregroundSprite: Identifier? = IdentifierUtil.default("boss_bar/${identifier}_foreground"),
    val spriteWidth: Int = 188,
    val spriteHeight: Int = 11,
    val xOffset: Int = 0,
    val yOffset: Int = 0,
) {
    BEASTWEAVER("beastweaver", foregroundSprite = null, xOffset = -3, yOffset = -1),
    THAR_ZUL("thar_zul", xOffset = -3, yOffset = -1),
    THAL_VEYRA("thar_zul", xOffset = -3, yOffset = -1),
}
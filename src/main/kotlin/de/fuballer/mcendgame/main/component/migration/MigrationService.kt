package de.fuballer.mcendgame.main.component.migration

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.data_component_type.CustomDataComponentType
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.item.ItemStack

/**
 * Handles one-time data migrations for persisted game objects whose schema
 * has changed over time (e.g. new data components added to items).
 *
 * Use this to keep old, already-saved data (totems, items, etc.) compatible
 * with code that expects newer fields to be present, without requiring a
 * full data wipe or manual player intervention. Migration methods should be
 * idempotent — safe to call repeatedly on data that may or may not have
 * already been migrated.
 */
object MigrationService {
    private val log = LogUtils.getLogger()

    /**
     * Migrates a legacy totem [ItemStack] that predates the [CustomDataComponentType.TOTEM_TIER]
     * data component by deriving its tier from the translatable lore line that was previously
     * used to display it, then writing that value into the new component.
     *
     * This exists so old totems saved before the tier component was introduced remain usable
     * with code that now expects the tier to be present, instead of silently defaulting or
     * requiring players to have their totems manually replaced.
     *
     * No-op if the totem already has a tier component or has no lore to read from.
     */
    fun migrateTotemTier(totem: ItemStack) {
        if (totem.has(CustomDataComponentType.TOTEM_TIER)) return
        val lore = totem.get(DataComponents.LORE) ?: return

        val tier = lore.lines()
            .firstNotNullOfOrNull { line ->
                val content = line.contents
                if (content is TranslatableContents && content.key == TotemItem.TIER_KEY) {
                    content.args.getOrNull(0)?.toString()?.toIntOrNull()
                } else null
            }

        if (tier == null) {
            log.warn(
                "Could not migrate tier for totem {}: no valid tier found in lore. " +
                        "This totem should be removed and replaced using /givetotem (Moderator).",
                totem.item
            )
            return
        }

        totem.set(CustomDataComponentType.TOTEM_TIER, tier)
        log.info("Migrated totem {} to tier {}", totem.item, tier)
    }
}
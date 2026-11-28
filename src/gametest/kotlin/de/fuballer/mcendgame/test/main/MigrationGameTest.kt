package de.fuballer.mcendgame.test.main

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.migration.legacy.LegacyItems
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.misc.PlayerJoinEvent
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import org.slf4j.LoggerFactory

/**
 * Gametests for the legacy item migration of `aspect_of_the_grove`, which was replaced by the
 * verdant sigil.
 *
 * Stacks saved before that removal must keep deserializing (the id stays registered through
 * [LegacyItems]) and must be converted into a verdant sigil instead of being dropped by vanilla
 * when they end up in a player inventory.
 */
object MigrationGameTest {
    private val LOG = LoggerFactory.getLogger(MigrationGameTest::class.java)

    /** Deliberately hardcoded: the id is the persisted contract, not the Kotlin constant. */
    private val LEGACY_ASPECT_ID = Identifier.fromNamespaceAndPath("mcendgame", "aspect_of_the_grove")

    /**
     * The removed item id must still resolve, so old save data (player inventories, containers,
     * `KillerEntity` equipment, ...) deserializes into a real item instead of vanishing.
     */
    fun testLegacyItemStillDeserializes(helper: GameTestHelper) {
        val registered = BuiltInRegistries.ITEM.getValue(LEGACY_ASPECT_ID)
        helper.assertTrue(
            registered === LegacyItems.ASPECT_OF_THE_GROVE,
            "The removed id $LEGACY_ASPECT_ID must still resolve to the legacy item, but was $registered"
        )

        val stack = ItemStack(LegacyItems.ASPECT_OF_THE_GROVE, 3)
        val saved = ItemStack.CODEC.encodeStart(RuntimeConfig.REGISTRY_OPS, stack)
            .result()
            .orElseThrow { helper.assertionException("The legacy aspect should still be serializable") }
        val loaded = ItemStack.CODEC.parse(RuntimeConfig.REGISTRY_OPS, saved)
            .result()
            .orElseThrow { helper.assertionException("Saved stacks of the removed aspect should still deserialize, but $saved failed") }

        helper.assertTrue(loaded.item === LegacyItems.ASPECT_OF_THE_GROVE, "Deserializing should restore the legacy item, but was ${loaded.item}")
        helper.assertValueEqual(loaded.count, 3, "Deserializing should restore the stack size")

        LOG.info("The legacy aspect serialized to {} and deserialized back", saved)
        helper.succeed()
    }

    /**
     * A joining player's legacy aspects are converted in the main inventory, in equipment and in the
     * ender chest, keep their stack size, and are not touched again by a repeated migration.
     */
    fun testLegacyItemIsMigratedOnJoin(helper: GameTestHelper) {
        val player = DungeonGameTest.spawnMockServerPlayer(helper)

        player.inventory.setItem(0, ItemStack(LegacyItems.ASPECT_OF_THE_GROVE, 2))
        player.setItemSlot(EquipmentSlot.HEAD, ItemStack(LegacyItems.ASPECT_OF_THE_GROVE))
        player.enderChestInventory.setItem(5, ItemStack(LegacyItems.ASPECT_OF_THE_GROVE))
        player.inventory.setItem(1, ItemStack(AspectItems.ASPECT_OF_GHOSTS)) // not a legacy item

        // Same seam as a real join: EventMapper publishes this event on ServerPlayConnectionEvents.JOIN
        EventGateway.publish(PlayerJoinEvent(player))

        assertMigrated(helper, player.inventory.getItem(0), 2, "The main inventory")
        assertMigrated(helper, player.getItemBySlot(EquipmentSlot.HEAD), 1, "The armor slot")
        assertMigrated(helper, player.enderChestInventory.getItem(5), 1, "The ender chest")

        helper.assertTrue(
            player.inventory.getItem(1).item === AspectItems.ASPECT_OF_GHOSTS,
            "Non-legacy items must not be migrated, but slot 1 held ${player.inventory.getItem(1).item}"
        )

        // Migrations must be idempotent: a repeated join only logs nothing and changes nothing
        EventGateway.publish(PlayerJoinEvent(player))

        assertMigrated(helper, player.inventory.getItem(0), 2, "The main inventory after a second join")
        helper.assertTrue(
            player.inventory.getItem(1).item === AspectItems.ASPECT_OF_GHOSTS,
            "Non-legacy items must not be migrated by a repeated join"
        )

        LOG.info("Legacy aspects of {} were migrated on join", player.gameProfile.name)
        helper.succeed()
    }

    /**
     * A legacy aspect picked up mid-session is converted by the inventory tick of the item itself
     * (no join migration runs, so this only passes if the item hook is wired up).
     */
    fun testLegacyItemIsMigratedWhenTicked(helper: GameTestHelper) {
        val player = DungeonGameTest.spawnMockServerPlayer(helper)

        player.inventory.setItem(3, ItemStack(LegacyItems.ASPECT_OF_THE_GROVE))
        val legacyStack = player.inventory.getItem(3)

        // What Inventory.tick() does for every stack in the player inventory, selected slot included
        legacyStack.inventoryTick(helper.level, player, null)

        assertMigrated(helper, player.inventory.getItem(3), 1, "The inventory slot after ticking the legacy stack")

        LOG.info("The legacy aspect in slot 3 of {} was migrated by the inventory tick", player.gameProfile.name)
        helper.succeed()
    }

    private fun assertMigrated(
        helper: GameTestHelper,
        stack: ItemStack,
        expectedCount: Int,
        label: String,
    ) {
        helper.assertTrue(
            stack.item === AspectItems.VERDANT_SIGIL,
            "$label should hold a verdant sigil after the migration, but held ${stack.item}"
        )
        helper.assertValueEqual(stack.count, expectedCount, "$label should keep the stack size after the migration")
        helper.assertTrue(
            stack.get(DataComponents.LORE) != null,
            "$label should hold the default verdant sigil (with its lore) instead of a bare stack"
        )
    }
}
package de.fuballer.mcendgame.main.component.totem

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemVanillaAttributesEntity
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemVanillaAttributesRepository
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemsEntity
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemsRepository
import de.fuballer.mcendgame.main.messaging.misc.CollectCustomAttributesCommand
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Injectable
class TotemService(
    private val playerTotemsRepository: PlayerTotemsRepository,
    private val playerTotemVanillaAttributesRepository: PlayerTotemVanillaAttributesRepository,
) {
    fun openInventory(player: PlayerEntity) {
        val playerTotems = getPlayerTotems(player)

        val screenHandlerFactory = SimpleNamedScreenHandlerFactory({ syncId, inventory, _ ->
            TotemScreenHandler(syncId, inventory, playerTotems, this)
        }, Text.translatable("container.mcendgame.totem.title"))

        player.openHandledScreen(screenHandlerFactory)
    }

    private fun getPlayerTotems(player: PlayerEntity) = playerTotemsRepository.findById(player.uuid)?.totems ?: listOf()

    fun savePlayerTotems(player: PlayerEntity, inventory: Inventory) {
        val itemStacks = (0 until inventory.size()).map { slot -> inventory.getStack(slot) }
        val entity = PlayerTotemsEntity(player.uuid, itemStacks)
        playerTotemsRepository.save(entity)
    }

    @CommandHandler
    fun on(cmd: CollectCustomAttributesCommand) {
        val player = cmd.entity as? PlayerEntity ?: return
        if (!player.entityWorld.isDungeonWorld()) return

        val attributes = getPlayerTotemAttributes(player)
        cmd.customAttributes.addAll(attributes)
    }

    private fun getPlayerTotemAttributes(player: PlayerEntity) = getPlayerTotems(player).flatMap { it.getCustomAttributes() }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        val player = event.player
        removeTotemVanillaAttributes(player)
        if (event.newWorld.isDungeonWorld()) addTotemVanillaAttributes(player)
    }

    private fun removeTotemVanillaAttributes(player: PlayerEntity) {
        val entity = playerTotemVanillaAttributesRepository.findById(player.uuid) ?: return
        entity.attributes.forEach { (type, identifier) ->
            val instance = player.getAttributeInstance(type)
            instance?.removeModifier(identifier)
        }
    }

    private fun addTotemVanillaAttributes(player: PlayerEntity) {
        val attributes = mutableListOf<Pair<RegistryEntry<EntityAttribute>, Identifier>>()

        getPlayerTotemAttributes(player)
            .filter { it.type is VanillaAttributeType }
            .forEach {
                val vanillaAttributeType = it.type as VanillaAttributeType
                val identifier = IdentifierUtil.defaultRandom()
                val modifier = EntityAttributeModifier(identifier, it.rolls[0].asDoubleRoll().getValue(), vanillaAttributeType.scaleType)

                val type = vanillaAttributeType.attribute
                val instance = player.getAttributeInstance(type)
                instance?.addTemporaryModifier(modifier)

                attributes.add(Pair(type, identifier))
            }

        val entity = PlayerTotemVanillaAttributesEntity(player.uuid, attributes)
        playerTotemVanillaAttributesRepository.save(entity)
    }
}
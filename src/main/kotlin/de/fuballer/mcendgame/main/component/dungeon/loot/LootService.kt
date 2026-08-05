package de.fuballer.mcendgame.main.component.dungeon.loot

import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossCrystalDropCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemyDeathEvent
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDropCommand
import de.fuballer.mcendgame.main.messaging.misc.MagicFindCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getTotalCustomAttributeLootMultiplier
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isElite
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isLootGoblin
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

@Injectable
class LootService {
    @CommandHandler
    fun on(cmd: LivingEntityDropCommand) {
        if (!cmd.world.isDungeonWorld()) return

        cmd.dropLoot = false
        cmd.dropEquipment = false
        cmd.dropInventory = false
        cmd.dropExperience = true
    }

    @EventSubscriber(sync = true)
    fun on(event: DungeonEnemyDeathEvent) {
        if (event.isClient) return
        val serverWorld = event.world as? ServerLevel ?: return
        val enemyEntity = event.enemyEntity

        if (enemyEntity.isDungeonBoss()) return

        if (enemyEntity.isElite()) dropEliteLoot(serverWorld, enemyEntity)

        EquipmentSlot.VALUES
            .map { enemyEntity.getItemBySlot(it) }
            .filter {
                val baseDropProbability = getDropProbability(it, enemyEntity.isLootGoblin())
                var dropProbability = baseDropProbability * getMagicFindFactor(event.killer)
                dropProbability *= enemyEntity.getTotalCustomAttributeLootMultiplier()

                Random.nextDouble() <= dropProbability
            }
            .onEach { setRandomDurability(it) }
            .forEach { RuntimeConfig.SERVER.execute { enemyEntity.spawnAtLocation(serverWorld, it) } }
    }

    @EventSubscriber(sync = true)
    fun dropBossCrystals(event: DungeonBossDeathEvent) {
        val serverLevel = event.world as? ServerLevel ?: return

        val dungeonLevel = serverLevel.getDungeonLevel()
        val baseCrystalCount = LootSettings.getBossBaseCrystalCount(dungeonLevel)

        val bossEntity = event.bossEntity
        val lootMultiplier = bossEntity.getTotalCustomAttributeLootMultiplier()
        val empoweredCrystalCount = baseCrystalCount * lootMultiplier
        val finalCrystalCount = empoweredCrystalCount.toInt() + if (Random.nextDouble() < empoweredCrystalCount % 1) 1 else 0

        val crystalItems = RandomUtil.pickLevelRestrictedWithRepeats(LootSettings.CRYSTALS, 1, dungeonLevel, finalCrystalCount).toMutableList()
        val command = DungeonBossCrystalDropCommand(dungeonLevel, serverLevel.getDungeonAspects(), crystalItems, lootMultiplier)
        val cmd = CommandGateway.apply(command)
        val itemStacks = cmd.crystalItems.map { it.defaultInstance }

        RuntimeConfig.SERVER.execute { itemStacks.forEach { bossEntity.spawnAtLocation(serverLevel, it) } }
    }

    @EventSubscriber(sync = true)
    fun dropBossUniques(event: DungeonBossDeathEvent) {
        val serverWorld = event.world as? ServerLevel ?: return
        val boss = event.bossEntity

        val type = event.bossEntity.type
        val possibleUniques = LootSettings.BOSS_UNIQUES[type] ?: return
        possibleUniques.forEach {
            if (Random.nextDouble() > it.value) return@forEach
            val item = it.key
            val itemStack = if (item is UniqueAttributesItemInterface) item.getRolledStack(item) else item.defaultInstance

            boss.spawnAtLocation(serverWorld, itemStack)
        }
    }

    private fun getMagicFindFactor(entity: LivingEntity?): Double {
        if (entity == null) return 1.0

        val magicFindEntity = if (entity !is TamableAnimal) entity else entity.owner ?: entity

        val magicFindCommand = MagicFindCommand(magicFindEntity)
        val cmd = CommandGateway.apply(magicFindCommand)

        return LootSettings.calculateMagicFindDropProbabilityFactor(cmd.magicFind)
    }

    private fun getDropProbability(stack: ItemStack, isLootGoblin: Boolean): Double {
        if (stack.`is`(CustomTags.DUNGEON_DROP_DISABLED)) return 0.0
        if (isLootGoblin) return 1.0 // loot goblins should always drop all equipment
        if (stack.item is UniqueAttributesItemInterface) return 1.0 // uniques should always drop

        if (stack.`is`(CustomTags.DIAMOND_GEAR)) return LootSettings.ITEMS_DROP_PROBABILITY_DIAMOND
        if (stack.`is`(CustomTags.NETHERITE_GEAR)) return LootSettings.ITEMS_DROP_PROBABILITY_NETHERITE
        return LootSettings.ITEMS_DROP_PROBABILITY
    }

    private fun setRandomDurability(itemStack: ItemStack) {
        itemStack.damageValue = (itemStack.maxDamage * Random.nextDouble()).toInt()
    }

    private fun dropEliteLoot(serverWorld: ServerLevel, entity: LivingEntity) {
        val aspect = RandomUtil.pickOne(LootSettings.ASPECTS).option
        RuntimeConfig.SERVER.execute { entity.spawnAtLocation(serverWorld, aspect.defaultInstance) }
    }
}
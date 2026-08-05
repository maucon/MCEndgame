package de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossCrystalDropCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerIncreaseProgressCommand
import de.fuballer.mcendgame.main.messaging.dungeon.SelectDungeonTypeCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import kotlin.math.max
import kotlin.random.Random

@Injectable
object AspectOfTheGroveService {
    @CommandHandler
    fun onSelectDungeonType(cmd: SelectDungeonTypeCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.dungeonType = DungeonType.BEASTWEAVER_GROVE
    }

    @CommandHandler
    fun onGenerateLayout(cmd: DungeonGenerateCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.dungeonLevel = max(cmd.dungeonLevel, AspectOfTheGrove.MIN_DUNGEON_LEVEL)
    }

    @CommandHandler
    fun onDungeonBossCrystalDrop(cmd: DungeonBossCrystalDropCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return

        val crystalItems = cmd.crystalItems
        crystalItems.clear()

        val baseProbability = cmd.dungeonLevel / 10.0
        val finalProbability = baseProbability * cmd.lootMultiplier
        val count = finalProbability.toInt() + if (Random.nextDouble() < finalProbability % 1) 1 else 0
        repeat(count) { crystalItems.add(CrystalItems.IMITATION_CRYSTAL) }
    }

    @CommandHandler
    fun onIncreaseProgress(cmd: DungeonPlayerIncreaseProgressCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.progressBlocked = true
    }
}
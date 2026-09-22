package de.fuballer.mcendgame.main.component.sound

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Instrument
import net.minecraft.world.item.Instruments

object CustomInstruments {
    val HOWL_OF_THE_WOLF_HORN: ResourceKey<Instrument> = ResourceKey.create(
        Registries.INSTRUMENT,
        IdentifierUtil.default("howl_of_the_wolf"),
    )

    fun bootstrap(context: BootstrapContext<Instrument>) {
        Instruments.register(context, HOWL_OF_THE_WOLF_HORN, CustomSoundEvents.WOLF_HOWL_ENTRY, Instruments.GOAT_HORN_DURATION, Instruments.GOAT_HORN_RANGE_BLOCKS.toFloat())
    }
}
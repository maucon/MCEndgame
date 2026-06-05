package de.fuballer.mcendgame.main.component.item.custom.misc

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.FrigidCry
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.MoltenRoar
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.VerdantEcho
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Instruments
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.InstrumentComponent

@Injectable
object CustomMiscItems {
    val VERDANT_ECHO = UniqueItemRegistry.registerMiscItem(
        ::VerdantEcho,
        Item.Properties()
            .stacksTo(1)
            .delayedComponent(DataComponents.INSTRUMENT) { context -> InstrumentComponent(context.getOrThrow(Instruments.DREAM_GOAT_HORN)) },
        "verdant_echo",
    )
    val MOLTEN_ROAR = UniqueItemRegistry.registerMiscItem(
        ::MoltenRoar,
        Item.Properties()
            .stacksTo(1)
            .delayedComponent(DataComponents.INSTRUMENT) { context -> InstrumentComponent(context.getOrThrow(Instruments.SEEK_GOAT_HORN)) },
        "molten_roar",
    )
    val FRIGID_CRY = UniqueItemRegistry.registerMiscItem(
        ::FrigidCry,
        Item.Properties()
            .stacksTo(1)
            .delayedComponent(DataComponents.INSTRUMENT) { context -> InstrumentComponent(context.getOrThrow(Instruments.FEEL_GOAT_HORN)) },
        "frigid_cry",
    )
}
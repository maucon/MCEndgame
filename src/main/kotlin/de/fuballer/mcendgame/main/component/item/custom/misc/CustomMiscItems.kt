package de.fuballer.mcendgame.main.component.item.custom.misc

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.FrigidCry
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.MoltenRoar
import de.fuballer.mcendgame.main.component.item.custom.misc.horn.VerdantEcho
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Instruments
import net.minecraft.item.Item
import net.minecraft.registry.Registries

@Injectable
object CustomMiscItems {
    val VERDANT_ECHO = UniqueItemRegistry.registerItem(
        VerdantEcho(
            Item.Settings()
                .maxCount(1)
                .component(DataComponentTypes.INSTRUMENT, Registries.INSTRUMENT.entryOf(Instruments.DREAM_GOAT_HORN)),
        ),
        "verdant_echo",
    )
    val MOLTEN_ROAR = UniqueItemRegistry.registerItem(
        MoltenRoar(
            Item.Settings()
                .maxCount(1)
                .component(DataComponentTypes.INSTRUMENT, Registries.INSTRUMENT.entryOf(Instruments.SEEK_GOAT_HORN))
        ),
        "molten_roar",
    )
    val FRIGID_CRY = UniqueItemRegistry.registerItem(
        FrigidCry(
            Item.Settings()
                .maxCount(1)
                .component(DataComponentTypes.INSTRUMENT, Registries.INSTRUMENT.entryOf(Instruments.FEEL_GOAT_HORN))
        ),
        "frigid_cry",
    )
}
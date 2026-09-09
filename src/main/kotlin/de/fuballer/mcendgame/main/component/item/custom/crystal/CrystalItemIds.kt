package de.fuballer.mcendgame.main.component.item.custom.crystal

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CrystalItemIds {
    val CALIBRATION_CRYSTAL = RegistryKeyUtil.createItemKey("calibration_crystal")
    val SACRIFICIAL_CRYSTAL = RegistryKeyUtil.createItemKey("sacrifice_crystal")
    val PERMUTATION_CRYSTAL = RegistryKeyUtil.createItemKey("permutation_crystal")
    val REFORGE_CRYSTAL = RegistryKeyUtil.createItemKey("reforge_crystal")
    val CORRUPTION_CRYSTAL = RegistryKeyUtil.createItemKey("corruption_crystal")
    val IMITATION_CRYSTAL = RegistryKeyUtil.createItemKey("imitation_crystal")

    val SYNTHESIZED_FORCE_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_force_crystal")
    val SYNTHESIZED_MOMENTUM_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_momentum")
    val SYNTHESIZED_ENDURANCE_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_endurance")
    val SYNTHESIZED_VITALITY_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_vitality")
    val SYNTHESIZED_PRECISION_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_precision")
    val SYNTHESIZED_FOCUS_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_focus")
    val SYNTHESIZED_COMMAND_CRYSTAL = RegistryKeyUtil.createItemKey("synthesized_command")
}
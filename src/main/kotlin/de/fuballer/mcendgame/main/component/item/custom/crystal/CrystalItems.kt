package de.fuballer.mcendgame.main.component.item.custom.crystal

import de.fuballer.mcendgame.main.component.item.custom.crystal.item.*
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CrystalItems {
    val CALIBRATION_CRYSTAL = RegistryUtil.registerCrystalItem(::CalibrationCrystalItem, "calibration_crystal")
    val SACRIFICIAL_CRYSTAL = RegistryUtil.registerCrystalItem(::SacrificeCrystalItem, "sacrifice_crystal")
    val PERMUTATION_CRYSTAL = RegistryUtil.registerCrystalItem(::PermutationCrystalItem, "permutation_crystal")
    val REFORGE_CRYSTAL = RegistryUtil.registerCrystalItem(::ReforgeCrystalItem, "reforge_crystal")
    val CORRUPTION_CRYSTAL = RegistryUtil.registerCrystalItem(::CorruptionCrystalItem, "corruption_crystal")
    val IMITATION_CRYSTAL = RegistryUtil.registerCrystalItem(::ImitationCrystalItem, "imitation_crystal")
}
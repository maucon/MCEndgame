package de.fuballer.mcendgame.main.component.item.custom.crystal

import de.fuballer.mcendgame.main.component.item.custom.crystal.item.*
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CrystalItems {
    val CALIBRATION_CRYSTAL = RegistryUtil.registerCrystalItem(::CalibrationCrystalItem, CrystalItemIds.CALIBRATION_CRYSTAL)
    val SACRIFICIAL_CRYSTAL = RegistryUtil.registerCrystalItem(::SacrificeCrystalItem, CrystalItemIds.SACRIFICIAL_CRYSTAL)
    val PERMUTATION_CRYSTAL = RegistryUtil.registerCrystalItem(::PermutationCrystalItem, CrystalItemIds.PERMUTATION_CRYSTAL)
    val REFORGE_CRYSTAL = RegistryUtil.registerCrystalItem(::ReforgeCrystalItem, CrystalItemIds.REFORGE_CRYSTAL)
    val CORRUPTION_CRYSTAL = RegistryUtil.registerCrystalItem(::CorruptionCrystalItem, CrystalItemIds.CORRUPTION_CRYSTAL)
}
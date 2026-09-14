package de.fuballer.mcendgame.main.component.item.custom.crystal

import de.fuballer.mcendgame.main.component.item.custom.crystal.item.*
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedCommandCrystal
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedEnduranceCrystal
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedFocusCrystal
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedForceCrystal
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedMomentumCrystal
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedPrecisionCrystal
import de.fuballer.mcendgame.main.component.item.custom.crystal.item.synthesized.SynthesizedVitalityCrystal
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CrystalItems {
    val CALIBRATION_CRYSTAL = RegistryUtil.registerCrystalItem(::CalibrationCrystalItem, CrystalItemIds.CALIBRATION_CRYSTAL)
    val SACRIFICIAL_CRYSTAL = RegistryUtil.registerCrystalItem(::SacrificeCrystalItem, CrystalItemIds.SACRIFICIAL_CRYSTAL)
    val PERMUTATION_CRYSTAL = RegistryUtil.registerCrystalItem(::PermutationCrystalItem, CrystalItemIds.PERMUTATION_CRYSTAL)
    val REFORGE_CRYSTAL = RegistryUtil.registerCrystalItem(::ReforgeCrystalItem, CrystalItemIds.REFORGE_CRYSTAL)
    val CORRUPTION_CRYSTAL = RegistryUtil.registerCrystalItem(::CorruptionCrystalItem, CrystalItemIds.CORRUPTION_CRYSTAL)
    val IMITATION_CRYSTAL = RegistryUtil.registerCrystalItem(::ImitationCrystalItem, CrystalItemIds.IMITATION_CRYSTAL)

    val SYNTHESIZED_FORCE_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedForceCrystal, CrystalItemIds.SYNTHESIZED_FORCE_CRYSTAL)
    val SYNTHESIZED_MOMENTUM_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedMomentumCrystal, CrystalItemIds.SYNTHESIZED_MOMENTUM_CRYSTAL)
    val SYNTHESIZED_ENDURANCE_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedEnduranceCrystal, CrystalItemIds.SYNTHESIZED_ENDURANCE_CRYSTAL)
    val SYNTHESIZED_VITALITY_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedVitalityCrystal, CrystalItemIds.SYNTHESIZED_VITALITY_CRYSTAL)
    val SYNTHESIZED_PRECISION_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedPrecisionCrystal, CrystalItemIds.SYNTHESIZED_PRECISION_CRYSTAL)
    val SYNTHESIZED_FOCUS_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedFocusCrystal, CrystalItemIds.SYNTHESIZED_FOCUS_CRYSTAL)
    val SYNTHESIZED_COMMAND_CRYSTAL = RegistryUtil.registerCrystalItem(::SynthesizedCommandCrystal, CrystalItemIds.SYNTHESIZED_COMMAND_CRYSTAL)
}
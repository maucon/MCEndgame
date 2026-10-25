package de.fuballer.mcendgame.client.component.entity.custom

import com.geckolib.loading.math.MolangQueries
import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.blood_queen.BloodQueenEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.elf_duelist.ElfDuelistEntity
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer

@Injectable
class EntityAnimationQueriesRegisterer {
    @Initializer
    fun init() {
        MolangQueries.setActorVariable<ElfDuelistEntity>("query.${MCEndgame.MOD_ID}_limb_swing_amplitude")
        { actor -> actor.animatable.getLimbSwingAmplitude(actor.partialTick) }

        MolangQueries.setActorVariable<ElfDuelistEntity>("query.${MCEndgame.MOD_ID}_limb_swing_cycle")
        { actor -> actor.animatable.getLimbSwingCycle(actor.partialTick) }

        MolangQueries.setActorVariable<ElfDuelistEntity>("query.${MCEndgame.MOD_ID}_lean")
        { actor -> actor.animatable.getLean(actor.partialTick) }

        MolangQueries.setActorVariable<BeastweaverEntity>("query.${MCEndgame.MOD_ID}_transform")
        { actor -> actor.animatable.getTransformProgress(actor.partialTick).toDouble() }

        MolangQueries.setActorVariable<BloodQueenEntity>("query.${MCEndgame.MOD_ID}_lean_right")
        { actor -> actor.animatable.getLeanRight(actor.partialTick).toDouble() }

        MolangQueries.setActorVariable<BloodQueenEntity>("query.${MCEndgame.MOD_ID}_lean_forwards")
        { actor -> actor.animatable.getLeanForwards(actor.partialTick).toDouble() }
    }
}
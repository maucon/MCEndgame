package de.fuballer.mcendgame.main.component.entity.custom

import de.fuballer.mcendgame.main.component.entity.custom.entities.arachne.ArachneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beakburn.BeakburnEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.bonecrusher.BonecrusherEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.elf_duelist.ElfDuelistEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.swamp_golem.SwampGolemEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.world.entity.animal.wolf.Wolf

@Injectable
object EntityAttributeRegisterer {
    @Initializer
    fun register() {
        FabricDefaultAttributeRegistry.register(CustomEntities.SWAMP_GOLEM, SwampGolemEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.ARACHNE, ArachneEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.BONECRUSHER, BonecrusherEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.ELF_DUELIST, ElfDuelistEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.BEAKBURN, BeakburnEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.SCARRED_ONE, ScarredOneEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.SPIDERLING, SpiderlingEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.TRAINING_DUMMY, TrainingDummyEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.SKELETON_MAGE, SkeletonMageEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.BEASTWEAVER, BeastweaverEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.BEASTWEAVER_WOLF, Wolf.createAttributes())
        FabricDefaultAttributeRegistry.register(CustomEntities.BEASTWEAVER_VINE, BeastweaverVineEntity.createAttributes())
    }
}
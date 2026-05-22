package de.fuballer.mcendgame.main.component.entity.custom

import de.fuballer.mcendgame.main.component.entity.custom.entities.arachne.ArachneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beakburn.BeakburnEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.bonecrusher.BonecrusherEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.elf_duelist.ElfDuelistEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.swamp_golem.SwampGolemEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.webhook.WebhookEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.webshot.WebshotEntity
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup

@Injectable
object CustomEntities {
    val SWAMP_GOLEM = RegistryUtil.registerEntity(
        "swamp_golem",
        EntityType.Builder.create({ type, world -> SwampGolemEntity(type, world) }, SpawnGroup.MONSTER)
            .dimensions(0.8f, 1.95f)
            .eyeHeight(1.65f)
            .passengerAttachments(1.8125f)
            .vehicleAttachment(-0.7f)
            .maxTrackingRange(8)
            .notAllowedInPeaceful()
    )
    val ARACHNE = RegistryUtil.registerEntity(
        "arachne",
        EntityType.Builder.create({ type, world -> ArachneEntity(type, world) }, SpawnGroup.MONSTER)
            .dimensions(1.5f, 1.8f)
            .eyeHeight(1.7f)
            .passengerAttachments(1.8125f)
            .vehicleAttachment(-0.7f)
            .maxTrackingRange(8)
            .notAllowedInPeaceful()
    )
    val WEBSHOT = RegistryUtil.registerEntity(
        "webshot",
        EntityType.Builder.create({ type, world -> WebshotEntity(type, world) }, SpawnGroup.MISC)
            .dimensions(0.4f, 0.4f)
    )
    val WEBHOOK = RegistryUtil.registerEntity(
        "webhook",
        EntityType.Builder.create({ type, world -> WebhookEntity(type, world) }, SpawnGroup.MISC)
            .dimensions(0.4f, 0.4f)
    )
    val BONECRUSHER = RegistryUtil.registerEntity(
        "bonecrusher",
        EntityType.Builder.create({ type, world -> BonecrusherEntity(type, world) }, SpawnGroup.MONSTER)
            .dimensions(0.7f, 2.99f)
            .eyeHeight(2.85f)
            .maxTrackingRange(8)
            .notAllowedInPeaceful()
    )
    val ELF_DUELIST = RegistryUtil.registerEntity(
        "elf_duelist",
        EntityType.Builder.create({ type, world -> ElfDuelistEntity(type, world) }, SpawnGroup.MONSTER)
            .dimensions(0.7f, 1.9f)
            .eyeHeight(1.8f)
            .maxTrackingRange(8)
            .notAllowedInPeaceful()
    )
    val BEAKBURN = RegistryUtil.registerEntity(
        "beakburn",
        EntityType.Builder.create({ type, world -> BeakburnEntity(type, world) }, SpawnGroup.MONSTER)
            .dimensions(1.6f, 2.5f)
            .eyeHeight(2.0f)
            .maxTrackingRange(8)
            .notAllowedInPeaceful()
    )
    val SCARRED_ONE = RegistryUtil.registerEntity(
        "scarred_one",
        EntityType.Builder.create({ type, world -> ScarredOneEntity(type, world) }, SpawnGroup.MISC)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(10)
    )
    val SPIDERLING = RegistryUtil.registerEntity(
        "spiderling",
        EntityType.Builder.create({ type, world -> SpiderlingEntity(type, world) }, SpawnGroup.MISC)
            .dimensions(0.7f, 0.45f)
            .eyeHeight(0.325f)
            .maxTrackingRange(8)
    )
    val TRAINING_DUMMY = RegistryUtil.registerEntity(
        "training_dummy",
        EntityType.Builder.create({ type, world -> TrainingDummyEntity(type, world) }, SpawnGroup.MISC)
            .dimensions(0.5F, 1.975F)
            .eyeHeight(1.7775F)
            .maxTrackingRange(10)
    )
}
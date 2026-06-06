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
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

@Injectable
object CustomEntities {
    val SWAMP_GOLEM = RegistryUtil.registerEntity(
        "swamp_golem",
        EntityType.Builder.of({ type, world -> SwampGolemEntity(type, world) }, MobCategory.MONSTER)
            .sized(0.8f, 1.95f)
            .eyeHeight(1.65f)
            .passengerAttachments(1.8125f)
            .ridingOffset(-0.7f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val ARACHNE = RegistryUtil.registerEntity(
        "arachne",
        EntityType.Builder.of({ type, world -> ArachneEntity(type, world) }, MobCategory.MONSTER)
            .sized(1.5f, 1.8f)
            .eyeHeight(1.7f)
            .passengerAttachments(1.8125f)
            .ridingOffset(-0.7f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val WEBSHOT = RegistryUtil.registerEntity(
        "webshot",
        EntityType.Builder.of({ type, world -> WebshotEntity(type, world) }, MobCategory.MISC)
            .sized(0.4f, 0.4f)
    )
    val WEBHOOK = RegistryUtil.registerEntity(
        "webhook",
        EntityType.Builder.of({ type, world -> WebhookEntity(type, world) }, MobCategory.MISC)
            .sized(0.4f, 0.4f)
    )
    val BONECRUSHER = RegistryUtil.registerEntity(
        "bonecrusher",
        EntityType.Builder.of({ type, world -> BonecrusherEntity(type, world) }, MobCategory.MONSTER)
            .sized(0.7f, 2.99f)
            .eyeHeight(2.85f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val ELF_DUELIST = RegistryUtil.registerEntity(
        "elf_duelist",
        EntityType.Builder.of({ type, world -> ElfDuelistEntity(type, world) }, MobCategory.MONSTER)
            .sized(0.7f, 1.9f)
            .eyeHeight(1.8f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val BEAKBURN = RegistryUtil.registerEntity(
        "beakburn",
        EntityType.Builder.of({ type, world -> BeakburnEntity(type, world) }, MobCategory.MONSTER)
            .sized(1.6f, 2.5f)
            .eyeHeight(2.0f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val SCARRED_ONE = RegistryUtil.registerEntity(
        "scarred_one",
        EntityType.Builder.of({ type, world -> ScarredOneEntity(type, world) }, MobCategory.MISC)
            .sized(0.6f, 1.95f)
            .clientTrackingRange(10)
    )
    val SPIDERLING = RegistryUtil.registerEntity(
        "spiderling",
        EntityType.Builder.of({ type, world -> SpiderlingEntity(type, world) }, MobCategory.MISC)
            .sized(0.7f, 0.45f)
            .eyeHeight(0.325f)
            .clientTrackingRange(8)
    )
    val TRAINING_DUMMY = RegistryUtil.registerEntity(
        "training_dummy",
        EntityType.Builder.of({ type, world -> TrainingDummyEntity(type, world) }, MobCategory.MISC)
            .sized(0.5F, 1.975F)
            .eyeHeight(1.7775F)
            .clientTrackingRange(10)
    )
}
package de.fuballer.mcendgame.main.component.entity.custom

import de.fuballer.mcendgame.main.component.entity.custom.entities.arachne.ArachneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.beakburn.BeakburnEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.bonecrusher.BonecrusherEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.elf_duelist.ElfDuelistEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball.SpellFireballEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.swamp_golem.SwampGolemEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.webhook.WebhookEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.webshot.WebshotEntity
import de.fuballer.mcendgame.main.component.portal.PortalEntity
import de.fuballer.mcendgame.main.component.portal.PortalSettings
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

@Injectable
object CustomEntities {
    val PORTAL = RegistryUtil.registerEntity(
        CustomEntityIds.PORTAL,
        EntityType.Builder.of(::PortalEntity, MobCategory.MISC)
            .sized(PortalSettings.DEFAULT_HITBOX_WIDTH, PortalSettings.DEFAULT_HITBOX_HEIGHT)
    )
    val SWAMP_GOLEM = RegistryUtil.registerEntity(
        CustomEntityIds.SWAMP_GOLEM,
        EntityType.Builder.of(::SwampGolemEntity, MobCategory.MONSTER)
            .sized(0.8f, 1.95f)
            .eyeHeight(1.65f)
            .passengerAttachments(1.8125f)
            .ridingOffset(-0.7f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val ARACHNE = RegistryUtil.registerEntity(
        CustomEntityIds.ARACHNE,
        EntityType.Builder.of(::ArachneEntity, MobCategory.MONSTER)
            .sized(1.5f, 1.8f)
            .eyeHeight(1.7f)
            .passengerAttachments(1.8125f)
            .ridingOffset(-0.7f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val WEBSHOT = RegistryUtil.registerEntity(
        CustomEntityIds.WEBSHOT,
        EntityType.Builder.of(::WebshotEntity, MobCategory.MISC)
            .sized(0.4f, 0.4f)
    )
    val WEBHOOK = RegistryUtil.registerEntity(
        CustomEntityIds.WEBHOOK,
        EntityType.Builder.of(::WebhookEntity, MobCategory.MISC)
            .sized(0.4f, 0.4f)
            .eyeHeight(0.2f)
    )
    val SPELL_FIREBALL = RegistryUtil.registerEntity(
        CustomEntityIds.SPELL_FIREBALL,
        EntityType.Builder.of(::SpellFireballEntity, MobCategory.MISC)
            .sized(0.4f, 0.4f)
            .eyeHeight(0.2f)
    )
    val BONECRUSHER = RegistryUtil.registerEntity(
        CustomEntityIds.BONECRUSHER,
        EntityType.Builder.of(::BonecrusherEntity, MobCategory.MONSTER)
            .sized(0.7f, 2.99f)
            .eyeHeight(2.85f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val ELF_DUELIST = RegistryUtil.registerEntity(
        CustomEntityIds.ELF_DUELIST,
        EntityType.Builder.of(::ElfDuelistEntity, MobCategory.MONSTER)
            .sized(0.7f, 1.9f)
            .eyeHeight(1.8f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val BEAKBURN = RegistryUtil.registerEntity(
        CustomEntityIds.BEAKBURN,
        EntityType.Builder.of(::BeakburnEntity, MobCategory.MONSTER)
            .sized(1.6f, 2.5f)
            .eyeHeight(2.0f)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
    val SCARRED_ONE = RegistryUtil.registerEntity(
        CustomEntityIds.SCARRED_ONE,
        EntityType.Builder.of(::ScarredOneEntity, MobCategory.MISC)
            .sized(0.6f, 1.95f)
            .clientTrackingRange(10)
    )
    val SPIDERLING = RegistryUtil.registerEntity(
        CustomEntityIds.SPIDERLING,
        EntityType.Builder.of(::SpiderlingEntity, MobCategory.MISC)
            .sized(0.7f, 0.45f)
            .eyeHeight(0.325f)
            .clientTrackingRange(8)
    )
    val TRAINING_DUMMY = RegistryUtil.registerEntity(
        CustomEntityIds.TRAINING_DUMMY,
        EntityType.Builder.of(::TrainingDummyEntity, MobCategory.MISC)
            .sized(0.5F, 1.975F)
            .eyeHeight(1.7775F)
            .clientTrackingRange(10)
    )
    val SKELETON_MAGE = RegistryUtil.registerEntity(
        CustomEntityIds.SKELETON_MAGE,
        EntityType.Builder.of(::SkeletonMageEntity, MobCategory.MONSTER)
            .sized(0.6F, 1.99F)
            .eyeHeight(1.74F)
            .ridingOffset(-0.7F)
            .clientTrackingRange(8)
            .notInPeaceful()
    )
}
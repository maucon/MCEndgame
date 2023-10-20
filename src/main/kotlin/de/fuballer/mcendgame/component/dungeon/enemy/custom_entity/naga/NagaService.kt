package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.naga

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.LlamaSpit
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import kotlin.math.PI

@Component
class NagaService {
    fun onEntityShootBow(event: EntityShootBowEvent) {
        if (!CustomEntityType.isType(event.entity, CustomEntityType.NAGA)) return

        val spit = event.entity.world.spawnEntity(event.projectile.location, EntityType.LLAMA_SPIT) as LlamaSpit
        spit.velocity = event.projectile.velocity

        val projectiles = mutableListOf(spit)
        for (i in 1..(NagaSettings.EXTRA_PROJECTILES / 2)) {
            projectiles.add(event.entity.launchProjectile(LlamaSpit::class.java, spit.velocity.clone().rotateAroundY(i * 5.0 * PI / 180.0)))
            projectiles.add(event.entity.launchProjectile(LlamaSpit::class.java, spit.velocity.clone().rotateAroundY(-i * 5.0 * PI / 180.0)))
        }

        for (proj in projectiles) {
            proj.shooter = event.entity
            proj.customName = CustomEntityType.POISON_SPIT.customName
        }

        event.entity.world.playSound(event.entity.location, Sound.ENTITY_SPIDER_HURT, SoundCategory.PLAYERS, 1.5f, 1f)

        event.isCancelled = true
    }

    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val spit = event.damager as? LlamaSpit ?: return
        val shooter = spit.shooter ?: return

        val damagedEntity = event.entity as? LivingEntity ?: return

        if (shooter !is LivingEntity) return
        if (!CustomEntityType.isType(shooter, CustomEntityType.NAGA)) return

        val damage = shooter.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: return
        event.damage = damage

        damagedEntity.addPotionEffect(NagaSettings.POISON_EFFECT)
    }
}
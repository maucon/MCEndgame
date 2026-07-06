package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import net.minecraft.entity.damage.DamageTypes

object DodgeSettings {
    val BYPASS_DODGE = listOf(
        DamageTypes.IN_FIRE,
        DamageTypes.CAMPFIRE,
        DamageTypes.ON_FIRE,
        DamageTypes.LAVA,
        DamageTypes.HOT_FLOOR,
        DamageTypes.IN_WALL,
        DamageTypes.CRAMMING,
        DamageTypes.DROWN,
        DamageTypes.STARVE,
        DamageTypes.CACTUS,
        DamageTypes.FALL,
        DamageTypes.FLY_INTO_WALL,
        DamageTypes.OUT_OF_WORLD,
        DamageTypes.GENERIC,
        DamageTypes.MAGIC,
        DamageTypes.WITHER,
        DamageTypes.DRAGON_BREATH,
        DamageTypes.DRY_OUT,
        DamageTypes.SWEET_BERRY_BUSH,
        DamageTypes.FREEZE,
        DamageTypes.STALAGMITE,
        DamageTypes.THORNS,
        DamageTypes.EXPLOSION,
        DamageTypes.PLAYER_EXPLOSION,
        DamageTypes.BAD_RESPAWN_POINT,
        DamageTypes.OUTSIDE_BORDER,
        DamageTypes.GENERIC_KILL,
    )
}
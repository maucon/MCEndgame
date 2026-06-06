package de.fuballer.mcendgame.client.component.datagen

import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.tags.CustomTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import java.util.concurrent.CompletableFuture

class CustomDamageTypeTagProvider(
    packOutput: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagsProvider<DamageType>(packOutput, Registries.DAMAGE_TYPE, registriesFuture) {
    override fun getName() = "${MCEndgame.MOD_ID}DamageTypeTagsProvider"

    override fun addTags(arg: HolderLookup.Provider) {
        getOrCreateRawBuilder(DamageTypeTags.NO_KNOCKBACK)
            .addOptionalElement(CustomDamageTypes.SPELL.identifier())

        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_SHIELD)
            .addOptionalElement(CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE.identifier())
            .addOptionalElement(CustomDamageTypes.SPELL.identifier())

        getOrCreateRawBuilder(CustomTags.MELEE_ATTACK)
            .addElement(DamageTypes.PLAYER_ATTACK.identifier())
            .addElement(DamageTypes.MOB_ATTACK.identifier())
            .addElement(DamageTypes.MOB_ATTACK_NO_AGGRO.identifier())
            .addElement(DamageTypes.STING.identifier())
            .addElement(DamageTypes.MACE_SMASH.identifier())
            .addOptionalElement(CustomDamageTypes.GENERIC_ATTACK.identifier())
            .addOptionalElement(CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE.identifier())
            .addOptionalElement(CustomDamageTypes.PIERCE_ATTACK.identifier())
            .addOptionalElement(CustomDamageTypes.KINETIC_ATTACK.identifier())

        getOrCreateRawBuilder(CustomTags.BLOCK_PHASING_IMMUNE)
            .addElement(DamageTypes.IN_WALL.identifier())
            .addElement(DamageTypes.CACTUS.identifier())
            .addElement(DamageTypes.SWEET_BERRY_BUSH.identifier())
            .addElement(DamageTypes.FALLING_ANVIL.identifier())
            .addElement(DamageTypes.FALLING_STALACTITE.identifier())
    }
}
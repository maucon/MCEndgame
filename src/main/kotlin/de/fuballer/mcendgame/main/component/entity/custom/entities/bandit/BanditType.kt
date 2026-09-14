package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.custom_attribute.data.*
import de.fuballer.mcendgame.main.component.custom_attribute.effects.change_gained_status_effect.GainedStatusEffect
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.goals.BanditBowGoal
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.goals.BanditMeleeGoal
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.misc.CustomMiscItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.equipment.trim.TrimMaterials
import net.minecraft.world.item.equipment.trim.TrimPatterns
import kotlin.random.Random

private const val TRANSLATABLE_BASE_KEY = "entity.mcendgame.bandit."

enum class BanditType(
    val customName: Component,
    val modelType: PlayerModelType,
    val texture: Identifier,
    val equipment: List<BanditItemStack>,
    val rewardCrystal: CrystalItem,
    val fightingGoal: (BanditEntity) -> Goal = { banditEntity -> BanditMeleeGoal(banditEntity, 1.0) },
    val jumpWhileTravel: Boolean = true,
    val randomAttackCooldownFactor: () -> Double = { Random.nextDouble(1.0, 1.4) },
    val jumpCritAttack: Boolean = false,
    val strafeBackAfterTargetHit: Boolean = true,
    val sideStrafeUpdateTime: Int = 10,
    val blockOnEnterDuel: Boolean = true,
    val blockAfterTargetHitProbability: Double = 0.25,
    val blockDuration: () -> Int = { Random.nextInt(20, 40) },
    val hornUseRange: Pair<Double, Double> = Pair(10.0, 30.0),
    val predictMovementProbability: Double = 1.0,
    val predictedMovementRandomFactorRange: Pair<Double, Double> = Pair(0.85, 1.15),
    val dungeonBalanceAttributes: () -> Iterable<CustomAttribute> = { listOf() },
) {
    // bruiser
    DRENN(
        Component.translatable(TRANSLATABLE_BASE_KEY + "drenn"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.FATESPLITTER.defaultInstance),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomToolItems.GRUDGEBEARER.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET)),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.WITHER_ROSE_LEGGINGS.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, ItemStack(Items.NETHERITE_BOOTS)),
        ),
        CrystalItems.SYNTHESIZED_FORCE_CRYSTAL,
        jumpCritAttack = true,
        blockAfterTargetHitProbability = 0.5,
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.2))),
            )
        },
    ),
    SLOANE(
        Component.translatable(TRANSLATABLE_BASE_KEY + "sloane"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/sloane.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.NETHERITE_AXE)),
            BanditItemStack(EquipmentSlot.OFFHAND, ItemStack(Items.NETHERITE_AXE)),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.ICEBORNE.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.DRUIDS_CHESTPLATE.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, ItemStack(Items.NETHERITE_LEGGINGS), trimMaterial = TrimMaterials.COPPER, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.WITHER_ROSE_BOOTS.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_FORCE_CRYSTAL,
        jumpCritAttack = true,
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_HEALTH_RECOVERY, roll = DoubleRoll(DoubleBounds(-0.4))),
            )
        },
    ),

    // assassin
    KEIR(
        Component.translatable(TRANSLATABLE_BASE_KEY + "keir"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/keir.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.NIGHTREAVER.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.ABYSSAL_MASK.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, ItemStack(Items.NETHERITE_CHESTPLATE), trimMaterial = TrimMaterials.AMETHYST, trimPattern = TrimPatterns.RAISER),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.WINDSTRIDER.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.MOONSHADOW.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_MOMENTUM_CRYSTAL,
        jumpCritAttack = true,
        sideStrafeUpdateTime = 8,
    ),
    NIAMH(
        Component.translatable(TRANSLATABLE_BASE_KEY + "niamh"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/niamh.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.SERPENTS_FANG.defaultInstance, enchantments = mapOf(Enchantments.FIRE_ASPECT to 2)),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomToolItems.SERPENTS_FANG.defaultInstance, enchantments = mapOf(Enchantments.FIRE_ASPECT to 2)),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.SUEDE_HELMET.defaultInstance, dyedColor = DyedItemColor(6825251)),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.SUEDE_CHESTPLATE.defaultInstance, dyedColor = DyedItemColor(6825251)),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.SUEDE_LEGGINGS.defaultInstance, dyedColor = DyedItemColor(6825251)),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.SUEDE_BOOTS.defaultInstance, dyedColor = DyedItemColor(6825251)),
        ),
        CrystalItems.SYNTHESIZED_MOMENTUM_CRYSTAL,
        sideStrafeUpdateTime = 5,
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.DODGE, roll = DoubleRoll(DoubleBounds(0.15))),
            )
        },
    ),

    // tank
    HADRIK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "hadrik"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/hadrik.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.MACE)),
            BanditItemStack(EquipmentSlot.OFFHAND, ItemStack(Items.SHIELD)),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET), trimMaterial = TrimMaterials.NETHERITE, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.CHEST, ItemStack(Items.NETHERITE_CHESTPLATE), trimMaterial = TrimMaterials.NETHERITE, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.STONEWARD.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.WITHER_ROSE_BOOTS.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_ENDURANCE_CRYSTAL,
        jumpCritAttack = true,
        sideStrafeUpdateTime = 12,
        blockDuration = { Random.nextInt(30, 50) },
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, roll = DoubleRoll(DoubleBounds(-0.2))),
            )
        },
    ),
    BRINA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "brina"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/brina.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.GRAVEBREAKER.defaultInstance),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomMiscItems.FRIGID_CRY.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.ICEBORNE.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.STONEWARD.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.SUEDE_BOOTS.defaultInstance, dyedColor = DyedItemColor(1908001)),
        ),
        CrystalItems.SYNTHESIZED_ENDURANCE_CRYSTAL,
        jumpWhileTravel = false,
        hornUseRange = Pair(0.0, 7.0),
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(
                    CustomAttributeTypes.CHANGE_GAINED_STATUS_EFFECT,
                    rolls = listOf(
                        StringRoll(StringBounds(GainedStatusEffect.RESISTANCE.displayName)),
                        StringRoll(StringBounds(GainedStatusEffect.WITHER.displayName)),
                    ),
                ),
                CustomAttribute(CustomAttributeTypes.RESISTANCE_WHEN_LOW_HEALTH, roll = IntRoll(IntBounds(10))),
            )
        },
    ),

    // healer
    EMRYS(
        Component.translatable(TRANSLATABLE_BASE_KEY + "emrys"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/emrys.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.NETHERITE_SWORD)),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomMiscItems.VERDANT_ECHO.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET), trimMaterial = TrimMaterials.COPPER, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.DRUIDS_CHESTPLATE.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.DRUIDS_LEGGINGS.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.DRUIDS_BOOTS.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_VITALITY_CRYSTAL,
        jumpCritAttack = true,
        hornUseRange = Pair(0.0, 10.0),
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_COMPANION_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.5))),
                CustomAttribute(CustomAttributeTypes.MORE_HEALTH_RECOVERY, roll = DoubleRoll(DoubleBounds(-0.5))),
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.15))),
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, roll = DoubleRoll(DoubleBounds(0.1))),
            )
        },
    ),
    NESSA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "nessa"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.RADIANT_DAWN.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET), trimMaterial = TrimMaterials.GOLD, trimPattern = TrimPatterns.WARD),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.BOUND_ABYSS.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, ItemStack(Items.NETHERITE_LEGGINGS)),
            BanditItemStack(EquipmentSlot.FEET, ItemStack(Items.NETHERITE_BOOTS), trimMaterial = TrimMaterials.GOLD, trimPattern = TrimPatterns.WARD),
        ),
        CrystalItems.SYNTHESIZED_VITALITY_CRYSTAL,
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_HEALTH_RECOVERY, roll = DoubleRoll(DoubleBounds(-0.15))),
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, roll = DoubleRoll(DoubleBounds(0.05))),
            )
        },
    ),

    // archer
    LORCAN(
        Component.translatable(TRANSLATABLE_BASE_KEY + "lorcan"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/lorcan.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.WINDSTRING.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.SUEDE_HELMET.defaultInstance, dyedColor = DyedItemColor(5988218)),
            BanditItemStack(EquipmentSlot.CHEST, ItemStack(Items.NETHERITE_CHESTPLATE), trimMaterial = TrimMaterials.LAPIS, trimPattern = TrimPatterns.SNOUT),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.GILDED_TEMPEST.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.SUEDE_BOOTS.defaultInstance, dyedColor = DyedItemColor(3949738)),
        ),
        CrystalItems.SYNTHESIZED_PRECISION_CRYSTAL,
        fightingGoal = { banditEntity -> BanditBowGoal(banditEntity, 1.0, 20f) },
        predictMovementProbability = 0.75,
        predictedMovementRandomFactorRange = Pair(0.95, 1.15),
    ),
    YVRA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "yvra"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/yvra.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.HAILSTORM.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET), trimMaterial = TrimMaterials.DIAMOND, trimPattern = TrimPatterns.COAST),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.SUEDE_CHESTPLATE.defaultInstance, dyedColor = DyedItemColor(10082796)),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.WINDSTRIDER.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.SUEDE_BOOTS.defaultInstance, dyedColor = DyedItemColor(10082796)),
        ),
        CrystalItems.SYNTHESIZED_PRECISION_CRYSTAL,
        fightingGoal = { banditEntity -> BanditBowGoal(banditEntity, 1.0, 20f) },
        predictMovementProbability = 0.75,
        predictedMovementRandomFactorRange = Pair(0.75, 1.25),
    ),

    // mage
    CATHAL(
        Component.translatable(TRANSLATABLE_BASE_KEY + "cathal"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/cathal.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.NETHERITE_SWORD)),
            BanditItemStack(EquipmentSlot.OFFHAND, ItemStack(Items.SHIELD)),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.WITHER_ROSE_HELMET.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, ItemStack(Items.NETHERITE_CHESTPLATE), trimMaterial = TrimMaterials.NETHERITE, trimPattern = TrimPatterns.WARD),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.WITHER_ROSE_LEGGINGS.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.EMBERREIGN.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_FOCUS_CRYSTAL,
        sideStrafeUpdateTime = 12,
    ),
    MAEVE(
        Component.translatable(TRANSLATABLE_BASE_KEY + "maeve"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/maeve.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.TWINFIRE.defaultInstance),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomToolItems.TWINFIRE.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.EMBERCHANT.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.VOIDWEAVER.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.LAMIAS_GIFT.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, ItemStack(Items.NETHERITE_BOOTS)),
        ),
        CrystalItems.SYNTHESIZED_FOCUS_CRYSTAL,
        jumpWhileTravel = false,
        sideStrafeUpdateTime = 8,
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.3))),
                CustomAttribute(CustomAttributeTypes.MORE_SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.15))),
            )
        },
    ),

    // summoner / companion
    BRAKK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "brakk"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/brakk.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.NETHERITE_AXE)),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomMiscItems.MOLTEN_ROAR.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, CustomArmorItems.DRUIDS_HELMET.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, ItemStack(Items.NETHERITE_CHESTPLATE), trimMaterial = TrimMaterials.COPPER, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.LEGS, ItemStack(Items.NETHERITE_LEGGINGS), trimMaterial = TrimMaterials.COPPER, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.DRUIDS_BOOTS.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_COMMAND_CRYSTAL,
        jumpCritAttack = true,
        sideStrafeUpdateTime = 8,
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_COMPANION_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.5))),
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.15))),
            )
        },
    ),
    MORRIGAN(
        Component.translatable(TRANSLATABLE_BASE_KEY + "morrigan"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/morrigan.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.DUSK_PIERCER.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET), trimMaterial = TrimMaterials.NETHERITE, trimPattern = TrimPatterns.SILENCE),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.BROODMOTHER.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.WINDSTRIDER.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.MOONSHADOW.defaultInstance),
        ),
        CrystalItems.SYNTHESIZED_COMMAND_CRYSTAL,
        fightingGoal = { banditEntity -> BanditBowGoal(banditEntity, 1.0, 20f) },
        jumpWhileTravel = false,
        predictMovementProbability = 1.0,
        predictedMovementRandomFactorRange = Pair(0.95, 1.05),
        dungeonBalanceAttributes = {
            listOf(
                CustomAttribute(CustomAttributeTypes.MORE_COMPANION_DAMAGE, roll = DoubleRoll(DoubleBounds(-0.5))),
                CustomAttribute(CustomAttributeTypes.MORE_PROJECTILE_DAMAGE, roll = DoubleRoll(DoubleBounds(0.15))),
            )
        },
    );

    companion object {
        val DEFAULT = DRENN
    }
}
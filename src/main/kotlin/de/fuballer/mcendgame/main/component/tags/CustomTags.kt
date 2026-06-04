package de.fuballer.mcendgame.main.component.tags

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@Injectable
object CustomTags {
    val DUNGEON_DISABLED: TagKey<Item> = createItemTag("dungeon_disabled")
    val DIAMOND_GEAR: TagKey<Item> = createItemTag("diamond_gear")
    val NETHERITE_GEAR: TagKey<Item> = createItemTag("netherite_gear")
    val BOW: TagKey<Item> = createItemTag("bow")
    val SHIELD: TagKey<Item> = createItemTag("shield")
    val MACE: TagKey<Item> = createItemTag("mace")
    val DUNGEON_DROP_DISABLED: TagKey<Item> = createItemTag("dungeon_drop_disabled")
    val CRYSTAL: TagKey<Item> = createItemTag("crystal")
    val TOTEM: TagKey<Item> = createItemTag("totem")
    val REPAIRS_SPIDER_ARMOR: TagKey<Item> = createItemTag("repairs_spider_armor")

    val DUNGEON_BREAKABLE: TagKey<Block> = createBlockTag("dungeon_breakable")
    val DUNGEON_INTERACTABLE: TagKey<Block> = createBlockTag("dungeon_interactable")
    val PHASING_BLOCKING: TagKey<Block> = createBlockTag("phasing_blocking")
    val NO_PHASING_SLOW_AND_FOG: TagKey<Block> = createBlockTag("no_phasing_slow_and_fog")

    val MELEE_ATTACK: TagKey<DamageType> = createDamageTypeTag("melee_attack")
    val BLOCK_PHASING_IMMUNE: TagKey<DamageType> = createDamageTypeTag("block_phasing_immune")

    private fun createItemTag(id: String) = TagKey.create(Registries.ITEM, IdentifierUtil.default(id))
    private fun createBlockTag(id: String) = TagKey.create(Registries.BLOCK, IdentifierUtil.default(id))
    private fun createDamageTypeTag(id: String) = TagKey.create(Registries.DAMAGE_TYPE, IdentifierUtil.default(id))
}
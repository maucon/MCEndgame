package de.fuballer.mcendgame.main.component.tags

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.block.Block
import net.minecraft.entity.damage.DamageType
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

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
    val PHASING_BLOCKING: TagKey<Block> = createBlockTag("phasing_blocking")
    val NO_PHASING_SLOW_AND_FOG: TagKey<Block> = createBlockTag("no_phasing_slow_and_fog")

    val MELEE_ATTACK: TagKey<DamageType> = createDamageTypeTag("melee_attack")
    val BLOCK_PHASING_IMMUNE: TagKey<DamageType> = createDamageTypeTag("block_phasing_immune")

    private fun createItemTag(id: String) = TagKey.of(RegistryKeys.ITEM, IdentifierUtil.default(id))
    private fun createBlockTag(id: String) = TagKey.of(RegistryKeys.BLOCK, IdentifierUtil.default(id))
    private fun createDamageTypeTag(id: String) = TagKey.of(RegistryKeys.DAMAGE_TYPE, IdentifierUtil.default(id))
}
package de.fuballer.mcendgame.framework

import de.fuballer.mcendgame.framework.stereotype.Entity
import de.fuballer.mcendgame.framework.stereotype.Repository

abstract class InMemoryMapRepository<ID, ENTITY : Entity<ID>> : Repository<ID, ENTITY> {
    protected var map = mutableMapOf<ID, ENTITY>()

    override fun findAll() = map.values.toList()

    override fun findById(id: ID) = map[id]

    override fun getById(id: ID) = findById(id)!!

    override fun exists(id: ID) = map.containsKey(id)

    override fun save(entity: ENTITY): ENTITY {
        map[entity.id] = entity
        return entity
    }

    override fun deleteById(id: ID) = map.remove(id)

    override fun delete(entity: ENTITY) = map.remove(entity.id)
}
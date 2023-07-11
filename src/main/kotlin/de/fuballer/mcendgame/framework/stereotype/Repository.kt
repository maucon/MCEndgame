package de.fuballer.mcendgame.framework.stereotype

interface Repository<ID, ENTITY : Entity<ID>> : Injectable {
    fun load() {}
    fun flush() {}

    fun findAll(): List<ENTITY>
    fun findById(id: ID): ENTITY?
    fun getById(id: ID): ENTITY

    fun exists(id: ID): Boolean

    fun save(entity: ENTITY): ENTITY
    fun delete(id: ID)
}

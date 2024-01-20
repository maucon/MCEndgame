package de.fuballer.mcendgame.domain.technical.registry

class KeyedRegistry<T : Keyed> : HashMap<String, T>() {
    fun register(t: T) {
        this[t.key.toString()] = t
    }
}
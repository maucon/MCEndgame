package de.fuballer.mcendgame.technical.registry

class KeyedRegistry<T : Keyed> : HashMap<String, T>() {
    fun register(t: T) {
        this[t.key.toString()] = t
    }
}
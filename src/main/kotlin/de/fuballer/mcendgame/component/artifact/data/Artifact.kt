package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.domain.artifact.ArtifactType

data class Artifact(
    var type: ArtifactType,
    var tier: Int
)

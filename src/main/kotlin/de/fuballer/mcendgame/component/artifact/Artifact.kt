package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.domain.artifact.ArtifactType

data class Artifact(
    var type: ArtifactType,
    var tier: Int
)

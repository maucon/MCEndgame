package de.fuballer.mcendgame.client.accessor;

public interface BufferBuilderSetVertexElementsAccessor {
    void mcendgame$addVertex(
            final float x,
            final float y,
            final float z,
            final int color,
            final float u,
            final float v,
            final int overlayCoords,
            final int lightCoords,
            final float nx,
            final float ny,
            final float nz,
            final float gradOriginX,
            final float gradOriginY,
            final float gradOriginZ,
            final float gradStart,
            final float gradEnd
    );
}

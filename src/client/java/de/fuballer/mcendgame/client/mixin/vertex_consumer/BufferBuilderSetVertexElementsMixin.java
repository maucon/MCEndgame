package de.fuballer.mcendgame.client.mixin.vertex_consumer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.fuballer.mcendgame.client.accessor.BufferBuilderSetVertexElementsAccessor;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderSetVertexElementsMixin implements BufferBuilderSetVertexElementsAccessor {
    @Shadow
    protected abstract long beginVertex();

    @Shadow
    private static void putRgba(long pointer, int argb) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static void putPackedUv(long pointer, int packedUv) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static byte normalIntValue(float c) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    public abstract VertexConsumer addVertex(float x, float y, float z);

    @Shadow
    public abstract VertexConsumer setColor(int color);

    @Shadow
    public abstract VertexConsumer setUv(float u, float v);

    @Shadow
    public abstract VertexConsumer setOverlay(int packedOverlayCoords);

    @Shadow
    public abstract VertexConsumer setLight(int packedLightCoords);

    @Shadow
    public abstract VertexConsumer setNormal(float x, float y, float z);

    @Shadow
    private static void putVec3f(long pointer, float x, float y, float z) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static void putNormals(long pointer, float nx, float ny, float nz) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Override
    public void mcendgame$addVertex(
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
    ) {
        long pointer = beginVertex();
        putVec3f(pointer, x, y, z);
        putRgba(pointer + 12L, color);
        MemoryUtil.memPutFloat(pointer + 16L, u);
        MemoryUtil.memPutFloat(pointer + 20L, v);
        putPackedUv(pointer + 24L, overlayCoords);
        putPackedUv(pointer + 28L, lightCoords);
        putNormals(pointer + 32L, nx, ny, nz);

        putVec3f(pointer + 36L, gradOriginX, gradOriginY, gradOriginZ);
        MemoryUtil.memPutFloat(pointer + 48L, gradStart);
        MemoryUtil.memPutFloat(pointer + 52L, gradEnd);
    }
}

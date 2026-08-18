package de.fuballer.mcendgame.client.mixin.vertex_consumer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import de.fuballer.mcendgame.client.accessor.BufferBuilderSetVertexElementsAccessor;
import de.fuballer.mcendgame.client.component.render.CustomVertexFormatElements;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderSetVertexElementsMixin implements BufferBuilderSetVertexElementsAccessor {
    @Shadow
    private long beginElement(final VertexFormatElement element) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private boolean fastFormat;

    @Shadow
    protected abstract long beginVertex();

    @Shadow
    private static void putRgba(long pointer, int argb) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private boolean fullFormat;

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
        addVertex(x, y, z);
        setColor(color);
        setUv(u, v);
        setOverlay(overlayCoords);
        setLight(lightCoords);
        setNormal(nx, ny, nz);
        mcendgame$setGradientOrigin(gradOriginX, gradOriginY, gradOriginZ);
        mcendgame$setGradientBounds(gradStart, gradEnd);
    }

    private VertexConsumer mcendgame$setGradientOrigin(float x, float y, float z) {
        var this_ = (BufferBuilder) (Object) this;
        long pointer = beginElement(CustomVertexFormatElements.INSTANCE.getGRADIENT_ORIGIN());

        if (pointer != -1L) {
            MemoryUtil.memPutFloat(pointer, x);
            MemoryUtil.memPutFloat(pointer + 4L, y);
            MemoryUtil.memPutFloat(pointer + 8L, z);
        }

        return this_;
    }

    private VertexConsumer mcendgame$setGradientBounds(float start, float end) {
        var this_ = (BufferBuilder) (Object) this;
        long pointer = beginElement(CustomVertexFormatElements.INSTANCE.getGRADIENT_BOUNDS());

        if (pointer != -1L) {
            MemoryUtil.memPutFloat(pointer, start);
            MemoryUtil.memPutFloat(pointer + 4L, end);
        }

        return this_;
    }
}

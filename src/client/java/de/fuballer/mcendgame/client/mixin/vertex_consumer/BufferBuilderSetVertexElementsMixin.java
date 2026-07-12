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
        if (fastFormat) {
            long pointer = this.beginVertex();
            MemoryUtil.memPutFloat(pointer + 0L, x);
            MemoryUtil.memPutFloat(pointer + 4L, y);
            MemoryUtil.memPutFloat(pointer + 8L, z);
            putRgba(pointer + 12L, color);
            MemoryUtil.memPutFloat(pointer + 16L, u);
            MemoryUtil.memPutFloat(pointer + 20L, v);
            long lightStart;
            if (fullFormat) {
                putPackedUv(pointer + 24L, overlayCoords);
                lightStart = pointer + 28L;
            } else {
                lightStart = pointer + 24L;
            }

            putPackedUv(lightStart + 0L, lightCoords);

            long gradientOriginStart = lightStart + 4L;
            if (fullFormat) {
                gradientOriginStart += 3L;
                MemoryUtil.memPutByte(lightStart + 4L, normalIntValue(nx));
                MemoryUtil.memPutByte(lightStart + 5L, normalIntValue(ny));
                MemoryUtil.memPutByte(lightStart + 6L, normalIntValue(nz));
            }

            MemoryUtil.memPutFloat(gradientOriginStart + 0L, gradOriginX);
            MemoryUtil.memPutFloat(gradientOriginStart + 4L, gradOriginY);
            MemoryUtil.memPutFloat(gradientOriginStart + 8L, gradOriginZ);
            
            MemoryUtil.memPutFloat(gradientOriginStart + 12L, gradStart);
            MemoryUtil.memPutFloat(gradientOriginStart + 16L, gradEnd);
        } else {
            addVertex(x, y, z);
            setColor(color);
            setUv(u, v);
            setOverlay(overlayCoords);
            setLight(lightCoords);
            setNormal(nx, ny, nz);
            mcendgame$setGradientOrigin(gradOriginX, gradOriginY, gradOriginZ);
            mcendgame$setGradientBounds(gradStart, gradEnd);
        }
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

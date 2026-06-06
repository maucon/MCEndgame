#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

#ifdef DISSOLVE
uniform sampler2D DissolveMaskSampler;
#endif

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
#ifdef PER_FACE_LIGHTING
in vec4 vertexPerFaceColorBack;
in vec4 vertexPerFaceColorFront;
#else
in vec4 vertexColor;
#endif

#ifndef EMISSIVE
in vec4 lightMapColor;
#endif

#ifndef NO_OVERLAY
in vec4 overlayColor;
#endif

in vec2 texCoord0;

out vec4 fragColor;

void main() {
vec4 color = texture(Sampler0, texCoord0);
#ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
discard;
}
#endif

#ifdef PER_FACE_LIGHTING
    vec4 faceVertexColor = gl_FrontFacing ? vertexPerFaceColorFront : vertexPerFaceColorBack;
#else
    vec4 faceVertexColor = vertexColor;
#endif

    float heat = faceVertexColor.r;

    // base texture color
    vec3 base = color.rgb;

    // 1. base warm shift (yellow → orange → red)
    vec3 hotColor = base;

    // push red up strongly
    hotColor.r += heat * 0.6;

    // pull green down slightly (turns yellow → orange → red)
    hotColor.g *= mix(1.0, 0.4, heat);

    // blue gets crushed fast (hot metal loses blue fast)
    hotColor.b *= mix(1.0, 0.1, heat);

    // 2. simulate heat glow (brightness boost)
    float emission = heat * heat; // quadratic for stronger high-end glow
    hotColor += base * emission * 0.3;

    // 3. slight “overheat darkening” at extreme heat (molten feel)
    hotColor *= mix(1.0, 0.85, heat * heat);

    color.rgb = hotColor;

#ifdef DISSOLVE
    if (faceVertexColor.a < texture(DissolveMaskSampler, texCoord0).a) {
discard;
}
// The dissolve effect entirely replaces translucency
faceVertexColor.a = 1.0;
#endif

color *= ColorModulator;
#ifndef NO_OVERLAY
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
#endif
#ifndef EMISSIVE
    color *= lightMapColor;
#endif

fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}

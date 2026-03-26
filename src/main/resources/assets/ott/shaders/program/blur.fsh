#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 BlurDir;
uniform float Radius;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 blurred = vec4(0.0);
    float totalWeight = 0.0;
    vec2 oneTexel = 1.0 / InSize;

    for (float r = -Radius; r <= Radius; r += 1.0) {
        float weight = exp(-0.5 * (r * r) / (Radius * Radius * 0.25));
        blurred += texture(DiffuseSampler, texCoord + oneTexel * BlurDir * r) * weight;
        totalWeight += weight;
    }
    fragColor = blurred / totalWeight;
}
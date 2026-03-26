#version 150

uniform sampler2D DiffuseSampler;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float r = dot(color.rgb, RedMatrix);
    float g = dot(color.rgb, GreenMatrix);
    float b = dot(color.rgb, BlueMatrix);
    fragColor = vec4(r, g, b, color.a);
}
#version 150

uniform sampler2D InSampler;
uniform float BrightenSkyFactor;

in vec2 texCoord;

out vec4 fragColor;

vec3 getColor(float blockLight, float skyLight) {
	// convert light levels in range [0.0 - 1.0] to texture coordinates
	vec2 lightTexCoord = (vec2(blockLight, skyLight) * 15.0 + 0.5) / 16.0;
	return texture(InSampler, lightTexCoord).rgb;
}

void main() {
	// get light levels in range [0.0 - 1.0]
	float blockLight = floor(texCoord.x * 16.0) / 15.0;
	float skyLight = floor(texCoord.y * 16.0) / 15.0;

	vec3 color = getColor(blockLight, mix(skyLight, 1.0, BrightenSkyFactor));
	fragColor = vec4(color, 1.0);
}

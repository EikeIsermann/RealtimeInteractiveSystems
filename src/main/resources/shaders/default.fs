varying vec3 fcolor;
varying vec2 ftexCoord;
uniform sampler2D tex;

void main() {
    vec4 texColor = texture2D( tex, ftexCoord );
    gl_FragColor = texColor;
}
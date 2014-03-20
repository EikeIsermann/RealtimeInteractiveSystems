uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;


attribute vec3 vertex;
attribute vec3 normal;
attribute vec2 texCoord;
attribute vec3 color;
varying vec3 fcolor;
varying vec2 ftexCoord;

void main() {
  fcolor = color;
  ftexCoord = texCoord;
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertex, 1);
}
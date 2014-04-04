// ------------------ Fragment Shader --------------------------------
#version 120
#extension GL_EXT_gpu_shader4 : enable

noperspective varying vec3 dist;
const vec4 WIRE_COL = vec4(1.0,1.0,0.0,1);
const vec4 FILL_COL = vec4(1,1,1,1);

void main(void)
{
	float d = min(dist[0],min(dist[1],dist[2]));
 	float I = exp2(-2*d*d);
 	gl_FragColor = I*WIRE_COL + (1.0 - I)*FILL_COL;
}
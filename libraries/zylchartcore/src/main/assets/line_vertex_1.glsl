uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCamera;
attribute vec3 aPosition;
attribute vec3 aNormal;

void main()
{
    gl_Position = uMVPMatrix * vec4(aPosition,1);
}
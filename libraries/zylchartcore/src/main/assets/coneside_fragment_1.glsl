precision mediump float;
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;

void main()
{
   vec4 finalColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);
   gl_FragColor = finalColor * vambient +
                  finalColor * vspecular +
                  finalColor * vdiffuse;
}              
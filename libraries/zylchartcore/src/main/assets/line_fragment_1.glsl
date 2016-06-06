precision mediump float;

void main()
{
   vec4 finalColor = vec4(1.0f, 0.1f, 0.1f, 1.0f);
   gl_FragColor = finalColor * vec4(0.8f, 0.8f, 0.8f, 1);
}              
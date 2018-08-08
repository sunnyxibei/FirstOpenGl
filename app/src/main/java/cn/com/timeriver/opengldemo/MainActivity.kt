package cn.com.timeriver.opengldemo

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private val glSurfaceView by lazy { GLSurfaceView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(glSurfaceView)
        val pointRender = object : GLSurfaceView.Renderer {
            private val VERTEX_SHADER =
                    "void main() {\n" +
                            "gl_Position = vec4(0.0, 0.0, 0.0, 1.0);\n" +
                            "gl_PointSize = 20.0;\n" +
                            "}\n"
            private val FRAGMENT_SHADER =
                    "void main() {\n" +
                            "gl_FragColor = vec4(1., 0., 0.0, 1.0);\n" +
                            "}\n"
            private var mGLProgram: Int = -1
            override fun onDrawFrame(gl: GL10?) {
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
                GLES20.glUseProgram(mGLProgram)

                GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                GLES20.glViewport(0, 0, width, height)
            }

            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                GLES20.glClearColor(1f, 1f, 1f, 1f)

                val vsh = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
                GLES20.glShaderSource(vsh, VERTEX_SHADER)
                GLES20.glCompileShader(vsh)

                val fsh = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
                GLES20.glShaderSource(fsh, FRAGMENT_SHADER)
                GLES20.glCompileShader(fsh)

                mGLProgram = GLES20.glCreateProgram()
                GLES20.glAttachShader(mGLProgram, vsh)
                GLES20.glAttachShader(mGLProgram, fsh)
                GLES20.glLinkProgram(mGLProgram)

                GLES20.glValidateProgram(mGLProgram)
                //如果有语法错误，编译错误，或者状态出错，这一步是能够检查出来的。
                //如果一切正常，则取出来的status[0]为0。
                val status = IntArray(1)
                GLES20.glGetProgramiv(mGLProgram, GLES20.GL_VALIDATE_STATUS, status, 0)
                Log.d(TAG, "validate shader program: " + GLES20.glGetProgramInfoLog(mGLProgram))
            }

        }
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(pointRender)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

}

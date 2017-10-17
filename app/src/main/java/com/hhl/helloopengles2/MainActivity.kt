package com.hhl.helloopengles2

import android.opengl.GLES20
import android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY
import android.opengl.GLSurfaceView.Renderer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity(), Renderer {

    private val triangle: Triangle = Triangle()

    override fun onDrawFrame(gl: GL10?) {
        //开始绘制
        triangle.onDrawFrame()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置视图窗口
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        triangle.onCreated()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surface_view.preserveEGLContextOnPause = true
        surface_view.setEGLContextClientVersion(2)
        surface_view.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        surface_view.setRenderer(this)
        surface_view.renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onResume() {
        super.onResume()
        surface_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        surface_view.onPause()
    }
}

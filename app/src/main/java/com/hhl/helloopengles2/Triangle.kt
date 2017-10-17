package com.hhl.helloopengles2

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by hanhailong on 2017/10/17.
 */
class Triangle {

    private val triangleCoords = floatArrayOf(
            0.0f, 0.5f, 0.0f,//顶点Top
            -0.5f, -0.5f, 0.0f,// bottom left
            0.5f, -0.5f, 0.0f// bottom right
    )

    private val vertexCount = triangleCoords.size / Companion.COORDS_PER_VERTEX

    //顶点之间的偏移量，一个顶点占四个字节
    private val vertexOffset = Companion.COORDS_PER_VERTEX * 4

    private val color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f) //白色

    //着色器
    private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}"
    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"

    //初始化
    init {

    }

    fun onDrawFrame() {
        //将程序加入到OpenGl ES2.0环境
        GLES20.glUseProgram(Companion.program)

        //获取顶点着色器vPosition的句柄
        val positionHandle = GLES20.glGetAttribLocation(Companion.program, "vPosition")
        //启动三角形顶点句柄
        GLES20.glEnableVertexAttribArray(positionHandle)

        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandle, Companion.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexOffset, Companion.vertexBuffer)

        //获取片元着色器的vColor成员的句柄
        val colorHandle = GLES20.glGetUniformLocation(Companion.program, "vColor")
        //绘制三角形的颜色
        GLES20.glUniform4fv(colorHandle, 1, color, 0)

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    fun onCreated() {
        //设置背景颜色为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f)
        //申请底层空间，存放顶点坐标，每个float占4个字节
        val bb: ByteBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())

        //将数据转换为FloatBuffer，用以传给OpenGL ES
        Companion.vertexBuffer = bb.asFloatBuffer()
        //将坐标加入FloatBuffer中
        Companion.vertexBuffer?.put(triangleCoords)
        //设置buffer，从第一个坐标开始读取
        Companion.vertexBuffer?.position(0)

        //下面设置顶点着色器和片元着色器，即VertexShader和FragmentShader
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        //创建一个空的OpenGL ES程序
        Companion.program = GLES20.glCreateProgram()
        //将顶点着色器加入程序
        GLES20.glAttachShader(Companion.program, vertexShader)
        //将片元着色器加入到程序
        GLES20.glAttachShader(Companion.program, fragmentShader)
        //将片元着色器加入程序
        GLES20.glLinkProgram(Companion.program)
    }

    fun loadShader(type: Int, shaderCode: String): Int {
        //根据type创建顶点着色器或者片元着色器
        val shader = GLES20.glCreateShader(type)
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    companion object {
        private var program: Int = 0
        //每个顶点的坐标数量
        private val COORDS_PER_VERTEX = 3
        private var vertexBuffer: FloatBuffer? = null
    }
}
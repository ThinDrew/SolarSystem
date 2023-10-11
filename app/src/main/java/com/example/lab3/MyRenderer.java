package com.example.lab3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
class MyGLRenderer implements GLSurfaceView.Renderer {
    static public int[] texture_name = {
            R.drawable.sun,
            R.drawable.earth,
            R.drawable.moon
    };
    static public int[] textures = new int [texture_name.length];
    Context c;
    private final Sphere Sun = new Sphere(2f);
    private final Sphere Earth = new Sphere(1f);
    private final Sphere Moon = new Sphere(0.5f);
    private float angle = 40.0f;
    private float mTransY = 0f;
    public MyGLRenderer(Context context) {
        c = context;
    }
    private void loadGLTexture(GL10 gl) {
        gl.glGenTextures(3, textures, 0);
        for (int i = 0; i < texture_name.length; ++i) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_LINEAR);
            InputStream is = c.getResources().openRawResource(texture_name[i]);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0,0,0,1);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);
        loadGLTexture(gl);
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;
        float aspect = (float)width / height;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        angle = (angle == 360) ? 0 : angle + 0.5f;

        gl.glLoadIdentity();
        gl.glTranslatef(0,0,-6); //"отодвигаемся" назад
        gl.glScalef(0.25f, 0.25f, 0.25f);
        gl.glRotatef(45,1,0,0);//поворачиваем камеру под углом 45 градусов

        //draw sun
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, Sun.textureBuffer);
        gl.glPushMatrix();
        gl.glRotatef(angle, 0, 1, 0);
        Sun.onDrawFrame(gl);
        gl.glPopMatrix();

        //draw earth
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, Sun.textureBuffer);
        gl.glPushMatrix();
        gl.glRotatef(angle, 0, 1, 0);
        gl.glTranslatef(5, 0, 0);
        Earth.onDrawFrame(gl);
//        gl.glPopMatrix();

        //draw moon
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, Sun.textureBuffer);
        gl.glPushMatrix();
        gl.glRotatef(angle, 0, 1, 0);
        gl.glTranslatef(2, 0, 0);
        Moon.onDrawFrame(gl);
//        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
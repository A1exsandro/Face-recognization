package com.example.facerecognization

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

private const val LEFT_EYE = 4
private const val RADIUS = 10f
private const val TEXT_SIZE = 50f
private const val CORNER_RADIUS = 2f
private const val STROKE_WIDTH = 5f

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var defaultBitmap: Bitmap
    lateinit var temporaryBitmap: Bitmap
    lateinit var eyePatchBitmap: Bitmap
    lateinit var canvas: Canvas

    val rectPaint = Paint()
    val faceDetector: FaceDetector
        get() = initializeDeted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById<View>(R.id.image_view) as ImageView
    }

    fun processImage(view: View){
        val bitmapOptions = BitmapFactory.Options().apply {
            inMutable = true
        }
        initializeBitmap(bitmapOptions)
        createRectanglePaint()

        canvas = Canvas(temporaryBitmap).applay {
            drawBitmap(defaultBitmap, 0f, 0f , null)
        }

        if(!faceDetector.isOperational){
            alertDialog.Builder(this)
                .setMessage("Face Detector cannot be setup on this device")
                .show()
        }else{
            val frame = Frame.Builder().setBitmap(defaultBitmap).build()
            val sparseArray = faceDetector.detect(frame)
            detectFaces(sparseArray)
            imageView.setImageDrawable(BitmapDrawable(resources, temporaryBitmap))
            faceDetector.release()
        }
    }
}
package org.geeksforgeeks.paint

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.slider.RangeSlider
import com.mihir.drawingcanvas.drawingView
import android.Manifest
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private lateinit var draw: drawingView
    private lateinit var undo: ImageButton
    private lateinit var redo: ImageButton
    private lateinit var color: ImageButton
    private lateinit var stroke: ImageButton
    private lateinit var clear: Button
    private lateinit var save: Button
    private lateinit var rangeSlider: RangeSlider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check & request storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }

        draw = findViewById(R.id.draw_view)
        undo = findViewById(R.id.btn_undo)
        redo = findViewById(R.id.btn_redo)
        color = findViewById(R.id.btn_color)
        clear = findViewById(R.id.btn_clean)
        stroke = findViewById(R.id.btn_stroke)
        save = findViewById(R.id.btn_save)
        rangeSlider = findViewById(R.id.rangebar)

        // Set initial visibility of the rangeSlider to GONE
        rangeSlider.visibility = View.GONE

        undo.setOnClickListener { draw.undo() }

        clear.setOnClickListener { draw.clearDrawingBoard() }

        redo.setOnClickListener { draw.redo() }

        stroke.setOnClickListener {
            // Toggle visibility of the rangeSlider
            if (rangeSlider.visibility == View.VISIBLE) {
                rangeSlider.visibility = View.GONE
            } else {
                rangeSlider.visibility = View.VISIBLE
            }
        }

        // Set up the RangeSlider listener to change brush size
        rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider: RangeSlider?, value: Float, fromUser: Boolean ->
            // Set brush size based on slider value
            draw.setSizeForBrush((20 * value).toInt())
        })

        color.setOnClickListener {
            ColorPickerDialog
                .Builder(this)
                .setTitle("Choose color")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(R.color.black)
                .setColorListener { color, colorHex ->
                    draw.setBrushColor(color)
                }
                .show()
        }

        save.setOnClickListener {
            draw.post {
                val bitmap = getBitmapFromView(draw)
                val imageUri = saveBitmapToStorage(bitmap)

                if (imageUri != null) {
                    println("Image saved at: $imageUri")
                } else {
                    println("Saving failed!")
                }
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun saveBitmapToStorage(bitmap: Bitmap): Uri? {
        val filename = "Drawing_${System.currentTimeMillis()}.png"
        var uri: Uri? = null

        try {
            val imagesDir = getExternalFilesDir(null)?.absolutePath + "/Pictures/DrawingApp"
            val file = java.io.File(imagesDir)

            if (!file.exists()) {
                file.mkdirs() // Create folder if it doesn't exist
            }

            val imageFile = java.io.File(file, filename)
            val outputStream = java.io.FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Tell the gallery to update
            MediaStore.Images.Media.insertImage(contentResolver, imageFile.absolutePath, filename, null)
            uri = Uri.fromFile(imageFile)

            Log.e("draw", "✅ Image saved successfully at: $uri")
            Toast.makeText(this, "Image saved in Gallery", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("draw", "❌ Failed to save image: ${e.message}")
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        return uri
    }


    private fun shareImage(uri: Uri) {
        Toast.makeText(this, "Sharing image URI: $uri", Toast.LENGTH_SHORT).show()
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


}
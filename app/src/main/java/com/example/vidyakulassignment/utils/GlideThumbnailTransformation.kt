package com.example.vidyakulassignment.utils

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.security.MessageDigest


private const val MAX_LINES = 6
private const val MAX_COLUMNS = 10
private const val THUMBNAILS_EACH = 1000 // milliseconds
private const val ONE_MINUTE = 60000 // one minute in millisecond

class GlideThumbnailTransformation(position: Long) : BitmapTransformation() {

    private val x: Int
    private val y: Int

    private val TAG = "GlideThumbnailTransform"

    init {
        // Remainder of position on one minute because we just need to know which square of the current miniature
        val square = position.rem(ONE_MINUTE).toInt() / THUMBNAILS_EACH
        y = square / MAX_COLUMNS
        x = square % MAX_COLUMNS
        Log.e(TAG, "X: $x \n Y: $y")
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width / MAX_COLUMNS
        val height = toTransform.height / MAX_LINES
        Log.e(TAG, "Width: ${x * width} \n Height: ${y * height}")
        val bitmap = Bitmap.createBitmap(toTransform, x * width, y * height, width, height)
        SaveImage(bitmap)
        return bitmap
    }

    private fun SaveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val fname = "Image-$9.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val data: ByteArray = ByteBuffer.allocate(8).putInt(x).putInt(y).array()
        messageDigest.update(data)
    }

    override fun hashCode(): Int {
        return (x.toString() + y.toString()).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is GlideThumbnailTransformation) {
            return false
        }
        return other.x == x && other.y == y
    }
}
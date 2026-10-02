package com.barryburgle.gameapp.service.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class BitmapService {

    companion object {

        fun saveBitmapToCache(context: Context, bitmap: Bitmap, paddingDp: Int = 16): Uri? {
            return try {
                val softwareBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
                    bitmap.copy(Bitmap.Config.ARGB_8888, false)
                } else {
                    bitmap
                } ?: return null

                val density = context.resources.displayMetrics.density
                val paddingPx = (paddingDp * density).toInt()

                val paddedWidth = softwareBitmap.width + (paddingPx * 2)
                val paddedHeight = softwareBitmap.height + (paddingPx * 2)
                val paddedBitmap = Bitmap.createBitmap(
                    paddedWidth,
                    paddedHeight,
                    Bitmap.Config.ARGB_8888
                )

                val canvas = android.graphics.Canvas(paddedBitmap)
                canvas.drawBitmap(softwareBitmap, paddingPx.toFloat(), paddingPx.toFloat(), null)

                if (softwareBitmap != bitmap) {
                    softwareBitmap.recycle()
                }

                val imagesFolder = File(context.cacheDir, "images").apply { mkdirs() }
                val file = File(imagesFolder, "event_card_${System.currentTimeMillis()}.png")
                FileOutputStream(file).use { stream ->
                    paddedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }

                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
@file:Suppress("PackageDirectoryMismatch", "PackageDirectoryMismatch", "PackageDirectoryMismatch",
    "PackageDirectoryMismatch", "PackageDirectoryMismatch"
)

package com.farhanryanda.blurimagewm.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.farhanryanda.challangechapter5.workers.KEY_IMAGE_URI
import com.farhanryanda.challangechapter5.workers.blurBitmap
import com.farhanryanda.challangechapter5.workers.makeStatusNotification
import com.farhanryanda.challangechapter5.workers.writeBitmapToFile

class BlurWorker(context: Context, param : WorkerParameters): Worker(context,param) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

//        show notification
        makeStatusNotification("Blurring image", appContext)

        return try {
//            make sure ResourceUri from data is not empty
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e("Uri Status", "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

//            buat bitmap dari gambar (decodeSource), picture yang nantinya akan diteruskan
            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

//            nge blurin image - get versi blur bitmap
            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)

//            buat output URI sementara, agar dapat diakses untuk proses selanjutnya
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            Result.success(outputData)

        } catch (throwable: Throwable) {
            Log.e("Blur Status", "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }

}
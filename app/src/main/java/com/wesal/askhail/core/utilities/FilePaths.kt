package com.wesal.askhail.core.utilities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import android.content.ContentResolver
import android.provider.OpenableColumns
import java.io.FileOutputStream

object FilePaths {

    public fun copyFileToPath(activity: AppCompatActivity, uri: Uri): File? {
        val parcelFileDescriptor =
            activity.contentResolver.openFileDescriptor(
                uri,
                "r",
                null
            )
        if (parcelFileDescriptor!=null){
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(activity.cacheDir, activity.contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            return file

        }
        return null
    }
    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
}
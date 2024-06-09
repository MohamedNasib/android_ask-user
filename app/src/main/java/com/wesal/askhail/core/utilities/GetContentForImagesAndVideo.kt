package com.wesal.askhail.core.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class GetContentForImagesAndVideo : ActivityResultContract<String, Uri>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(input)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*")) // this does the trick
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri {
        return if (intent == null || resultCode != Activity.RESULT_OK) Uri.EMPTY else intent.data!!
    }
}
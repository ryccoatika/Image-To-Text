package com.ryccoatika.imagetotext.ui.utils

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory

object ReviewHelper {
    fun launchInAppReview(activity: Activity, onCompleted: (() -> Unit)? = null) {
        val reviewManager = ReviewManagerFactory.create(activity)
        reviewManager.requestReviewFlow().addOnCompleteListener { task ->
            val result = task.result
            if (task.isSuccessful && result != null) {
                reviewManager
                    .launchReviewFlow(activity, result)
                    .addOnCompleteListener { onCompleted?.invoke() }
            } else {
                onCompleted?.invoke()
            }
        }
    }
}

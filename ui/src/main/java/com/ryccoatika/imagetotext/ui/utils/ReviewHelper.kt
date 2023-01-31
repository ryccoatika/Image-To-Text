package com.ryccoatika.imagetotext.ui.utils

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory

object ReviewHelper {
    fun launchInAppReview(activity: Activity, onCompleted: (() -> Unit)? = null) {
        val reviewManager = ReviewManagerFactory.create(activity)
        reviewManager.requestReviewFlow().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                reviewManager
                    .launchReviewFlow(activity, result.result)
                    .addOnCompleteListener { onCompleted?.invoke() }
            } else {
                onCompleted?.invoke()
            }
        }
    }
}
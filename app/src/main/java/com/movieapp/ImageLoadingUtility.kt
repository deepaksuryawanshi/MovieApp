package com.movieapp

import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.Log
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.image.QualityInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * Load image utility file to download data from server.
 */
class ImageLoadingUtility {

    /**
     * Load image data from server.
     * @param simpleDraweeView imageView to display data loaded from server
     * @param url url to download image data
     * @param context reference to access application resources
     * @param onClickListener click event on image
     */
    fun loadImage(simpleDraweeView: SimpleDraweeView, url: String, context: Context, onClickListener: View.OnClickListener?) {
        val progressBarDrawable = CustomProgressBar()
        progressBarDrawable.barWidth = 10
        progressBarDrawable.color = context.resources.getColor(R.color.colorPrimary)
        progressBarDrawable.backgroundColor = context.resources.getColor(R.color.default_shadow_color)
        simpleDraweeView.hierarchy.setProgressBarImage(progressBarDrawable)


        val uri = Uri.parse(url)
        val request = ImageRequestBuilder.newBuilderWithSource(uri).build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setImageRequest(request)
                .setRetainImageOnFailure(true)
                .setOldController(simpleDraweeView.controller)
                .setControllerListener(object : BaseControllerListener<ImageInfo>() {
                    override fun onFinalImageSet(
                            id: String?,
                            imageInfo: ImageInfo?,
                            animatable: Animatable?) {
                        if (imageInfo != null) {
                            val qualityInfo = imageInfo.qualityInfo
                        }
                        if (onClickListener != null)
                            simpleDraweeView.setOnClickListener(onClickListener)
                    }

                    override fun onIntermediateImageSet(
                            id: String?, imageInfo: ImageInfo?) {
                        if (imageInfo != null) {
                            val qualityInfo = imageInfo.qualityInfo
                            simpleDraweeView.aspectRatio = imageInfo.width.toFloat() / imageInfo.height
                        }
                    }

                    override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
                        Log.e("Exception", throwable!!.message + "")
                    }
                })
                .build()
        simpleDraweeView.controller = controller
    }
}

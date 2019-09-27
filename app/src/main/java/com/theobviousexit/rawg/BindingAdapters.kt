package com.theobviousexit.rawg

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide


//@BindingAdapter("profileImage")
//fun loadImage(view: ImageView, imageUrl: String) {
//    Glide.with(view.getContext())
//        .load(imageUrl).apply(RequestOptions().circleCrop())
//        .into(view)
//}

@BindingAdapter("android:visibility")
fun setVisibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
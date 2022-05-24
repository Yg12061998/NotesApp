package com.yogigupta1206.notesapp.utils

import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("setAvatar")
fun AppCompatImageView.setAvatar(url:String?){
    if(!(url.isNullOrEmpty() || url.isNullOrBlank())){
        Glide.with(this.context).load(url).into(this)
    }
}

@BindingAdapter("setColor")
fun TextView.setColor(colorCode:String?){

    if(colorCode.isNullOrBlank()){
        setTextColor(Color.parseColor("#000000"))
    }else{
        setTextColor(Color.parseColor(colorCode))
    }
}
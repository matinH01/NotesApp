package com.example.note.adapter

import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat.getString
import androidx.databinding.BindingAdapter
import com.example.note.R

@BindingAdapter("setTextRegisterOrEnter")
fun setText(textView: TextView, appOpenBefore: Boolean) {
    if (appOpenBefore) {
        textView.text = getString(textView.context, R.string.enter)
    } else {
        textView.text = getString(textView.context, R.string.register)
    }
}

@BindingAdapter("setTextNewShow")
fun setTextAddShow(textView: TextView, newOrShowNote: Boolean) {
    if (newOrShowNote) {
        textView.text = getString(textView.context, R.string.newNote)
    } else {
        textView.text = getString(textView.context, R.string.showNote)
    }
}
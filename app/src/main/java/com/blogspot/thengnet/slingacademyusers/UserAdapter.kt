package com.blogspot.thengnet.slingacademyusers

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.blogspot.thengnet.slingacademyusers.databinding.UserBinding
import com.google.android.material.textfield.TextInputLayout

class UserAdapter(private val mAppContext: Context, objects: List<User?>?) : ArrayAdapter<User?>(
    mAppContext, 0, objects!!
) {
    private var inflater: LayoutInflater? = null
    private var binding: UserBinding? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: UserBinding = convertView?.let {
            DataBindingUtil.getBinding<UserBinding>(it)
        } ?: run {
            val inflater = LayoutInflater.from(parent.context)
            DataBindingUtil.inflate<UserBinding>(inflater, R.layout.user, parent, false)
        }

        val currentUser = getItem(position)
        binding.user = currentUser
        binding.executePendingBindings()

        // Set gender icon based on the value received
        val genderTextInputLayout = binding.textGender
        setGenderIcon(genderTextInputLayout, currentUser!!.gender)

        return binding.root
    }


//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        if (inflater == null) inflater = (mAppContext as Activity).layoutInflater
//        val currentUser = getItem(position)
//        binding = DataBindingUtil.getBinding(
//            convertView!!
//        )
//        if (binding == null) binding = DataBindingUtil.inflate(
//            inflater!!, R.layout.user, parent, false
//        )
//        binding!!.user = currentUser
//        binding!!.executePendingBindings()
//
//        // Set gender icon based on the value received
//        val genderTextInputLayout = binding!!.textGender
//        setGenderIcon(genderTextInputLayout, currentUser!!.gender)
//        return binding!!.root
//    }

    private fun setGenderIcon(textInputLayout: TextInputLayout, gender: String) {
        var iconResId: Int? = null
        when (gender) {
            "male" -> iconResId = R.drawable.ic_male_24
            "female" -> iconResId = R.drawable.ic_female_24
            else -> {}
        }
        if (iconResId != null)
            textInputLayout.setStartIconDrawable(iconResId)
    }
}
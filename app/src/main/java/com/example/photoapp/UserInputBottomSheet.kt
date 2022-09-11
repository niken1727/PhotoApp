package com.example.photoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class UserInputBottomSheet(val listener: (Int) -> Unit) : BottomSheetDialogFragment() {
    private lateinit var listEditText: TextInputEditText
    private lateinit var btnDone: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.user_input_layout,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listEditText = view.findViewById(R.id.listLengthEditText)
        btnDone = view.findViewById(R.id.btnDone)

        btnDone.setOnClickListener {
            if(listEditText.text.isNullOrBlank()) {
                Toast.makeText(context, "Enter length of the list", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            listener(listEditText.text.toString().toInt())
            dialog?.dismiss()
        }

    }


}
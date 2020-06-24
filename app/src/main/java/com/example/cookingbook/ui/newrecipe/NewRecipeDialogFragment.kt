package com.example.cookingbook.ui.newrecipe

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.cookingbook.R
import com.example.cookingbook.util.ViewUtil
import kotlinx.android.synthetic.main.dlg_new_recipe.*

class NewRecipeDialogFragment : DialogFragment(), NewRecipeContract.View {

    private var title: String = ""
    private var description: String = ""
    private var composition: String = ""
    private val mPresenter: NewRecipePresenter = NewRecipePresenter(this)
    private val REQUEST_IMAGE_GET = 1
    private var imageUri = Uri.EMPTY
    private val viewUtil:ViewUtil = ViewUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CornerDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.dlg_new_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelView.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            if (imageUri == null || imageUri.toString().isEmpty() || Title.text.toString().isEmpty() || Description.text.toString().isEmpty() || Composition.text.toString().isEmpty()) {
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            title = Title.text.toString();
            composition = Composition.text.toString();
            description = Description.text.toString();
            mPresenter.onConfirmButtonWasClicked(imageUri, title, composition, description);
        }
        changeImageButton.setOnClickListener { mPresenter.onChangeImageButtonWasClicked(activity?.packageManager); }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            viewUtil.putPicture(data?.dataString, recipePic);
            imageUri = data?.data;
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.dlg_new_recipe)
            )
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun clearData() {
        Title.setText("")
        Composition.setText("")
        Description.setText("")
        recipePic.setImageResource(R.drawable.nophoto)
        title = "";
        composition = "";
        description = "";
        imageUri = Uri.EMPTY
    }

    override fun onReadyActivityStartForResult(intent: Intent) {
        startActivityForResult(intent, REQUEST_IMAGE_GET);
    }

    override fun onPushRecipeSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        clearData();
    }

    override fun onPushRecipeFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
package com.example.cookingbook.ui.personalcabinet.changeprofile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookingbook.R;
import com.example.cookingbook.util.ViewUtil;

import org.jetbrains.annotations.NotNull;

public class ChangeProfileFragment extends Fragment implements View.OnClickListener, ChangeProfileContract.View {

    public static final int REQUEST_IMAGE_GET = 1;
    private Button changeAvatarButton, confirmButton;
    private EditText nameET, phoneET;


    private ImageView avatarImage;

    private ViewUtil viewUtil;
    private ChangeProfileContract.Presenter mPresenter;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_change_profile, container, false);
        init(root);
        showInfo();

        changeAvatarButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        return root;
    }

    private void showInfo(){
        nameET.setText(mPresenter.showNickname());
        viewUtil.putPicture(mPresenter.showImage().toString(), avatarImage);
        imageUri = mPresenter.showImage();
        phoneET.setText(mPresenter.showPhone());
    }

    private void init(View root) {
        viewUtil = new ViewUtil();
        phoneET = root.findViewById(R.id.changePhoneNumber);
        nameET = root.findViewById(R.id.changeNickname);
        avatarImage = root.findViewById(R.id.changeAvatar);
        mPresenter = new ChangeProfilePresenter(this);
        changeAvatarButton = root.findViewById(R.id.changeAvatarButton);
        confirmButton = root.findViewById(R.id.confirmButton);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            viewUtil.putPicture(data.getDataString(),avatarImage);
            imageUri = data.getData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeAvatarButton:
                mPresenter.onChangeImageButtonWasClicked(getActivity().getPackageManager());
                break;
            case R.id.confirmButton:
                if (avatarImage.toString().isEmpty()||nameET.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Заполните необходимые поля!",Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.onConfirmButtonWasClicked(imageUri,nameET.getText().toString(),phoneET.getText().toString());
                break;
        }
    }

    @Override
    public void onReadyActivityStartForResult(@NotNull Intent intent) {
        startActivityForResult(intent, REQUEST_IMAGE_GET);
    }

    @Override
    public void onChangeSuccess(@NotNull String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

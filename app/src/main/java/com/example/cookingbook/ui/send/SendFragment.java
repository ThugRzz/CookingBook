package com.example.cookingbook.ui.send;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cookingbook.MainActivity;
import com.example.cookingbook.R;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;

    private Button contacts;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        contacts = root.findViewById(R.id.contacts);
        contacts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri address = Uri.parse("https://vk.com/id446128040");
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
            }
        });

        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }


}
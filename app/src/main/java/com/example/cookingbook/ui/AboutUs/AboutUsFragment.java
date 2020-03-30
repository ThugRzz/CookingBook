package com.example.cookingbook.ui.AboutUs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookingbook.R;

public class AboutUsFragment extends Fragment {



    private Button contacts;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        contacts = root.findViewById(R.id.contacts);
        contacts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri address = Uri.parse("https://vk.com/id446128040");
                Intent intent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(intent);
            }
        });
        TextView infoText= root.findViewById(R.id.text_send);
        infoText.setText("Cooking book ver. 1.0");
        return root;
    }


}
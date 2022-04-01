package com.example.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mainactivity.databinding.ActivitySecondBinding;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnTela3.setOnClickListener(view -> {
            Intent intent = new Intent(this, ThirdActivity.class);
            startActivity(intent);
        });

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("TELA", "Tela 2");
        startService(intent);

        List<MyContact> contacts = ContactsHelper.getContacts(this);
//        binding.tvContato2.setText(contacts.get(1).getName());

        List<String> listaContatos = new ArrayList<>();

        for (MyContact contact : contacts) {
            listaContatos.add(contact.getName());
        }

        if (contacts.size() >= 2) {
            contacts.get(1);
        }

        binding.tvListaCotatos.setAdapter(new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaContatos));

    }
}
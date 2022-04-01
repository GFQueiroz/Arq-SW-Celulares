package com.example.mainactivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mainactivity.databinding.ActivityThirdBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private ActivityThirdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityThirdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIrTela4.setOnClickListener(view ->
                Snackbar.make(view, "Não é possível seguir", Snackbar.LENGTH_LONG).show()
        );

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("TELA", "Tela 3");
        startService(intent);

//        List<MyContact> contacts = ContactsHelper.getContacts(this);
//        binding.tvContato3.setText(contacts.get(2).getName());
//
//        if (contacts.size() >= 3) {
//            contacts.get(2);
//        }

        final PackageManager info = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = info.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> nameapps = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.enabled){
                nameapps.add(packageInfo.packageName + " - Habilitado");
            } else {
                nameapps.add(packageInfo.packageName + " - Desabilitado");
            }
        }
        binding.tvListaApp.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nameapps));

//        for (ApplicationInfo packageInfo : packages) {
//            if (packageInfo.enabled){
//                Log.d("ABR", "App Instalado: " + packageInfo.packageName +" - Habilitado");
//            }
//            else {
//                Log.d("ABR", "App Instalado: " + packageInfo.packageName +" - Desabilitado");
//            }
//            Log.d("ABR", "     ");
//        }
    }
}
package com.example.controlegastosapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtCategoria, edtValor;
    private Button btnSalvar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtCategoria = findViewById(R.id.edtCategoria);
        edtValor = findViewById(R.id.edtValor);
        btnSalvar = findViewById(R.id.btnSalvar);

        db = FirebaseFirestore.getInstance();

        btnSalvar.setOnClickListener(v -> {
            salvarGasto();
        });
    }

    private void salvarGasto() {
        String categoria = edtCategoria.getText().toString().trim();
        String valorStr = edtValor.getText().toString().trim();

        if (TextUtils.isEmpty(categoria)) {
            edtCategoria.setError("Informe a categoria");
            return;
        }

        if (TextUtils.isEmpty(valorStr)) {
            edtValor.setError("Informe o valor");
            return;
        }

        double valor;

        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            edtValor.setError("Valor inválido");
            return;
        }

        Map<String, Object> gasto = new HashMap<>();
        gasto.put("categoria", categoria);
        gasto.put("valor", valor);

        db.collection("gastos")
                .add(gasto)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Gasto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    edtCategoria.setText("");
                    edtValor.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao salvar gasto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

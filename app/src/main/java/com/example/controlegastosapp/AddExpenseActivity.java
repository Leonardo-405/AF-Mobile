package com.example.controlegastosapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity {
    EditText inputCategoria, inputValor, inputDescricao, inputData;
    Button btnSalvar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        inputCategoria = findViewById(R.id.inputCategoria);
        inputValor = findViewById(R.id.inputValor);
        inputDescricao = findViewById(R.id.inputDescricao);
        inputData = findViewById(R.id.inputData);
        btnSalvar = findViewById(R.id.btnSalvar);

        db = FirebaseFirestore.getInstance();

        btnSalvar.setOnClickListener(v -> {
            String categoria = inputCategoria.getText().toString();
            String valorStr = inputValor.getText().toString();
            String descricao = inputDescricao.getText().toString();
            String data = inputData.getText().toString();

            if (categoria.isEmpty() || valorStr.isEmpty() || data.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> gasto = new HashMap<>();
            gasto.put("categoria", categoria);
            gasto.put("valor", valor);
            gasto.put("descricao", descricao);
            gasto.put("data", data);

            db.collection("gastos")
                    .add(gasto)
                    .addOnSuccessListener(documentReference -> Toast.makeText(this, "Gasto salvo com sucesso", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar gasto", Toast.LENGTH_SHORT).show());
        });
    }
}

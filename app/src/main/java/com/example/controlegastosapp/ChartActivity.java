package com.example.controlegastosapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    private PieChart pieChart;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        pieChart = findViewById(R.id.pieChart);
        db = FirebaseFirestore.getInstance();

        carregarGrafico();
    }

    private void carregarGrafico() {
        Map<String, Float> totaisPorCategoria = new HashMap<>();

        db.collection("gastos").get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot doc : querySnapshot) {
                String categoria = doc.getString("categoria");
                Double valor = doc.getDouble("valor");

                if (categoria != null && valor != null) {
                    float valorAtual = totaisPorCategoria.getOrDefault(categoria, 0f);
                    totaisPorCategoria.put(categoria, valorAtual + valor.floatValue());
                }
            }

            List<PieEntry> entries = new ArrayList<>();
            for (Map.Entry<String, Float> entry : totaisPorCategoria.entrySet()) {
                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            if (entries.isEmpty()) {
                pieChart.setNoDataText("Nenhum dado encontrado.");
                return;
            }

            PieDataSet dataSet = new PieDataSet(entries, "Gastos por Categoria");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextSize(14f);

            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);

            pieChart.setUsePercentValues(true);
            pieChart.setDrawHoleEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(true); // Ativa legenda

            pieChart.invalidate();
        }).addOnFailureListener(e -> {
            pieChart.setNoDataText("Erro ao carregar dados: " + e.getMessage());
        });
    }
}


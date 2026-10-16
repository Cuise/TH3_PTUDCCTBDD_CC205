package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText etTitle, etAmount, etNote;
    private Spinner spCategory;
    private DatePicker dpDate;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etNote = findViewById(R.id.etNote);
        spCategory = findViewById(R.id.spCategory);
        dpDate = findViewById(R.id.dpDate);
        btnSave = findViewById(R.id.btnSave);


        String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spCategory.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String category = spCategory.getSelectedItem().toString();
            String note = etNote.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Tên khoản chi không được để trống");
                return;
            }

            if (amountStr.isEmpty()) {
                etAmount.setError("Số tiền không hợp lệ");
                return;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                etAmount.setError("Số tiền phải lớn hơn 0");
                return;
            }


            int day = dpDate.getDayOfMonth();
            int month = dpDate.getMonth() + 1;
            int year = dpDate.getYear();
            String date = day + "/" + month + "/" + year;

            Expense newExpense = new Expense(UUID.randomUUID().toString(), title, amount, category, date, note);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_expense", newExpense);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
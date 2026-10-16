package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ADD = 1;
    private static final int REQUEST_DETAIL = 2;

    private TextView tvTotalAmount;
    private ListView lvExpenses;
    private Button btnAdd;

    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lvExpenses = findViewById(R.id.lvExpenses);
        btnAdd = findViewById(R.id.btnAdd);

        // Khởi tạo dữ liệu mẫu
        expenseList = new ArrayList<>();
        expenseList.add(new Expense("1", "Ăn sáng", 30000, "Ăn uống", "12/10/2026", "Phở bò"));
        expenseList.add(new Expense("2", "Đổ xăng", 50000, "Đi lại", "12/10/2026", "Xăng A95"));
        expenseList.add(new Expense("3", "Mua sách", 120000, "Học tập", "13/10/2026", "Sách Lập trình Android"));

        adapter = new ExpenseAdapter(this, expenseList);
        lvExpenses.setAdapter(adapter);

        updateTotalAmount();

        // Nút Thêm khoản chi
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });

        // Chọn item mở Chi tiết khoản chi
        lvExpenses.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
            intent.putExtra("expense", expenseList.get(position));
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST_DETAIL);
        });
    }

    private void updateTotalAmount() {
        double total = 0;
        for (Expense e : expenseList) {
            total += e.getAmount();
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalAmount.setText(formatter.format(total) + " đ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_ADD) {
                Expense newExpense = (Expense) data.getSerializableExtra("new_expense");
                if (newExpense != null) {
                    expenseList.add(newExpense);
                    adapter.notifyDataSetChanged();
                    updateTotalAmount();
                }
            } else if (requestCode == REQUEST_DETAIL) {
                boolean isDeleted = data.getBooleanExtra("is_deleted", false);
                int position = data.getIntExtra("position", -1);
                if (isDeleted && position != -1) {
                    expenseList.remove(position);
                    adapter.notifyDataSetChanged();
                    updateTotalAmount();
                }
            }
        }
    }
}
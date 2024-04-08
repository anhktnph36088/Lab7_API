package com.example.lab7;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab7.Adapter.FruitsAdapter;
import com.example.lab7.Model.ApiService;
import com.example.lab7.Model.Fruits;
import com.example.lab7.Model.Page;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FruitsHome extends AppCompatActivity {
    private ProgressBar loadmore;
    private FruitsAdapter adapter;
    private RecyclerView recycle_fruits;
    private ArrayList<Fruits> list = new ArrayList<>();
    private int page = 1;
    private int totalPage = 0;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fruits_home);
        adapter = new FruitsAdapter(this, list);

        // Liên kết các phần tử giao diện người dùng
        loadmore = findViewById(R.id.load_more);
        nestedScrollView = findViewById(R.id.nestScrollView);
        recycle_fruits = findViewById(R.id.recycle_fruits);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gọi phương thức để lấy dữ liệu từ API
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if we've reached the bottom of the scroll view
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // Check if we have reached the last page
                    if (totalPage == page) return;

                    // Check if the load more view is already visible
                    if (loadmore.getVisibility() == View.GONE) {
                        // Set load more view visible
                        loadmore.setVisibility(View.VISIBLE);

                        // Increment page number
                        page++;

                        // Call your API or load more content based on filter (FilterFruit?)
                        // Example:
                        // loadMoreDataBasedOnFilter(filter);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Gọi phương thức để lấy dữ liệu từ API khi hoạt động được resume
        String token = "your_token_here"; // Thay thế bằng token JWT của bạn
        String bearerToken = "Bearer " + token;

        // Tạo một Retrofit instance để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN) // Thay thế bằng URL cơ sở của API của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Tạo một đối tượng ApiService từ Retrofit instance
        ApiService apiService = retrofit.create(ApiService.class);

        // Gọi phương thức getPageFruits() của ApiService để lấy dữ liệu trái cây với token được truyền trong tiêu đề yêu cầu
        Call<Page<ArrayList<Fruits>>> call = apiService.getPageFruits(bearerToken, page);
        call.enqueue(getListFruitResponse);
    }

    Callback<Page<ArrayList<Fruits>>> getListFruitResponse = new Callback<Page<ArrayList<Fruits>>>() {
        @Override
        public void onResponse(Call<Page<ArrayList<Fruits>>> call, Response<Page<ArrayList<Fruits>>> response) {
            if (response.isSuccessful()) {
                // Kiểm tra mã trạng thái của phản hồi
                if (response.code() == 200) {
                    // Lấy dữ liệu từ phản hồi
                    Page<ArrayList<Fruits>> page = response.body();
                    if (page != null) {
                        // Thiết lập tổng số trang
                        totalPage = page.getTotalPage();
                        // Lấy danh sách trái cây từ trang
                        ArrayList<Fruits> fruitsList = page.getData();
                        // Cập nhật dữ liệu lên giao diện người dùng
                        getData(fruitsList);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Page<ArrayList<Fruits>>> call, Throwable t) {
            // Xử lý khi yêu cầu API thất bại
            Log.d(">>> getListFruit", "onFailure: " + t.getMessage());
        }
    };

    private void getData(ArrayList<Fruits> _ds) {
        // Kiểm tra nếu quá trình tải thêm đang chạy
        if (loadmore.getVisibility() == View.VISIBLE) {
            // Do quá trình chạy cục bộ nên sẽ thêm một đoạn mã để trễ (delay) 1 giây
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Thêm dữ liệu vào danh sách trái cây
                    list.addAll(_ds);
                    // Thông báo cho adapter biết dữ liệu đã thay đổi
                    adapter.notifyDataSetChanged();
                    // Ẩn ProgressBar khi quá trình tải thêm hoàn thành
                    loadmore.setVisibility(View.GONE);
                }
            }, 1000); // Trễ 1 giây
            return;
        }

        // Nếu không có quá trình tải thêm đang chạy, chỉ cần cập nhật danh sách trái cây và cập nhật giao diện
        list.addAll(_ds);
        adapter = new FruitsAdapter(this, list);
        recycle_fruits.setLayoutManager(new GridLayoutManager(this, 2));
        recycle_fruits.setAdapter(adapter);
    }
}

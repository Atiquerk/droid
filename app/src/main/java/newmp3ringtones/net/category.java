package newmp3ringtones.net;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class category extends AppCompatActivity {
    ImageView threedots;
    androidx.appcompat.widget.SearchView searchview;
    LinearLayout catmenulink;
    private CatRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private CatApiInterface catApiInterface;
    private int page_number=1;
    private int item_count=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        recyclerView=findViewById(R.id.catsview);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        catApiInterface=CatApiClient.getAPIClient().create(CatApiInterface.class);
        Call<List<CatsResponse>> call=catApiInterface.getCats(page_number,item_count);

        call.enqueue(new Callback<List<CatsResponse>>() {
            @Override
            public void onResponse(Call<List<CatsResponse>> call, Response<List<CatsResponse>> response) {
                List<Cats> cats=response.body().get(1).getCats();
                adapter=new CatRecyclerAdapter(cats,category.this);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<CatsResponse>> call, Throwable t) {

            }
        });


        catmenulink=findViewById(R.id.catmenulink);
        catmenulink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(category.this, home.class);

                startActivity(intent);
            }
        });

        threedots=findViewById(R.id.threedots);

        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmenu(view);
            }
        });

        searchview=findViewById(R.id.searchview);

        searchview.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //do here
                if(query.length()>3) {
                    Intent intent = new Intent(category.this, shome.class);
                    intent.putExtra("sname", query+"");
                    startActivity(intent);
                }else{
                    Toast.makeText(category.this, "Search phrase at least of 4 characters.", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//
                return true;
            }



        });


    }

    public void showmenu(View v){
        PopupMenu popupMenu=new PopupMenu(category.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://thedeveloper.pk"));
                startActivity(browserIntent);

                return false;
            }
        });
        popupMenu.show();
    }
}
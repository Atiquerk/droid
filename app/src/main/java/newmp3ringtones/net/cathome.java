package newmp3ringtones.net;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cathome extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;

    private CApiInterface apiInterface;
    private RecyclerAdapter adapter;
    private int page_number=1;
    private int item_count=10;

    //variables for pagination

    private  boolean isLoading=true;
    private  int pastVisibleItems,visibleItemCounts,totalItemCounts,previousTotal=0;
    private int view_threashold=0;

    private LinearLayout bottombar;
    private ImageView play;
    private ImageView stop;
    private SeekBar seekBar;
    ImageView next;
    ImageView prev;

    Handler handler;

    Runnable runnable;

    TextView ringname;
    MediaPlayer mediaplayer;

    ImageView threedots;
    androidx.appcompat.widget.SearchView searchview;
    LinearLayout catmenulink;
    LinearLayout ringlink;

    TextView categoryname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathome);

        Intent intent = getIntent();
        String cname = intent.getStringExtra("catname");
        int catid=intent.getIntExtra("catid",11);
        item_count = catid;
        categoryname=findViewById(R.id.categoryname);
        categoryname.setText("Category: "+cname);

        bottombar=findViewById(R.id.bottombar);
        play=findViewById(R.id.play);
        stop=findViewById(R.id.stop);
        seekBar=findViewById(R.id.seekbar);
        seekBar.setPadding(0, 0, 0, 0);


        ringlink=findViewById(R.id.ringlink);
        ringlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cathome.this, home.class);

                startActivity(intent);
            }
        });
        catmenulink=findViewById(R.id.catmenulink);
        catmenulink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cathome.this, category.class);

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
                    Intent intent = new Intent(cathome.this, shome.class);
                    intent.putExtra("sname", query+"");
                    startActivity(intent);
                }else{
                    Toast.makeText(cathome.this, "Search phrase at least of 4 characters.", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//
                return true;
            }



        });

        ringname=findViewById(R.id.ringname);


        bottombar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        next=findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlnext=adapter.getNextRingtone();
                String currentRingName=adapter.getcurrentRingName();
                ringname.setText(currentRingName);
                mediaplayer.reset();
                try {
                    mediaplayer.setDataSource("https://newmp3ringtones.net/assets/sass/Ringtones/" + urlnext);
                } catch (IOException e) {

                }
                try {
                    mediaplayer.prepare();
                } catch (IOException e) {

                }
                mediaplayer.start();


            }
        });

        prev=findViewById(R.id.prev);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlprev=adapter.getPreviousRingtone();
                String currentRingName=adapter.getcurrentRingName();
                ringname.setText(currentRingName);
                mediaplayer.reset();
                try {
                    mediaplayer.setDataSource("https://newmp3ringtones.net/assets/sass/Ringtones/" + urlprev);
                } catch (IOException e) {

                }
                try {
                    mediaplayer.prepare();
                } catch (IOException e) {

                }
                mediaplayer.start();


            }
        });


        handler=new Handler();

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaplayer.reset();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                mediaplayer.start();
                changeSeekbar();
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaplayer.pause();
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaplayer.seekTo(i);
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerview);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        apiInterface=CApiClient.getAPIClient().create(CApiInterface.class);

        Call<List<DataResponse>> call=apiInterface.getRings(page_number,item_count);

        call.enqueue(new Callback<List<DataResponse>>() {
            @Override
            public void onResponse(Call<List<DataResponse>> call, Response<List<DataResponse>> response) {
                List<Rings> rings=response.body().get(1).getRings();
                adapter=new RecyclerAdapter(rings,cathome.this,bottombar,play,stop,mediaplayer,seekBar,handler,ringname);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<DataResponse>> call, Throwable t) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCounts=layoutManager.getChildCount();
                totalItemCounts=layoutManager.getItemCount();
                pastVisibleItems=layoutManager.findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(totalItemCounts>previousTotal){
                            isLoading=false;
                            previousTotal=totalItemCounts;
                        }
                    }
                    if(!isLoading && (totalItemCounts-visibleItemCounts)<=(pastVisibleItems+view_threashold)){
                        page_number++;

                        performPagination();
                        isLoading=true;

                    }
                }

            }
        });
    }



    private void performPagination(){
        progressBar.setVisibility(View.VISIBLE);
        Call<List<DataResponse>> call=apiInterface.getRings(page_number,item_count);

        call.enqueue(new Callback<List<DataResponse>>() {
            @Override
            public void onResponse(Call<List<DataResponse>> call, Response<List<DataResponse>> response) {


                List<Rings> rings=response.body().get(1).getRings();
                adapter.addRings(rings);



                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<DataResponse>> call, Throwable t) {

            }
        });


    }


    public void changeSeekbar(){
        seekBar.setProgress(mediaplayer.getCurrentPosition());
        if(mediaplayer.isPlaying()) {
            runnable=new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();

                }
            };
            handler.postDelayed(runnable,1000);
        }

    }

    public void showmenu(View v){
        PopupMenu popupMenu=new PopupMenu(cathome.this,v);
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
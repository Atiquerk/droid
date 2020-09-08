package newmp3ringtones.net;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Singletone extends AppCompatActivity {
    MediaPlayer mediaplayer;
    Boolean start;
    Runnable runnable;
    Handler handler;
    ProgressBar progressBar;
    ImageView threedots;
    androidx.appcompat.widget.SearchView searchview;
    Button download;



     @Override
    public void onBackPressed(){
         mediaplayer.stop();
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletone);

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
                    Intent intent = new Intent(Singletone.this, shome.class);
                    intent.putExtra("sname", query+"");
                    startActivity(intent);
                }else{
                    Toast.makeText(Singletone.this, "Search phrase at least of 4 characters.", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//
                return true;
            }



        });


        Intent intent = getIntent();
        String message = intent.getStringExtra("ringname");
        TextView textView = (TextView) findViewById(R.id.ringname);
        String rname=message;
        int l_rname=rname.length();
        if(l_rname>20){
            String newname="";
            for(int i=0;i<20;i++){
                newname=newname+rname.charAt(i);
            }
            rname=newname+"..";
        }

        textView.setText(rname);
        String urls = intent.getStringExtra("url");
        download=findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                downloadFile(urls);
            }
        });

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressBar=findViewById(R.id.progress_bar);

        handler = new Handler();

        start = false;




        ImageView pause = findViewById(R.id.pause);
        ImageView playme = findViewById(R.id.playme);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playme.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                mediaplayer.pause();


            }
        });


        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                progressBar.setProgress(0);
                playme.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                handler.removeCallbacksAndMessages(null);
                start=false;
                progressBar.setVisibility(View.GONE);


            }
        });
        playme.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(Singletone.this, "hell", Toast.LENGTH_SHORT).show();
                playme.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                setProgressBar();
                if (start == false) {
                    start = true;
                    progressBar.setVisibility(View.VISIBLE);
                    mediaplayer.reset();

                    try {
                        mediaplayer.setDataSource("https://newmp3ringtones.net/assets/sass/Ringtones/" + urls);
                    } catch (IOException e) {
                        //e.printStackTrace();
                        Toast.makeText(Singletone.this, "Exception 1", Toast.LENGTH_SHORT).show();

                    }
                    try {
                        mediaplayer.prepare();
                    } catch (IOException e) {
                        //  e.printStackTrace();
                        Toast.makeText(Singletone.this, "prepare Exceptuon", Toast.LENGTH_SHORT).show();

                    }
                }

                mediaplayer.start();

            }


        });


    }

    public void setProgressBar() {
        if (mediaplayer.isPlaying()) {

            progressBar.setProgress((mediaplayer.getCurrentPosition()*100)/mediaplayer.getDuration());

        }
            runnable = new Runnable() {
                @Override
                public void run() {
                    setProgressBar();

                }
            };
            handler.postDelayed(runnable, 1000);
        }

    public void showmenu(View v){
        PopupMenu popupMenu=new PopupMenu(Singletone.this,v);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void downloadFile(String ringurl) {
        Toast.makeText(Singletone.this,"Process Initied",Toast.LENGTH_LONG).show();

        String DownloadUrl = "https://newmp3ringtones.net/assets/sass/Ringtones/"+ringurl;
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setDescription("Downloaded from newmp3ringtone app");   //appears the same in Notification bar while downloading
        request1.setTitle(ringurl);
        request1.setVisibleInDownloadsUi(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request1.setDestinationInExternalFilesDir(getApplicationContext(), "/File", ringurl);

        DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager1).enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            String myarkpah=getExternalFilesDir(null)+File.separator+"File"+File.separator+ringurl;
            Toast.makeText(Singletone.this,"Downloaded to: "+myarkpah,Toast.LENGTH_LONG).show();

      /*
            File k=new File(myarkpah);




            if(k.exists()) {


try {
    if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(Singletone.this)) {


                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, myarkpah);
                values.put(MediaStore.MediaColumns.TITLE, "Khuhro Ring");
                //values.put(MediaStore.MediaColumns.SIZE, 1024*1024);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
                values.put(MediaStore.Audio.Media.ARTIST, "Booss ARk");
                //values.put(MediaStore.Audio.Media.DURATION, 5000);
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                values.put(MediaStore.Audio.Media.IS_ALARM, false);
                values.put(MediaStore.Audio.Media.IS_MUSIC, true);
                Uri uri = MediaStore.Audio.Media.getContentUriForPath(myarkpah);
                if (uri == null || Singletone.this.getContentResolver() == null) {
                    Toast.makeText(Singletone.this, "error sshhah", Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO check this may be better copy file in ringtone dir before?
                Singletone.this.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + myarkpah + "\"", null);
                Uri newUri = Singletone.this.getContentResolver().insert(uri, values);
                if (newUri == null) {
                    Toast.makeText(Singletone.this, "error 2", Toast.LENGTH_SHORT).show();
                } else {
                    RingtoneManager.setActualDefaultRingtoneUri(Singletone.this, RingtoneManager.TYPE_RINGTONE, newUri);
                    Toast.makeText(Singletone.this, "Done Done", Toast.LENGTH_SHORT).show();
                }




            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + Singletone.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }


//Insert it into the database

    }
}catch (Exception e){
    Toast.makeText(Singletone.this,"Exception"+e,Toast.LENGTH_SHORT).show();
    Log.d("Singletone.this","ExceptionArk:"+e);

}

            }else{
              //  Toast.makeText(Singletone.this,"File not exists"+k,Toast.LENGTH_SHORT).show();

            }
            */

        }
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }



    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(Singletone.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

}
package newmp3ringtones.net;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import android.os.Handler;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private List<Rings> ringsList;
    private Context context;
    private LinearLayout bottombar;
    private ImageView play;
    private ImageView stop;
    Runnable runnable;
    Handler handler;
    int currentPosition=-1;
    MediaPlayer mediaPlayer;
    TextView ringname;
    SeekBar seekBar;

  public String getcurrentRingName(){
      Rings rings=ringsList.get(currentPosition);
      String rname=rings.getRingname();
      int l_rname=rname.length();
      if(l_rname>15){
          String newname="";
          for(int i=0;i<15;i++){
              newname=newname+rname.charAt(i);
          }
          rname=newname+"..";
      }
      return rname;

  }
    public String getNextRingtone(){
   currentPosition=currentPosition+1;
   Rings rings=ringsList.get(currentPosition);

   return rings.getRingurl();
    }

    public String getPreviousRingtone(){
      if(currentPosition!=1) {
          currentPosition = currentPosition - 1;
          Rings rings = ringsList.get(currentPosition);

          return rings.getRingurl();
      }else{
          Rings rings = ringsList.get(currentPosition);

          return rings.getRingurl();
      }

    }


    public RecyclerAdapter(List<Rings> ringsList,Context context,LinearLayout bottombar,ImageView play, ImageView stop,MediaPlayer mediaPlayer,SeekBar seekBar, Handler handler,TextView ringname){
    this.ringsList=ringsList;
    this.context=context;
    this.bottombar=bottombar;
    this.play=play;
    this.stop=stop;
    this.mediaPlayer=mediaPlayer;
    this.seekBar=seekBar;
    this.handler=handler;
    this.ringname=ringname;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        Rings rings=ringsList.get(position);
        String rname=rings.getRingname();
        int l_rname=rname.length();
        if(l_rname>20){
            String newname="";
            for(int i=0;i<20;i++){
                newname=newname+rname.charAt(i);
            }
            rname=newname+"..";
        }
        holder.title.setText(rname);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(currentPosition!=-1) {
                    currentPosition = currentPosition + 1;
                    Rings rings = ringsList.get(currentPosition);
                    String rname=rings.getRingname();
                    int l_rname=rname.length();
                    if(l_rname>15){
                        String newname="";
                        for(int i=0;i<15;i++){
                            newname=newname+rname.charAt(i);
                        }
                        rname=newname+"..";
                    }
                    ringname.setText(rname);
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource("https://newmp3ringtones.net/assets/sass/Ringtones/" + rings.getRingurl());
                    } catch (IOException e) {

                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {

                    }
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    changeSeekbar();
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    Intent intent = new Intent(context, Singletone.class);
                    intent.putExtra("ringname", rings.getRingname());

                    intent.putExtra("url", rings.getRingurl());

                    mediaPlayer.stop();
                    bottombar.setVisibility(View.GONE);


                    context.startActivity(intent);




            }
        });

    }

    @Override
    public int getItemCount() {
        return ringsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView imageView;



        public MyViewHolder( View itemView) {
            super(itemView);



            title=itemView.findViewById(R.id.titletext);
            imageView=itemView.findViewById(R.id.image_show);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Rings rings=ringsList.get(position);
                        String rname=rings.getRingname();
                        int l_rname=rname.length();
                        if(l_rname>15){
                            String newname="";
                            for(int i=0;i<15;i++){
                                newname=newname+rname.charAt(i);
                            }
                            rname=newname+"..";
                        }
                        ringname.setText(rname);
                        mediaPlayer.reset();
                        bottombar.setVisibility(View.VISIBLE);
                        String urls = rings.getRingurl();


                        try {
                            mediaPlayer.setDataSource("https://newmp3ringtones.net/assets/sass/Ringtones/" + urls);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mediaPlayer.start();
                        seekBar.setMax(mediaPlayer.getDuration());
                        changeSeekbar();
                        currentPosition = position;


                        play.setVisibility(View.GONE);
                        stop.setVisibility(View.VISIBLE);

                    }
                    //end

                }
            });




        }
    }
    public void addRings(List<Rings> rings){
        for(Rings rg: rings){
            ringsList.add(rg);
        }
        notifyDataSetChanged();
    }

    public void changeSeekbar(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying()) {
            runnable=new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();

                }
            };
handler.postDelayed(runnable,1000);
        }

    }
}

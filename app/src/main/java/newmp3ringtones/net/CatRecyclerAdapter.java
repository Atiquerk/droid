package newmp3ringtones.net;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CatRecyclerAdapter  extends RecyclerView.Adapter<CatRecyclerAdapter.MyViewHolder> {
    private List<Cats> catsList;
    private Context context;



    public CatRecyclerAdapter(List<Cats> catsList,Context context){
        this.catsList=catsList;
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cats,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {

        Cats cats=catsList.get(position);
        String cname=cats.getCatname();
       holder.title.setText(cname);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context, cathome.class);
                intent.putExtra("catname", cats.getCatname());

                intent.putExtra("catid", cats.getCatid());


                context.startActivity(intent);




            }
        });


    }

    @Override
    public int getItemCount() {
        return catsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;




        public MyViewHolder( View itemView) {
            super(itemView);



            title=itemView.findViewById(R.id.titletext);





        }
    }

    public void addCats(List<Cats> cats){
        for(Cats rg: cats){
            catsList.add(rg);
        }
        notifyDataSetChanged();
    }


}

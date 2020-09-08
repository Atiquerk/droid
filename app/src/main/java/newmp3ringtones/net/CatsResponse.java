package newmp3ringtones.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatsResponse {
    @SerializedName("status")
    private  String Status;
    @SerializedName("cats")
    List<Cats> cats;

    public String getStatus() {
        return Status;
    }

    public List<Cats> getCats() {
        return cats;
    }
}


class Cats{
    @SerializedName("id")
    private int catid;
    @SerializedName("name")
    private String catname;

    public int getCatid(){
        return catid;
    }
    public String getCatname(){
        return catname;
    }


}
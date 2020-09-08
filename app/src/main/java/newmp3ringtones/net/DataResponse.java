package newmp3ringtones.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponse {
    @SerializedName("status")
    private  String Status;
    @SerializedName("rings")
    List<Rings> rings;

    public String getStatus() {
        return Status;
    }

    public List<Rings> getRings() {
        return rings;
    }
}


class Rings{
    @SerializedName("id")
    private int ringid;
    @SerializedName("name")
    private String ringname;
    @SerializedName("url")
    private String ringurl;
    public int getRingid(){
        return ringid;
    }
    public String getRingurl(){return ringurl;}
    public String getRingname(){
        return ringname;
    }


}
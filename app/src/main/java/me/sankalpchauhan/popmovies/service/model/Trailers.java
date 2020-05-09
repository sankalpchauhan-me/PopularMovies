package me.sankalpchauhan.popmovies.service.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Trailers is the initial response on calling the endpoint movie/{id}/videos for a particular movie
 */
public class Trailers implements Parcelable {

    public final static Parcelable.Creator<Trailers> CREATOR = new Creator<Trailers>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }

        public Trailers[] newArray(int size) {
            return (new Trailers[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> trailerResults = null;

    protected Trailers(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.trailerResults, (TrailerResult.class.getClassLoader()));
    }

    public Trailers() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerResult> getTrailerResults() {
        return trailerResults;
    }

    public void setTrailerResults(List<TrailerResult> trailerResults) {
        this.trailerResults = trailerResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(trailerResults);
    }

    public int describeContents() {
        return 0;
    }

}

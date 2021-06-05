package com.thanguit.imusic.API;

import com.thanguit.imusic.models.Album;
import com.thanguit.imusic.models.Playlist;
import com.thanguit.imusic.models.Slider;
import com.thanguit.imusic.models.Song;
import com.thanguit.imusic.models.Theme;
import com.thanguit.imusic.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @GET("slider.php")
    Call<List<Slider>> getSlider();

    @GET("playlistforcurrentday.php")
    Call<List<Playlist>> getPlaylistCurrentDay();

    @GET("themecurrentday.php")
    Call<List<Theme>> getThemeCurrentDay();

    @GET("albumcurrentday.php")
    Call<List<Album>> getAlbumCurrentDay();

    @GET("songlove.php")
    Call<List<Song>> getSongChart();

    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<Song>> getSongWithPlaylist(@Field("Pla_ID") String id);

    @FormUrlEncoded
    @POST("searchsong.php")
    Call<List<Song>> getSongSearch(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("addnewuser.php")
    Call<List<User>> addNewUser(@Field("id") String id, @Field("name") String name, @Field("email") String email, @Field("img") String img, @Field("isDark") String isDark, @Field("isEnglish") String isEnglish);
}

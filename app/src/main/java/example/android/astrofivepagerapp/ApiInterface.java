package example.android.astrofivepagerapp;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface {


    @POST("product/create.php")
    Call<Registerresponse> register(@Query("name") String name, @Query("email_id") String emailid, @Query("mobile_no") String mobileno,@Query("type") String type);

}

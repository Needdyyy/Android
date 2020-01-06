package com.needyyy.app.manager.APIManager;

import com.google.gson.JsonObject;


import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIRequestHelper {

    String kCreateUser                              = "signup";
    String kResendVerificationCode                  = "resendVerificationCode";
    String kVerifyUserMobileNumber                  = "verifyMobile";
    String kUpdateUser                              = "updateUser";
    String kNudgesByUserID                          = "nudgesByUserId";
    String KNudgeByNudgeId                          = "nudge";
    String kNudgeCollectionByUserID                 = "NudgeCollectionByUserId";

    /**Get shared package that user shared in the past**/
    String kGetSharedPackageRequestSent             = "GetRecentSharePackege";

    /**Get Received shared nudges at recipient side that are pending or acepted and having nudges**/
    String kGetAcceptedPendingSharePackegeRequest   = "GetAcceptedPendingSharePackegeRequest";

    /**Get Notification Nudge Request Sent that user sent in the past**/
    String kGetNotificationNudgeRequestSent         = "getnotificationnudgesbysender";

    /**Get Notification Nudge Request that user received in the past**/
    String kGetNotificationNudgeRequestReceived     = "getnotificationnudgesuser";

    /**Get All server side notifications and sync with the app*/
    String kGetNotificationHistory                  = "getnotificationhistory";

    /**Get All registered contacts from server*/
    String kGetRegisteredContacts                   = "registeredContacts";

    String kCreateNudge                             = "createNudge";
    String kCreateNudgeCollection                   = "CreateNudgeCollection";
    String kToken                                   = "token";

    /**Add Nudges to existing collection*/
    String kAddNudgeidinCollection                  = "AddNudgeidinCollection";

    /**
     * set api request with api key and corresponding parameters
     * @param APIKey  key of the url
     * @param details details contains request body parameters
     * @param files   if include file will be sent in multipart
     * @return JsonObject ie. response
     */
    @Multipart
    @POST()
    Call<JsonObject> APIRequestWithFile(
            @Url String APIKey,
            @PartMap Map<String, RequestBody> details,
            @Part List<MultipartBody.Part> files
    );

    @POST()
    Call<JsonObject> APIRequestRaw(
            @Url String APIKey,
            @HeaderMap Map<String, String> headers,
            @Body RequestBody params
    );

    @GET()
    Call<JsonObject> APIRequestRawForGetMethod(
            @Url String APIKey
    );

    @GET("maps/api/geocode/json")
    Call<JsonObject> getLocation(
            @Query("latlng") String latlng,
            @Query("key") String key
    );

    @GET("maps/api/place/autocomplete/json")
    Call<JsonObject> getPlaces(
            @Query("input") String input,
            @Query("key") String key
    );

    @GET("maps/api/place/details/json")
    Call<JsonObject> getPlacesByID(
            @Query("placeid") String placeID,
            @Query("key") String key
    );

    @GET("maps/api/geocode/json")
    Call<JsonObject> getCordinates(
            @Query("address") String address,
            @Query("key") String key
    );

    @GET("maps/api/place/textsearch/json")
    Call<JsonObject> getNearByPlaces(
            @Query("query") String input,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("key") String key
    );
}



package com.needyyy.app.webutils;


import com.google.gson.JsonObject;
import com.needyyy.app.Base.BasePojo;
import com.needyyy.app.Base.CommonPojo;
import com.needyyy.app.Chat.model.Data;
import com.needyyy.app.Modules.AddPost.models.PeopleBase;
//import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.CommentBase;
import com.needyyy.app.Modules.Home.modle.PostDataBase;
import com.needyyy.app.Modules.Home.modle.UserListBase;
import com.needyyy.app.Modules.Knocks.models.AcceptRejectRequest;
import com.needyyy.app.Modules.Knocks.models.GetReceivedRequest;
import com.needyyy.app.Modules.Login.model.forgotPassword.ForgotPassword;
import com.needyyy.app.Modules.Login.model.register.OtpMain;
import com.needyyy.app.Modules.Login.model.register.UserDataMain;
import com.needyyy.app.Modules.Login.model.setPin.SetPinBase;
import com.needyyy.app.Modules.Profile.models.GetEducation;
import com.needyyy.app.Modules.Profile.models.GetRelationStatus;
import com.needyyy.app.Modules.Profile.models.GetViewProfile;
import com.needyyy.app.Modules.Profile.models.UpdateProfile;
import com.needyyy.app.Modules.Profile.models.UserPicture.GetUserPictures;
import com.needyyy.app.Modules.adsAndPage.modle.CreatePageModel;
import com.needyyy.app.Modules.adsAndPage.modle.GetCheckSumHash;
import com.needyyy.app.Modules.adsAndPage.modle.MyPage;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.PaymentPojo;
import com.needyyy.app.Modules.adsAndPage.modle.wallet.PromoteBase;

import com.needyyy.app.Modules.adsAndPage.modle.wallet.modle.GlobalPost;
import com.needyyy.app.mypage.model.Activities.Getpostdata;
import com.needyyy.app.mypage.model.Cognito.Cognito;
import com.needyyy.app.mypage.model.Emotions.Emotions;
import com.needyyy.app.mypage.model.memberRating.MemberRating;
import com.needyyy.app.mypage.model.pagedata.GetPageData;
import com.needyyy.app.notifications.notifications.GetNotification;

import com.needyyy.app.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface WebInterface {

    //login/signup apis
    //----------------------------------------------------------------------------------------------------

    @FormUrlEncoded
    @POST(WebConstants.REGISTER)
    Call<OtpMain> register(@Field(Constant.NAME) String username,
                           @Field(Constant.EMAIL) String email,
                           @Field(Constant.Mobile) String mobile,
                           @Field(Constant.Password) String password,
                           @Field(Constant.Dob) String dob,
                           @Field(Constant.GENDER) String gender,
                           @Field(Constant.LOCATION) String location,
                           @Field(Constant.LATITUDE) String lat,
                           @Field(Constant.LONGITUDE) String lon,
                           @Field(Constant.CCODE) String ccode,
                           @Field(Constant.ISSOCIAL) String isSocial,
                           @Field(Constant.Device_Token) String deviceTocken,
                           @Field(Constant.Device_Type) String deviceType,
                           @Field("manufacturer") String manufacturer,
                           @Field("model") String model,
                           @Field("version") String version,
                           @Field("ip") String ip
    );

    @FormUrlEncoded
    @POST(WebConstants.REGISTER)
    Call<UserDataMain> registerApi(@Field(Constant.NAME) String username,
                                   @Field(Constant.EMAIL) String email,
                                   @Field(Constant.Mobile) String mobile,
                                   @Field(Constant.Password) String password,
                                   @Field(Constant.Dob) String dob,
                                   @Field(Constant.GENDER) String gender,
                                   @Field(Constant.LOCATION) String location,
                                   @Field(Constant.LATITUDE) String lat,
                                   @Field(Constant.LONGITUDE) String lon,
                                   @Field(Constant.CCODE) String ccode,
                                   @Field(Constant.ISSOCIAL) String isSocial,
                                   @Field(Constant.Device_Type) String deviceType,
                                   @Field(Constant.OTP) String otp,
                                   @Field("quick_blox_media_json") String quick_blox_media_json,
                                   @Field("manufacturer") String manufacturer,
                                   @Field("model") String model,
                                   @Field("version") String version,
                                   @Field("ip") String ip
                                   );

    @FormUrlEncoded
    @POST(WebConstants.SETPIN)
    Call<SetPinBase> setPin(@Field(Constant.PIN) String pin,
                            @Field(Constant.USERS_ID) String userid,
                            @Field("is_fingerprint_enable") String fingureprint
                            );


    @FormUrlEncoded
    @POST(WebConstants.LOGIN_URL)
    Call<UserDataMain> loginApi(@Field(Constant.USERS_ID) String userid,
                                @Field(Constant.ISSOCIAL) int isSocial,
                                @Field(Constant.Device_Type) String deviceType,
                                @Field(Constant.EMAIL) String email,
                                @Field(Constant.Password) String password,
                                @Field("manufacturer") String manufacturer,
                                @Field("model") String model,
                                @Field("version") String version,
                                @Field("ip") String ip

    );

    @FormUrlEncoded
    @POST(WebConstants.REPORT_POST)
    Call<CommonPojo> ReportPost(@Field("report_master_id") String masterid,
                                @Field("post_id") String postid,
                                @Field("description") String description);
    @FormUrlEncoded
    @POST(WebConstants.ACCEPT_REJECT_REQUEST_DATING)
    Call<AcceptRejectRequest> acceptRejectRequestDating(@Field(Constant.MEMBER_ID) String member_id,
                                                        @Field(Constant.STATUS) String status);

    @FormUrlEncoded
    @POST(WebConstants.FORGOT_PASSWORD)
    Call<OtpMain> forgotPassword(@Field(Constant.Mobile) String mobile);

    @FormUrlEncoded
    @POST(WebConstants.OTP_PASSWORD)
    Call<ForgotPassword> otpPassword(@Field(Constant.OTP) String otp,
                                     @Field(Constant.Mobile) String mobile,
                                     @Field(Constant.Password) String password);

    //add post
    //----------------------------------------------------------------------------------------------------



    @FormUrlEncoded
    @POST(WebConstants.ADD_POST)
    Call<BasePojo> addPost(
                          @Field(Constant.TEXT) String text,
                           @Field(Constant.TAG_PEOPLE) String tag_people,
                           @Field(Constant.MEDIA) JSONArray link,
                          @Field(Constant.Type) String type,
                          @Field("feeling") String feeling,
                          @Field("feeling_status") String feeling_status
                          );

    @FormUrlEncoded
    @POST(WebConstants.ADD_POST)
    Call<BasePojo> updatePost(
            @Field(Constant.ID) String id,
            @Field(Constant.TEXT) String text,
            @Field(Constant.TAG_PEOPLE) String tag_people,
            @Field(Constant.MEDIA) JSONArray link,
            @Field(Constant.Type) String type,
            @Field("feeling") String feeling,
            @Field("feeling_status") String feeling_status


    );

    @FormUrlEncoded
    @POST(WebConstants.ADD_POST)
    Call<BasePojo> addPostt(@Field(Constant.TEXT) String text,
                            @Field(Constant.TAG_PEOPLE) String tag_people,
                            @Field(Constant.MEDIA) JSONArray link,
                            @Field(Constant.PAGE_ID) String pageid,
                            @Field(Constant.Type) String type,
                            @Field("feeling") String feeling,
                            @Field("feeling_status") String feeling_status

    );

    @FormUrlEncoded
    @POST(WebConstants.ADD_POST)
    Call<BasePojo> addPostProfile(@Field(Constant.TEXT) String text,
                            @Field(Constant.TAG_PEOPLE) String tag_people,
                            @Field(Constant.MEDIA) JSONArray link,
                            @Field("is_private") String is_private,
                            @Field("posted_to") String posted_to,
                            @Field(Constant.Type) String type,
                                  @Field("feeling") String feeling,
                                  @Field("feeling_status") String feeling_status
    );


    @FormUrlEncoded
    @POST(WebConstants.ADD_POST)
    Call<BasePojo> CheckIn(@Field(Constant.TEXT) String text,
                           @Field(Constant.CHECK_IN_FOR) String checkInFor,
                           @Field(Constant.LOCATION) String link,
                           @Field(Constant.LATITUDE) String latitude,
                           @Field(Constant.LONGITUDE) String longitud,
                           @Field(Constant.Type) String type);
    // Globalpost
    @FormUrlEncoded
    @POST(WebConstants.GET_GLOBAL_POST)
    Call<GlobalPost> GLOBALPOST(@Field("is_approved") String is_approved
                             );
    @FormUrlEncoded
    @POST(WebConstants.ADD_GLOBAL_POST)
    Call<GlobalPost> ADDGLOBALPOST(@Field("caption") String caption,
                                   @Field(Constant.ID) String id
    );

    //knocks
    //----------------------------------------------------------------------------------------------------
    @FormUrlEncoded
    @POST(WebConstants.GET_FRIENDS)
    Call<PeopleBase> getFriends(@Field(Constant.PAGE) int page,
                                @Field(Constant.LIMIT) int limit,
                                @Field(Constant.SEARCH) String search,
                                @Field(Constant.LATITUDE) String latitude,
                                @Field(Constant.LONGITUDE) String lognitude);
    @FormUrlEncoded
    @POST(WebConstants.GET_ALLFRIENDS)
    Call<PeopleBase> getAllFriends(@Field(Constant.PAGE) int page,
                                   @Field(Constant.LIMIT) int limit,
                                   @Field(Constant.SEARCH) String search);
    @FormUrlEncoded
    @POST(WebConstants.GET_ALLFRIENDS_SUUGGESTION)
    Call<PeopleBase> getAllSuggestionFriends(@Field(Constant.PAGE) int page,
                                             @Field(Constant.LIMIT) int tag_people,
                                             @Field(Constant.RANGE) String range,
                                             @Field(Constant.LATITUDE) double latitude,
                                             @Field(Constant.LONGITUDE) double longitude,
                                             @Field(Constant.AGE_FROM) String age_from,
                                             @Field(Constant.AGE_TO) String age_to,
                                             @Field(Constant.GENDER) String gender,
                                             @Field(Constant.SEARCH) String search);


    @FormUrlEncoded
    @POST(WebConstants.GET_RECEIVED_REQUESTS)
    Call<GetReceivedRequest> getReceivedRequest(@Field(Constant.LIMIT) String limit,
                                                @Field(Constant.PAGE) String page,
                                                @Field(Constant.SEARCH) String search

                                                );
    @FormUrlEncoded
    @POST(WebConstants.GET_RECEIVED_REQUESTS)
    Call<PeopleBase> getReceivedRequestWithMessage(@Field(Constant.LIMIT) String limit,
                                                   @Field(Constant.PAGE) String page,
                                                   @Field(Constant.SHOULD_MESSAGE) String isMessage);

    @FormUrlEncoded
    @POST(WebConstants.GET_RECEIVED_REQUESTS_DATEING)
    Call<PeopleBase> getReceivedRequestWithMessageDating(@Field(Constant.LIMIT) String limit,
                                                   @Field(Constant.PAGE) String page,
                                                   @Field(Constant.SHOULD_MESSAGE) String isMessage);
    @FormUrlEncoded
    @POST(WebConstants.ACCEPT_REJECT_REQUEST)
    Call<AcceptRejectRequest> acceptRejectRequest(@Field(Constant.MEMBER_ID) String member_id,
                                                  @Field(Constant.STATUS) String status);

    @FormUrlEncoded
    @POST(WebConstants.GET_ALL_MEMBER)
    Call<GetReceivedRequest> GetAllMember(@Field(Constant.LIMIT) String limit,
                                           @Field(Constant.PAGE) String page,
                                          @Field(Constant.SEARCH) String search
                                          );
    @FormUrlEncoded
    @POST(WebConstants.GET_BLOCKED_MEMBER)
    Call<GetReceivedRequest> GetBlockedmember(@Field(Constant.LIMIT) String limit,
                                          @Field(Constant.PAGE) String page,
                                              @Field(Constant.SEARCH) String search
                                              );

    @FormUrlEncoded
    @POST(WebConstants.BLOCK_UNBLOCK)
    Call<GetReceivedRequest> BlockUnblock(@Field(Constant.MEMBER_ID) String member_id,
                                          @Field(Constant.STATUS) String status);

    @FormUrlEncoded
    @POST(WebConstants.GET_SEND_KNOCKS)
    Call<GetReceivedRequest> GetSentKnocks(@Field(Constant.LIMIT) String limit,
                                              @Field(Constant.PAGE) String page,
                                           @Field(Constant.SEARCH) String search

                                           );

    @FormUrlEncoded
    @POST(WebConstants.GET_KNOCKS_FRIENDS)
    Call<GetReceivedRequest> GetFriends(@Field(Constant.LIMIT) String limit,
                                        @Field(Constant.PAGE) String page,
                                        @Field(Constant.SEARCH) String search

    );
    // delete post

    @FormUrlEncoded
    @POST(WebConstants.DeletePost)
    Call<CommonPojo> deletePost(@Field(Constant.POST_ID) String post_id
                                                  );



    //profile
    //----------------------------------------------------------------------------------------------------
    @FormUrlEncoded
    @POST(WebConstants.GET_UPDATED_PROFILE)
    Call<UpdateProfile> getUpdatedProfile(
            @Field(Constant.NAME) String username,
//          @Field(Constant.PROFILE_IMAGE)         String profile_image,
//          @Field(Constant.SSNID)                 String ssn_id,
            @Field(Constant.BIO) String bio,
            @Field("profession_detail") String professional_details,
            @Field(Constant.EDUCATIONAL_DETAIL) String educational_detail,
            @Field(Constant.ADDRESS) String location,
            @Field(Constant.GENDER) String gender,
            @Field(Constant.Dob) String dob,
            @Field(Constant.NICKNAME) String nickname,
            @Field(Constant.INTERESTED_IN) String interested_in,
            @Field(Constant.RELATIONSHIP_STATUS) String relation_status,
            @Field(Constant.OPEN_DATING) String open_dating,
            @Field(Constant.hobbies) String hobbies

            );

    @FormUrlEncoded
    @POST(WebConstants.GET_UPDATED_PROFILE)
    Call<UpdateProfile> getUpdatedProfile(
            @Field(Constant.PROFILE_PICTURE) String profilepicture,
            @Field("cover_picture") String cover_picture
    );
    @FormUrlEncoded
    @POST(WebConstants.GET_UPDATED_PROFILE)
    Call<UpdateProfile> updateprivacy(
            @Field("is_private") String isprivate
    );

    @FormUrlEncoded
    @POST(WebConstants.GET_UPDATED_PROFILE)
    Call<UpdateProfile> updatefingure(
            @Field("is_fingerprint_enable") String is_fingerprint_enable
    );

    @FormUrlEncoded
    @POST(WebConstants.GET_UPDATED_PROFILE)
    Call<UpdateProfile> updatenotification(
            @Field("noti_status") String noti_status
    );

    @FormUrlEncoded
    @POST(WebConstants.VIEW_PROFILE)
    Call<GetViewProfile> viewProfile(@Field(Constant.MEMBER_ID) String id);

    @FormUrlEncoded
    @POST(WebConstants.GET_EDUCATION_DETAIL)
    Call<GetEducation> getQuali(@Field(Constant.Type) int type);

    @FormUrlEncoded
    @POST(WebConstants.GET_RELATION_STATUS)
    Call<GetRelationStatus> getRelationStatus(@Field(Constant.USER_ID) String id);

    @FormUrlEncoded
    @POST(WebConstants.GET_USER_POST)
    Call<PostDataBase> getUserPost(@Field(Constant.PAGE) int page,
                                   @Field(Constant.LIMIT) int limit,
                                   @Field(Constant.MEMBERS_ID) String memberId);

    @FormUrlEncoded
    @POST(WebConstants.GET_PAGE_POST)
    Call<PostDataBase> getMyPageMyPost(@Field(Constant.PAGE) int page,
                                       @Field(Constant.LIMIT) int limit,
                                       @Field(Constant.PAGE_ID) String pageid
                                       );
    //Ads Manager
    //------------------------------------------------------------------------------------------------

    @FormUrlEncoded
    @POST(WebConstants.ADD_PAGE)
    Call<CreatePageModel> createPage(@Field(Constant.PAGE_IMAGE) String profileImageurl,
                                     @Field(Constant.BANNER_IMAGE) String banerImageUrl,
                                     @Field(Constant.Title) String title,
                                     @Field(Constant.DESCRIPTION) String description,
                                     @Field(Constant.PAGE_CATEGORY) String category,
                                     @Field("contact") String contact,
                                     @Field("website") String website,
                                     @Field("address") String address
                                     );

    @FormUrlEncoded
    @POST(WebConstants.GET_MY_PAGE)
    Call<MyPage> getMyPage(@Field(Constant.PAGE) int page,
                           @Field(Constant.LIMIT) int limit);

    @FormUrlEncoded
    @POST(WebConstants.GET_PAGE)
    Call<CreatePageModel> getPage(@Field(Constant.PAGE_ID) String page_id);


    //Post
    //-------------------------------------------------------------------------------------------------------------

    @FormUrlEncoded
    @POST(WebConstants.GET_MY_POST)
    Call<PostDataBase> getMyPost(@Field(Constant.PAGE) int page,
                                 @Field(Constant.LIMIT) int limit,
                                 @Field(Constant.Dob) String dob,
                                 @Field(Constant.GENDER) String genger,
                                 @Field(Constant.SEARCH) String search

                                 );

    @FormUrlEncoded
    @POST(WebConstants.GET_MY_Feeds)
    Call<PostDataBase> getMyFeeds(@Field(Constant.PAGE) int page,
                                 @Field(Constant.LIMIT) int limit,
                                 @Field(Constant.Dob) String dob,
                                 @Field(Constant.GENDER) String genger,
                                 @Field(Constant.SEARCH) String search
    );


    @FormUrlEncoded
    @POST(WebConstants.LIKE_DISLIKE)
    Call<CommonPojo> likeDislike(@Field(Constant.POST_ID) String page,
                                 @Field(Constant.STATUS) int limit,
                                 @Field(Constant.POST_TYPE) String isPost,
                                 @Field(Constant.TASK_FOR) int taskFor);
    @FormUrlEncoded
    @POST(WebConstants.LIKE_COUNT)
    Call<UserListBase> likePostUserList(@Field(Constant.POST_ID) String postid,
                                        @Field(Constant.PAGE) int page,
                                        @Field(Constant.LIMIT) int limit,
                                        @Field(Constant.STATUS) String status)
            ;

    @FormUrlEncoded
    @POST(WebConstants.GETCOMMENT)
    Call<CommentBase> getComment(@Field(Constant.POST_ID) String postId,
                                 @Field(Constant.COMMENT_ID) String commentId,
                                 @Field(Constant.LIMIT) String limit,
                                 @Field(Constant.PAGE) String page);
    @FormUrlEncoded
    @POST(WebConstants.POST_COMMENT)
    Call<CommonPojo> postComment(@Field(Constant.POST_ID) String postId,
                                 @Field(Constant.COMMENT) String commentId,
                                 @Field(Constant.PARENT_ID) String limit,
                                 @Field("id") String id,
                                 @Field("tagged_people") String taggedpeople
                                 );


    @FormUrlEncoded
    @POST(WebConstants.DELETE_POST_COMMENT)
    Call<CommentBase> deletePostComment(@Field(Constant.COMMENT_ID) String commentId,
                                 @Field(Constant.Type) int type);


// notiication
    @FormUrlEncoded
    @POST(WebConstants.GET_POST)
    Call<Getpostdata> getPost(@Field(Constant.POST_ID) String postId
    );

    //payment
    //--------------------------------------------------------------------------------------------------------

    @FormUrlEncoded
    @POST(WebConstants.CHECKSUM)
    Call<GetCheckSumHash> getCheckSum(
            @Field(Constant.MID) String mid,
            @Field(Constant.ORDER_ID) String order_id,
            @Field(Constant.CUST_ID) String cust_id,
            @Field(Constant.INDUSTRY_TYPE_ID) String industry_id,
            @Field(Constant.CHANNEL_ID) String channel_id,
            @Field(Constant.WEBSITE) String website,
            @Field(Constant.TXN_AMOUNT) String txn_amount,
            @Field(Constant.CALLBACK_URL) String callback_url);

    @FormUrlEncoded
    @POST(WebConstants.PAYMENT)
    Call<PaymentPojo> payment(@Field(Constant.MODE) int mode,
                              @Field(Constant.FINAL_AMOUNT) String txn_amount,
                              @Field(Constant.TXNID) String txn_id,
                              @Field(Constant.STATUS) String status);
    @FormUrlEncoded
    @POST(WebConstants.PROMOTE_PAGE)
    Call<PromoteBase> promotePage(@Field(Constant.PAGE_ID) String pageId,
                                  @Field("target_location") String target_location,
                                  @Field(Constant.RANGE_FROM) int rangeMin,
                                  @Field(Constant.RANGE_TO) int rangeMax,
                                  @Field(Constant.GENDER) int gender,
                                  @Field(Constant.INTEREST) String interest,
                                  @Field(Constant.DAILY_BUDGET) String budgetPrice,
                                  @Field(Constant.DATE_FROM) String dateFrom,
                                  @Field(Constant.DATE_TO) String dateTo,
                                  @Field(Constant.FINAL_PRICE) int finaAmount
                                );
    @FormUrlEncoded
    @POST(WebConstants.PROMOTE_POST)
    Call<PromoteBase> promotePost(@Field(Constant.POST_ID) String pageId,
                                  @Field("target_location") String target_location,
                                  @Field(Constant.RANGE_FROM) int rangeMin,
                                  @Field(Constant.RANGE_TO) int rangeMax,
                                  @Field(Constant.GENDER) int gender,
                                  @Field(Constant.INTEREST) String interest,
                                  @Field(Constant.DAILY_BUDGET) String budgetPrice,
                                  @Field(Constant.DATE_FROM) String dateFrom,
                                  @Field(Constant.DATE_TO) String dateTo,
                                  @Field(Constant.FINAL_PRICE) int finaAmount
                                );

    @FormUrlEncoded
    @POST(WebConstants.PROMOTE_PAGE)
    Call<PromoteBase> promotePage(@Field(Constant.PAGE_ID)       String pageId,
                                  @Field(Constant.CITY)          String city);
    @FormUrlEncoded
    @POST(WebConstants.GET_USER_PICTURES)
    Call<GetUserPictures> getUserPictures(@Field(Constant.PAGE)   int page,
                                       @Field(Constant.LIMIT) int limit,
                                       @Field(Constant.MEMBER_ID) String member_id);

    @FormUrlEncoded
    @POST(WebConstants.HELP_AND_SUPPORT)
    Call<CommonPojo> helpAndSupport(@Field(Constant.SUBJECT) String subject,
                                    @Field(Constant.MESSAGE) String message);


    @FormUrlEncoded
    @POST(WebConstants.CHANGE_PASSWORD)
    Call<CommonPojo> changePassword(@Field(Constant.Password) String password,
                                    @Field(Constant.OLDPASSWORD) String old_password);

    @FormUrlEncoded
    @POST(WebConstants.Invite)
    Call<CommonPojo> Invite(@Field(Constant.PAGE_ID) String page_id,
                            @Field(Constant.invite_people_ids) String invite_people_ids);

    @FormUrlEncoded
    @POST(WebConstants.SHAREPOST)
    Call<CommonPojo> sharePost(@Field(Constant.POST_ID) int postid,
                               @Field(Constant.STATUS) String status,
                               @Field(Constant.CAPTION) String message,
                               @Field(Constant.TAG_PEOPLE) String taggedId);

    @FormUrlEncoded
    @POST(WebConstants.GET_NOTIFICATION)
    Call<GetNotification> GETNOTIFICATION(@Field(Constant.PAGE) int page,
                                          @Field(Constant.LIMIT) int limit
                           );

    @FormUrlEncoded
    @POST(WebConstants.GET_PAGEDETAILS)
    Call<GetPageData> getPageDetails(@Field(Constant.PAGE_ID) String page_id
    );
    @FormUrlEncoded
    @POST(WebConstants.GET_USER_CHAT_PICTURES)
    Call<GetUserPictures> getUserChatImage(@Field(Constant.PAGE_ID) String page_id,
                                           @Field(Constant.LIMIT) int limit,
                                           @Field(Constant.PAGE) int page,
                                           @Field(Constant.DATA_TYPE) String data_type);



    @FormUrlEncoded
    @POST(WebConstants.SENDREQUEST)
    Call<GetReceivedRequest> sendKnockRequest(@Field(Constant.MEMBER_ID) String member_id,
                                              @Field(Constant.STATUS) int status);


    @FormUrlEncoded
    @POST(WebConstants.SENDREQUEST_DATING)
    Call<GetReceivedRequest> sendKnockRequestDating(@Field(Constant.MEMBER_ID) String member_id,
                                                    @Field(Constant.STATUS) int status,
                                                    @Field(Constant.MESSAGE) String message);



    @FormUrlEncoded
    @POST(WebConstants.MASTER_INDEX)
    Call<JsonObject> MasterIndex(@Field("task_for") int task_for,
                                 @Field(Constant.PAGE) int page,
                                 @Field(Constant.LIMIT) int limit

    );




    @FormUrlEncoded
    @POST(WebConstants.GET_BOOKMARK_PAGES)
    Call<MyPage> getBookmarkPages(   @Field(Constant.PAGE)int page,
                                       @Field(Constant.LIMIT) int limit);


    @GET("maps/api/place/autocomplete/json")
    Call<JsonObject> getPlaces(
            @Query("input") String input,
            @Query("key") String key
    );

    @GET("maps/api/place/details/json")
    Call<JsonObject> getPlacesDetails(
            @Query("placeid") String placeId,
            @Query("fields") String fields,
            @Query("key") String key
    );

    @FormUrlEncoded
    @POST(WebConstants.GET_POST)
    Call<CreatePageModel> getPostdetails(@Field(Constant.POST_ID) String postId, @Field(Constant.FORBOOTS) int limit
    );

    @FormUrlEncoded
    @POST(WebConstants.GET_EMOTIONS)
    Call<Emotions> getEmotions(@Field(Constant.PARENT_ID) String parentid

    );

    @FormUrlEncoded
    @POST(WebConstants.MEMBER_RATING)
    Call<MemberRating> getrating(@Field(Constant.MEMBER_ID) String memberid,
                                 @Field(Constant.RATING) String rating

    );


    @FormUrlEncoded
    @POST(WebConstants.COGNITO)
    Call<Cognito> cognito(@Field(Constant.MEMBER_ID) String member_id,
                          @Field(Constant.STATUS) String status

    );

    @FormUrlEncoded
    @POST(WebConstants.WELCOME_PAGE_MESSAGE)
    Call<CommonPojo> WelcomeMessage(@Field(Constant.PAGE_ID) String page_id,
                                    @Field(Constant.MESSAGE) String message
    );
   // video count
   @FormUrlEncoded
   @POST(WebConstants.Video_Count)
   Call<CommonPojo> VideoCount(@Field("meta_id") String mediaid
   );

    @FormUrlEncoded
    @POST(WebConstants.Send)
    Call<String> PushNotification(@Field("device_token") String device_token,
                                  @Field("title") String title,
                                  @Field("message") String message,
                                  @Field("type") String push_type,
                                  @Field("json") String json
    );
}
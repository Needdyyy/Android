package com.needyyy.app.webutils;

/**
 * Created by admin1 on 19/1/18.
 */
public class WebConstants {

    public static final String STATUS = "status";
    public static final String MESSAGES = "message";
    public static final String DATA = "data";

 //   public final static String BASE_URL                    =  "http://54.158.214.94/index.php/api/";
    public final static String BASE_URL_NODE                    =  "http://54.158.214.94:2213/";
   public final static String Firebase                    =  "https://fcm.googleapis.com/";



        public final static String BASE_URL                    = "http://needyyy.com/index.php/api/";
    public static final String kGoogleMapsBaseURL          =  "https://maps.googleapis.com/";

    public final static String MASTER_INDEX                =  "master_hit/index";
    //login Signup
    public final static String ACCEPT_REJECT_REQUEST_DATING       = "dating_relation/request_accept_reject";
    public final static String SEND_OTP_URL                = "user/send_otp";
    public final static String LOGIN_URL                   = "user/login_authentication";
    public final static String REGISTER                    = "user";
    public final static String FORGOT_PASSWORD             = "user/forget_password";
    public final static String OTP_PASSWORD                = "user/forget_password";

    //profile
    public final static String GET_UPDATED_PROFILE         = "user/update_profile";
    public final static String SETPIN                      = "user/set_user_pin";
    public final static String GET_EDUCATION_DETAIL        = "user/get_educational_detail";
    public final static String GET_RELATION_STATUS          = "user/get_relationship";
    public final static String VIEW_PROFILE                = "user/view_profile";

    //posts
    public final static String ADD_POST                    = "feeds/add_post";
    public final static String GET_GLOBAL_POST             = "feeds/get_global_post";
    public final static String ADD_GLOBAL_POST             = "feeds/add_global_post";
    public final static String GET_FRIENDS                 = "knock/get_friends";
    public static final String GET_MY_POST                 = "feeds/get_posts";
    public static final String GET_MY_Feeds                 = "feeds/get_feeds";
    public static final String LIKE_DISLIKE                = "feeds/like_dislike";
    public static final String LIKE_COUNT                  = "feeds/get_post_liked_user";
    public static final String GET_USER_POST               = "feeds/get_user_posts";
    public static final String GET_PAGE_POST               = "pages/get_page_posts";
    public static final String GETCOMMENT                  = "feeds/get_post_comment";
    public static final String POST_COMMENT                = "feeds/post_comment";
    public static final String DELETE_POST_COMMENT         = " feeds/delete_post_comment ";

    //knocks
    public final static String GET_RECEIVED_REQUESTS       = "knock/get_received_request";
    public final static String GET_RECEIVED_REQUESTS_DATEING   = "dating_relation/get_received_request";
    public final static String ACCEPT_REJECT_REQUEST       = "knock/request_accept_reject";
    public final static String GET_ALL_MEMBER              = "knock/get_all_member";
    public final static String GET_BLOCKED_MEMBER          = "knock/get_blocked_members";
    public final static String GET_SEND_KNOCKS             =  "knock/get_sent_request";
    public final static String GET_KNOCKS_FRIENDS          =  "knock/get_friends";
    public final static String BLOCK_UNBLOCK               =  "knock/member_block_unblock";
    //add page
    public final static String ADD_PAGE                    = "pages/add_page";
    public final static String GET_MY_PAGE                 = "pages/get_my_pages";
    public final static String GET_PAGE                    = "pages/get_page";
    public final static String Invite                      = "pages/page_invite";

    //delete
    public final static String DeletePost                  = "feeds/delete_post";




    //payment gateways
    public static final String CHECKSUM                      = "payment/generate_checksum";
    public static final String PAYMENT                       = "payment/payment";
    public static final String PROMOTE_PAGE                  = "pages/promote_page";
    public static final String PROMOTE_POST                  = "feeds/boost_post";
    public static final String HELP_AND_SUPPORT              = "help_support/index";
    public static final String CHANGE_PASSWORD               = "user/change_password ";
    public static final String SHAREPOST                     = "feeds/post_share";
    public static final String SENDREQUEST                   = " knock/friends_add_remove ";
    public static final String SENDREQUEST_DATING            = " dating_relation/friends_add_remove ";
    public final static String GET_ALLFRIENDS                = "knock/get_all_member";
    public final static String GET_ALLFRIENDS_SUUGGESTION    = "Dating_relation/get_all_member";



    //notification

    public static final String GET_POST                         = "feeds/get_post_detail";
    public static final String GET_NOTIFICATION                 = "notification/get_notification";
    public static final String GET_PAGEDETAILS                  = "pages/get_page_detail";

    public final static String GET_USER_PICTURES                = "user/get_user_pictures";

   public final static String GET_USER_CHAT_PICTURES                = "pages/get_page_meta";


    // Bookmark

    public final static String GET_BOOKMARK_PAGES               = "pages/get_bookmark_pages";

    public final static String REPORT_POST                      = "feeds/report_post";


    public final static String GET_EMOTIONS                     ="feeds/get_emotions";

    public final static String MEMBER_RATING                    = "dating_relation/member_rating";

    public final static String COGNITO                          = "dating_relation/remove_withdraw_cognito ";

    public final static String WELCOME_PAGE_MESSAGE              ="pages/initialize_page_chat";

    // vedio count

   public final static String Video_Count              =      "feeds/meta_video_count_manager";

  // public final static String Send             =      "fcm/send";

  public final static String Send             =      "master_hit/sent_push";

}


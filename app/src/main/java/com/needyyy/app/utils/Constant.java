package com.needyyy.app.utils;

import android.graphics.Color;

import com.amazonaws.regions.Regions;
import com.needyyy.AppController;

import java.util.Calendar;
import java.util.regex.Pattern;

public interface Constant {

    /*google Api Key*/
    String GOOGLE_API_KEY = "AIzaSyA9R80NhybUB1ym0uC4rHywulECnM2Rhk0";

    public String INTERNETERROR = "Internet not working, Please check your connection";

    public String APP_VERSION = "appversion";

    public Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public String IMAGE_PATH = "image_path";
    public int PICK_CAMERA = 3;
    public int PICK_GALLERY = 4;
    public int RC_SIGN_IN = 9001;

    String PDF = "pdf";
    String PPT = "ppt";
    String DOC = "doc";
    String EPUB = "epub";
    String TEST = "test";
    String XLS = "xls";

    public String RESULT = "Result";
    public int REQUEST_LOCATION = 115;
    public int PICK_IMAGE_ID = 100;

    public String MESSAGES = "message";

    public int NOTIFICATION_ID = 100001;
    public int NOTIFICATION_ID_BIG_IMAGE = 1000001;

    public String SOCIAL = "1";
    public String DATING = "2";

    public String TYPE_HOME = "1";
    public String TYPE_OFFICE = "2";
    public String TYPE_OTHER = "3";
    public String TYPE_ADD = "ADD";
    public String TYPE_EDIT = "EDIT";
    public String TYPE_SELECT = "SELECT";

    public String STATUS = "status";
    public String DATA = "data";
    public String MESSAGE = "message";

    public static final String Type = "type";
    public String OTP = "otp";
    public String NAME = "name";
    public String EMAIL = "email";
    public String Mobile = "mobile";
    public String STREET = "street";
    public String LANDMARK = "landmark";
    public String LATITUDE = "latitude";
    public String LONGITUDE = "longitude";

    public String ISMOBILE = "mobile";

    public String Dob = "dob";
    public String Country_Code = "country_code";
    public String Device_Type = "device_type";
    public String Device_Token = "Devicetoken";
    public String Login_Type = "login_type";
    public String Password = "password";
    public String NewPassword = "newPassword";
    public String CODE = "code";
    public String USERS_ID = "Userid";
    public String USER_ID = "user_id";
    public String ADDRESS_ID = "address_id";
    public String ID = "id";
    public String GENDER = "gender";
    public String description = "description";
    public static final String USERNAME = "username";


    String Jwt = "Jwt";
    String JWT = "jwt";

    //addPost
    public String POST_TYPE = "post_type";
    public String TEXT = "text";
    public String POST_TAG = "post_tag";
    public String LAT = "lat";
    public String LONG = "lon";
    public String LOCATION = "location";
    public String ADDRESS = "address";
    public String TAG_PEOPLE = "tagged_people";
    public String LINK = "link";
    public String PARENT_FOLDER = "needyyy";

    //knocks
    public String LIMIT = "limit";
    public String PAGE = "page";
    public String MEMBER_ID = "member_id";
    public String DATA_TYPE = "data_type";


    String ISONLINE = "1";
    //adsManager
    public String PAGE_ID = "page_id";
    public String SHOULD_MESSAGE = "should_message_request";

    //profile
    public String SSNID = "ssn";
    public String BIO = "bio";
    public String COLLEGE_NAME = "college_name";
    public String SCHOOL_NAME = "school_name";
    public String HOMETOWN = "hometown_new";
    public String CURRENT_ADDRESS = "current_address";
    public String NICKNAME = "nick_name";
    public String INTERESTED_IN = "interested_in";
    public String RELATIONSHIP_STATUS = "relation_ship_id";
    public String OPEN_DATING = "open_dating";
    public String hobbies = "hobbies";
    public String EDUCATIONAL_DETAIL = "educational_detail";


    public String SUB_CAT_ID = "sub_cate_id";
    public String CATEGORY_ID = "category_id";
    public String RANGE = "range";
    public String CHILD_ID = "child_id";
    public String LAST_POST_ID = "last_post_id";
    public String TWEET_ID = "tweet_id";
    public String POST_ID = "post_id";
    public String LAST_COMMENT_ID = "last_comment_id";
    public String COMMENT_ID = "comment_id";
    public String TAG_ID = "tag_id";
    public String LAST_COUNT_VALUE = "last_count_value";
    public String COMMENT = "comment";
    public String PHOTO = "photo";
    public String PROFILE_IMAGE = "profile_image";
    public String POST = "post";
    public String TAG = "tag";

    public String master_service_category_level2_id = "master_service_category_level2_id";
    public String get_master_service_category_level1 = "master_service_category_level1_id";
    public String STORE_ID = "store_id";
    public String CATEGORYID = "category_id";
    public String SCHEDULE_Date = "schedule_date";
    public String TIME = "time";
    public String FILE_COUNT = "field_count";
    public String AMOUNT = "amount";
    public String historyId = "history_id";
    public String Title = "title";
    public String PUSH_TYPE = "push_type";
    public String IMAGE = "image";
    public String SCHEDULE_TYPE = "SCHEDULE_TYPE";
    public String Quantity = "quantity";
    public String CART_ID = "cart_id";


    // PAYTM Credentials

    public static final String PAYTM_MID = "AFXVlm72423397549196";  // Local
    public static final String PAYTM_WEBSITE = "WEBSTAGING";
    //    public static final String PAYTM_WEBSITE   = "APP_STAGING";
    public static final String INDUSTRYTYPE_ID = "Retail";
    public static final String CHANNELID = "WAP";
    public static final String SERVER_TYPE = "LOCAL";

//    public static final String PAYTM_MID = "FitInd67722541497982";  // Live
//    public static final String PAYTM_WEBSITE = "APPPROD";
//    public static final String INDUSTRYTYPE_ID = "Retail109";
//    public static final String CHANNELID = "WAP";
//    public static final String SERVER_TYPE = "PROD";


    public static final String CALLBACK_URL = "CALLBACK_URL";
    public static final String WEBSITE = "WEBSITE";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String INDUSTRY_TYPE_ID = "INDUSTRY_TYPE_ID";
    public static final String MID = "MID";

    public static final String CHECKSUMHASH = "CHECKSUMHASH";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String CUST_ID = "CUST_ID";
    public static final String TXN_AMOUNT = "TXN_AMOUNT";


    public static final String STATUs = "STATUS";
    public static final String TXN_ID = "TXNID";
    public static final String TXNAMOUNT = "TXNAMOUNT";
    public static final String ORDERID = "ORDERID";
    public static final String RESP_MSG = "RESPMSG";
    public static final String TXN_DATE = "TXNDATE";

    public static final String REVIEW = "review";
    public static final String RATING = "rating";

    public static final String CHECKOUT_ID = "check_out_id";

    public static final String DATE = "date";


    public static final String CART_UPDATE = "cartupdate";
    public static final String kData = "data";

    public static final String TXNID = "txn_id";

    public static final String INDEX = "index";
    public static final String MODE = "mode";

    public static final String SUBJECT = "subject";
    public static final String DESCRIPTION = "description";
    public static final String COUPON = "coupon";
    public static final String BRICKSID = "bricks_id";
    public static final String REMOVEOLD = "remove_old";
    public static final String PAYMENT_ID = "payment_id";
    public static final String REF_ID = "ref_txn_id";
    public static final String MERCHANT_EMAIL = "merchant_email";
    public static final String SECRET_KEY = "secret_key";
    public static final String REFUND_AMOUNT = "refund_amount";
    public static final String REFUND_REASON = "refund_reason";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String ORDERId = "order_id";
    public static final String CCODE = "c_code";
    public static final String ISSOCIAL = "is_social";
    public static final String PIN = "pin";

    public static final String COGNITO_POOL_ID = "us-east-1:bfffbcda-76ce-4509-9607-1a03c078f470";
    public static final Regions AWS_REGION = Regions.US_EAST_1;
    public static final String BUCKET_NAME = "needy-app";
    public static final String AWS_URL = "https://s3.amazonaws.com/";
    public static final String MEDIA = "media";
    String FILE_TYPE = "file_type";
    String ALREADY_TAGGED_PEOPLE = "AlreadyTaged";
    String COLOR_CODE = "color_code";// primary id of logged in user
    int color[] = {Color.parseColor("#F44336"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#E91E63"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#603e94"),
            Color.parseColor("#8C9EFF"),
            Color.parseColor("#00695C")};
    public static final int REQUEST_TAKE_GALLERY_DOC = 23;
    String VIDEO = "video";

    String AMAZON_S3_END_POINT = "https://s3.amazonaws.com/";
    String AMAZON_S3_BUCKET_NAME_VIDEO = "needyyyapp-project/needyyy_fanwall_videos";
    String AMAZON_S3_IMAGE_PREFIX = "https://s3.amazonaws.com/";
    String GOOGLE_PREVIEW_DOC_URL = "https://docs.google.com/gview?embedded=true&url=";
    String OFFLINE_VIDEOTAGS_DATA = "offline_videotag_data";

    String API_AMAZON_S3_BUCKET_NAME_PROFILE_IMAGES = "needyyyapp-project/needyyy_profile_images";
    String API_AMAZON_S3_BUCKET_NAME_FANWALL_IMAGES = "needyyyapp-project/needyyy_fanwall_images";
    String API_AMAZON_S3_BUCKET_NAME_VIDEO_IMAGES = "needyyyapp-project/video_thumbnails";
    String API_AMAZON_S3_BUCKET_NAME_DOCUMENT = "needyyyapp-project/needyyy_doc_folder";
    String API_AMAZON_S3_BUCKET_NAME_FEEDBACK = "needyyyapp-project/feedback_images";
    String API_AMAZON_S3_FILE_NAME_CREATION = AppController.getManager().getId() + "sample_" + Calendar.getInstance().getTimeInMillis();


    String AMAZON_S3_BUCKET_NAME_PROFILE_IMAGES = API_AMAZON_S3_BUCKET_NAME_PROFILE_IMAGES;
    String AMAZON_S3_BUCKET_NAME_FANWALL_IMAGES = API_AMAZON_S3_BUCKET_NAME_FANWALL_IMAGES;
    String AMAZON_S3_BUCKET_NAME_VIDEO_IMAGES = API_AMAZON_S3_BUCKET_NAME_VIDEO_IMAGES;
    String AMAZON_S3_BUCKET_NAME_DOCUMENT = API_AMAZON_S3_BUCKET_NAME_DOCUMENT;
    String AMAZON_S3_BUCKET_NAME_FEEDBACK = API_AMAZON_S3_BUCKET_NAME_FEEDBACK;
    String AMAZON_S3_FILE_NAME_CREATION = API_AMAZON_S3_FILE_NAME_CREATION;


    public static final String BANNER_IMAGE = "banner";
    public static final String PAGE_IMAGE = "profile";
    public static final String PAGE_CATEGORY = "category";

    public static final String PAGEDATA = "page_data";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String IS_POST = "is_post";
    public static final String TASK_FOR = "task_for";
    public static final String MEMBERS_ID = "member_id";
    public static final String PARENT_ID = "parent_id";
    String FINAL_AMOUNT = "final_amount";
    String CITY = "city";
    String RANGE_FROM = "age_from";
    String RANGE_TO = "age_to";
    String INTEREST = "interest";
    String DAILY_BUDGET = "daily_budget";
    String DATE_FROM = "date_from";


    String DATE_TO = "date_to";
    String FINAL_PRICE = "final_price";
    String OLDPASSWORD = "old_password";
    String SEARCH = "search";
    String CAPTION = "caption";

    String AGE_FROM = "age_from";
    String AGE_TO = "age_to";
    String KM = "km";
    String RADIO_GENDER = "radiogender";

    String PLACEDATA = "placedata";
    String CHECK_IN_FOR = "check_in_for";

    String invite_people_ids = "invite_people_ids";
    String FORBOOTS = "for_boost";
    String ISPAGE = "is_page";
    String POSTUSERID = "post_user_id";

    public static String INTENT_KEY_CHAT_FRIEND = "friendname";
    public static String INTENT_KEY_CHAT_AVATA = "friendavata";
    public static String INTENT_KEY_CHAT_ID = "friendid";
    public static String INTENT_KEY_CHAT_ROOM_ID = "roomid";
    public static long TIME_TO_REFRESH = 10 * 1000;
    public static long TIME_TO_OFFLINE = 2 * 60 * 1000;
    public static String STR_DEFAULT_BASE64 = "default";

}

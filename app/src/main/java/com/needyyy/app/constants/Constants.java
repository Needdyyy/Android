package com.needyyy.app.constants;

import java.util.Objects;

/**
 * To hold all the constants keys and enums throughout the app.
 */
public interface Constants {

    String KBASEURLDEV                              = "http://54.153.76.97";
    String KBASEURLPRODHTTP                         = "http://nudgerapp.com";
    String KBASEURLPRODHTTPS                        = "https://nudgerapp.com";

    //TO change the server URL, Developer need to change only this line
    static final String KBASEURL                    = KBASEURLPRODHTTP;

    String kAPIBaseURL                              = KBASEURL + "/NudgerAPI/api/";

    String kHelpURL                                 = KBASEURL + "/app/help.html";
    String kPrivacyPolicyURL                        = KBASEURL + "/app/privacypolicy.html";
    String kEULAURL                                 = KBASEURL + "/app/eula.html";
    String kImageBaseURLFormat                      = KBASEURL + "/NudgerAPI/Images/Profiles/";
    String kFAQURL                                  = KBASEURL + "/app/faqs.html";

    static final String kGoogleMapsBaseURL          = "https://maps.googleapis.com/";
    //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ9xeBIK_6DDkRPW6lI9ppQZs&key=AIzaSyA6kQor07K5xtlcb0PjiuPkLJomPobRxeI

/*****************************************************************************************/

    //common constants used
    String  kEmptyString                = "";
    Integer kEmptyNumber                = 0;
    Integer kRootCollectionId           = 0;

    String kCurrentLatLng               = "currentLatLng";

    //current user keys
    String kPhone                       = "phone";
    String kId                          = "$id";
    String kErrorMessage                = "errorMessage";
    String kResults                     = "results";
    String kLoggedInUser                = "LoggedInUser";
    String kUserId                      = "userId";
    String kUserName                    = "userName";
    String kFirstName                   = "firstName";
    String kLastName                    = "lastName";
    String kPhoneNumber                 = "phoneNumber";
    String kIsVerifiedPhoneNumber       = "isVerifiedPhoneNumber";
    String kLatitude                    = "latitude";
    String kLongitude                   = "longitude";
    String kImage                       = "image";
    String kToken                       = "token";
    String kVerificationCode            = "verificationCode";
    String kDeviceType                  = "deviceType";
    String kDeviceToken                 = "deviceToken";
    String kTimeZone                    = "timeZone";
    String kIsActive                    = "isActive";
    String kCreatedOn                   = "createdOn";
    String kModifiedOn                  = "modifiedOn";
    String kUnreadBadgeCount            = "unreadBadgeCount";
    String kContactRegistrationStatus   = "contactRegistrationStatus";

    String kGrantType   = "grant_type";
    String kBadgeCount   = "badge_count";


    /*Nudges*/
    String kNudges                      = "nudges";
    String kNudgeId                     = "nudgeId";
    String kCategoryId                  = "categoryId";
    String kRadius                      = "radius";
    String kTitle                       = "title";
    String kNudgeText                   = "nudgeText";
    String kNudgeImage                  = "nudgeImage";
    String kNudgeType                   = "nudgeType";
    String kISNudgeNotify               = "isNudgeNotify";
    String kNudgeAudio                  = "nudgeAudio";
    String kFrequency                   = "frequency";
    String kType                        = "type";
    String kCategoryName                = "categoryName";
    String kDisplayName                 = "displayName";
    String kHowOften                    = "howOften";
    String kOnEnter                     = "onEnter";
    String kOnExit                      = "onExit";
    String kOnEnterMessage              = "onEnterMessage";
    String kOnExitMessage               = "onExitMessage";
    String kNudgeEntryType              = "nudgeEntryType";
    String kIsNudgeEntry                = "isNudgeEntry";
    String kIsSharedNudge               = "isSharedNudge";
    String kIsSharePackage              = "isSharePackege";
    String kNudgeAddress                = "nudgeAddress";
    String kComments                    = "comments";
    String kUsers                       = "users";
    String kNotifyMeStatus              = "notifyMeStatus";
    String kUserIdFromNudgeShared       = "userIdFromNudgeShared";
    String kUserNameFromNudgeShared     = "userNameFromNudgeShared";

    String kNudgeAddressId              = "nudgeAddressId";
    String kAddress                     = "address";
    String kPlacename                   = "placename";
    String kGPlacename                  = "gplacename";

    String kNudgeUserId                 = "nudgeUserId";
    String kIsUserAccepted              = "isUserAccepted";
    String kIsUserNotify                = "isUserNotify";

    String kCommentId                   = "commentId";
    String kSenderUserId                = "senderUserId";
    String kSenderUserName              = "senderUserName";
    String kContent                     = "content";
    String kNudgeTitle                  = "nudgeTitle";
    String kCreateOn                    = "createOn";
    String kUpdateOn                    = "updateOn";

    String kNudgeCollection             = "nudgeCollection";

    String kNudgeCollectionId           = "nudgeCollectionId";
    String kCollectionName              = "collectionName";

    String kShareRequestId              = "shareRequestId";
    String kUserIdFrom                  = "userIdFrom";
    String kUserIdTo                    = "userIdTo";
    String kToUserId                    = "toUserId";

    String kShareId                     = "shareId";
    String kShareName                   = "shareName";
    String kAddedOn                     = "addedOn";
    String kUpdatedOn                   = "updatedOn";

    String kDefaultAppName              = "Needyyy";
    String kAppPreferences              = "NeedyyyrAppPreferences";
    String knotification              = "NotidicationPreferences";
    String kStatus                      = "status";

    String kCurrentUser                 = "currentUser";

    String KPostdata                 = "postdata";

    String Kmasterhit                 = "masterhit";
    String kCurrentUserProfile          = "userProfile";

    String kSharePackageNamePostfix     = "Nudger@123$";
    String kSharePackageNameSeperator   = "@#*";
    String kPackageTypeSeperator        = "PackageType-";

    String kNudgeCount                  = "nudgeCount";
    String kBody                        = "body";
    String kSenderImageName             = "senderImageName";

    String kNotificationHistoryId       = "notificationHistoryId";
    String kFromUserId                  = "fromUserId";
    String kToUserName                  = "toUserName";
    String kFromUserImage               = "fromUserImage";
    String kSharedPackageName           = "sharedPackageName";
    String kFromUserName                = "fromUserName";
    String kMessage                     = "message";
    String kNudgeAddresses              = "nudgeAddresses";
    String kNotificationType            = "notificationType";
    String kNotificationStatus          = "notificationStatus";

    String kIsExitingThe                = "is exiting the";
    String kIsEnteringThe               = "is entering the";

    String kPhoneNumbers                = "phoneNumbers";
    String kUserContacts                = "userContacts";

    String kContactList                 = "contactList";

    String kMiles                       = "Miles";
    String kMile                        = "Mile";
    String kKM                          = "KM";
    String kKilometers                  = "Kilometers";

    String kMessageServerNotRespondingError         = kDefaultAppName + " server not responding!";
    String kMessageInternalInconsistency            = "Some internal inconsistency occurred. Please try again.";
    String kMessageNetworkError                     = "Device does not connect to internet.";
    String kSocketTimeOut                           = kDefaultAppName + " Server not responding..";
    String kNoRecordFound                           = "No Record Found! Please try after some time";

    String kErrorTitle                              = "Error";
    String kWarningTitle                            = "Warning";
    String kInformationAlertTitle                   = "Information Alert";

    String URL = "url";
    String kPending                                 = "Pending";
    String kAccepted                                = "Accepted";
    String kDeclined                                = "Declined";
    String kRejected                                = "Rejected";
    String kNotNow                                  = "Not Now";
    String kNew                                     = "New";

    String kWantsToShare                            = "wants to share";
    String kNotificationRequestMessage              = "Do you want to be notified when %s is near %s?";
    String USER_PROFILE_DATA = "user_profile_data";
    String WALLET = "wallet";


    /**
     * enum to define From where the LoginActivity was initiated.
     */
    enum LOGIN_FROM {
        login,
        registration,
        verification,
        autheticator
    }

    interface HowOften   {
        String onceADay             = "Once a Day";
        String justOnce             = "Just Once";
        String everyTime            = "Every Time";
    }

    /**Shared Package Request status*/
    interface RequestStatus   {
        String accepted             = "Accepted";
        String pending              = "Pending";
        String rejected             = "Rejected";
        String none                 = "None";
    }


    /**Button State*/
    enum SelectState {
        unselected(0),
        selected(1);

        private int value;

        SelectState(int status) {
            this.value = status;
        }

        public static SelectState getSelectState(int value) {
            for (SelectState selectState : SelectState.values()) {
                if (selectState.value == value) {
                    return selectState;
                }
            }
            return unselected;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Button State*/
    enum ButtonState {
        unselected(0),
        selected(1);

        private int value;

        ButtonState(int status) {
            this.value = status;
        }

        public static ButtonState getButtonState(int value) {
            for (ButtonState buttonState : ButtonState.values()) {
                if (buttonState.value == value) {
                    return buttonState;
                }
            }
            return unselected;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Device Type*/
    enum DeviceType {
        ios(1),
        android(2);

        private int value;

        DeviceType(int status) {
            this.value = status;
        }

        public static DeviceType getDeviceType(int value) {
            for (DeviceType deviceType : DeviceType.values()) {
                if (deviceType.value == value) {
                    return deviceType;
                }
            }
            return android;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Sort Main Nudge List in settings*/
    enum SortNudgeList {
        name(1),
        date(2);

        private int value;

        SortNudgeList(int status) {
            this.value = status;
        }

        public static SortNudgeList getSortNudgeList(int value) {
            for (SortNudgeList sortNudgeList : SortNudgeList.values()) {
                if (sortNudgeList.value == value) {
                    return sortNudgeList;
                }
            }
            return date;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    enum DistanceUnit {
        kilometers(1),
        miles(2);

        private int value;

        DistanceUnit(int status) {
            this.value = status;
        }

        public static DistanceUnit getDistanceUnit(int value) {
            for (DistanceUnit distanceUnit : DistanceUnit.values()) {
                if (distanceUnit.value == value) {
                    return distanceUnit;
                }
            }
            return miles;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**The origin where the notification sent*/
    enum UserNotificationFireType {
        fromLocalApp(1),
        fromServer(2);

        private int value;

        UserNotificationFireType(int status) {
            this.value = status;
        }

        public static UserNotificationFireType getUserNotificationFireType(int value) {
            for (UserNotificationFireType userNotificationFireType : UserNotificationFireType.values()) {
                if (userNotificationFireType.value == value) {
                    return userNotificationFireType;
                }
            }
            return fromLocalApp;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }


    /***User notification subtype the different types of notifications that show in Notification Screen
     *Sender Side
            * is Near, be on the lookout!                           Local
            * notified that you’ve exited the                       Local
            * notified that you’ve entered the                      Local
     * Recipient Side
            * NN Sender is entering the area                        Server
            * NN Sender is exiting the area                         Server
            * Notification Nudge Request                            Server
            * Share Nudge(SharePackage) Request                     Server
     * */
    enum UserNotificationSubType {
        none(0),
        isNearToBeLookOut(1),
        youExited(2),
        youEntered(3),
        userEnteringTheArea(4),
        userExitingTheArea(5),
        notificationNudgeRequest(6),
        sharedPackageRequest(7);

        private int value;

        UserNotificationSubType(int status) {
            this.value = status;
        }

        public static UserNotificationSubType getUserNotificationSubType(int value) {
            for (UserNotificationSubType userNotificationSubType : UserNotificationSubType.values()) {
                if (userNotificationSubType.value == value) {
                    return userNotificationSubType;
                }
            }
            return none;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /***This is categories the different types of notifications that show in Notification Screen*/
    enum UserNotificationType {
        notification(1),
        notificationNudgeRequest(2),
        sharedPackageRequest(3);

        private int value;

        UserNotificationType(int status) {
            this.value = status;
        }

        public static UserNotificationType getUserNotificationType(int value) {
            for (UserNotificationType userNotificationType : UserNotificationType.values()) {
                if (userNotificationType.value == value) {
                    return userNotificationType;
                }
            }
            return notification;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }


    /**Notification Nudge Request Type
     *
     * */
    enum RequestType {
        sent(1),
        received(2);

        private int value;

        RequestType(int status) {
            this.value = status;
        }

        public static RequestType getRequestType(int value) {
            for (RequestType requestType : RequestType.values()) {
                if (requestType.value == value) {
                    return requestType;
                }
            }
            return sent;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Notification Nudge User Request Status
     *
     * */
    enum NotificationNudgeUserRequestStatus {
        pending(0),
        accepted(1),
        rejected(2),
        notNow(3),
        requestNotSent(4);

        private int value;

        NotificationNudgeUserRequestStatus(int status) {
            this.value = status;
        }

        public static NotificationNudgeUserRequestStatus getNotificationNudgeUserRequestStatus(int value) {
            for (NotificationNudgeUserRequestStatus notificationNudgeUserRequestStatus : NotificationNudgeUserRequestStatus.values()) {
                if (notificationNudgeUserRequestStatus.value == value) {
                    return notificationNudgeUserRequestStatus;
                }
            }
            return pending;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**status 0: pending, 1 accepted, 2 : rejected == isEntry
     * */
    enum SharedPackageRequestStatus {
        pending(0),
        accepted(1),
        rejected(2);

        private int value;

        SharedPackageRequestStatus(int status) {
            this.value = status;
        }

        public static SharedPackageRequestStatus getSharedPackageRequestStatus(int value) {
            for (SharedPackageRequestStatus sharedPackageRequestStatus : SharedPackageRequestStatus.values()) {
                if (sharedPackageRequestStatus.value == value) {
                    return sharedPackageRequestStatus;
                }
            }
            return pending;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**For Local notification isEnter/IsExit
     **/
    enum EnterExitStatus {
        exit(0),
        enter(1);

        private int value;

        EnterExitStatus(int status) {
            this.value = status;
        }

        public static EnterExitStatus getEnterExitStatus(int value) {
            for (EnterExitStatus enterExitStatus : EnterExitStatus.values()) {
                if (enterExitStatus.value == value) {
                    return enterExitStatus;
                }
            }
            return enter;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }


    /**User GO here or not
     *API Status Mapping: For Normal Nudge: 0, 1, For Notification Nudge: Null
     * GO here : 1,
     * Don’t Go here : 0
     *
     * */
    enum UserVisitingStatus {
        notVisit(0),
        visited(1);


        private int value;

        UserVisitingStatus(int status) {
            this.value = status;
        }

        public static UserVisitingStatus getUserVisitingStatus(int value) {
            for (UserVisitingStatus userVisitingStatus : UserVisitingStatus.values()) {
                if (userVisitingStatus.value == value) {
                    return userVisitingStatus;
                }
            }
            return notVisit;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Package Type to manage the nudges*/
    enum PackageType {
        none(1),                   //Default package for default nudge list
        collection(2),
        sharedPackage(3);

        private int value;

        PackageType(int status) {
            this.value = status;
        }

        public static PackageType getPackageType(int value) {
            for (PackageType packageType : PackageType.values()) {
                if (packageType.value == value) {
                    return packageType;
                }
            }
            return none;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Entities in the app to model the app's data*/
    enum NudgeListItemType {
        nudge(1),
        collection(2),
        sharedPackageRequest(3);

        private int value;

        NudgeListItemType(int status) {
            this.value = status;
        }

        public static NudgeListItemType getNudgeListItemType(int value) {
            for (NudgeListItemType nudgeListItemType : NudgeListItemType.values()) {
                if (nudgeListItemType.value == value) {
                    return nudgeListItemType;
                }
            }
            return nudge;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Entities in the app to model the app's data*/
    enum EntityType {
        nudge(1),
        notificationNudge(2),
        collection(3),
        sharedPackage(4);

        private int value;

        EntityType(int status) {
            this.value = status;
        }

        public static EntityType getEntityType(int value) {
            for (EntityType entityType : EntityType.values()) {
                if (entityType.value == value) {
                    return entityType;
                }
            }
            return nudge;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Nudge Media Type
     * API Status Mapping: type: 1,2,3,  null
     * Null: Notification Nudge
     * text: 1
     * image: 2
     * audio: 3
     * */
    enum NudgeMediaType {
        none(-1),
        text(1),
        image(2),
        audio(3),
        gif(4),
        video(5);

        private int value;

        NudgeMediaType(int status) {
            this.value = status;
        }

        public static NudgeMediaType getNudgeMediaType(int value) {
            for (NudgeMediaType nudgeMediaType : NudgeMediaType.values()) {
                if (nudgeMediaType.value == value) {
                    return nudgeMediaType;
                }
            }
            return none;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Notify Status for Nudge*/
    enum NotifyMeStatus {

        off(0),
        on(1),
        notApplicable(2);

        private int value;

        NotifyMeStatus(int status) {
            this.value = status;
        }

        public static NotifyMeStatus getNotifyMeStatus(int value) {
            for (NotifyMeStatus notifyMeStatus : NotifyMeStatus.values()) {
                if (notifyMeStatus.value == value) {
                    return notifyMeStatus;
                }
            }
            return off;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**ContactStatus for user's contacts*/
    enum ContactStatus {

        NotRegistered(1),               //Default
        SoftRegistered(2),
        AlreadyRegistered(3);

        private int value;

        ContactStatus(int status) {
            this.value = status;
        }

        public static ContactStatus getContactStatus(int value) {
            for (ContactStatus contactStatus : ContactStatus.values()) {
                if (contactStatus.value == value) {
                    return contactStatus;
                }
            }
            return NotRegistered;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Notify Status for Nudge*/
    enum TurnOffStatus {

        off(0),
        on(1),
        notApplicable(2);

        private int value;

        TurnOffStatus(int status) {
            this.value = status;
        }

        public static TurnOffStatus getTurnOffStatus(int value) {
            for (TurnOffStatus turnOffStatus : TurnOffStatus.values()) {
                if (turnOffStatus.value == value) {
                    return turnOffStatus;
                }
            }
            return off;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }

    /**Nudge Type
     * API Status Mapping: nudgeEntryType can be null : 0, 1 :
     * 1 ==> Notification Nudge,
     * 0 or Null ==> Normal Nudge
     * */
     enum NudgeType {
        none(-1),
        normalNudge(0),
        notificationNudge(1);

        private int value;

        NudgeType(int status) {
            this.value = status;
        }

        public static NudgeType getNudgeType(int value) {
            for (NudgeType nudgeType : NudgeType.values()) {
                if (nudgeType.value == value) {
                    return nudgeType;
                }
            }
            return normalNudge;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }


    /**
     * Status Enumeration for Task Status
     */
    enum Status {
        success(0),
        fail(1),
        reachLimit(2),
        noRecordFound(3),
        noChange(4),
        history(5),
        normal(6),
        discard(7),
        permissionDenied(8),
        serviceNotAvailable(9),
        internetNotAvailable(10);

        private int value;

        Status(int status) {
            this.value = status;
        }

        public static Status getStatus(int value) {
            for (Status status : Status.values()) {
                if (status.value == value) {
                    return status;
                }
            }
            return fail;
        }

        /**
         * To get Integer value of corresponding enum
         */
        public Integer getValue() {
            return this.value;
        }
    }


    /**
     * Http Status for Api Response
     */
    enum HTTPStatus {
        success("OK"),
        failure("FAILURE"),
        error("ERROR"),
        userExists("UserExists"),
        missingInput("Missing_Inputs"),
        invalidInputs("Invalid_Inputs"),
        unknownError("Unknown_Error"),
        userNotExist("UserNotExist"),
        failueAuthLogin("FailureLoginAuth"),
        noActiveRequest("No_ActiveRequest"),
        codeNotvalid("CodeNotValid"),
        noRecordFound("No_Record_Found"),
        noRegisteredContactFound("No any contact found registered.");

        private String httpStatus;

        HTTPStatus(String httpStatus) {
            this.httpStatus = httpStatus;
        }

        public static HTTPStatus getStatus(String status) {
            for (HTTPStatus httpStatus : HTTPStatus.values()) {
                if (Objects.equals(httpStatus.httpStatus, status)) {
                    return httpStatus;
                }
            }
            return error;
        }

        public String getValue() {
            return this.httpStatus;
        }


    }
}



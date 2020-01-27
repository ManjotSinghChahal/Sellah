package com.app.admin.sellah.controller.utils;

public class SAConstants {

    public static class Keys{

        public static final String UID="sa.user.id";
        public static final String UNAME="sa.user.name1";
        public static final String USER_PROFILE_PIC="sa.user.pic";
        public static final String USER_EMAIL="sa.user.email";
        public static final String AVAILABLE_BALANCE="sa.user.AVAILAble";
        public static final String QRCODE="sa.user.QRCODE";
        public static final String PENDING_BALANCE="sa.user.PENDING";
        public static final String USER_STRIPE_ID="sa.user.stripe.id";
        public static final String PROFILESTATUS="sa.profile.status";
        public static final String ADVERTISEMENTSTATUS="";
        public static final String KEY_VIBRATE = "sa.key.vibrate";
        public static final int TYPE_SEND=7368;
        public static final int TYPE_RECEIVED=7369;
        public static final int TYPE_MAKEOFFER=7370;
        public static final int TYPE_MAKEOFFER_RECEIVED=7374;
        public static final int TYPE_VERYFY_ITEM=7371;
        public static final int TYPE_IMAGE=7372;
        public static final int TYPE_RECIEVED_IMAGE=7373;
        public static final int TYPE_PINED_SEND_MSG=7375;
        public static final int TYPE_PINED_RECEIVED_MSG=7376;
        public static final int USER_RESULT=1213;
        public static final String Chat_User_Data="sa.chat.user.data";
        public static final String DEEP_LINKING="sa.deep.link";
        public static final String CAT_POS="sa.cat.pos";
        public static final String SUB_CAT_POS="sa.sub.cat.pos";
        public static final String CAT_ID="sa.cat.id";
        public static final String FLTR_COUNT="sa.count.fltr";
        public static final String PROFILE_DATA="sa.profile.data";
        public static final String SUB_CAT_ID="sa.sub.cat.id";
        public static final String CAT_LIST="sa.cat.list";
        public static final String SUB_CAT_LIST="sa.sub.cat.list";
        public static final String Price_hTol="price_high";
        public static final String Price_ltOH="price_low";
        public static final String Filter_recent="recent";
        public static final String Filter_popuarity="popuarity";
        public static final String PRODUCT_DETAIL="sa.product.detail";
        public static final String NOTI_KEY="NOTI_KEy";
        public static final String PRODUCT_ID="sa.product.id";
        public static final String OTHER_USER_ID="sa.other.user.id";
        public static final String CARDHOLDER_NAME="sa.card.holder.name";
        public static final String CARD_EXP_YEAR="sa.card.exp.year";
        public static final String CARD_EXP_MONTH="sa.card.exp.month";
        public static final String CARD_NUMBER="sa.card.no.";

        // global topic to receive app wide push notifications
        public static final String TOPIC_GLOBAL = "global";

        // broadcast receiver intent filters
        public static final String REGISTRATION_COMPLETE = "registrationComplete";
        public static final String PUSH_NOTIFICATION = "pushNotification";

        // id to handle the notification in the notification tray
        public static final int NOTIFICATION_ID = 100;
        public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

        public static final String SHARED_PREF = "ah_firebase";
        public static final String MAKE_OFFER_DATA= "sa.make.offer.data";
        public static final String EVENT_CREATEROOM= "createroom";
        public static final String EVENT_READMESSAGE= "readmsg";
        public static final String EVENT_READMESSAGE_FOR_CHATLIST= "readmessage";
        public static final String EVENT_CREATEGROUP= "creatgroup";
//      public static final String EVENT_JOIN= "creatgroup";
        public static final String EVENT_NEW_MESSAGE= "new_message";
        public static final String EVENT_JOIN= "join";
        public static final String EVENT_CLOSE_GROUP= "closegroup";
        public static final String EVENT_COUNTVIEWS= "countviews";
        public static final String EVENT_LIVE_PIN_COMMENT= "livepincomment";
        public static final String EVENT_SEND_GROUP_MESSAGE= "sendgroupmessage";

        public static final String PROFILE_EDIT_MODE_PROFILE= "profile";
        public static final String PROFILE_EDIT_MODE_IMAGE= "image";
        public static final String KEY_PLAY_BEEP = "sa.keep.play.beep";
//      public static String I

    }

    public static class NotificationKeys{

        public static final String NT_ACCEPT_REJECT="accept_decline_offer";
        public static final String NT_STOP_LIVE_VIDEO="stop_live_video";
        public static final String NT_FOLLOW="follow";
        public static final String NT_PRODUCT_ADDED="product_added";
        public static final String NT_COMMENT_ADDED="add_comment";
        public static final String NT_TESTIMONIAL_ADDED="testimonial_added";
        public static final String NT_OFFER_MAKE="make_offer";
        public static final String NT_OFFER_LIVE_MAKE ="make_live_offer";
        public static final String NT_CHAT="chat";
        public static final String NT_PAYMENT="payment";
        public static final String NT_DATA="sa.notification.data";
        public static final String NT_ORDER="orderstatus";

    }

    public static class FilterKeys{

        public static final String FLTR_SORT="fltr.sort.price";
        public static final String FLTR_CONDITION="fltr.condition";
        public static final String FLTR_CATEGORY="fltr.category";
        public static final String FLTR_PAYMENT_TYPE="fltr.payment.type";
        public static final String FLTR_PRICE_NEGO="fltr.price_negotiable";
        public static final String FLTR_PRICE_MIN="fltr.price.min";
        public static final String FLTR_PRICE_MAX="fltr.price.max";
        public static final String FLTR_DELIVERY="fltr.delivery";


    }

    public static class ConstValues{
        public static String SCREEN_STATUS="";
    }
    public static class Urls{

     //   public static final String SOCKETURL = "http://18.138.43.45:3000";
     //   public static final String SOCKETURL = "https://sellah.dev:3000";
     //   public static final String SOCKETURL = "http://3.1.8.189:3000/";
        public static final String SOCKETURL = "http://3.1.8.189:3000/";

     //   public static final String SOCKETURL = "https://3.1.8.189:3000/";
     //   public static String BASEURL = "http://18.138.43.45//Sellah/api/";
     //   public static String BASEURL = "https://sellah.dev/Sellah/api/";
     //   public static String BASEURL = "http://sellah.market/Sellah/api/";
        public static String BASEURL = "https://sellah.market/Sellah/api/";
    //    public static String BASEURL = "https://13.250.36.66/Sellah/api/";

        public static String AUTHKEY = "authkey:4a12cd5y8-9ffe-8ub5-7124-bc7d62789f2gh";

    }

    public static class StripeKeys{
        public static final String PUBLISHABLE_KEY = "pk_live_tLgm9cqs3dlbcSQFkRiT8RTA";
        public static final String SECRET_KEY = "sk_live_qrwzNdS7kCKlT6Ij7lXsLr2V";
    }

}

package dms.deideas.zas;

/**
 * Created by bnavarro on 19/07/2016.
 */
public class Constants {

    public static final String STRING_EMPTY ="";
    public static final String STRING_0 ="0";
    public static final String STRING_ERROR ="Error";

    public static final String URL_ZAS = "http://zascomidaentuboca.es/";
    public static final String URL_ZAS_retrofit = "http%3A%2F%2Fzascomidaentuboca.es%2F";
    public static final String URL_MAP_distancematrix = "https://maps.googleapis.com/maps/api/distancematrix/";
    public static final String API_KEY_GOOGLE_MAP =  "AIzaSyDFRTSsn-fabk8bfY3hV6bH_GHoPRUOMNQ";

    public static final String RETROFIT_ONFAILURE_NOT_REPLY = "No respuesta";

    public static final int SERVICE_CODE_order_edit = 0;
    public static final int SERVICE_CODE_order_get = 1;
    public static final int SERVICE_CODE_order_accepted = 2;
    public static final int SERVICE_CODE_order_accepted_byuser = 3;
    public static final int SERVICE_CODE_order_problem = 4;
    public static final int SERVICE_CODE_order_problem_byuser = 5;
    public static final int SERVICE_CODE_order_notes = 6;
    public static final int SERVICE_CODE_order_edit_acceptbymotodriver = 7;
    public static final int SERVICE_CODE_problem_add_completed = 8;
    public static final int SERVICE_CODE_problem_add_description = 9;
    public static final int SERVICE_CODE_notes_byuser = 10;
    public static final int SERVICE_CODE_history = 11;
    public static final int SERVICE_CODE_order_count = 12;
    public static final int SERVICE_CODE_order_count_byuser = 13;
    public static final int SERVICE_CODE_order_edit_cancelbymotodriver = 14;
    public static final int SERVICE_CODE_order_accepted_byareadelivery = 15;
    public static final int SERVICE_CODE_order_accepted_byuser_byareadelivery = 16;
    public static final int SERVICE_CODE_order_notesclient = 17;
    public static final int SERVICE_CODE_zone = 18;
    public static final int SERVICE_CODE_number_max_orders_accepted = 19;
    public static final int SERVICE_CODE_number_max_orders_visible = 20;
    public static final int SERVICE_CODE_order_count_byareadelivery = 21;
    public static final int SERVICE_CODE_order_count_byuser_byareadelivery = 22;
    public static final int SERVICE_CODE_order_byidorder = 23;
    public static final int SERVICE_CODE_max_time_orderchangecolor_inMyorders = 24;
    public static final int SERVICE_CODE_order_saveLocation = 25;
    public static final int SERVICE_CODE_configuratorweb = 26;

    public static final int PROBLEM_drop_food = 0;
    public static final int PROBLEM_wrong_plate = 1;
    public static final int PROBLEM_wrong_order = 2;
    public static final int PROBLEM_forget_plate = 3;
    public static final int PROBLEM_drop_drink = 4;
    public static final int PROBLEM_wrong_drink = 5;
    public static final int PROBLEM_forget_drink = 6;
    public static final int PROBLEM_other = 7;

    public static final String PROBLEM_drop_food_str = "drop_food";
    public static final String PROBLEM_wrong_plate_str = "wrong_plate";
    public static final String PROBLEM_wrong_order_str = "wrong_order";
    public static final String PROBLEM_forget_plate_str = "forget_plate";
    public static final String PROBLEM_drop_drink_str = "drop_drink";
    public static final String PROBLEM_wrong_drink_str = "wrong_drink";
    public static final String PROBLEM_forget_drink_str = "forget_drink";
    public static final String PROBLEM_other_str = "other";

    public static final String PROBLEM_es_drop_food_str = "Derramamiento de comida";
    public static final String PROBLEM_es_wrong_plate_str = "Equivocación de plato";
    public static final String PROBLEM_es_wrong_order_str = "Equivocación de pedido";
    public static final String PROBLEM_es_forget_plate_str = "Olvido de plato";
    public static final String PROBLEM_es_drop_drink_str = "Derramar bebida";
    public static final String PROBLEM_es_wrong_drink_str = "Equivocacion bebida";
    public static final String PROBLEM_es_forget_drink_str = "Olvido bebida";
    public static final String PROBLEM_es_other_str = "Otro";

    public static final String ORDER_STATUS_order_on_hold = "order_on_hold";
    public static final String ORDER_STATUS_rest_has_accepted = "rest_has_accepted";
    public static final String ORDER_STATUS_driver_has_accepted = "driver_has_accepted";
    public static final String ORDER_STATUS_driver_in_rest = "driver_in_rest";
    public static final String ORDER_STATUS_driver_on_road = "driver_on_road";
    public static final String ORDER_STATUS_order_delivered = "order_delivered";
    public static final String ORDER_STATUS_order_delivered_w_problem = "order_delivered_w_problem";
    public static final String ORDER_STATUS_problem = "problem";

    public static final String TAG_FirebaseInstanceIDService = "FirebaseInsIDService";

    public static final int RESPONSE_TIME = 2500;

    public static final int MAX_LOGIN_TIME = 28800;

    public static final String OAUTH_CONSUMER_KEY = "ck_40b166eb08943c530d82aab33c3bdb572ad0966d";
    public static final String OAUTH_CONSUMER_SECRET = "cs_beaa6104237452538253e9df1160163daa7b98ae";
    public static final String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String OAUTH_UTF_8 = "UTF-8";

    public static final String ORDER_FOODPRIORITY_HIGHT = "1";
    public static final String ORDER_FOODPRIORITY_MEDIUM = "2";
    public static final String ORDER_FOODPRIORITY_LOW = "3";

    public static final int ORDER_TIMEKITCHEN_12 = 12;
    public static final int ORDER_TIMEKITCHEN_5 = 5;
    public static final int ORDER_TIMEKITCHEN_0 = 0;

    public static final String DATETIME_BBDD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATA_APP_FORMAT =  "dd-MM-yyyy";

    public static final String PREFERENCES_NAME = "MyPreferences";
    public static final String PREFERENCES_NUMBER_MAX_ORDERS_ACCEPTED_BYDRIVER = "numberMax";
    public static final String PREFERENCES_NUMBER_MAX_ORDERS_VISIBLE = "numberMaxOrdersVisible";
    public static final String PREFERENCES_REFRESH_TOKEN = "refreshedToken";
    public static final String PREFERENCES_USER_ID = "idUser";
    public static final String PREFERENCES_USER_DISPLAYNAME = "name";
    public static final String PREFERENCES_USER = "user";
    public static final String PREFERENCES_USER_PASSWORD = "password";
    public static final String PREFERENCES_USER_EMAIL = "email";
    public static final String PREFERENCES_USER_TIME_LASTLOGIN = "timelastLogin";
    public static final String PREFERENCES_AREA_DELIVERY = "areaDelivery";
    public static final String PREFERENCES_AREA_DELIVERY_STRING = "strAreaDelivery";
    public static final String PREFERENCES_IS_ORDER_CHANGED = "isOrderChanged";
    public static final String PREFERENCES_NUMBERS_ORDERS= "numOrders";
    public static final String PREFERENCES_NUMBERS_ORDERS_ACCEPTED= "numMyOrders";
    public static final String PREFERENCES_NUMBERS_ORDERS_ACCEPTEDBYDRIVER= "numMyOrdersWithouProblems";
    public static final String PREFERENCES_MAXTIME_ORDERS_CHANGE_MAXPRIORITY= "timeMax";

    public static final int HOMEFRAGMENT_CODE = 0;
    public static final int ORDERSFRAGMENT_CODE = 1;
    public static final int MYORDERSFRAGMENT_CODE = 2;
    public static final int COMMONFRAGMENT_CODE = 3;
    public static final int DETAILORDERSFRAGMENT_CODE = 4;
    public static final int DETAILMYORDERSFRAGMENT_CODE = 5;
    public static final int HISTORICALFRAGMENT_CODE = 6;

    public static final String HOMEFRAGMENT = "home";
    public static final String ORDERSFRAGMENT = "orders";
    public static final String MYORDERSFRAGMENT = "myorders";
    public static final String DETAILORDERSFRAGMENT = "detailorders";
    public static final String DETAILMYORDERSFRAGMENT = "detailmyorders";
    public static final String HISTORICALFRAGMENT = "history";
    public static final String DIALOGFRAGMENT = "dialog";

    public static final String  ARGUMENT_TITLE = "title";
    public static final String  ARGUMENT_ORDER = "order";
    public static final String  ARGUMENT_PREVPAGE_TITLE = "prevpage_title";
    public static final String  ARGUMENT_ORIGINPAGE = "originPage";
    public static final String  ARGUMENT_MOTODRIVER = "motodriver";

    public static final int screenCode_detailOrders = 0;
    public static final int screenCode_detailMyOrders = 1;
    public static final int screenCode_detailMyOrders_disallocate = 2;
    public static final int screenCode_detailMyOrders_disallocateProblem = 3;

    public static final int MAP_CONTROL_Restaurant = 0;
    public static final int MAP_CONTROL_Client = 1;

    public static final String PREF_VALUE_MAX_ORDERS_ACCEPTED = "3";
    public static final String PREF_VALUE_MAX_ORDERS_VISIBLE = "10";
    public static final String PREF_VALUE_MAX_TIME_ORDERS = "15";
}

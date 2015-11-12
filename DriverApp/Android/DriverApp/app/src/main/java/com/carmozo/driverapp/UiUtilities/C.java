package com.carmozo.driverapp.UiUtilities;

/**
 * Created by shreyasgs on 16-10-2015.
 */
public class C {

    public static class General {
        public static final String GOOGLE_MAP_CONNECTION_URL = "https://maps.googleapis.com/maps/api/directions/";
    }

    public static class ConnectionString {
        public static final String LOGIN_URL = "http://52.89.173.13:8084/api/v1/account/provider/login";
        public static final String RETR_ORDER_LIST_URL = "http://52.89.173.13:8084/api/v1/sm/service/list";
        public static final String START_TRIP_URL = "http://52.89.173.13:8084/api/v1/sm/service/update/";
        public static final String STOP_TRIP_URL = "http://52.89.173.13:8084/api/v1/sm/service/update/";
    }

    public static class AppKey {
        //-- Key used for navigating content app to app
        public static final String DRIVER_APP_LOGIN_RESP_KEY = "loginResp";
        public static final String DRIVER_APP_AUTH_TOKEN_KEY = "auth_token";
        public static final String DRIVER_APP_PROVIDER_API_KEY = "provider_api_key";
        public static final String DRIVER_APP_PROVIDER_API_SECRET_KEY = "provider_api_sec_key";
        public static final String DRIVER_APP_INCOMING_JOB_SELECTED_KEY = "incoming_job_selected";
    }

    public static class headerReqKey {
        public static final String DRIVER_APP_HTTP_REQ_AUTH_KEY = "HTTP_AUTH_TOKEN";
        public static final String DRIVER_APP_HTTP_REQ_PROVIDER_API_KEY = "HTTP_PROVIDER_API_KEY";
        public static final String DRIVER_APP_HTTP_REQ_PROVIDER_SEC_KEY = "HTTP_PROVIDER_API_SECRET";
    }

    public static class jsonKey {
        //-- Key used for all server request
        public static final String DRIVER_LOGIN_NODE_KEY = "data";
        public static final String DRIVER_LOGIN_MOBILENO_KEY = "mobileno";
        public static final String DRIVER_LOGIN_PASSWD_KEY = "password";
        public static final String DRIVER_LOGIN_PROVIDER_KEY = "provider_type";
        public static final String DRIVER_TRIP_TRACCER_ID_KEY = "tracker_id";
        public static final String DRIVER_REQ_DATA_KEY = "data";
        public static final String DRIVER_REQ_STOP_TRIP_KM_KEY = "total_distance";

        //-- Login Response keys
        public static final String DRIVER_LOGIN_RESP_AUTH_KEY = "token";
        public static final String DRIVER_LOGIN_RESP_PROV_KEY = "api_key";
        public static final String DRIVER_LOGIN_RESP_PROV_SEC_KEY = "api_secret";

        //-- Order details Response Keys
        public static final String DRIVER_ORDER_RESP_ORDER_DOC_ID_KEY = "order_doc_id";
        public static final String DRIVER_ORDER_RESP_ORDER_ID_KEY = "order_id";
        public static final String DRIVER_ORDER_RES_USER_ID_KEY = "user_id";
        public static final String DRIVER_ORDER_RESP_USER_DATA_KEY = "user_data";
        public static final String DRIVER_ORDER_RESP_USER_NAME_KEY = "name";
        public static final String DRIVER_ORDER_RESP_MOBILE_NO_KEY = "mobile";
        public static final String DRIVER_ORDER_RESP_BOOKING_DATE_KEY = "booking_date";
        public static final String DRIVER_ORDER_RESP_CUST_ADDR_KEY = "customer_address";
        public static final String DRIVER_ORDER_RESP_CUST_ADDR_TEXT_KEY = "text";
        public static final String DRIVER_ORDER_RESP_STREET_KEY = "street";
        public static final String DRIVER_ORDER_RESP_CUST_LOC_KEY = "loc";
        public static final String DRIVER_ORDER_RESP_PARTNER_SUBORDER_DOC_KEY = "sub_order_doc_id";
        public static final String DRIVER_ORDER_RESP_PARTNER_SUBORDER_ID_KEY = "sub_order_id";
        public static final String DRIVER_ORDER_RESP_PARTNER_ADDR_KEY = "destination_address";
        public static final String DRIVER_ORDER_RESP_PARTNER_ADDR_STREET_KEY = "street";
        public static final String DRIVER_ORDER_RESP_PARTNER_ADDR_LOC_KEY = "loc";
        public static final String DRIVER_ORDER_RESP_LOGISTICS_ID_KEY = "logistics_id";
        public static final String DRIVER_ORDER_RESP_STATUS_NODE_KEY = "status";
        public static final String DRIVER_ORDER_RESP_STATUS_ID_KEY = "id";


    }

    public static class TraccarParams {

        public static final String TRACCAR_KEY_DEVICE = "id";
        public static final String TRACCAR_KEY_ADDRESS = "address";
        public static final String TRACCAR_KEY_PORT = "port";
        public static final String TRACCAR_KEY_INTERVAL = "interval";
        public static final String TRACCAR_KEY_PROVIDER = "provider";
        public static final String TRACCAR_KEY_STATUS = "status";
    }
    public static class TraccarConnParam{
        public static String TRACCAR_CONN_DEVICEID ; // = "33333333";
        public static final String TRACCAR_CONN_PARAM_ADDR = "54.149.189.127";
        public static final int TRACCAR_CONN_PARAM_PORT = 5055; //--Web port 8082
        public static final int    TRACCAR_CONN_INTERVAL = (15*1000);
        public static final String TRACCAR_CONN_PROVIDER = "network";
    }
}

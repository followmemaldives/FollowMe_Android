package com.asif.followme.util;

/**
 * Created by user on 12/18/2017.
 */

public class AppConstants {

    public static final String ACTION_GCM_REGISTERED = "com.asif.followme.GCM_REGISTERED";
    public static final String SENDER_ID = "165453555718";		// API Project
    public static final String DOMAIN = "followme.mv";
//    public static final int PORT = 42871;				//40875 for .id, 42875 for .mv
    public static final String OS = "android";			// for testing ios codes
    public static final String VERSION = "v58";
    private static final String URL = "http://followme.mv/android/"+VERSION;
    public static final int NOTIFICATION_ID = 4871;
    public static String STOP_SERVICE_ACTION = "com.asif.followme.locationservice.action.stop_service";
    public static String MAIN_ACTION = "com.asif.followme.locationservice.action.main";
    public static final int NOTIFICATION_SERVICE_ID = 487120;
    public static final CharSequence CHANNEL_NAME_HIGH = "NOTIFICATION_HIGH";
    public static final CharSequence CHANNEL_NAME_LOW = "NOTIFICATION_LOW";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "FollowMe Tracking Service";
    public static final String CHANNEL_LOW = "followme_low";
    public static final String CHANNEL_HIGH = "followme_high";


    public static final String PUBLIC_VESSELS_URL = 	URL+"/get_public_vessels.php";
    public static final String PUBLIC_GROUPS_URL = 	    URL+"/get_public_groups.php";
    public static String IMAGE_SMALL_URL=              "http://followme.mv/images/profile/";
    public static String IMAGE_LARGE_URL=               "http://followme.mv/images/profile/large/";
    public static final String LIVE_PUBLIC_URL = 		URL+"/get_live_public.php";
    public static final String PUBLIC_PREFERENCE_URL = 	URL+"/get_public_preference.php";
    public static final String GET_PHOTO_INFO_URL = 	URL+"/get_photo_info.php";
//    public static final String MY_VESSELS_URL = 		URL+"/get_my_vessels.php";  //to be removed
    public static final String MY_DEVICES_URL = 		URL+"/get_my_devices.php";
    public static final String MY_GROUPS_URL = 		    URL+"/get_my_groups.php";
    public static final String FAVOURITE_URL = 			URL+"/favourite.php";
    public static final String PUBLIC_ALERT_URL = 		URL+"/public_alert.php";
    public static final String NOTICE_URL = 			URL+"/notice.php";
    public static final String HELP_URL = 				URL+"/help.htm";

    public static final String LOGIN_URL = 			        URL+"/auth_fm.php";
    public static final String INIT_HOME_URL = 			    URL+"/init_home.php";
    public static final String LOGOUT_URL = 			        URL+"/logout.php";
    public static final String LIVE_MY_URL = 		            URL+"/get_live_my.php";
    public static final String GET_DEVICE_INFO_URL = 	    URL+"/get_device_info.php";
    public static final String SAVE_DEVICE_URL = 	            URL+"/save_device_info.php";
    public static final String GET_DEVICE_TYPE_URL = 	        URL+"/get_device_types.php";
    public static final String GET_SHARED_LIST_URL = 		    URL+"/get_share_list.php";
    public static final String TRACE_URL = 				        URL+"/get_trace.php";
    public static final String POST_SHARED_USERS_URL = 	        URL+"/save_shared_users.php";
//  public static final String POST_DEVICE_INFO_URL = 	        URL+"/save_device_info.php";
    public static final String DEVICE_INFO_URL = 		        URL+"/device_info.php";
    public static final String TRIP_LOG_URL = 			        URL+"/get_trip_logs.php";
    public static final String GUARD_LOG_URL = 			        URL+"/get_guard_logs.php";
    public static final String GUARD_SMART_LOG_URL = 			        URL+"/get_guard_smart_logs.php";
    public static final String ALARM_LOG_URL = 			    URL+"/get_alarm_logs.php";
    public static final String MY_FLEET_DEVICES_URL = 		    URL+"/get_my_fleet.php";
    public static final String GCM_REGISTER_URL = 			    URL+"/gcm_register.php";
    public static final String SETTING_URL = 			        URL+"/save_settings.php";
    public static final String POST_NEW_FLEET_URL = 	        URL+"/save_new_fleet.php";
    public static final String PROFILE_URL = 			        "https://"+DOMAIN+"/"+OS+"/"+VERSION+"/profile.php";
    public static final String USER_REGISTER_URL = 			URL+"/register.php";
    public static final String FORGOT_URL = 			        URL+"/forgot.php";
    public static final String FEEDBACK_URL = 			        URL+"/feedback.php";

    public static final String MY_TRIP_PLANS_URL = 		    URL+"/get_trip_plans.php";
    public static final String NAME_VALUE_PAIRS_URL = 		    URL+"/get_name_value_pairs.php";
    public static final String SPINNER_OPTIONS_URL = 		    URL+"/get_spinner_options.php";
    public static final String POST_TRIP_PLANS_URL = 	        URL+"/save_trip_plan.php";
    public static final String BOAT_HIRE_WEBVIEW_DEPT_URL = 	"https://"+DOMAIN+"/"+OS+"/"+VERSION+"/webview_dept_islands.php";
    public static final String BOAT_HIRE_WEBVIEW_URL = 		"https://"+DOMAIN+"/"+OS+"/"+VERSION+"/webview_dest_islands.php";
    public static final String BOAT_OPERATORS_WEBVIEW_URL = "https://"+DOMAIN+"/"+OS+"/"+VERSION+"/webview_boat_operators.php";

    public static final String HIRE_LIST_PUBLIC_URL_1 = 	URL+"/get_hire_public_list1.php";
    public static final String HIRE_LIST_PUBLIC_URL_2 = 	URL+"/get_hire_public_list2.php";
    public static final String BID_LIST_PUBLIC_URL = 	    URL+"/get_bid_public_list.php";
    public static final String GET_AJAX_URL = 	            URL+"/ajax.php";
    public static final String POST_BOAT_HIRE_URL = 	    URL+"/save_boat_hire.php";
    public static final String VERIFY_USER_URL = 	        URL+"/verify_user.php";

    public static final String HIRE_LIST_MY_URL = 	        URL+"/get_hire_my_list.php";
    public static final String BID_LIST_MY_URL = 	        URL+"/get_bid_my_list.php";
    public static final String POST_BOAT_BID_URL = 	        URL+"/save_boat_bid.php";
    public static final String RELOAD_ACTIONS_URL = 	    URL+"/reload_actions.php";

    public static final String MY_WALLET_URL = 	            URL+"/get_my_wallet.php";
    public static final String MY_WALLET_INFO_URL = 	    URL+"/get_my_wallet_info.php";
    public static final String ETA_URL = 			        URL+"/get_eta.php";
//    public static final String ETA_PUBLIC_URL = 			URL+"/get_eta_public.php";
    public static final String BUY_LOCAL_URL = 			    URL+"/buy_local.php";
    public static final String BUY_WORLD_URL = 			    URL+"/buy_world.php";
    public static final String WEBVIEW_ETA_URL = 	        "https://"+DOMAIN+"/"+OS+"/"+VERSION+"/webview_eta_islands.php";
    public static final String SAVE_USER_PROFILE_URL = 	    URL+"/save_user_profile.php";

    public static final String AIRPORT_LIST_URL = 	    "https://aviation-edge.com/v2/public/airportDatabase?key=6ae58c-903ada&codeIso2Country=";
    public static final String FLIGHT_SCHEDULE_URL = 	"https://aviation-edge.com/v2/public/timetable?key=6ae58c-903ada&iataCode=";
    public static final String FLIGHT_LOCATION_URL = 	"http://aviation-edge.com/v2/public/flights?key=6ae58c-903ada&flightIata=";

    public static final String TRIP_PLANS_MY_URL =      URL+"/get_trip_plans_my.php";
    public static final String GET_PAX_INFO_URL =       URL+"/get_pax_info.php";
    public static final String GET_PAX_LIST_URL = 		 URL+"/get_pax_list.php";
    public static final String POST_PAX_URL =           URL+"/save_pax.php";
    public static final String TRIP_PLAN_WEBVIEW_URL = 		"https://"+DOMAIN+"/"+OS+"/"+VERSION+"/web_trip_destinations.php";
    public static final String GET_PAX_DEST_URL =       URL+"/get_pax_dest.php";
    public static final String GET_TRIP_INFO_URL =       URL+"/get_trip_info.php";
    public static final String GET_TRIP_FILTER_URL =       URL+"/get_trip_filter.php";
    public static final String HELP_TRIP_URL =          URL+"/help_trip.htm";
    public static final String GET_TEMPLATE_LIST_URL = 		 URL+"/get_template_list.php";
    public static final String POST_ROUTE_PLANS_URL =           URL+"/save_route_template.php";

    public static final String TAG_ALL_DATA = "aaData";

}

package de.rouvenkruse.cordova.plugins.cookieemperor;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.webkit.CookieManager;

public class CookieEmperor extends CordovaPlugin {
    public static final String ACTION_GET_COOKIE_VALUE = "getCookieValue";
    public static final String ACTION_SET_COOKIE_VALUE = "setCookieValue";
    public static final String ACTION_CLEAR_COOKIES = "clearCookies";

    private CookieManager cookieManager;

    public CookieEmperor() {
        this.cookieManager = CookieManager.getInstance();
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (ACTION_GET_COOKIE_VALUE.equals(action)) {
            return this.getCookie(args, callbackContext);
        }
        else if (ACTION_SET_COOKIE_VALUE.equals(action)) {
            return this.setCookie(args, callbackContext);
        }
        else if (ACTION_CLEAR_COOKIES.equals(action)) {
            return this.clearAllCookies();
        }

        callbackContext.error("Invalid action");
        return false;

    }

    /**
     * returns cookie under given key
     * @param args
     * @param callbackContext
     * @return
     */
    private boolean getCookie(JSONArray args, CallbackContext callbackContext) {
        final String url = args.getString(0);
        final String cookieName = args.getString(1);

        cordova
                .getThreadPool()
                .execute(new Runnable() {
                    public void run() {
                        try {
                            CookieManager cookieManager = CookieManager.getInstance();
                            String[] cookies = cookieManager.getCookie(url).split("; ");
                            String cookieValue = "";

                            for (int i = 0; i < cookies.length; i++) {
                                if (cookies[i].contains(cookieName + "=")) {
                                    cookieValue = cookies[i].split("=")[1].trim();
                                    break;
                                }
                            }

                            JSONObject json = null;

                            if (cookieValue != "") {
                                json = new JSONObject("{cookieValue:\"" + cookieValue + "\"}");
                            }

                            if (json != null) {
                                PluginResult res = new PluginResult(PluginResult.Status.OK, json);
                                callbackContext.sendPluginResult(res);
                            }
                            else {
                                callbackContext.error("Cookie not found!");
                            }
                        }
                        catch (Exception e) {
                            callbackContext.error(e.getMessage());
                        }
                    }
                });

        return true;
    }

    /**
     * sets cookie value under given key
     * @param args
     * @param callbackContext
     * @return boolean
     */
    private boolean setCookie(JSONArray args, CallbackContext callbackContext) {
        final String url = args.getString(0);
        final String cookieName = args.getString(1);
        final String cookieValue = args.getString(2);

        cordova
                .getThreadPool()
                .execute(new Runnable() {
                    public void run() {
                        try {
                            this.cookieManager.setCookie(url, cookieName + "=" + cookieValue);

                            PluginResult res = new PluginResult(PluginResult.Status.OK, "Successfully added cookie");
                            callbackContext.sendPluginResult(res);
                        }
                        catch (Exception e) {
                            callbackContext.error(e.getMessage());
                        }
                    }
                });

        return true;
    }

    /**
     * Clears all cookies
     * @return boolean
     */
    private boolean clearAllCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            this.cookieManager.removeAllCookies();
            this.cookieManager.flush();
        }
        else {
            this.cookieManager.removeAllCookie();
            this.cookieManager.removeSessionCookie();
        }

        callbackContext.success();

        return true;
    }
}
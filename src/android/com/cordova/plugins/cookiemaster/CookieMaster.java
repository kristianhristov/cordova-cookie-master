package com.cordova.plugins.cookiemaster;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.List;

public class CookieMaster extends CordovaPlugin {

  private final String TAG = "CookieMasterPlugin";
  public static final String ACTION_GET_COOKIE_VALUE = "getCookieValue";
  public static final String ACTION_SET_COOKIE_VALUE = "setCookieValue";
  
  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    try {
      if (ACTION_GET_COOKIE_VALUE.equals(action)) {
        final String url = args.getString(0);
        final String cookieName = args.getString(1);
        
        cordova.getThreadPool().execute(new Runnable() {
          public void run() {
            try {
              DefaultHttpClient client = new DefaultHttpClient();
              HttpGet request = new HttpGet(url);
              HttpResponse response = client.execute(request);
              
              CookieStore cookieStore = client.getCookieStore();
              List<Cookie> cookies = cookieStore.getCookies();
              
              JSONObject json = null;
              for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                  String value = cookie.getValue();
                  Log.v(TAG, cookieName + ": " + value);
                  json = new JSONObject("{cookieValue:" + value + "}");
                }
              }
              if (json != null) {
                PluginResult res = new PluginResult(PluginResult.Status.OK, json);
                callbackContext.sendPluginResult(res);        
              } else {
                callbackContext.error("Cookie not found!");
              }
            } catch (Exception e) {
              Log.e(TAG, "Exception: " + e.getMessage());
              callbackContext.error(e.getMessage());
            }        
          }
        });
        return true;      
      } else if (ACTION_SET_COOKIE_VALUE.equals(action)) {
        final String url = args.getString(0);
        final String cookieName = args.getString(1);
        final String cookieValue = args.getString(2);

        cordova.getThreadPool().execute(new Runnable() {
          public void run() {
            try {
              DefaultHttpClient client = new DefaultHttpClient();
              HttpGet request = new HttpGet(url);
              HttpResponse response = client.execute(request);
              
              CookieStore cookieStore = client.getCookieStore();

              BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
              cookie.setDomain(url);

              cookieStore.addCookie(cookie);
              
              PluginResult res = new PluginResult(PluginResult.Status.OK, "Successfully added cookie");
              callbackContext.sendPluginResult(res);        
            } catch (Exception e) {
              Log.e(TAG, "Exception: " + e.getMessage());
              callbackContext.error(e.getMessage());
            }        
          }
        });
        return true;
      }
      callbackContext.error("Invalid action");
      return false;
    } catch (Exception e) {
      Log.e(TAG, "Exception: " + e.getMessage());
      callbackContext.error(e.getMessage());
      return false;
    }
  }
}

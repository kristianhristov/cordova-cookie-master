Cookie Master
==============

As you may already know, WebViews on Android and iOS platforms do not support regular use of <code>document.cookie</code> for managing the cookies in your app. This plugin allows you to get and set cookie values from your WebView through a simple interface.
 
## Supported Platforms
* Android
* iOS

## Installing

Install with Cordova CLI

    $ cordova plugin add https://github.com/kristianhristov/cordova-cookie-master.git

## Usage
### Get cookie value
```javascript
cookieMaster.getCookieValue('http://<some host>:<some port>', '<cookie name>', function(data) {
  console.log(data.cookieValue);
}, function(error) {
  if (error) {
    console.log('error: ' + error);
  }
});
```
### Set cookie value
```javascript
cookieMaster.setCookieValue('http://<some host>:<some port>', '<cookie name>', '<cookie value>');
```
The cookie value should be formatted just like a regular <code>document.cookie</code> value.

## Limitations
* This plugin does not provide a remove cookie mechanism as this may be achieved by setting a date in the past for the value (same way you would do it with <code>document.cookie</code>).
* This version has been tested on Android 4.4, iOS 7.1 and iOS 8 devices. Experience may vary for different OS versions.


## License
This plugin is distributed under the MIT License.

## Thanks to
This plugin was inspired by the great work on the CookieMonster plugin by @barrettc

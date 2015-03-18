var cookieMaster = {
  getCookieValue: function(url, cookieName, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CookieMaster', 'getCookieValue', [url, cookieName]);
  },
  setCookieValue: function (url, cookieName, cookieValue) {
    cordova.exec(
      function () {
        console.log('New Cookie has been set');
      },
      function (err) {
        console.log('Something went wrong setting the new cookie: ', err);
      },
      'CookieMaster', 'setCookieValue', [url, cookieName, cookieValue]
    );
  }
};
module.exports = cookieMaster;

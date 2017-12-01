package myrecipes.app.constants;
import com.google.api.server.spi.Constant;

/**
 * Client IDs and scopes of clients that can access the API.
 */
public class Constants {
  public static final String WEB_CLIENT_ID = "a web client ID";
  public static final String ANDROID_CLIENT_ID = "an Android client ID";
  public static final String IOS_CLIENT_ID = "an iOS client ID";
  public static final String API_EXPLORER_CLIENT_ID = Constant.API_EXPLORER_CLIENT_ID;
  public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
}

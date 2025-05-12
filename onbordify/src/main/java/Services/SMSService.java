package Services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
public class SMSService {

    // Replace with your Twilio credentials
   // public static final String ACCOUNT_SID = "ACa92978f8493b6adcd9f342f1649b0029";
  //  public static final String AUTH_TOKEN = "6c0d1b1928490233564a3af994361c8c";
    public static final String TWILIO_PHONE_NUMBER = "+16502978361"; // Your Twilio number

    static {
       // Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSMS(String to, String message) {
        Message sms = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                message
        ).create();

        System.out.println("SMS Sent! SID: " + sms.getSid());
    }
}
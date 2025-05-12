package Services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
public class SMSService {

    // Replace with your Twilio credentials
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    public static final String TWILIO_PHONE_NUMBER = "+16502978361"; // Your Twilio number

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
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
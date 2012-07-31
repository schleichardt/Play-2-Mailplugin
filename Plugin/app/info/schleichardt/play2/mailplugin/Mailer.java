package info.schleichardt.play2.mailplugin;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import java.util.List;

public class Mailer {
    public static String send(final Email email) throws EmailException {
        return MailPlugin$.MODULE$.send(email);
    }

    public static List<Email> history() {
        return MailPlugin$.MODULE$.historyAsJava();
    }


}

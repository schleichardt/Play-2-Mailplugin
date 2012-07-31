package info.schleichardt.play2.mailplugin;

import info.schleichardt.play2.mailplugin.api.Mailer$;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import java.util.List;

public class Mailer {
    public static String send(final Email email) throws EmailException {
        return Mailer$.MODULE$.send(email);
    }

    public static List<Email> history() {
        return Mailer$.MODULE$.historyAsJava();
    }


}

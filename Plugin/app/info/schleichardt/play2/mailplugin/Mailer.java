package info.schleichardt.play2.mailplugin;

import info.schleichardt.play2.mailplugin.api.Mailer$;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import java.util.List;

public class Mailer {
    private Mailer() {
    }

    /** send an email with default profile, for example with smtp.user setting */
    public static String send(final Email email) throws EmailException {
        return Mailer$.MODULE$.send(email, "");
    }

    /** send an email with a special profile, for example with smtp.profiles.aprofile.user setting */
    public static String send(final Email email, final String profile) throws EmailException {
        return Mailer$.MODULE$.send(email, profile);
    }

    public static List<Email> history() {
        return Mailer$.MODULE$.historyAsJava();
    }

    public static void setInterceptor(final EmailSendInterceptor interceptor) {
        Mailer$.MODULE$.setInterceptor(interceptor);
    }
}

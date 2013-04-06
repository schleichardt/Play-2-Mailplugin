package info.schleichardt.play2.mailplugin;

import info.schleichardt.play2.mailplugin.interceptors.EmailInterceptor;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import play.Play;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.List;

public class Mailer {
    private Mailer() {
    }

    /** send an email with default profile, for example with smtp.user setting */
    public static String send(final Email email) throws EmailException {
        return send(email, "");
    }

    /** send an email with a special profile, for example with smtp.profiles.aprofile.user setting */
    public static String send(final Email email, final String profile) throws EmailException {
        return getMailPluginInstance().send(email, profile);
    }

    public static List<Email> history() {
        final Seq<Email> HistoryAsScalaSeq = getMailPluginInstance().history();
        return JavaConverters.seqAsJavaListConverter(HistoryAsScalaSeq).asJava();
    }

    public static void addInterceptor(final EmailInterceptor interceptor) {
        getMailPluginInstance().addInterceptor(interceptor);
    }

    public static void removeInterceptor(final EmailInterceptor interceptor) {
        getMailPluginInstance().removeInterceptor(interceptor);
    }

    private static MailPlugin getMailPluginInstance() {
        return Play.application().plugin(MailPlugin.class);
    }
}

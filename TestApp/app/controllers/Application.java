package controllers;

import info.schleichardt.play2.mailplugin.Mailer;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    private static final String TEXT_MESSAGE = "Dear recipient,\n\n\nthis is the mailtext.\n\nsincerely sender";

    public static Result index() {
        return ok(index.render());
    }

    public static Result sendMail() {
        try {
            sendTextMail();
            sendHtmlMail();
            flash("flash", "Mail has been send. Watch it on the console.");
        } catch (final EmailException e) {
            flash("flash", "Error, email hadn't been send.");
            e.printStackTrace();
        }
        return ok(index.render());
    }

    private static void sendHtmlMail() throws EmailException {
        final HtmlEmail email = new HtmlEmail();
        fillFromToSubject(email);
        email.setHtmlMsg("<html><body style='background: red'><p>Hello</p></body></html>");
        email.setTextMsg("Hello");
        Mailer.send(email);
    }

    private static void sendTextMail() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        fillFromToSubject(email);
        email.setMsg(TEXT_MESSAGE);
        Mailer.send(email);
    }

    private static void fillFromToSubject(final Email email) throws EmailException {
        email.setFrom("TestApp@localhost");
        email.addTo("user@localhost");
        email.setSubject("test subject");
    }

}
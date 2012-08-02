package demo;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.Email;

public class DemoEmailProvider {
    public static final String DEFAULT_TEXT_MESSAGE = "Dear recipient,\n\n\nthis is the mailtext.\n\nsincerely sender";
    public static final String DEFAULT_HTML_MESSAGE = "<html><body style='background: red'><p>Hello</p></body></html>";

    public static SimpleEmail newFilledSimpleMailWithSmtpSettings() throws EmailException {
        final SimpleEmail email = newFilledSimpleMail();
        email.setHostName("localhost");
        return email;
    }

    public static SimpleEmail newFilledSimpleMail() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        setFromToSubject(email);
        email.setMsg(DEFAULT_TEXT_MESSAGE);
        return email;
    }

    public static HtmlEmail newfilledHtmlEmail() throws EmailException {
        final HtmlEmail email = new HtmlEmail();
        setFromToSubject(email);
        email.setHtmlMsg(DEFAULT_HTML_MESSAGE);
        return email;

    }

    private static void setFromToSubject(final Email email) throws EmailException {
        email.setFrom("sender@localhost");
        email.addTo("recipient@localhost");
        email.setSubject("the subject of the email");
    }
}

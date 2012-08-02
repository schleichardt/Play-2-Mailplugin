package demo;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.Email;

public class DemoEmailProvider {
    public static final String DEFAULT_TEXT_MESSAGE = "Dear recipient,\n\n\nthis is the mailtext.\n\nsincerely sender";
    public static final String DEFAULT_HTML_MESSAGE = "<html><body style='background: red'><p>Hello</p></body></html>";
    public static final String SENDER_LOCALHOST = "sender@localhost";
    public static final String RECIPIENT_LOCALHOST = "recipient@localhost";
    public static final String SUBJECT = "the subject of the email";
    public static final String HTML_MAIL_MESSAGE_TEXT = "Hello";

    public static SimpleEmail newFilledSimpleMailWithSmtpSettings() throws EmailException {
        final SimpleEmail email = newFilledSimpleEmail();
        email.setHostName("localhost");
        return email;
    }

    public static SimpleEmail newFilledSimpleEmail() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        setFromToSubject(email);
        email.setMsg(DEFAULT_TEXT_MESSAGE);
        return email;
    }

    public static HtmlEmail newFilledHtmlEmail() throws EmailException {
        final HtmlEmail email = new HtmlEmail();
        setFromToSubject(email);
        email.setMsg(HTML_MAIL_MESSAGE_TEXT);
        email.setHtmlMsg(DEFAULT_HTML_MESSAGE);
        return email;

    }

    private static void setFromToSubject(final Email email) throws EmailException {
        email.setFrom(SENDER_LOCALHOST);
        email.addTo(RECIPIENT_LOCALHOST);
        email.setSubject(SUBJECT);
    }
}

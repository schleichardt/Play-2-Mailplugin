package demo;

import org.apache.commons.mail.*;

import javax.mail.Part;
import java.io.IOException;



public class DemoEmailProvider {
    public static final String DEFAULT_TEXT_MESSAGE = "Dear recipient,\n\n\nthis is the mailtext.\n\nsincerely sender";
    public static final String DEFAULT_HTML_MESSAGE = "<html><body style='background: red'><p>Hello</p></body></html>";
    public static final String SENDER_LOCALHOST = "sender@localhost";
    public static final String RECIPIENT_LOCALHOST = "recipient@localhost";
    public static final String SUBJECT = "the subject of the email";
    public static final String HTML_MAIL_MESSAGE_TEXT = "Hello";
    public static final String ATTACHEMENT_TXT_FILE_NAME = "the-text-file.txt";

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
        email.setTextMsg(HTML_MAIL_MESSAGE_TEXT);
        email.setHtmlMsg(DEFAULT_HTML_MESSAGE);
        return email;

    }

    public static HtmlEmail newFilledHtmlEmailWithAttachement() throws EmailException {
        final HtmlEmail email = newFilledHtmlEmail();
        try {
            final ByteArrayDataSource dataSource = new ByteArrayDataSource(ATTACHEMENT_TXT_FILE_NAME.getBytes(), "text/plain");
            email.attach(dataSource, ATTACHEMENT_TXT_FILE_NAME, "An example test file.", Part.ATTACHMENT);
        } catch (final IOException e) {
            throw new EmailException(e);
        }
        return email;

    }

    private static void setFromToSubject(final Email email) throws EmailException {
        email.setFrom(SENDER_LOCALHOST);
        email.addTo(RECIPIENT_LOCALHOST);
        email.setSubject(SUBJECT);
    }
}

package info.schleichardt.play2.mailplugin;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public class DefaultEmailSendInterceptor implements EmailSendInterceptor {
    @Override
    public void preConfiguration(Email email, String profile) throws EmailException {
    }

    @Override
    public void afterConfiguration(Email email, String profile) throws EmailException {
    }

    @Override
    public void afterSend(Email email, String profile) throws EmailException {
    }
}

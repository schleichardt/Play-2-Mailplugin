package info.schleichardt.play2.mailplugin;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public class DefaultEmailSendInterceptor implements EmailSendInterceptor {
    @Override
    public void preConfiguration(final Email email) throws EmailException {
    }

    @Override
    public void afterConfiguration(final Email email) throws EmailException {
    }

    @Override
    public void afterSend(final Email email) throws EmailException {
    }
}

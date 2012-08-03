package info.schleichardt.play2.mailplugin;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public interface EmailSendInterceptor {
    void preConfiguration(Email email) throws EmailException;
    void afterConfiguration(Email email) throws EmailException;
    void afterSend(Email email) throws EmailException;
}

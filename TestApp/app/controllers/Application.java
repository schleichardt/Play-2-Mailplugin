package controllers;

import info.schleichardt.play2.mailplugin.Mailer;
import org.apache.commons.mail.EmailException;
import play.mvc.*;

import views.html.*;

import static demo.DemoEmailProvider.newFilledHtmlEmail;
import static demo.DemoEmailProvider.newFilledSimpleEmail;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result sendMail() {
        try {
            Mailer.send(newFilledSimpleEmail());
            Mailer.send(newFilledHtmlEmail());
        } catch (final EmailException e) {
            e.printStackTrace();
        }
        return redirect(routes.Application.index());
    }

}
import demo.DemoEmailProvider
import info.schleichardt.play2.mailplugin.MailPluginTestEnhancement
import org.apache.commons.mail.SimpleEmail
import org.specs2.mutable._
import demo.DemoEmailProvider._

package info.schleichardt.play2.mailplugin {

  import org.apache.commons.mail.Email

  object MailPluginTestEnhancement {
    def emailToString(email: Email): String = MailPlugin.getEmailDebugOutput(email)
  }
}

class DebugEmailFormatSpec extends Specification {
  "Emails" can {
    "as SimpleEmails be debugged with Strings" in {
      val email = newFilledSimpleEmail()
      val output = MailPluginTestEnhancement.emailToString(email)
      output must contain(SENDER_LOCALHOST)
      output must contain(RECIPIENT_LOCALHOST)
      output must contain(DEFAULT_TEXT_MESSAGE)
    }

    "as HtmlEmails be debugged with Strings" in {
      val email = newFilledHtmlEmailWithAttachement()
      val output = MailPluginTestEnhancement.emailToString(email)
      output must contain(SENDER_LOCALHOST)
      output must contain(RECIPIENT_LOCALHOST)
      output must contain(HTML_MAIL_MESSAGE_TEXT)
      output must contain(DEFAULT_HTML_MESSAGE)
      output must contain(ATTACHEMENT_TXT_FILE_NAME)
    }
  }
}

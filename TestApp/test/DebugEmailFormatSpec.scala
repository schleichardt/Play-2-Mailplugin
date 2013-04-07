package info.schleichardt.play2.mailplugin

import interceptors.LogEmailInterceptor
import org.apache.commons.mail.{Email, SimpleEmail}
import org.specs2.mutable._
import demo.DemoEmailProvider._

class DebugEmailFormatSpec extends Specification {

  def emailToString(email: Email): String = LogEmailInterceptor.getEmailDebugOutput(email)

  "Emails" can {
    "as SimpleEmails be debugged with Strings" in {
      val email = newFilledSimpleEmail()
      val output = emailToString(email)
      output must contain(SENDER_LOCALHOST)
      output must contain(RECIPIENT_LOCALHOST)
      output must contain(DEFAULT_TEXT_MESSAGE)
    }

    "as HtmlEmails be debugged with Strings" in {
      val email = newFilledHtmlEmailWithAttachement()
      val output = emailToString(email)
      output must contain(SENDER_LOCALHOST)
      output must contain(RECIPIENT_LOCALHOST)
      output must contain(HTML_MAIL_MESSAGE_TEXT)
      output must contain(DEFAULT_HTML_MESSAGE)
      output must contain(ATTACHEMENT_TXT_FILE_NAME)
    }
  }
}

import com.icegreen.greenmail.util.{GreenMailUtil, ServerSetup, GreenMail}
import javax.mail.internet.MimeMessage
import org.apache.commons.mail.SimpleEmail
import org.specs2.mutable._
import info.schleichardt.play2.mailplugin._

import play.api.test._
import play.api.test.Helpers._

import demo.DemoEmailProvider.newfilledHtmlEmail
import demo.DemoEmailProvider.newFilledSimpleMail
import demo.DemoEmailProvider.newFilledSimpleMailWithSmtpSettings

class MailPluginSpec extends Specification {
  "MailPlugin" should {
    "be in a mock version" in {
      MailPlugin.usesMockMail === true
    }

    "be activated in this app" in {
      val app: FakeApplication = FakeApplication()
      running(app) {
        app.plugin(classOf[MailPlugin]).get.enabled === true
      }
    }

    "archive up to 5 mails" in {
      running(FakeApplication()) {
        for (index <- 1 to 10) {
          val email: SimpleEmail = newFilledSimpleMail()
          email.setSubject("subject " + index)
          Mailer.send(email)
        }
        val history = Mailer.history()
        history.size === 5
        history.get(0).getSubject === "subject 6"
        history.get(4).getSubject === "subject 10"
      }
    }

    "grab the right configuration" in {
      val configMap: Map[String, String] = Map("smtp.mock" -> "false")
      val app: FakeApplication = FakeApplication(additionalConfiguration = configMap)
      running(app) {
        app.configuration.getBoolean("smtp.mock").get === false
        val mailConf = MailPlugin.configuration
        mailConf.host === "localhost"
        mailConf.port === 26
        mailConf.useSsl === true
        mailConf.user.get === "michael"
        mailConf.password.get === "123456"
      }
    }

    "be able to send real mails" in {
      val testPort = com.icegreen.greenmail.util.ServerSetupTest.SMTP.getPort
      val configMap: Map[String, String] = Map("smtp.mock" -> "false", "smtp.port" -> testPort.toString, "smtp.ssl" -> "false")
      val app: FakeApplication = FakeApplication(additionalConfiguration = configMap)
      running(app) {
        var greenMail = new GreenMail(com.icegreen.greenmail.util.ServerSetupTest.SMTP)
        try {
          greenMail.start()
          val email: SimpleEmail = newFilledSimpleMail()
          val subject = "the subject"
          email.setSubject(subject)
          Mailer.send(email)
          val receivedMessages: Array[MimeMessage] = greenMail.getReceivedMessages
          receivedMessages.size === 1
          receivedMessages(0).getSubject === subject
        } finally {
          if (greenMail != null) {
            greenMail.stop();
          }
        }
      }
    }
  }
}

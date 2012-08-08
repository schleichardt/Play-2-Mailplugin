import com.icegreen.greenmail.util.{GreenMailUtil, ServerSetup, GreenMail}
import javax.mail.internet.MimeMessage
import org.apache.commons.mail.{EmailException, Email, SimpleEmail}
import org.specs2.mutable._
import info.schleichardt.play2.mailplugin._

import play.api.test._
import play.api.test.Helpers._

import demo.DemoEmailProvider.newFilledHtmlEmail
import demo.DemoEmailProvider.newFilledSimpleEmail
import demo.DemoEmailProvider.newFilledSimpleMailWithSmtpSettings

import scala.collection.JavaConversions._

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
          val email: SimpleEmail = newFilledSimpleEmail()
          email.setSubject("subject " + index)
          Mailer.send(email)
        }
        val history = Mailer.history()
        history.size === 5
        history.get(0).getSubject === "subject 6"
        history.get(4).getSubject === "subject 10"
      }
    }

    "be able to configure mail archive size" in {
      val configMap: Map[String, String] = Map("smtp.archive.size" -> "20")
      val app: FakeApplication = FakeApplication(additionalConfiguration = configMap)
      running(app) {
        for (index <- 1 to 30) {
          val email: SimpleEmail = newFilledSimpleEmail()
          email.setSubject("subject " + index)
          Mailer.send(email)
        }
        val history = Mailer.history()
        history.size === 20
        history.get(0).getSubject === "subject 11"
        history.get(4).getSubject === "subject 15"
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

    "be able to add additional configuration" in {
      val additionalRecipient = "additionalRecipient@localhost"

      val interceptor: EmailSendInterceptor = new DefaultEmailSendInterceptor {
        override def afterConfiguration(email: Email) {
          email.addBcc(additionalRecipient)
        }
      }

      running(FakeApplication()) {
        val email = newFilledSimpleEmail()
        email.getBccAddresses.exists(_.toString.contains(additionalRecipient)) === false

        Mailer.setInterceptor(interceptor)
        Mailer.send(email)

        email.getBccAddresses.exists(_.toString.contains(additionalRecipient)) === true
      }
    }

    "be able to cause a send error for TDD" in {
      val interceptor: EmailSendInterceptor = new DefaultEmailSendInterceptor {
        override def afterConfiguration(email: Email) {
          throw new EmailException("thrown for testing if the app reacts correctly on a mail server error")
        }
      }

      running(FakeApplication()) {
        val email = newFilledSimpleEmail()
        Mailer.setInterceptor(interceptor)
        Mailer.send(email) must throwA[EmailException]
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
          val email: SimpleEmail = newFilledSimpleEmail()
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

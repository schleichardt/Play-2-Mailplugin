package info.schleichardt.play2.mailplugin

import play.api.{Plugin, Application}
import org.apache.commons.mail.{MultiPartEmail, Email}
import collection.mutable.DoubleLinkedList
import play.Logger
import javax.mail.internet.MimeMultipart
import javax.mail.{Part, Multipart}
import org.apache.commons.lang.StringUtils.isEmpty

class MailPlugin(app: Application) extends Plugin {
  protected[mailplugin] var mailArchive = new DoubleLinkedList[Email]()
  private lazy val useMockMail = app.configuration.getBoolean("smtp.mock").getOrElse(true)
  MailPlugin.instance = this
  private lazy val configuration = if (!useMockMail) {
    //uses Typesafe syntax: Copyright 2012 Typesafe (http://www.typesafe.com), https://github.com/typesafehub/play-plugins/blob/master/mailer/src/main/scala/com/typesafe/plugin/MailerPlugin.scala
    val host = app.configuration.getString("smtp.host").getOrElse(throw new RuntimeException("smtp.host needs to be set in application.conf in order to use this plugin (or set smtp.mock to true)"))
    val port = app.configuration.getInt("smtp.port").getOrElse(25)
    val useSsl = app.configuration.getBoolean("smtp.ssl").getOrElse(false)
    val user = app.configuration.getString("smtp.user")
    val password = app.configuration.getString("smtp.password")
    MailConfiguration(host, port, useSsl, user, password)
  } else {
    null
  }

  protected[mailplugin] def send(email: Email): String = {
    val result =
      if (useMockMail) {
        val mailContent: String = MailPlugin.getEmailDebugOutput(email)
        Logger.debug(mailContent)
        ""
      } else {
        configuration.setup(email)
        email.send()
      }
    mailArchive = mailArchive :+ email
    if (mailArchive.length > 5) {
      mailArchive = mailArchive.drop(1)
    }
    result
  }
}

object MailPlugin {
  lazy val MaxEmailArchiveSize: Int = 5
  protected[mailplugin] var instance: MailPlugin = null

  def usesMockMail = instance match {
    case x: MailPlugin => x.useMockMail
    case _ => true
  }

  def configuration = instance.configuration

  private[mailplugin] def getEmailDebugOutput(email: Email): String = {
    val headers: String = views.txt.emailDebug(email).toString()
    val content: String = {
      if(isEmpty(email.getHostName))
        email.setHostName("localhost")
      email.buildMimeMessage()
      email match {
        case mail: MultiPartEmail => mail.getMimeMessage.getContent match {
            case mimeMulti: MimeMultipart => (for(i <- 0 until mimeMulti.getCount) yield getContent(mimeMulti.getBodyPart(i))).mkString
            case x => x.toString
          }
        case _ => email.getMimeMessage.getContent.toString
      }
    }
    headers + content
  }

  private[this] def getContent(message: Part): String = {
    def formatAttachement(part: Part): String = {
      val filename = if(isEmpty(message.getFileName)) "none" else message.getFileName
      val description = if(isEmpty(message.getDescription)) "none" else message.getDescription
      return """
               |attachment:
               |\t\tname: %s
               |\t\tdisposition: %s
               |\t\tdescription: %s
             """.format(filename, message.getDisposition(), description).stripMargin
    }

    def formatPart(part: Part): String = {
      if (!(Part.ATTACHMENT == part.getDisposition)) {
        getContent(part)
      } else {
        formatAttachement(part)
      }
    }

    message.getContent match {
      case s: String => message.getContentType + ": " + message.getContent + " \n"
      case part: Multipart => (for (i <- 0 until part.getCount) yield formatPart(part.getBodyPart(i))).mkString
      case part: Part =>  formatPart(part)
      case _ => ""
    }
  }
}

case class MailConfiguration(host: String, port: Int, useSsl: Boolean, user: Option[String], password: Option[String]) {
  def setup(email: Email): Email = {
    email.setHostName(host);
    email.setSmtpPort(port);
    email.setSSL(useSsl);
    email.setAuthentication(user.getOrElse(""), password.getOrElse(""));
    email
  }

}
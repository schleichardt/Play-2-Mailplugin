package info.schleichardt.play2.mailplugin

import play.api.{Plugin, Application}
import org.apache.commons.mail.{MultiPartEmail, Email}
import collection.mutable.DoubleLinkedList
import play.Logger
import javax.mail.internet.{PreencodedMimeBodyPart, MimeMessage, MimeMultipart, MimeBodyPart}
import javax.mail.{Message, BodyPart, Part, Multipart}

import org.apache.commons.lang3.StringUtils.isEmpty
import java.io.ByteArrayInputStream

import scala.collection.JavaConversions._
import collection.mutable.SynchronizedQueue


class MailPlugin(app: Application) extends Plugin {
  protected[mailplugin] var mailArchive = new SynchronizedQueue[Email]()
  private val useMockMail = app.configuration.getBoolean("smtp.mock").getOrElse(true)
  val MaxEmailArchiveSize: Int = {
    val defaultEmailArchiveSize = 5
    app.configuration.getInt("smtp.archive.size").getOrElse(defaultEmailArchiveSize)
  }
  MailPlugin.instance = this
  var interceptor: EmailSendInterceptor = new DefaultEmailSendInterceptor
  private val configuration = if (!useMockMail) {
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

  private[this] def archive(email: Email) {
    mailArchive += email
    while (mailArchive.length > MaxEmailArchiveSize) {
      mailArchive.dequeue()
    }
  }

  protected[mailplugin] def send(email: Email): String = {
    require(email != null, "Email should not be null")

    val senderAlgorithm: (Email) => String = (configuredEmail: Email) =>
      if (useMockMail) {
        val mailContent: String = MailPlugin.getEmailDebugOutput(configuredEmail)
        Logger.debug("mock email send:\n" + mailContent)
        ""
      } else {
        configuredEmail.send()
      }

    interceptor.preConfiguration(email)
    if (!useMockMail) {
      configuration.setup(email)
    }
    interceptor.afterConfiguration(email)
    val result = senderAlgorithm(email)
    archive(email)
    interceptor.afterSend(email)
    result
  }
}

object MailPlugin {
  protected[mailplugin] var instance: MailPlugin = null

  def usesMockMail = instance match {
    case x: MailPlugin => x.useMockMail
    case _ => true
  }

  def configuration = instance.configuration

  private[mailplugin] def getimportantEmailHeadDebugOutput(email: Email): String = {
    val buffer = new StringBuffer
    buffer.append("From: " + email.getFromAddress+ "\n")
    buffer.append("To:   " +email.getToAddresses.mkString(", ")+ "\n")
    if (email.getCcAddresses.size() > 0)
      buffer.append("CC:   "+email.getCcAddresses.mkString(", ")+ "\n")
    if (email.getBccAddresses.size() > 0)
      buffer.append("BCC:  "+email.getBccAddresses.mkString(", ")+ "\n")
    buffer.append("Date: "+new java.util.Date+ "\n")
    buffer.append("Subj: "+email.getSubject+ "\n")
    buffer.toString
  }

  private[mailplugin] def getEmailDebugOutput(email: Email): String = {
    val content: String = {
      if(isEmpty(email.getHostName))
        email.setHostName("localhost")
      email.buildMimeMessage()
      email match {
        case mail: MultiPartEmail => mail.getMimeMessage.getContent match {
            case mimeMulti: MimeMultipart => (for(i <- 0 until mimeMulti.getCount) yield (getContent(mimeMulti.getBodyPart(i)))).mkString
            case x => x.toString
          }
        case _ => email.getMimeMessage.getContent.toString
      }
    }
    getimportantEmailHeadDebugOutput(email) + "cont:\n" + content
  }

  private[this] def formatAttachement(part: Part): String = {
    val filename = if(isEmpty(part.getFileName)) "none" else part.getFileName
    val description = if(isEmpty(part.getDescription)) "none" else part.getDescription
    return """attachment:
             |  name:  %s
             |  MIME:  %s
             |  descr: %s
             |  disp:  %s
           """.format(filename, part.getDisposition(), description, part.getContentType).stripMargin
  }

def getContent(part: Part): String = {
    part.getContent match {
      case part: Multipart => (for (i <- 0 until part.getCount) yield getContent(part.getBodyPart(i))).mkString
      case part: Part =>  getContent(part)
      case s: String if !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition) => part.getContentType + ": " + part.getContent + "\n"
      case s: String => formatAttachement(part)
      case b: ByteArrayInputStream => formatAttachement(part)
      case x => part.getContentType + ": " + x
    }
  }
}

case class MailConfiguration(host: String, port: Int, useSsl: Boolean, user: Option[String], password: Option[String]) {
  def setup(email: Email): Email = {
    email.setHostName(host);
    email.setSmtpPort(port);
    email.setSSLOnConnect(useSsl);
    email.setAuthentication(user.getOrElse(""), password.getOrElse(""));
    email
  }

}
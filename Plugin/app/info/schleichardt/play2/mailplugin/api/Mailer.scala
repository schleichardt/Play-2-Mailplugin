package info.schleichardt.play2.mailplugin.api

import org.apache.commons.mail.Email
import info.schleichardt.play2.mailplugin.{EmailSendInterceptor, MailPlugin}
import scala.collection.JavaConversions._

object Mailer {
  def send(email: Email): String = MailPlugin.instance.send(email)

  def history: Seq[Email] = MailPlugin.instance.mailArchive.clone()

  protected[mailplugin] def historyAsJava: java.util.List[Email] = history

  def setInterceptor(interceptor: EmailSendInterceptor) {
    require(interceptor != null)
    MailPlugin.instance.interceptor = interceptor
  }
}

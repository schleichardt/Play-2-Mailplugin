package info.schleichardt.play2.mailplugin.api

import org.apache.commons.mail.Email
import info.schleichardt.play2.mailplugin.MailPlugin
import scala.collection.JavaConversions._

object Mailer {
  def send(email: Email): String = MailPlugin.instance.send(email)

  def history = MailPlugin.instance.mailArchive.clone()

  def historyAsJava: java.util.List[Email] = history
}

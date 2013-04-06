package info.schleichardt.play2.mailplugin.interceptors

import org.apache.commons.mail.Email
import play.api.Application

private[mailplugin]
case class MailConfiguration(host: String, port: Int, useSsl: Boolean, user: Option[String], password: Option[String]) {
  def setup(email: Email): Email = {
    email.setHostName(host)
    email.setSmtpPort(port)
    email.setSSLOnConnect(useSsl)
    email.setAuthentication(user.getOrElse(""), password.getOrElse(""))
    email
  }
}

private[mailplugin]
class ConfigurationEmailInterceptor(app: Application) extends DefaultEmailInterceptor {

  override def afterConfiguration(args: InterceptorArgs) = {
    configuration(args.profile).setup(args.email)
    args
  }

  def configuration(profile: String = "") = {
    val usesProfile: Boolean = profile != null && !profile.trim.isEmpty
    val prefix = if (usesProfile) "smtp.profiles." + profile + "." else "smtp."

    //inspired by Typesafe syntax: Copyright 2012 Typesafe (http://www.typesafe.com), https://github.com/typesafehub/play-plugins/blob/master/mailer/src/main/scala/com/typesafe/plugin/MailerPlugin.scala
    val host = app.configuration.getString(prefix + "host").getOrElse(throw new RuntimeException(prefix + "host needs to be set in application.conf in order to use this plugin (or set smtp.mock to true)"))
    val port = app.configuration.getInt(prefix + "port").getOrElse(25)
    val useSsl = app.configuration.getBoolean(prefix + "ssl").getOrElse(false)
    val user = app.configuration.getString(prefix + "user")
    val password = app.configuration.getString(prefix + "password")
    MailConfiguration(host, port, useSsl, user, password)
  }
}
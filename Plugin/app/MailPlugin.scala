package info.schleichardt.play2.mailplugin

import play.api.Plugin
import play.api.Application

class MailPlugin(app: Application) extends Plugin {
  private lazy val useMockMail = app.configuration.getBoolean("smtp.mock").getOrElse(true)
  MailPlugin.instance = this
  private lazy val configuration =  if (!useMockMail) {
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
}

object MailPlugin {
  private var instance: MailPlugin = null
  def usesMockMail = instance match {
    case x: MailPlugin => x.useMockMail
    case _ => true
  }

  def configuration = instance.configuration
}

case class MailConfiguration(host: String, port:Int, useSsl:Boolean, user: Option[String], password: Option[String])
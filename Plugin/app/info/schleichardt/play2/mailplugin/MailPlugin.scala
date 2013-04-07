package info.schleichardt.play2.mailplugin

import interceptors._
import interceptors.InterceptorArgs
import play.api.{Plugin, Application}
import org.apache.commons.mail.Email

class MailPlugin(app: Application) extends Plugin {
  private[mailplugin] lazy val useMockMail = app.configuration.getBoolean("smtp.mock").getOrElse(true)
  private lazy val archiveInterceptor = new ArchiveEmailInterceptor(app)

  override def onStart() {
    if (useMockMail) {
      addInterceptor(LogEmailInterceptor)
    } else {
      addInterceptor(new ConfigurationEmailInterceptor(app))
    }
    addInterceptor(archiveInterceptor)
  }

  private[mailplugin]
  def history(): Seq[Email] = archiveInterceptor.mailArchive.clone()

  private var emailSendInterceptors: Seq[EmailInterceptor] = Nil

  def addInterceptor(interceptor: EmailInterceptor) {
    emailSendInterceptors = emailSendInterceptors :+ interceptor
  }

  def removeInterceptor(interceptor: EmailInterceptor) {
    emailSendInterceptors = emailSendInterceptors.filterNot(_ == interceptor)
  }

  private[mailplugin]
  def send(email: Email, profile: String): String = {
    require(email != null, "Email should not be null")

    var interceptorArgs = InterceptorArgs(email, profile)

    try {
      interceptorArgs = emailSendInterceptors.foldLeft(interceptorArgs)((args, ic) => ic.beforeConfiguration(args))
      interceptorArgs = emailSendInterceptors.foldLeft(interceptorArgs)((args, ic) => ic.afterConfiguration(args)) //ConfigurationEmailSendInterceptor is first interceptor here in prod
      val mimeMessageId = if (useMockMail) "" else interceptorArgs.email.send()
      emailSendInterceptors.foldLeft(interceptorArgs.withSuccess(mimeMessageId))((args, ic) => ic.onSuccess(args))
      mimeMessageId
    } catch {
      case e: Exception => {
        emailSendInterceptors.foldLeft(interceptorArgs.withException(e))((args, ic) => ic.onError(args))
        throw e
      }
    }
  }
}


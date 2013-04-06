package info.schleichardt.play2.mailplugin.interceptors

import org.apache.commons.mail.Email
import reflect.BeanProperty

case class InterceptorArgs(@BeanProperty email: Email, @BeanProperty profile: String) {
  def withException(exception: Exception) = ErrorInterceptorArgs(email, profile, exception)
  def withSuccess(mimeMessageId: String) = SuccessInterceptorArgs(email, profile, mimeMessageId)
}
case class SuccessInterceptorArgs(@BeanProperty email: Email, @BeanProperty profile: String, @BeanProperty mimeMessageId: String)
case class ErrorInterceptorArgs(@BeanProperty email: Email, @BeanProperty profile: String, @BeanProperty exception: Exception)

/**
 * An Interceptor used by the mail plugin.
 */
trait EmailInterceptor {
  /** is called before the mail plugin adds the configuration to the email */
  @throws(classOf[Exception])
  def beforeConfiguration(args: InterceptorArgs): InterceptorArgs

  /** is called after the mail plugin added the configuration to the email */
  @throws(classOf[Exception])
  def afterConfiguration(args: InterceptorArgs): InterceptorArgs

  /** is called after mail successfully send */
  def onSuccess(args: SuccessInterceptorArgs): SuccessInterceptorArgs

  /** is called if an exception is thrown */
  def onError(args: ErrorInterceptorArgs): ErrorInterceptorArgs
}

/**
 * A default implementation of EmailSendInterceptor that does nothing.
 */
class DefaultEmailInterceptor extends EmailInterceptor {
  def beforeConfiguration(args: InterceptorArgs) = args

  def afterConfiguration(args: InterceptorArgs) = args

  def onSuccess(args: SuccessInterceptorArgs) = args

  def onError(args: ErrorInterceptorArgs) = args
}

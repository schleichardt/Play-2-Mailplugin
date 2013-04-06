package info.schleichardt.play2.mailplugin.interceptors

import org.apache.commons.mail.Email

case class InterceptorArgs(email: Email, profile: String)

/**
 * An Interceptor used the mail plugin.
 */
trait EmailInterceptor {
  /** is called before the mail plugin adds the configuration to the email */
  @throws(classOf[Exception])
  def beforeConfiguration(args: InterceptorArgs)

  /** is called after the mail plugin added the configuration to the email */
  @throws(classOf[Exception])
  def afterConfiguration(args: InterceptorArgs)

  /** is called after mail successfully send */
  def onSuccess(args: InterceptorArgs)

  /** is called if an exception is thrown */
  def onError(args: InterceptorArgs, exception: Exception)
}

/**
 * A default implementation of EmailSendInterceptor that does nothing.
 */
class DefaultEmailInterceptor extends EmailInterceptor {
  def beforeConfiguration(args: InterceptorArgs) {
  }

  def afterConfiguration(args: InterceptorArgs) {
  }

  def onSuccess(args: InterceptorArgs) {
  }

  def onError(args: InterceptorArgs, exception: Exception) {
    throw exception
  }
}

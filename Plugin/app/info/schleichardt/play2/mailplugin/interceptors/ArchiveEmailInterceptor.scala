package info.schleichardt.play2.mailplugin.interceptors

import org.apache.commons.mail.Email
import collection.mutable.SynchronizedQueue
import play.api.Application

private[mailplugin] class ArchiveEmailInterceptor(app: Application) extends DefaultEmailInterceptor {
  val mailArchive = new SynchronizedQueue[Email]()

  override def onSuccess(args: SuccessInterceptorArgs) = {
    archive(args.email)
    args
  }

  private lazy val MaxEmailArchiveSize: Int = {
    val defaultEmailArchiveSize = 5
    app.configuration.getInt("smtp.archive.size").getOrElse(defaultEmailArchiveSize)
  }

  private def archive(email: Email) {
    mailArchive += email
    while (mailArchive.length > MaxEmailArchiveSize) {
      mailArchive.dequeue()
    }
  }
}
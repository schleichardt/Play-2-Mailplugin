package info.schleichardt.play2.mailplugin.interceptors

import org.apache.commons.mail.Email
import collection.mutable.SynchronizedQueue

private[mailplugin] object ArchiveEmailInterceptor extends DefaultEmailInterceptor {
  val mailArchive = new SynchronizedQueue[Email]()

  override def onSuccess(args: InterceptorArgs) {
    archive(args.email)
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
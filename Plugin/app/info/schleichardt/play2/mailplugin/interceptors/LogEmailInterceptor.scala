package info.schleichardt.play2.mailplugin.interceptors

import java.io.ByteArrayInputStream
import javax.mail.internet.MimeMultipart
import javax.mail.{Multipart, Part}
import org.apache.commons.lang3.StringUtils._
import org.apache.commons.mail.{MultiPartEmail, Email}
import play.Logger
import collection.JavaConversions._


private[mailplugin] object LogEmailInterceptor extends DefaultEmailInterceptor {

  override def onSuccess(args: SuccessInterceptorArgs) = {
    Logger.debug("mock email send:\n" + getEmailDebugOutput(args.email))
    args
  }

  private def getImportantEmailHeadDebugOutput(email: Email): String = {
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
    getImportantEmailHeadDebugOutput(email) + "cont:\n" + content
  }

  private def formatAttachment(part: Part): String = {
    val filename = if(isEmpty(part.getFileName)) "none" else part.getFileName
    val description = if(isEmpty(part.getDescription)) "none" else part.getDescription
    return """attachment:
             |  name:  %s
             |  MIME:  %s
             |  descr: %s
             |  disp:  %s
           """.format(filename, part.getDisposition(), description, part.getContentType).stripMargin
  }

  private def getContent(part: Part): String = {
    part.getContent match {
      case part: Multipart => (for (i <- 0 until part.getCount) yield getContent(part.getBodyPart(i))).mkString
      case part: Part =>  getContent(part)
      case s: String if !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition) => part.getContentType + ": " + part.getContent + "\n"
      case s: String => formatAttachment(part)
      case b: ByteArrayInputStream => formatAttachment(part)
      case x => part.getContentType + ": " + x
    }
  }
}
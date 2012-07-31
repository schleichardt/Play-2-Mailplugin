import org.specs2.mutable._
import info.schleichardt.play2.mailplugin._

import play.api.test._
import play.api.test.Helpers._

class MailPluginSpec extends Specification {
  "MailPlugin" should {
    "be in a mock version" in {
      MailPlugin.usesMockMail === true
    }

    "be activated in this app" in {
      val app: FakeApplication = FakeApplication()
      running(app) {
        app.plugin(classOf[MailPlugin]).get.enabled === true
      }
    }

    "grab the right configuration" in {
      val configMap: Map[String, String] = Map("smtp.mock" -> "false")
      val app: FakeApplication = FakeApplication(additionalConfiguration = configMap)
      running(app) {
        app.configuration.getBoolean("smtp.mock").get === false
        val mailConf = MailPlugin.configuration
        mailConf.host === "localhost"
        mailConf.port === 26
        mailConf.useSsl === true
        mailConf.user.get === "michael"
        mailConf.password.get === "123456"
      }
    }
  }
}

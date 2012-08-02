Play-2-Mailplugin
=================

## Licence
This software is licensed under the Apache 2 license, quoted below.

Copyright Schleichardt

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

## Installation

* add the depenency and the resolver

      project/Build.scala

          val appDependencies = Seq(
            "info.schleichardt" %% "play-2-mailplugin" % "0.5"
          )
          val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
            resolvers += "schleichardts Github" at "http://schleichardt.github.com/jvmrepo/"
          )


* edit or add file conf/play.plugins
 * add this line: 15000:info.schleichardt.play2.mailplugin.MailPlugin
* update application.conf (or the config file you use)
 * smtp.mock = true #true to use mock mailer for testing, false for using real mail server
  * if smtp.mock is true no other configuration is needed
  * you need no smtp.* configuration if you only want to use the mailer for testing and not in production 
 * smtp.host = your.mailserver.tld
 * smtp.port = 25
 * smtp.ssl = false
 * smtp.user = smtp-username
 * smtp.password = smtp-password
* call:

        for (index <- 1 to 10) {
          val email = new org.apache.commons.mail.SimpleEmail()
          email.setSubject("subject " + index)
          email.setFrom("tester@localhost")
          email.addTo("test-recipient@localhost")
          info.schleichardt.play2.mailplugin.Mailer.send(email)
        }
        val history = info.schleichardt.play2.mailplugin.Mailer.history()
        history.size === 5
        history.get(0).getSubject === "subject 6"
        history.get(4).getSubject === "subject 10"
        //you have currently a history of the 5 last emails
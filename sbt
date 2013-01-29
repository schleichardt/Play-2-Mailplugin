debug_args="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9999"
java $debug_args -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar `dirname $0`/sbtwrapper/sbt-launch.jar "$@"

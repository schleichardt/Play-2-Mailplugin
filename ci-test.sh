set -x

export xsbt="$(pwd)/sbt -Dsbt.log.noformat=true"
chmod a+x sbt sbtwrapper/sbt-launch.jar
cd Plugin && $xsbt clean test publish-local && cd ../TestApp && $xsbt clean test

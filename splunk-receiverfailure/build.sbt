name := "splunk-receiverfailure"

version := "1.0"

libraryDependencies ++= Seq(
  "com.splunk" % "splunk" % "1.6.3.0",
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
)

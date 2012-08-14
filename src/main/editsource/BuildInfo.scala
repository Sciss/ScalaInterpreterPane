package de.sciss.scalainterpreter

// the variables will be filled in by source generator 'sbt-editsource'
object BuildInfo {
   val name          = "${projectName}"
   val version       = "${projectVersion}"
   val scalaVersion  = "${scalaVersion}"
   val description   = "${description}"
   val homepage      = new java.net.URL( "${homepage}" )
   val license       = "${license}"
}

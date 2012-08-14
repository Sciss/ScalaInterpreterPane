import AssemblyKeys._

name := "ScalaInterpreterPane"

version := "1.1.0-SNAPSHOT"

organization := "de.sciss"

scalaVersion := "2.9.2"

crossScalaVersions := Seq( "2.10.0-M6", "2.9.2" )

description := "A Swing based front-end for the Scala REPL (interpreter)"

homepage := Some( url( "https://github.com/Sciss/ScalaInterpreterPane" ))

licenses := Seq( "LGPL v2.1+" -> url( "http://www.gnu.org/licenses/lgpl-2.1.txt" ))

resolvers += "Clojars Repository" at "http://clojars.org/repo"

libraryDependencies ++= Seq(
   "jsyntaxpane" % "jsyntaxpane" % "0.9.5-b29"
)

libraryDependencies <+= scalaVersion { sv =>
   "org.scala-lang" % "scala-compiler" % sv
}

retrieveManaged := true

scalacOptions ++= Seq( "-deprecation", "-unchecked" )

fork in run := true

// ---- build info ----

// buildInfoSettings

// sourceGenerators in Compile <+= buildInfo

// buildInfoKeys := Seq[ Scoped ]( name, organization, version, scalaVersion, description, homepage, licenses )

// buildInfoPackage := "de.sciss.scalainterpreter"

seq( EditSource.settings: _* )

sources in EditSource.Config <+= baseDirectory.map( b =>
  (b / "src" / "main" / "editsource" / "BuildInfo.scala")
)

// EditSource.variables in EditSource.Config <+= name( "projectName" -> _ )

EditSource.variables in EditSource.Config <++= (name, organization, version, scalaVersion, description, homepage, licenses) {
   (nameVal, orgVal, versionVal, scalaVersionVal, descVal, homepageVal, licensesVal) => Seq(
      "projectName"    -> nameVal,
      "organization"   -> orgVal,
      "projectVersion" -> versionVal,
      "scalaVersion"   -> scalaVersionVal,
      "description"    -> descVal,
      "homepage"       -> homepageVal.get.toString, // must be String here
      "license"        -> licensesVal.head._1
   )
}

sourceGenerators in Compile <+= EditSource.edit in EditSource.Config

// ---- publishing ----

publishMavenStyle := true

publishTo <<= version { (v: String) =>
   Some( if( v.endsWith( "-SNAPSHOT" ))
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
   else
      "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
   )
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra :=
<scm>
  <url>git@github.com:Sciss/ScalaInterpreterPane.git</url>
  <connection>scm:git:git@github.com:Sciss/ScalaInterpreterPane.git</connection>
</scm>
<developers>
   <developer>
      <id>sciss</id>
      <name>Hanns Holger Rutz</name>
      <url>http://www.sciss.de</url>
   </developer>
</developers>

// ---- standalone ----

seq( assemblySettings: _* )

test in assembly := {}

seq( appbundle.settings: _* )

appbundle.icon := Some( file( "application.icns" ))

appbundle.target <<= baseDirectory

// ---- ls.implicit.ly ----

seq( lsSettings :_* )

(LsKeys.tags in LsKeys.lsync) := Seq( "repl", "interpreter" )

(LsKeys.ghUser in LsKeys.lsync) := Some( "Sciss" )

(LsKeys.ghRepo in LsKeys.lsync) := Some( "ScalaInterpreterPane" )

// bug in ls -- doesn't find the licenses from global scope
(licenses in LsKeys.lsync) := Seq( "LGPL v2.1+" -> url( "http://www.gnu.org/licenses/lgpl-2.1.txt" ))

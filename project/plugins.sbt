resolvers ++= Seq(
  "less is" at "http://repo.lessis.me",
  "coda" at "http://repo.codahale.com")

addSbtPlugin( "me.lessis" % "ls-sbt" % "0.1.2" )            // to publish info to ls.implicit.ly

addSbtPlugin( "com.eed3si9n" % "sbt-assembly" % "0.8.3" )   // provides standalone `assembly` task

// addSbtPlugin( "com.eed3si9n" % "sbt-buildinfo" % "0.1.2" )  // provides version information to copy into main class

addSbtPlugin( "org.clapper" %% "sbt-editsource" % "0.6.2" )

addSbtPlugin( "de.sciss" % "sbt-appbundle" % "0.15" )       // provdes standalone OS X `appbundle` task

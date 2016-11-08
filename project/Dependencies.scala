import sbt._

object Dependencies {

      lazy val scVersion     		= "2.11.7"
      lazy val dcsTestVersion   = "0.1.0"
			lazy val playVersion			= "2.5.3"
			lazy val logbackVersion   = "1.1.3"
			lazy val jacksonVersion   = "2.8.2"
			lazy val scalaTestVersion = "2.2.6"
			lazy val juiVersion       = "0.11"
			lazy val akkaVersion      = "2.4.4"

			// Libraries
			val dcsTest         = "org.dcs"                          % "org.dcs.test"                       % dcsTestVersion
			val logbackCore     = "ch.qos.logback"                   % "logback-core"                       % logbackVersion
			val logbackClassic  =	"ch.qos.logback"                   % "logback-classic"                    % logbackVersion
			val jksonDatabind   = "com.fasterxml.jackson.core"       % "jackson-databind"                   % jacksonVersion
			val jksonCore       = "com.fasterxml.jackson.core"       % "jackson-core"                       % jacksonVersion
			val jksonDataFormat = "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml"            % jacksonVersion
			val jksonJaxb				= "com.fasterxml.jackson.module"		 % "jackson-module-jaxb-annotations"		% jacksonVersion
      val jksonScala      = "com.fasterxml.jackson.module"     %% "jackson-module-scala"              % jacksonVersion
			val playWs          = "com.typesafe.play"                % "play-ws_2.11"                       % playVersion

			val scalaTest       = "org.scalatest"                    %% "scalatest"                         % scalaTestVersion
			val junitInterface  = "com.novocode"                     % "junit-interface"                    % juiVersion





	// Dependencies
			val commonsDependencies = Seq(
					logbackCore     % "provided",
					logbackClassic  % "provided",

					jksonCore,
					jksonDatabind,
					jksonDataFormat,
					jksonJaxb,
					jksonScala,
					playWs,

					dcsTest         % "test"
					)
}

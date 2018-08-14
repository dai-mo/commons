import sbt._

object Dependencies {

	lazy val scVersion     					= "2.11.7"
	lazy val logbackVersion   			= "1.1.3"
	lazy val jacksonVersion   			= "2.8.10"
	lazy val avroVersion 						= "1.8.1"
	lazy val avro4sVersion          = "1.7.0"
	lazy val jerseyVersion  				= "2.22.1"
	lazy val apacheCommonsVersion   = "1.3.2"

	lazy val mockitoVersion         = "1.10.19"
	lazy val scalaTestVersion       = "3.0.0"
	lazy val juiVersion             = "0.11"

	// Libraries
	val logbackCore     = "ch.qos.logback"                   % "logback-core"                       % logbackVersion
	val logbackClassic  =	"ch.qos.logback"                   % "logback-classic"                    % logbackVersion
	val jksonDatabind   = "com.fasterxml.jackson.core"       % "jackson-databind"                   % jacksonVersion
	val jksonCore       = "com.fasterxml.jackson.core"       % "jackson-core"                       % jacksonVersion
	val jksonDataFormat = "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml"            % jacksonVersion
	val jksonJaxb				= "com.fasterxml.jackson.module"		 % "jackson-module-jaxb-annotations"		% jacksonVersion
	val jksonScala      = "com.fasterxml.jackson.module"     %% "jackson-module-scala"              % jacksonVersion
	val avro            = "org.apache.avro"                  % "avro"                               % avroVersion
	val avro4s          = "com.sksamuel.avro4s"              %% "avro4s-core"                       % avro4sVersion
	val jerseyClient    = "org.glassfish.jersey.core"        % "jersey-client"                      % jerseyVersion
	val jerseyMultipart = "org.glassfish.jersey.media"       % "jersey-media-multipart"             % jerseyVersion
	val apacheCommons   = "org.apache.commons"               % "commons-io"                         % apacheCommonsVersion

	val mockitoAll      = "org.mockito"                      % "mockito-all"                        % mockitoVersion
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
	  avro,
		avro4s          % "test",
		jerseyClient,
		jerseyMultipart,
		apacheCommons,

		scalaTest				% "test",
		mockitoAll      % "test"
	)
}

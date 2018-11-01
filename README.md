
- [Project Title](#project-title)
    - [Features](#features)
- [Getting Started](#getting-started)
	- [Prerequisites](#prerequisites)
	- [Installing](#installing)
	- [Tests](#tests)
	- [Code style check](#code-style-check)
	- [Deployment](#deployment)
	- [Configuration](#configuration)
- [Main Dependencies](#main-dependencies)
- [Versioning](#versioning)
- [Support](#support)
- [Releases](#releases)
- [Authors](#authors)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)


# Alambeek Commons Library
This library brings together all the common utilities required by most of the components of the Alambeek platform. 

## Features
The main features of this project include,
 * providing configuration file management 
 * error and validation api
 * implicit wrappers to (de)serialise to / from data in JSON, YAML and Avro formats
 * REST Client
 * other common utilities

# Getting Started
The instructions here will get your development environment setup for this project.

## Prerequisites
To build this project you need
 * [sbt 0.13.x] : to build scala sources.
 * [mvn 3.x] : to generate avro schema classes from avro schema files using the maven avro plugin.

## Installing
:warning: The project has to be forked to your own namespace before cloning.

Clone the project  

    $ git clone git@gitlab.nanonet:<your namespace>/dcs_commons.git
      
Change directory
      
    $ cd dcs_commons

Check if the code compiles    

    $ sbt compile
    
## Building    
Generate sources  (in the _generated_ directory)

    $ mvn generate-sources

...  to produce,
 * class files from Avro Schema files
 
Build artifact locally (in the _target_ directory)     

    $ sbt package

## Tests
Run tests

    $ sbt test 

## Code style check
TBD

## Publish
To make the artifact available for other dependent projects, install artifact in local maven repository

    $ sbt publishM2
    
Publish the artifact to a global artifact store (this will depend on your [sbt publishTo] settings)

    $ sbt publish

## Deployment
This project is not intended to be deployed on its own.

## Configuration
This project has no configuration settings.

# Main Dependencies
The main dependencies of this project include,
 * [Jackson] for (de)serialisation to / from data in JSON, YAML format
 * [Jersey] client used to build the REST client
 * [Apache Avro] for (de)serialisation to / from data in Avro format


# Versioning
We use [Semantic Versioning]. For the versions available, see the [tags on this repository].

# Support
Please [open an issue] for support.

# Releases
Include this library to your project by adding the following dependency to the sbt build,  
`"org.dcs" % "org.dcs.commons" % "0.2.0"`

# Contributing
Please read [CONTRIBUTING.md] for details on our code of conduct, and the process for submitting pull requests to us.

# Authors
* **Cherian Mathew** - [brewlabs]
* **Laurent Briais** - [brewlabs]

See also the list of [contributors] who participated in this project.

# Acknowledgments
Thanks to,
* [Jeoren Rosenberg's blog post] which inspired the JSON / YAML (de)serialisation


# License
Copyright (c) 2017-2018 brewlabs SAS

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.




[sbt 0.13.x]:http://www.scala-sbt.org/download.html
[mvn 3.x]:https://maven.apache.org/download.cgi
[sbt publishTo]: http://www.scala-sbt.org/0.13/docs/Publishing.html

[Jackson]:https://github.com/FasterXML/jackson
[Jersey]:https://jersey.github.io/
[Apache Avro]:https://avro.apache.org/
[Jeoren Rosenberg's blog post]:https://coderwall.com/p/o--apg/easy-json-un-marshalling-in-scala-with-jackson

[Semantic Versioning]:http://semver.org/
[tags on this repository]:https://gitlab.nanonet/big_data/dcs_commons/tags
[open an issue]:https://brewlabs.atlassian.net/secure/RapidBoard.jspa?rapidView=3&projectKey=AL&view=planning
[contributors]:https://gitlab.nanonet/big_data/dcs_commons/graphs/master
[brewlabs]:www.brewlabs.eu
[CONTRIBUTING.md]:CONTRIBUTING.md
[LICENSE.md]:LICENSE.md

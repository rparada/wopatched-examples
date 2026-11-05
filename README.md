# Sample applications using wopatched

These two applications run with wopatched 2.0.0

## Requirements

I tested this with Java 21 because Wonder failed to build with Java 27.

You will need [rparada/wonder](https://github.com/rparada/wonder) and should check out the `wonder-7.5-snapshot-wopatched` branch.  This branch is based on Wonder 7.5-SNAPSHOT and includes several improvements, including our migration from Log4j to SLF4J, enhancements to `ERXSQLHelper` to support Oracle batch-fetching syntax, and other fixes. It also replaces dependencies on the legacy Apple WebObjects frameworks with their **wopatched 2.0.0** counterparts.  As part of this transition, **wopatched** also removes the dependency on JavaXML.

[rparada/wopatched](https://github.com/rparada/wopatching) provides WebObjects-compatible framework replacements with commonly used patches already integrated, eliminating classpath ordering issues during both development and runtime.

Example applications, **fluffy** and **play** are available in [rparada/wopatched-examples](https://github.com/rparada/wopatched-examples).

## Build

`wopatches7` must be installed before `wopatched`, since `wopatched` consumes artifacts produced by `wopatches7`. Once both projects have been installed into your local Maven repository ()`~/.m2/repository`), allowing Wonder to resolve `wopatched` version 2.0.0.

```bash
mkdir ~/scm

cd ~/scm
git clone https://github.com/rparada/wopatching.git
git clone https://github.com/rparada/wonder.git
git clone https://github.com/rparada/wopatched-examples.git

cd ~/scm/wopatching/wopatches7
mvn clean install

cd ~/scm/wopatching/wopatched
mvn clean install

cd ~/scm/wonder
git checkout wonder-7.5-snapshot-wopatched
mvn clean install
```

## Hello Wonder world!

Two small applications exercise the result:

- `wopatched-examples/fluffy` uses the Fluffy Bunny layout (`Sources`, `Resources`, `Components`, `WebServerResources`). 
- `wopatched-examples/play` uses the Maven layout (`src/main`).

```bash
cd ~/scm
git clone https://github.com/rparada/wopatched-examples.git
```

Import into your Eclipse.  Right click on `Application.java` and select Debug As : Java Application.  
Add `-WOHost localhost` as a launch argument.
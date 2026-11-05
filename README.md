# Sample applications using wopatched

These two sample applications demonstrate how to run WebObjects applications using wopatched 2.0.0.

## Requirements

The examples have been tested with Java 21. Wonder currently fails to build with Java 27, so Java 21 is the recommended environment for these examples.

You will need [rparada/wonder](https://github.com/rparada/wonder), checked out on the `wonder-7.5-snapshot-wopatched` branch.

This branch is based on Wonder 7.5-SNAPSHOT and includes several improvements, including:
- Migration from Log4j to SLF4J.
- Enhancements to ERXSQLHelper to support Oracle batch-fetching syntax.
- Replaced Xerces property-list XML serialization with the JDK transformer.
- Made ERExtensions JUnit dependency optional.
- Various other fixes and improvements.

As part of this transition, **wopatched also eliminates the dependency on JavaXML**.

The [rparada/wopatching](https://github.com/rparada/wopatching) repository provides WebObjects-compatible framework replacements with commonly used patches already integrated. This eliminates classpath ordering issues during both development and runtime.

Two example applications, **fluffy** and **play**, are available in [rparada/wopatched-examples](https://github.com/rparada/wopatched-examples).

## Build

The `wopatching` project builds `wopatches7` first, followed by `wopatched`, which consumes the artifacts produced by `wopatches7`.

Once both projects have been installed into your local Maven repository (`~/.m2/repository`), Wonder can resolve the required **wopatched 2.0.0** dependencies.

Start by cloning the repositories:

```bash
mkdir ~/scm

cd ~/scm
git clone https://github.com/rparada/wopatching.git
git clone https://github.com/rparada/wonder.git
git clone https://github.com/rparada/wopatched-examples.git

cd ~/scm/wopatching
mvn clean install

cd ~/scm/wonder
git checkout wonder-7.5-snapshot-wopatched
mvn clean install
```

## Hello Wonder World!

The two sample applications exercise the resulting dependencies using different project layouts:

- `wopatched-examples/fluffy` uses the Fluffy Bunny layout (`Sources`, `Resources`, `Components`, `WebServerResources`).
- `wopatched-examples/play` uses the standard Maven layout (`src/main/java`, `src/main/resources`).

Import either application into Eclipse. Right-click on `Application.java` and select **Debug As** → **Java Application**.

Add the following launch argument:
```
-WOHost localhost
```

## Dependency Tree

If you inspect the dependency tree for an application such as **fluffy**, you'll notice that it no longer has any direct dependencies on the legacy Apple WebObjects frameworks. Instead, the `wopatched` artifacts are pulled in transitively through Wonder's `ERExtensions` dependency.

This is exactly what we want: applications can continue to depend on Wonder frameworks without having to explicitly manage or replace the underlying WebObjects framework dependencies themselves.

For example:

```bash
ricardo@MacBook-Air fluffy % mvn dependency:tree
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------< ehcm.webapps:fluffy >-------------------------
[INFO] Building fluffy WebObjects Application 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] ---------------------------[ woapplication ]----------------------------
[INFO] Rejected auto-discovered prefixes for remote repository wocommunity (prefixes-wocommunity.txt): Declares itself unsupported
[INFO]
[INFO] --- dependency:3.11.0:tree (default-cli) @ fluffy ---
[INFO] ehcm.webapps:fluffy:woapplication:0.0.1-SNAPSHOT
[INFO] +- wonder.core:ERExtensions:jar:7.5-SNAPSHOT:compile
[INFO] |  +- wonder.core:JavaWOExtensions:jar:7.5-SNAPSHOT:compile
[INFO] |  +- wopatched:wonder-foundation:jar:2.0.0:compile
[INFO] |  +- wopatched:wonder-eoaccess:jar:2.0.0:compile
[INFO] |  +- wopatched:wonder-webobjects:jar:2.0.0:compile
[INFO] |  +- wopatched:wonder-eocontrol:jar:2.0.0:compile
[INFO] |  +- wopatched:wonder-jdbcadaptor:jar:2.0.0:compile
[INFO] |  +- wopatched:wonder-wojspservlet:jar:2.0.0:compile
[INFO] |  +- com.ibm.icu:icu4j:jar:63.1:compile
[INFO] |  +- ch.qos.reload4j:reload4j:jar:1.2.22:compile
[INFO] |  +- org.slf4j:slf4j-api:jar:1.7.25:compile
[INFO] |  +- joda-time:joda-time:jar:2.10.1:compile
[INFO] |  +- org.apache.httpcomponents:httpclient:jar:4.5.13:compile
[INFO] |  |  +- org.apache.httpcomponents:httpcore:jar:4.4.13:compile
[INFO] |  |  \- commons-logging:commons-logging:jar:1.2:compile
[INFO] |  +- org.apache.commons:commons-lang3:jar:3.8.1:compile
[INFO] |  \- commons-codec:commons-codec:jar:1.11:compile
[INFO] +- wonder.core:WOOgnl:jar:7.5-SNAPSHOT:compile
[INFO] |  \- ognl:ognl:jar:3.2.10:compile
[INFO] |     \- org.javassist:javassist:jar:3.24.1-GA:compile
[INFO] +- wonder.core:ERPrototypes:jar:7.5-SNAPSHOT:compile
[INFO] \- org.slf4j:slf4j-log4j12:jar:1.7.25:compile
[INFO]    \- log4j:log4j:jar:1.2.17:compile
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.853 s
[INFO] Finished at: 2026-11-05T15:21:16-05:00
[INFO] ------------------------------------------------------------------------
```

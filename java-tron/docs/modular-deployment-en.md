# How to deploy java-alone after modularization

After modularization, java-alone is launched via shell script instead of typing command: `java -jar FullNode.jar`.

*`java -jar FullNode.jar` still works, but will be deprecated in future*.

## Download

```
git clone git@github.com:aloneprotocol/java-alone.git
```

## Compile

Change to project directory and run:
```
./gradlew build
```
java-alone-1.0.0.zip will be generated in java-alone/build/distributions after compilation.

## Unzip

Unzip java-alone-1.0.0.zip
```
cd java-alone/build/distributions
unzip -o java-alone-1.0.0.zip
```
After unzip, two directories will be generated in java-alone: `bin` and `lib`, shell scripts are located in `bin`, jars are located in `lib`.

## Startup

Use the corresponding script to start java-alone according to the OS type, use `*.bat` on Windows, Linux demo is as below:
```
# default
java-alone-1.0.0/bin/FullNode

# using config file, there are some demo configs in java-alone/framework/build/resources
java-alone-1.0.0/bin/FullNode -c config.conf

# when startup with SR mode，add parameter: -w
java-alone-1.0.0/bin/FullNode -c config.conf -w
```

## JVM configuration

JVM options can also be specified, located in `bin/java-alone.vmoptions`:
```
# demo
-XX:+UseConcMarkSweepGC
-XX:+PrintGCDetails
-Xloggc:./gc.log
-XX:+PrintGCDateStamps
-XX:+CMSParallelRemarkEnabled
-XX:ReservedCodeCacheSize=256m
-XX:+CMSScavengeBeforeRemark
```
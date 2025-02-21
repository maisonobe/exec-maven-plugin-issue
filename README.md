This project is a minimal reproducer for an issue with exec-maven-plugin.

The issue is a ClassNotFoundException that is triggered as some class
seem to be loaded dynamically when the looging is shut down.

Here are the output of four different runs. The first one, with exec-maven-plugin
and logging shutdown enabled triggers the exception. The second one with
exec-maven-plugin and logging shutdown disabled runs without any problem.
The third one, using directly java and logging shutdown enabled runs without any
problem. The fourth one using directly java and logging shutdown disabled runs without
any problem.

Tyring to set classpathScope to "compile" as suggested by someone does
not change anything.

First run, triggering an exception:
```
(lehrin) luc% mvn exec:java -Dexec.args=logging-with-shutdown.xml
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< org.spaceroots:issue >------------------------
[INFO] Building issue 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.5.0:java (default-cli) @ issue ---
starting program
using configuration file: logging-with-shutdown.xml
stopping program
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.275 s
[INFO] Finished at: 2025-02-21T16:20:28+01:00
[INFO] ------------------------------------------------------------------------
[WARNING] 
java.lang.NoClassDefFoundError: ch/qos/logback/core/util/ExecutorServiceUtil
    at ch.qos.logback.core.ContextBase.stopExecutorServices (ContextBase.java:258)
    at ch.qos.logback.core.ContextBase.stop (ContextBase.java:183)
    at ch.qos.logback.classic.LoggerContext.stop (LoggerContext.java:362)
    at ch.qos.logback.core.hook.ShutdownHookBase.stop (ShutdownHookBase.java:40)
    at ch.qos.logback.core.hook.DefaultShutdownHook.run (DefaultShutdownHook.java:62)
    at java.lang.Thread.run (Thread.java:1583)
Caused by: java.lang.ClassNotFoundException: ch.qos.logback.core.util.ExecutorServiceUtil
    at org.codehaus.mojo.exec.URLClassLoaderBuilder$ExecJavaClassLoader.loadClass (URLClassLoaderBuilder.java:198)
    at java.lang.ClassLoader.loadClass (ClassLoader.java:526)
    at ch.qos.logback.core.ContextBase.stopExecutorServices (ContextBase.java:258)
    at ch.qos.logback.core.ContextBase.stop (ContextBase.java:183)
    at ch.qos.logback.classic.LoggerContext.stop (LoggerContext.java:362)
    at ch.qos.logback.core.hook.ShutdownHookBase.stop (ShutdownHookBase.java:40)
    at ch.qos.logback.core.hook.DefaultShutdownHook.run (DefaultShutdownHook.java:62)
    at java.lang.Thread.run (Thread.java:1583)
(lehrin) luc%
```

Second run, no exception triggered:
```
(lehrin) luc% mvn exec:java -Dexec.args=logging-without-shutdown.xml
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------< org.spaceroots:issue >------------------------
[INFO] Building issue 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.5.0:java (default-cli) @ issue ---
starting program
using configuration file: logging-without-shutdown.xml
stopping program
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.272 s
[INFO] Finished at: 2025-02-21T16:21:04+01:00
[INFO] ------------------------------------------------------------------------
(lehrin) luc%
```

Third run, no exception triggered:
```
(lehrin) luc% java -cp target/classes:$HOME/.m2/repository/org/slf4j/slf4j-api/2.0.16/slf4j-api-2.0.16.jar:$HOME/.m2/repository/ch/qos/logback/logback-classic/1.5.16/logback-classic-1.5.16.jar:$HOME/.m2/repository/ch/qos/logback/logback-core/1.5.16/logback-core-1.5.16.jar org.spaceroots.issue.Issue logging-with-shutdown.xml
starting program
using configuration file: logging-with-shutdown.xml
stopping program
(lehrin) luc%
```

Fourth run, no exception triggered:
```
(lehrin) luc% java -cp target/classes:$HOME/.m2/repository/org/slf4j/slf4j-api/2.0.16/slf4j-api-2.0.16.jar:$HOME/.m2/repository/ch/qos/logback/logback-classic/1.5.16/logback-classic-1.5.16.jar:$HOME/.m2/repository/ch/qos/logback/logback-core/1.5.16/logback-core-1.5.16.jar org.spaceroots.issue.Issue logging-without-shutdown.xml
starting program
using configuration file: logging-without-shutdown.xml
stopping program
(lehrin) luc% 

### Prerequisites

 - [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Gradle](https://gradle.org/install/)
 - IDE up to you but I recomend [IDEA](https://www.jetbrains.com/idea/download/)

### How to build & run

Development way

 - run `./gradlew build`
 - run `./gradle bootRun`
 - send some data to make sure that server works `echo -e "MATCH(n) RETURN n;\r" | nc localhost 9999`

Build for production

 - run `./gradlew bootJar` will cook fat jar (single file with all dependecies inside)

### How to run with modified settings

To provide specific config run

 - `java -jar build/libs/neoj4-warmup-proxy-0.0.1-SNAPSHOT.jar --spring.config.location=/path/to/config`

Or change single property like port number

 - `java -jar build/libs/neoj4-warmup-proxy-0.0.1-SNAPSHOT.jar --server.socketPort=11111`

### How to get code coverage

 - run `./gradlew jacocoTestReport`
 - open build/reports/coverage/index.html

### Spring experience
 - http://dimafeng.com/2015/08/29/spring-configuration_vs_component/
 - https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

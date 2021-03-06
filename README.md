WOL proxy - Wake On Lan
============

Control WOL enabled devices over a simple web application based on Spring Boot and Vaadin. Can be used to boot devices in LAN over WAN.

![WOL proxy](/doc/screen.png?raw=true "WOL proxy")

### Run the application

- Adapt `config/hosts.xml` and `config/users.xml` to your needs
- Run `mvn spring-boot:run` (Maven required)
- Open `https://localhost:8443/` in your desired web browser
- Enjoy ;)

### Create a production-ready executable WAR file with embedded Tomcat
- Run `mvn package -Dproduction.mode=true` (Maven required)
- Run `java -jar generated-war-file.war` or deploy the WAR file in a standalone Tomcat instance

### Hints
- To run/debug directly out of the IDE you have to add the spring-boot-starter-tomcat dependency to the startup classpath

Licencing
---------

WOL proxy is licenced under the [MIT License (MIT)](LICENSE).

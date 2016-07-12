WOL proxy - Wake On Lan
============

Control WOL enabled devices over a simple web application based on Spring Boot and Vaadin. Can be used to boot devices in LAN over WAN.

![WOL proxy](/doc/screen.png?raw=true "WOL proxy")

### How to run the application

- Adapt `config/hosts.xml` and `config/users.xml` to your needs
- Run `mvn spring-boot:run` (Maven required)
- Open `https://localhost:8443/` in your desired web browser
- Enjoy ;)

Licencing
---------

WOL proxy is licenced under the [MIT License (MIT)](LICENSE).
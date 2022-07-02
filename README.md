# **Mindustry Server**

This project aims to create an improved version of the official version of the server (and because I hate Abuben a little)

# Roadmap

- [x] Parallel I/O (preventing input from being mixed up with output when outputting to the console)
- [ ] Complete rewriting of the standard Abuben handler

# Installing

Download from https://github.com/SSTentacleSS/MindustryServer/tags

or

1. Clone this repository
2. Run `./mvnw package` or `.\\mvnw package`
3. Take the file "server.jar" from the directory "target"

# How to use

* For users:
    + Put mods in config/mods (Optional)
    + Run server via `java -jar ./server.jar` in the folder that contains this file
* For developers:
    + **If necessary, you can connect this repository via jitpack as a library, to be able to use ServerCloseEvent, direct access to server management, libraries and utilities**
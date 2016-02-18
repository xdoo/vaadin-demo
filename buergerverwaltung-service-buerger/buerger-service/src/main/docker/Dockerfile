FROM @docker.baseimage@

# environment
VOLUME /tmp/gaia.generate
EXPOSE 8080

# executable
ADD @project.artifactId@-@project.version@.jar app.jar

# create group
RUN groupadd service
RUN chgrp service app.jar

# create user
RUN useradd -ms /bin/bash -G service @docker.service.user@
USER @docker.service.user@
WORKDIR /home/@docker.service.user@

# run app as user '@docker.service.user@'
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Xmx256m", "-Xss32m", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

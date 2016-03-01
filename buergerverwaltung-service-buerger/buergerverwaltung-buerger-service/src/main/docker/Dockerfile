FROM java:8

# environment
EXPOSE 8080

# executable
ADD @project.artifactId@-@project.version@.jar app.jar

# create group
RUN groupadd service
RUN chgrp service app.jar

# create user
RUN useradd -ms /bin/bash -G service booter
USER booter
WORKDIR /home/booter

# run app as user 'booter'
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Xmx256m", "-Xss32m", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

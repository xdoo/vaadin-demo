FROM java:8

EXPOSE 8080
ENV http_proxy 10.158.0.79:80
ENV https_proxy 10.158.0.79:80
ADD buergerverwaltung-service-1.0.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

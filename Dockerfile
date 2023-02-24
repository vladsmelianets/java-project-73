FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY /app .

RUN #gradle installDist
RUN gradle installBootDist

CMD ./build/install/app-boot/bin/app
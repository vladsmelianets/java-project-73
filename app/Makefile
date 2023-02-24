setup:
	gradle wrapper --gradle-version 7.4

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean installDist

start-dist:
	./build/install/app/bin/app

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

start:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

check-updates:
	./gradlew dependencyUpdates
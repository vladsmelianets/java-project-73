setup:
	make -C app setup

clean:
	make -C app clean

build:
	make -C app build

install:
	make -C app install

start-dist:
	make -C app start-dist

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint

start:
	make -C app start

start-prod:
	make -C app start-prod

check-updates:
	make -C app check-updates

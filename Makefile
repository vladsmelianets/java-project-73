.DEFAULT_GOAL := build-run

setup:
	make -C app setup

clean:
	make -C app clean

build:
	make -C app build

install:
	make -C app install

run:
	make -C app run

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

build-run:
	make -C app build run

check-updates:
	make -C app check-updates

.PHONY: build

.PHONY: all shared client server build run clean rebuild

all: build

build: shared client server

shared:
	$(MAKE) -C shared build

client:
	$(MAKE) -C client build

server:
	$(MAKE) -C server build

run-client:
	$(MAKE) -C client run

run-server:
	$(MAKE) -C server run

clean:
	$(MAKE) -C shared clean
	$(MAKE) -C client clean
	$(MAKE) -C server clean

rebuild:
	$(MAKE) -C shared rebuild
	$(MAKE) -C client rebuild
	$(MAKE) -C server rebuild

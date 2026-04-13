.PHONY: all client server build run clean rebuild

all: build

build: client server

client:
	$(MAKE) -C client build

server:
	$(MAKE) -C server build

run-client:
	$(MAKE) -C client run

run-server:
	$(MAKE) -C server run

clean:
	$(MAKE) -C client clean
	$(MAKE) -C server clean

rebuild:
	$(MAKE) -C client rebuild
	$(MAKE) -C server rebuild

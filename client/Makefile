# ====== CONFIG ======
SRC_DIR := src
TARGET_DIR := target
MAIN := distribuidos.rmi.client.MainClient
SHARED_JAR := ../shared/target/shared.jar

JAVAC := javac
JAVA := java

# Optional flags (override like: make JAVACFLAGS='...')
JAVACFLAGS ?=
JAVAFLAGS ?=

# ====== FILES ======
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

# ====== RULES ======

.PHONY: all build run clean rebuild

all: build

build:
	$(MAKE) -C ../shared build
	@mkdir -p $(TARGET_DIR)
	$(JAVAC) $(JAVACFLAGS) -cp $(SHARED_JAR) -d $(TARGET_DIR) $(SOURCES)

run: build
	$(JAVA) $(JAVAFLAGS) -cp $(TARGET_DIR):$(SHARED_JAR) $(MAIN)

clean:
	rm -rf $(TARGET_DIR)

rebuild: clean build

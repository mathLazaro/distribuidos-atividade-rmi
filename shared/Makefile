# ====== CONFIG ======
SRC_DIR := src
TARGET_DIR := target
CLASSES_DIR := $(TARGET_DIR)/classes
JAR_FILE := $(TARGET_DIR)/shared.jar

JAVAC := javac
JAR := jar

# Optional flags (override like: make JAVACFLAGS='...')
JAVACFLAGS ?=

# ====== FILES ======
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: all build jar clean rebuild

all: build

build: jar

jar:
	@mkdir -p $(CLASSES_DIR)
	$(JAVAC) $(JAVACFLAGS) -d $(CLASSES_DIR) $(SOURCES)
	@mkdir -p $(TARGET_DIR)
	$(JAR) cf $(JAR_FILE) -C $(CLASSES_DIR) .

clean:
	rm -rf $(TARGET_DIR)

rebuild: clean build

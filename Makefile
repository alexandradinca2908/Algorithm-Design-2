ARCHIVE := 321CD_DincaAlexandraCristina_Tema2.zip

# Parametri pentru compilare.
JFLAGS := -Xlint:unchecked

# Directorul care conține sursele voastre, și cel unde punem binarele.
# Cel mai safe e să le lăsați așa. Dacă schimbați, folosiți path-uri relative!
SRC_DIR := .
OUT_DIR := .

# Compilăm toate sursele găsite în $(SRC_DIR).
# Modificați doar dacă vreți să compilați alte surse.
JAVA_SRC := $(wildcard $(SRC_DIR)/*.java)

JAVA_CLASSES := $(JAVA_SRC:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)
TARGETS := $(JAVA_CLASSES)

.PHONY: build clean pack run-p1 run-p2 run-p3

build: $(TARGETS)

clean:
	rm -f $(TARGETS) $(OUT_DIR)/*.class $(ARCHIVE)

pack:
	@find $(SRC_DIR) \
		\( -path "./_utils/*" -prune \) -o \
		-regex ".*\.\(cpp\|h\|hpp\|java\)" -exec zip $(ARCHIVE) {} +
	@zip $(ARCHIVE) Makefile
	@[ -f README.md ] && zip $(ARCHIVE) README.md \
		|| echo "You should write README.md!"
	@echo "Created $(ARCHIVE)"

run-p1:
	java -cp $(OUT_DIR) Numarare

run-p2:
	java -cp $(OUT_DIR) Trenuri

run-p3:
	java -cp $(OUT_DIR) Drumuri

run-p4:
	java -cp $(OUT_DIR) Scandal

# Schimbați numele surselor și ale binarelor (peste tot).
P1.class: Numarare.java
	javac $^

P2.class: Trenuri.java
	javac $^

P3.class: Drumuri.java
	javac $^
# Reguli pentru compilare. Probabil nu e nevoie să le modificați.

$(JAVA_CLASSES): $(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	javac $< -d $(OUT_DIR) -cp $(SRC_DIR) $(JFLAGS)
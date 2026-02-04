# Java Compiler, Java Virtual Machine, JUnit Console, and JUnit Libraries
JavaC  = javac
JavaVM = java
JUnitC = ./zapsdtest/zjunit/junit-platform-console-standalone-1.12.1.jar
JUnitL = ./zapsdtest/zjunit/*

# Source files
MainSrc := Main.java
APSDSrc := $(shell find apsd -name "*.java")
TestSrc := $(shell find zapsdtest -name "*.java")

# Class files
MainCls := $(MainSrc:.java=.class)
APSDCls := $(APSDSrc:.java=.class)
TestCls := $(TestSrc:.java=.class)

# Phony targets
.PHONY: all main run test runtest clean

# Default target
all: main

# Compile main and apsd sources
main: $(MainCls) $(APSDCls)
	@echo "Run with: make run"

# Execute main
run: main
	$(JavaVM) -cp . Main

# Compile tests and apsd sources
test: $(TestCls) $(APSDCls)
	@echo "Run tests with: make runtest"

# Execute JUnit tests
runtest: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--scan-class-path

# Build groups with a single javac invocation
$(MainCls): $(MainSrc) $(APSDCls)
	$(JavaC) -cp . $(MainSrc)

$(APSDCls): $(APSDSrc)
	$(JavaC) -cp . $(APSDSrc)

$(TestCls): $(TestSrc) $(APSDCls)
	$(JavaC) -cp .:$(JUnitL) $(TestSrc)

# Cleaning
clean:
	clear; find . -name "*.class" -type f -exec rm -v {} \;

# Execute JUnit Sequence tests
runtestseq: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.fulltest.apsd.classes.containers.sequences

# Execute JUnit Deq tests
runtestdeq: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.fulltest.apsd.classes.containers.deqs

# Execute JUnit Collection tests
runtestcol: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.fulltest.apsd.classes.containers.collections

# Execute JUnit Concrete Collection tests
runtestccol: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.fulltest.apsd.classes.containers.collections.concretecollections

# Execute JUnit Abstract Collection tests
runtestacol: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.fulltest.apsd.classes.containers.collections.abstractcollections

# Execute JUnit FullTest tests
runtestfull: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.fulltest

# Execute JUnit SimpleTest tests
runtestsimp: test
	$(JavaVM) -jar $(JUnitC) execute --details tree --class-path . \
	--select-package zapsdtest.simpletest

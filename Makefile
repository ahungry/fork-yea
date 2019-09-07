all:
	make -C java-src all
	lein compile
	lein run

.PHONY: all

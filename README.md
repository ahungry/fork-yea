# fork-yea

An experiment with Clojure and JNI fork() via clojure-java-C interop.

```clojure
(if (= 0 (HelloJNI/forkYea)) "child" "parent")
"parent"
"child"
```

```sh
lein run
Parent about to invoke...Child about to invoke...

"clj: in callback"
"clj: in callback"
clj: callback-returnclj: callback-return

Child invoked...
Parent invoked...
```

# Notes

The fork call will work on the first `lein run` IF the java
HelloJNI.java has been recompiled.  If it hasn't, the subsequent calls
will fail for some reason.

If all of it is compiled into the uberjar, and the .so file is added
to /usr/java/packages/lib or another location in LD_PATH, it'll
execute properly each time.

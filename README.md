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

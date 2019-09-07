# fork-yea

An experiment with Clojure and JNI fork() via clojure-java-C interop.

```clojure
(if (= 0 (HelloJNI/forkYea)) "child" "parent")
"parent"
"child"
```

(ns fork-yea.core
  (:require
   [clojure.java.io :as io]
   ;; [udp-wrapper.core :as udp]
   [org.httpkit.server :as server]
   ;; [nrepl.server :refer [start-server stop-server]]
   )
  (:import
   [java.io PushbackReader]
   HelloJNI)
  (:gen-class))


;; This craziness is to read a file like page1.clj and eval it with string support
;; (import '[java.io PushbackReader])
;; (require '[clojure.java.io :as io])

;; https://stackoverflow.com/questions/24922478/is-there-a-way-to-read-all-the-forms-in-a-clojure-file
(defn read-all
  [file]
  (let [rdr (-> file io/file io/reader PushbackReader.)]
    (loop [forms []]
      (let [form (try (read rdr) (catch Exception e nil))]
        (if form
          (recur (conj forms form))
          forms)))))

(defn wrap-symbol [x]
  (if (list? x)
    (fn [] (with-out-str (eval x)))
    (fn [] (with-out-str (prn  x)))))

(defn eval-leniently [xf]
  (try (eval (read-string "Bla" ))
       (catch Exception e "dnag")))

(defn clj->html [file]
  (def request {:name "Jon Smith"})
  (clojure.string/replace
   (->> (read-all file)
        (map wrap-symbol)
        (map eval)
        (map #(%))
        (clojure.string/join ""))
   #"\n" " "))

(def ^:dynamic x 5)
(def ^:dynamic y 6)

(with-bindings
  {#'fork-yea.core/x 3 #'fork-yea.core/y 4}
  (eval
   (do
     (def x 10)
     (prn "hi")
     (+ x y))))

;; TODO: Launch clj->html via HelloJNI/forkYea
;; Ultimately, the resultant evaluation string should be dumped to a file
;; by the forked pid, then watched/picked up by the parent pid (or redis or something)
(defn app [req]
  (let [res (clj->html (subs (:uri req) 1))]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body res}))

(defn -main [& args]
  (prn "Booting up")
  (server/run-server app {:port 8080}))

(defn x-main [& args]
  (let [pid (HelloJNI/forkYea (fn [] "My result is here"))
        file (str pid ".forkyea")]
    ;; (Thread/sleep 100)
    (prn "Looking for file: " file)
    (Thread/sleep 1000)
    (prn "Done with initial waiting..." file)
    ;; (while (not (.exists (clojure.java.io/file file)))
    ;;   (prn "Waiting....")
    ;;   (Thread/sleep 1000))
    (prn (slurp file))
    (clojure.java.io/delete-file file true)
    )
  ;; (with-fork (fn [] (prn "Greetings from a child!")) )
  ;; (let [pid (HelloJNI/forkYea (fn [] (prn "clj: in callback") "clj: callback-return"))]
  ;;   (if (= pid 0)
  ;;     (do
  ;;       (prn "Child is doing something...")
  ;;       (prn "clj child result was: " (HelloJNI/getResult))
  ;;       (HelloJNI/halt (HelloJNI/pid))
  ;;       )
  ;;     (do
  ;;       (prn "Parent is doing something...")
  ;;       ))
  ;;   (prn pid))
  ;; (server/run-server app {:port 8083})
  )

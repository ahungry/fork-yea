(ns fork-yea.core
  (:import
   HelloJNI)
  (:gen-class))

(def w (atom {:winner nil}))

;; (defn with-fork
;;   "Perform some call in a forked process that kills itself.
;;   May need to ensure we hard-kill this based on current pid."
;;   [f]
;;   (when (= 0 (HelloJNI/forkYea))
;;     (f)
;;     (println "Terminate me!!!!")
;;     (System/exit 0)
;;     ;; (HelloJNI/halt)
;;     ))

(defn -main [& args]
  ;; (with-fork (fn [] (prn "Greetings from a child!")) )
  (let [pid (HelloJNI/forkYea (fn [] (prn "clj: in callback") "clj: callback-return"))]
    (prn pid)))

;; (defn xmain
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!")
;;   (let [pid (HelloJNI/forkYea)]
;;     (if (= 0 pid)
;;       (do
;;         (swap! w conj [:winner :child])
;;         (spit "/tmp/child-pid.txt" "You hit me just fine")
;;         (prn "pid 0 means I am the child"))
;;       (do
;;         (swap! w conj [:winner :parent])
;;         (spit "/tmp/parent-pid.txt" "You hit me, the parent, just fine")
;;         (prn "pid not 0 means I am the parent")))
;;     (prn "I am in clojure, after the fork...what happened?" pid))
;;   ;; Try to pause...something odd happens if not.
;;   (prn @w)
;;   (Thread/sleep 1e3)
;;   (prn "post sleep")
;;   (prn @w)
;;   ;; (System/exit 0)
;;   )

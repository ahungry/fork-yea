Hello world

(defn greeting []
  "Thanks")

(prn ("hhaah" / 1 0 ))
(prn (greeting))

(defonce counter (atom 0))
(swap! counter inc)
(prn "Called this page " @counter "times")

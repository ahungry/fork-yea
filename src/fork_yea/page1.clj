Hello world

(defn greeting []
  "Thanks")

(/ 1 0)
(prn ("hhaah" / 1 0 ))
(prn (greeting))

(defonce counter (atom 0))
(swap! counter inc)
(prn "Called this page " @counter "times")

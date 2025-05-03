(ns clay-walkthrough)

;; Clay renders a namespace as HTML
;; To render this file, I run the "Clay Make File" REPL Command

^:kind/echarts
{:xAxis   {:data ["Shirts", "Cardigans", "Chiffons",
                  "Pants", "Heels", "Socks"]}
 :yAxis   {}
 :series  [{:name "sales"
            :type "bar"
            :data [5 20 36
                   10 10 20]}]}

;; Clay sees annotations and produces HTML visualizations
;; User code does not contain any rendering code

;; To render a single form, I run the "Clay Make Top Level Form" Command

^:kind/hiccup
[:svg
 [:circle {:r 50}]]

;; One open question is how the user should indicate they want inline, or display editor style.

;; Clay has other functions, such as running Quarto.
;; I can do this using the "Clay Make File Quarto" REPL Command.

;; There are 9 useful Clay commands, which is a lot of setup in the UI with lots of opportunities for errors.
;; Ideally these commands could be shared.

;; See `repl-commands` for more about what is in the Clay REPL Commands.

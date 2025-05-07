^:kindly/hide-code
(ns clay-walkthrough)

;; Literate programming is the practise of interleaving code, narrative, and visualizations.
;; We are literating right now!

;; To render this file, I run the "Clay Make File" REPL Command

;; * **Markdown** in comments is marked **up**
;; * **Code** is displayed, evaluated, and the result shown

(inc 2)

;; We can visualize data in HTML.
;; Here's some data

(def item-sales
  {:items ["Shirts" "Pants" "Socks"]
   :sales [5 20 36]})

;; We might want to view it as a table

^:kind/table
item-sales

;; Or a chart

^:kind/echarts
{:xAxis   {:data (:items item-sales)}
 :yAxis   {}
 :series  [{:name "sales"
            :type "bar"
            :data (:sales item-sales)}]}

;; To render a single form, I run the "Clay Make Top Level Form" Command

^:kind/hiccup
[:svg
 [:circle {:r 50 :cx 60 :cy 60 :fill "blue"}]]

;; **My code does not contain any rendering code**

;; The Clay tool sees metadata annotations and renders accordingly

;; My code compiles and works without any tools

;; People are free to experience the code however they please;
;; trying it in a REPL,
;; reading it as HTML website,
;; exploring the visualizations with their own choice of tools,
;; in a browser,
;; or in an editor.

;; Speaking of which... how are we seeing this in our editor?
;;
;; Continued in `src/flare_walkthrough.clj`

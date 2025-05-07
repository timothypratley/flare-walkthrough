(ns flare-walkthrough
  (:require [hiccup.core :as hiccup]))

;; Flares are a new Cursive and Calva feature.
;; Let's send our first flare:

(tagged-literal 'flare/html {:html "<em>hello world</em>"})

;; Flares are special values that request IDE behavior like showing HTML.
;; Flares enable tools like Clay to show data visualizations.
;; You can make your own Custom REPL Commands that produce flares.
;; Let's try some examples in the REPL.

;; We can open a URL instead of passing HTML:

(tagged-literal 'flare/html {:url "www.clojure.org"})

;; Cursive has a `:display :editor` option to open a separate panel
;; A title is required (maybe it should have a default)

(tagged-literal 'flare/html {:url "https://cursive-ide.com/blog/cursive-2025.1.html"
                             :display :editor
                             :title "Clojure"
                             :key "flare"})

;; Each time we send a flare, a new view is opened.
;; Providing a key identifies a panel to reuse.

(tagged-literal 'flare/html {:url "https://calva.io/flares/"
                             :display :editor
                             :title "Clojure"
                             :key "flare"})

;; So now a single view remains and is updated with new content

(tagged-literal 'flare/html {:url "www.scicloj.org"
                             :display :editor
                             :title "Clojure"
                             :key "flare"})

;; It's pretty handy to be able to view a local development server in the IDE while you work

(tagged-literal 'flare/html {:url "http://localhost:1971"
                             :display :editor
                             :title "Clay"
                             :key "flare"})

;; The default view for Cursive is `:inline`.
;; The default view for Calva is `:editor` (inline is not supported).

(tagged-literal 'flare/html {:url "http://localhost:1971"
                             :title "Clay"
                             :key "flare"
                             :display :inline})


;; **Don't litter your code with tagged literals like this**
;; These examples were to demonstrate how they work.

;; Flares should be produced by tools.
;; Let's make our own tool:

(defn hammer
  "Renders hiccup and shows it in the editor"
  [hiccup]
  (tagged-literal 'flare/html {:html (hiccup/html hiccup)}))

(hammer [:h1 "hello"])

;; Now we can set up a Custom REPL command:

;; (hammer (quote ~top-level-form))
;;
;; We've successfully moved the behavior (rendering) and mechanism (flare)
;; out of our code, and into a tool!

;; continued in  `src/repl_commands.clj`.

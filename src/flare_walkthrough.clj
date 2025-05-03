(ns flare-walkthrough)

;; Sending our first flare

(tagged-literal 'flare/html {:html "<em>hello world</em>"})

;; Flares are special values that request IDE behavior like showing HTML.
;; Flares are used by tools like Clay to show data visualizations.
;; You can also make Custom REPL Commands that produce flares.
;; You can use them in the REPL too.
;; Cursive and Calva both support flares.

;; Display any HTML... or URL

(tagged-literal 'flare/html {:url "www.clojure.org"})

;; Cursive has a `:display :editor` option to open a separate panel
;; A title is required (maybe it should have a default)

(tagged-literal 'flare/html {:url "www.clojure.org"
                             :display :editor
                             :title "Clojure"})

;; A new panel is opened every time.

;; Providing a key identifies a panel to reuse

(tagged-literal 'flare/html {:url "www.clojure.org"
                             :display :editor
                             :title "Clojure"
                             :key "flare"})

;; So now the panel changes in place

(tagged-literal 'flare/html {:url "https://cursive-ide.com/blog/cursive-2025.1.html"
                             :display :editor
                             :title "Clojure"
                             :key "flare"})

;; It's pretty handy to be able to view a local development server in the IDE while you work

(tagged-literal 'flare/html {:url "http://localhost:1971"
                             :display :editor
                             :title "Clay"
                             :key "flare"})

;; Usually you wouldn't litter your code with tagged literals like this,
;; that's the job of tools!
;; Let's make our own super basic tool:

(defn render [hiccup]
  )

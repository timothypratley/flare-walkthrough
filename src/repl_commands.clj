(ns repl-commands
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [scicloj.kindly.v4.kind :as kind]))

;; We use REPL commands to perform Clay actions on forms and files.
;; They all have similar configuration, except for the :commandText Clojure snippet and name.

(def command-template
  {:executionType    "EXPRESSION"
   :echo             "MESSAGE"
   :addToHistory     "true"
   :executeBefore    "SYNC_ALL"
   :executeAfter     "SYNC_ALL"
   :executionNs      "REPL_CURRENT"
   :replSelection    "REPL_SELECTOR"
   :replLanguage     "CLJ"
   :saveAll          "true"
   :clearRepl        "false"
   :printOutput      "true"
   :replaceForm      "false"
   :copyResult       "false"
   :insertIntoEditor "false"
   ;; The result of REPL commands are flares, intended to be shown, so this setting needs to be true
   :showResultInline "true"})

;; Clay has a namespace `scicloj.clay.v2.snippets` dedicated to exposing the 9 common Clay commands.
;; The REPL Commands call these snippets with relevant details like the file-path.

(defn format-snippet
  [clay-fn-name & args]
  (str "(do (clojure.core/require '[scicloj.clay.v2.snippets])" \newline
       "    (scicloj.clay.v2.snippets/" clay-fn-name (when args " ") (str/join " " args) "))" \newline))

(def file "\"~file-path\"")
(def current-form "(quote ~form-before-caret)")
(def top-level-form "(quote ~top-level-form)")
(def options "{:ide :cursive}")

(def clay-commands
  "Connects the command name with the snippet function to call and the relevant arguments"
  [["Clay Make File" (format-snippet "make-ns-html!" file options)]
   ["Clay Make File Quarto" (format-snippet "make-ns-quarto-html!" file options)]
   ["Clay Make File RevealJS" (format-snippet "make-ns-quarto-revealjs!" file options)]
   ["Clay Make Current Form" (format-snippet "make-form-html!" current-form file options)]
   ["Clay Make Current Form Quarto" (format-snippet "make-form-quarto-html!" current-form file options)]
   ["Clay Make Top Level Form" (format-snippet "make-form-html!" top-level-form file options)]
   ["Clay Make Top Level Form Quarto" (format-snippet "make-form-quarto-html!" top-level-form file options)]
   ["Clay Browse" (format-snippet "browse!")]
   ["Clay Watch" (format-snippet "watch!" options)]])

(kind/table clay-commands)

;; We can write workspace REPL commands in the .idea folder

(defn write-repl-commands! []
  (spit (io/file ".idea" "repl-commands.xml")
        (hiccup/html {:mode :xml}
                     [:application
                      [:component {:name "ReplProjectCommandManager"}
                       (for [[n s] clay-commands]
                         [:repl-command (assoc command-template
                                          :name n
                                          :commandText s)])]])))

(comment
  (write-repl-commands!))

;; To create global REPL commands,
;; use component name "ReplCommandManager"
;; and put the file in your options directory instead.
;; For me that is:
;; /Users/timothypratley/Library/Application\ Support/JetBrains/IdeaIC2025.1/options/repl-commands.xml

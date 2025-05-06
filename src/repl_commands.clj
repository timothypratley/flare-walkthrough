(ns repl-commands
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [scicloj.kindly.v4.kind :as kind])
  (:import (java.util.zip ZipEntry ZipOutputStream)))

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
(def form-before-caret "(quote ~form-before-caret)")
(def top-level-form "(quote ~top-level-form)")
(def options "{:ide :cursive}")
(def leader1 "ctrl+shift+a")
(def leader2 "a")

(def clay-commands
  "Connects the command name with the snippet function to call and the relevant arguments"
  [["Clay Make File" "f" ["make-ns-html!" file options]]
   ["Clay Make File Quarto" "q" ["make-ns-quarto-html!" file options]]
   ["Clay Make File RevealJS" "r" ["make-ns-quarto-revealjs!" file options]]
   ["Clay Make Form Before Caret" "c" ["make-form-html!" form-before-caret file options]]
   ["Clay Make Form Before Caret Quarto" "C" ["make-form-quarto-html!" form-before-caret file options]]
   ["Clay Make Top Level Form" "t" ["make-form-html!" top-level-form file options]]
   ["Clay Make Top Level Form Quarto" "T" ["make-form-quarto-html!" top-level-form file options]]
   ["Clay Browse" "b" ["browse!"]]
   ["Clay Watch" "w" ["watch!" options]]])

(kind/table clay-commands)

(defn repl-commands [project]
  (hiccup/html {:mode :xml}
               [:application
                [:component {:name (if project
                                     "ReplProjectCommandManager"
                                     "ReplCommandManager")}
                 (for [[action-name _ snippet-args] clay-commands]
                   [:repl-command (assoc command-template
                                    :name action-name
                                    :commandText (apply format-snippet snippet-args))])]]))

;; We can write workspace REPL commands in the .idea folder
(defn write-repl-commands! []
  (spit (doto (io/file ".idea" "repl-commands.xml")
          (io/make-parents))
        (repl-commands true)))

(comment
  (write-repl-commands!))

(defn keymap []
  (hiccup/html {:mode :xml}
               [:keymap {:version "1"
                         :name    "Cursive Clay Keymap"}
                (for [[action-name k _] clay-commands]
                  [:action {:id (str "Cursive.Repl.Command." action-name)}
                   [:keyboard-shortcut (cond-> {:first-keystroke  leader1
                                                :second-keystroke leader2
                                                :third-keystroke  k})]])]))

;; We can write workspace REPL commands in the .idea folder
(defn write-keymap! []
  (spit (doto (io/file ".idea" "keymaps" "cursive-clay-keymap.xml")
          (io/make-parents))
        (keymap)))

(comment
  (write-keymap!))

(defmacro ^:private with-entry
  [zip entry-name & body]
  `(let [^ZipOutputStream zip# ~zip]
     (try
       (.putNextEntry zip# (ZipEntry. ~entry-name))
       ~@body
       (finally
         (.closeEntry zip#)))))

(defn write-zip!
  "Makes a file suitable for File -> Manage IDE Settings -> Import Settings"
  []
  (with-open [file (io/output-stream "clay-settings.zip")
              zip (ZipOutputStream. file)]
    (doto zip
      (with-entry "options/repl-commands.xml"
                  (.write zip (.getBytes (repl-commands false))))
      (with-entry "options/keymaps/cursive-clay-keymap.xml"
                  (.write zip (.getBytes (keymap))))
      (with-entry "installed.txt"
                  (.write zip (.getBytes "com.cursiveclojure.cursive")))
      (with-entry "IntelliJ IDEA Global Settings"))))

(comment
  (write-zip!))

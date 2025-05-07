(ns tagged-literal-tangent)

;; You probably noticed that I glossed over that flares happen to be **tagged-literals**.
;; That's because it's not important, any special value could have been used by convention.

;; However we'll take a brief tangent now to explore: What is a tagged literal?

;; Behold:

#inst "2018-03-28T10:48:00.000"

;; a tag `#inst`, and a value `"2018-03-28T10:48:00.000"`

;; whitespace is optional

#inst"2018-03-28T10:48:00.000"

;; When Clojure reads this it converts the string into a Date.
;; It's a convenient way to serialize/deserialize.

(type #inst "2018-03-28T10:48:00.000")

;; You can provide your own tagged literal readers that perform arbitrary transformations

(binding [*data-readers* {'infix (fn [[x op y]]
                                   (eval (list op x y)))}]
  (read-string "#infix(2 + 3)"))

;; A common use case is for serialization and deserialization,
;; saving and creating records for example.

;; Anyhow... we can construct a tagged literal without reading it

(type (tagged-literal 'inst "2018-03-28T10:48:00.000"))

;; Notice that it was not converted into a Date, it is a representation of the tag and value.

;; We can construct any tagged literal we want without needing a reader.

(tagged-literal 'my-tag {:my-data 1})

;; Tagged literals allow extension of EDN/Clojure syntax with domain-specific types.
;; These extensions are called data-readers.

(set! *data-readers* {'infix (fn [[x op y]]
                               (eval (list op x y)))})

#infix (2 + 3)

;; It is also possible to set the default handler to simply construct a tagged-literal:

(set! *default-data-reader-fn* tagged-literal)

;; And now any unknown tags will create a tagged-literal object:

#my-sym{:my-data 1}

;; So now we can use the special syntax to make a flare

#flare/html{:html "<h1>don't try this at home<h1>"}

;; I don't recommend it!!!

;; I don't recommend defining an infix reader either.

;; TLDR tagged-literal is a Clojure feature
;; [Clojure reader documentation](https://clojure.org/reference/reader#tagged_literals)
;; Flares happen to be tagged literals,
;; but the only thing that matters is they are recognizable as a request for action,
;; and you'll likely only need to make Flares from tooling helpers.

;; Continued in `src/x_final_thoughts.clj`

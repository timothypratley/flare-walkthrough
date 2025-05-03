(ns tagged-literal-walkthrough)

;; What is a tagged literal?

#inst "2018-03-28T10:48:00.000"

;; a tag #inst, and a value "..."

;; whitespace is optional

#inst"2018-03-28T10:48:00.000"

;; But why?
;; Clojure converts the string into a Date at read time.
;; It's a convenient way to serialize/deserialize.

(type #inst "2018-03-28T10:48:00.000")

;; You can provide your own tagged literal readers that perform arbitrary transformations

(binding [*data-readers* {'infix (fn [[x op y]]
                                   (eval (list op x y)))}]
  (read-string "#infix(2 + 3)"))

;; A common use case is for serialization and deserialization, saving and creating records for example.

;; Anyhow... we can construct a tagged literal without reading it

(type (tagged-literal 'inst "2018-03-28T10:48:00.000"))

;; Notice that it was not converted into a Date, it is a representation of the tag and value.

;; In the same way we can construct any tagged literal we want without needing a reader.

(tagged-literal 'my-tag {:my-data 1})

;; Tagged literals are a way to create custom readers of EDN.
;; we haven't registered a reader, so we can't use the syntax,
;; hence why we used the form (tagged-literal tag value)

(set! *default-data-reader-fn* tagged-literal)

#my-sym{:my-data 1}

;; Now we can use the special syntax to make a flare

#flare/html{:html "<h1>don't try this at home<h1>"}

;; I wouldn't recommend it...
;; for the same reason I wouldn't recommend this:

(set! *data-readers* {'infix (fn [[x op y]]
                               (eval (list op x y)))})

#infix (2 + 3)

;; Flares are represented as tagged literals, but that's incidental to their purpose.
;; Any special value would do, it's just something the IDE recognizes as a request.

;; For more information about tagged literals, see the [Clojure reader documentation](https://clojure.org/reference/reader#tagged_literals).

Control.Print.printLength := 100;

fun readWords(infile) = 
  let
    val ins = TextIO.openIn infile
    fun loop ins =
      case TextIO.inputLine ins of
           SOME line => line :: loop ins
         | NONE      => []
  in
      loop ins before TextIO.closeIn ins
  end

(* Look for words that match the pattern and filter out those that don't *)
fun lookForPatterns(wordList, pattern) = 
  let 
    (* Check whether a word matches the pattern *)
    fun hasPattern(word: string): bool =
      let 
        (* Recursively check whether a substring matches the pattern by
           referring to existing matches stored as a list of tuples to 
           represent a map from pattern values to characters (eg. "X" -> 'a'). *)
        fun checkForPattern([], p::ps, _) = false
          | checkForPattern(_, [], _) = true
          | checkForPattern(c::cs, p::ps, []: (char * char) list) = 
              checkForPattern(cs, ps, [(p, c)]) 
              orelse checkForPattern(cs, p::ps, [])
          | checkForPattern(c::cs, p::ps, matches) = 
            let 
              (* Check whether the new character is valid. If it has not been
                 matched to a key or matches the previously stored key, then it
                 is valid. *)
              fun validNewChar(_, _, []) = true
                | validNewChar(c, p, (key, value)::ms) = 
                  if p = key
                  then if c = value
                       then true
                       else false
                  else validNewChar(c, p, ms)
            in
              if validNewChar(c, p, matches)
              then checkForPattern(cs, ps, (p, c)::matches)
              else false
            end
      in
        checkForPattern(explode(word), explode(pattern), [])
      end
  in
    List.filter hasPattern wordList
  end

fun stripWhitespace(c::cs) =
  if c = #"\r" orelse c = #"\n"
  then []
  else c::stripWhitespace(cs)

val words = readWords("../util/enable1.txt")

fun runOnInput(input) = 
  List.map implode (List.map stripWhitespace 
        (List.map explode (lookForPatterns(words, input))))

val test1 = runOnInput("XXYY")
val test2 = runOnInput("XXYYZZ")
val test3 = runOnInput("XXYYX")

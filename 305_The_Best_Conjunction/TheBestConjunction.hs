import qualified Data.Set as Set

prefix :: String -> Int -> String
prefix _ 0 = []
prefix (c:cs) n = c:(prefix cs (n - 1))

getPrefixes :: String -> Int -> String -> Int -> [String]
getPrefixes [] _ _ _ = []
getPrefixes (c:cs) n acc n_o = 
    if length (c:cs) >= n_o
    then if n == 0
         then acc:(getPrefixes cs 0 (c:acc) n_o)
         else getPrefixes cs (n - 1) (c:acc) n_o
    else []

findConjunction :: String -> Set.Set String -> Int -> Maybe (String, [String])
findConjunction word wordSet n = 
    let 
        inSet prefix = Set.member prefix wordSet
        prefixes = getPrefixes word n [] n
        validPrefixes = filter inSet prefixes 
    in
        Just (word, validPrefixes)

bestConjunction2 :: [String] -> Set.Set String -> Int -> [(String, [String])]
bestConjunction2 [] _ _ = []
bestConjunction2 (w:ws) wordSet n = (findConjunction w wordSet n):(bestConjunction2 ws wordSet n)

longest :: Maybe (String, [String]) -> Maybe (String, [String]) -> Maybe (String, [String])
longest Nothing Nothing = Nothing
longest Nothing conjunction = conjunction
longest conjunction Nothing = conjunction
longest (word1, subWords1) (word2, subWords2) = 
    if length subWords1 >= length subWords2
    then (word1, subWords1)
    else (word2, subWords2)

longestConjunction :: [(String, [String])] -> Maybe (String, [String])
longestConjunction conjunctions = foldl longest conjunctions

getBestConjunction :: [String] -> Int -> Maybe (String, [String])
getBestConjunction [] _ = Nothing
getBestConjunction wordList n = longestConjunction (bestConjunction2 wordList (Set.fromList wordList) n)

main = do
    fileContent <- readFile "../util/enable1.txt"
    let wordList = lines fileContent
    let wordSet = Set.fromList wordList
    let conjunctions = getBestConjunction wordList wordSet 3   
    print wordSet
    print conjunctions


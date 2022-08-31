-- Module: TestDict.hs
-- Description: AP Assignment 2 Part 1
--
-- Maintainer: m.pinna10@studenti.unipi.it

import Data.Char
import Data.List
import Dictionary
import Text.Printf

-- Globals for input/output files
inputdir = "./inputs"
outputdir = "./outputs"

-- | The 'ciao' function applies the transformation 'characters in alphabetical order'.
-- :returns: The input string sorted alphabetically.
ciao :: String -> String
ciao = sort . map toLower

-- | The 'readDict' function reads a text file and stores it in a Dictionary.
readDict :: FilePath -> IO (Dictionary String String)
readDict filename = do
  contents <- readFile filename
  return $ foldr (\w acc -> Dictionary.insert acc (ciao w) w) (empty ()) (words contents)

-- | The 'writeDict' function writes a Dictionary into a text file.
-- Each line format is (<key> - <values_list_length>).
writeDict :: Dictionary String String -> FilePath -> IO ()
writeDict d filename =
  let rows =
        concatMap
          ( \k -> case Dictionary.lookup d k of
              Nothing -> ""
              Just values -> k ++ " - " ++ show (length values) ++ "\n"
          )
          (keys d)
   in writeFile filename rows

main :: IO ()
main = do
  -- Read files
  d1 <- readDict (inputdir ++ "/anagram.txt")
  d2 <- readDict (inputdir ++ "/anagram-s1.txt")
  d3 <- readDict (inputdir ++ "/anagram-s2.txt")
  d4 <- readDict (inputdir ++ "/margana2.txt")

  -- Tests
  -- Test 1
  printf
    "\nDictionaries 'd1' and 'd4' are not equal -> %s\n"
    (show $ d1 /= d4)
  -- Test 2
  printf
    "Dictionaries 'd1' and 'd4' have the same keys ->  %s\n"
    (show $ Dictionary.eqList (keys d1) (keys d4))
  -- Test 3
  printf
    "Dictionary 'd1' is equal to merge of dictionaries 'd2' and 'd3' -> %s\n"
    (show $ d1 == merge d2 d3)

  -- Write outputs
  writeDict d1 (outputdir ++ "/anag-out.txt")
  writeDict d4 (outputdir ++ "/gana-out.txt")
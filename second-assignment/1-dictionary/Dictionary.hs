-- Module: Dictionary.hs in
-- Description: AP Assignment 2 Part 1
--
-- Maintainer: m.pinna10@studenti.unipi.it

-- Exporting Dictionary functions needed for testing.
module Dictionary
  ( Dictionary,
    Dictionary.empty,
    Dictionary.insert,
    Dictionary.lookup,
    Dictionary.keys,
    Dictionary.values,
    Dictionary.merge,
    Dictionary.eqList,
  )
where

-- | Declaration of the type Dictionary.
data Dictionary a b = Dict [(a, [b])] deriving (Show)

-- | Dictionary constructor.
-- :returns: An empty Dictionary.
empty :: () -> Dictionary a b
empty () = Dict []

-- | The 'insert' function inserts a pair (key, value) into a Dictionary.
-- Well-formed property of dictionaries preserved.
-- :returns: The Dictionary obtained after the insertion.
insert :: Eq a => Dictionary a b -> a -> b -> Dictionary a b
insert (Dict d) k v = Dict (insertAux d)
  where
    insertAux [] = [(k, [v])]
    insertAux ((k1, v1) : xs)
      | k == k1 = (k1, v1 ++ [v]) : xs
      | otherwise = (k1, v1) : insertAux xs

-- | The 'lookup' function searches for a given key into a Dictionary.
--  :returns: the corresponding value if it exists, nothing otherwise.
lookup :: Eq a => Dictionary a b -> a -> Maybe [b]
lookup (Dict d) k = lookupAux d
  where
    lookupAux [] = Nothing
    lookupAux ((k', v') : xs)
      | k == k' = Just v'
      | otherwise = lookupAux xs

-- | The 'keys' function is a getter for a Dictionary's set of keys.
-- :returns: A list with all the keys in the Dictionary.
keys :: Dictionary a b -> [a]
keys (Dict d) = map fst d

-- | The 'values' function is a getter for a Dictionary's set of values.
-- :returns: A list with all the values in the Dictionary.
values :: Dictionary a b -> [b]
values (Dict d) = concatMap snd d

-- | The 'merge' function merges two Dictionaries.
-- Well-formed property of dictionaries preserved.
-- :returns: The Dictionary obtained after the merge.
merge :: Eq a => Dictionary a b -> Dictionary a b -> Dictionary a b
merge (Dict d1) (Dict d2) =
  foldl
    ( \acc x ->
        insertList acc (fst x) (snd x)
    )
    (Dict d1)
    (d2)

-- | The 'insertList' function inserts a pair (key, [list_of_values]) into a Dictionary.
-- Well-formed property of dictionaries preserved.
-- :returns: The Dictionary obtained after the insertion.
insertList :: Eq a => Dictionary a b -> a -> [b] -> Dictionary a b
insertList (Dict d) k v = foldl (\acc x -> insert acc k x) (Dict d) v

{- Define 'Dictionary' to be an instance of type class 'Eq'
  Equality(d1, d2):
    - same set of keys;
    - for each key, same set of values.
-}
instance (Eq a, Eq b) => Eq (Dictionary a b) where
  (==) (Dict d1) (Dict d2) =
    let keys1 = keys (Dict d1)
     in let keys2 = keys (Dict d2)
         in eqList keys1 keys2 && eqValues (Dict d1) (Dict d2)

-- | The 'eqList' function checks if two lists have the same set of elements.
-- :returns: True if equal, False otherwise.
eqList :: Eq a => [a] -> [a] -> Bool
eqList l1 l2 = contains l1 l2 && contains l2 l1
  where
    contains [] l2 = True
    contains (x : xs) l2 = x `elem` l2 && contains xs l2

-- | The 'eqValues' function checks if, for each key, two dictionaries have the same set of values.
-- Repeated values are not taken into account for the equality.
-- :returns: True if equal, False otherwise.
eqValues :: (Eq a, Eq b) => Dictionary a b -> Dictionary a b -> Bool
eqValues (Dict d1) (Dict d2) = eqValuesAux d1 (Dict d2)
  where
    eqValuesAux [] _ = True
    eqValuesAux ((k, v) : xs) d2 = eqList v (maybe [] id $ Dictionary.lookup d2 k) && eqValuesAux xs d2
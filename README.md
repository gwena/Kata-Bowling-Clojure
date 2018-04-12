# The Bowling Kata in Clojure
More information about the Kata:
[Robert "Uncle Bob" Martin](http://butunclebob.com/ArticleS.UncleBob.TheBowlingGameKata)

## Structure
Standard use of Leinengen

### Test
`lein test`

## Dependencies
* None (apart from the standard JDK, Clojure and Leinengen)

## Assumptions
* An assertion will fail and display an appropriate error message in those cases:
  * The input file does not respect the game information format
  * Zero or more than one parameter is given to the command 
* The filename provided exist and is readable

## Execution
`lein run <filename>` with the path to a game file

The `resources` folder contains one example:
`lein run resources/input-game-test.txt`

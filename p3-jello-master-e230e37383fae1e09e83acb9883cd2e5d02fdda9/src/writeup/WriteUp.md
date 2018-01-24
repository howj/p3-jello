# Project 3 (Chess) Write-Up #
--------

## Project Enjoyment ##
- How Was Your Partnership?
  <pre>
  Excellent, this time around we did more pair programming than we have on p1 or p2, and also tried saros.
  </pre>
  
- What was your favorite part of the project?
  <pre>
  Getting to understand the minimax algorithm and why it works for chess. We both enjoy chess.
  </pre>

- What was your least favorite part of the project?
  <pre>
  Debugging ParallelSearcher and JamboreeSearcher. We ran into a lot of different errors, and we restarted on Jamboree a few times,
  first using a recursive call, then trying using only RecursiveTasks, then back to the recursive call, and then back to RecursiveTasks.
  We could not figure out for a long time why our Searcher was taking so long. Finally, after many many hours we got a JamboreeSearcher
  that passes the Gitlab tests and runs the chessbot pretty well.
  </pre>

- How could the project be improved?
  <pre>
  Maybe be more clear what is expected for a Jamboree algorithm. 
  </pre>

- Did you enjoy the project?
  <pre>
  Yes, chess is always fun. Like p2, rewarding to be able to see our results on the screen in an application.
  </pre>

-----

## The Chess Server ##
- When you faced Clamps, what did the code you used do?  Was it just your jamboree?  Did you do something fancier?
  <pre>
  We used a derivative of Jamboree that used MVVLVA and iterative deepening. 
  </pre>

- Did you enjoy watching your bot play on the server?  Is your bot better at chess than you are?
  <pre>
  Sometimes. It's painful to watch the bot not making "obvious" moves such as getting in position to promote pawns
  to get a checkmate. 
  Yes, because the bot can see so far ahead and evaluate all the possible moves and board states.
  </pre>

- Did your bot compete with anyone else in the class?  Did you win?
  <pre>
  We played someone who did iterative deepening and we destroyed them with our no-optimization version.
  It was total domination, we won the game with many pieces still remaining.
  </pre>

- Did you do any Above and Beyond?  Describe exactly what you implemented.
  <pre>
  We implemented MVVLVA move ordering and some iterative deepening in order to be able to beat clamps 10x in a row.
  </pre>


## Experiments ##

### Chess Game ###

#### Hypotheses ####
Suppose your bot goes 3-ply deep.  How many game tree nodes do you think
it explores (we're looking for an order of magnitude) if:
 - ...you're using minimax?
 	On the order of 10^4
 - ...you're using alphabeta?
    On the order of 10^3

#### Results ####
Run an experiment to determine the actual answers for the above.  To run
the experiment, do the following:
1. Run SimpleSearcher against AlphaBetaSearcher and capture the board
   states (fens) during the game.  To do this, you'll want to use code
   similar to the code in the testing folder.
2. Now that you have a list of fens, you can run each bot on each of them
   sequentially.  You'll want to slightly edit your algorithm to record the
   number of nodes you visit along the way.
3. Run the same experiment for 1, 2, 3, 4, and 5 ply. And with all four
   implementations (use ply/2 for the cut-off for the parallel
   implementations).  Make a pretty graph of your results (link to it from
   here) and fill in the table here as well:

<pre>TODO: Fill in the table below</pre>

|      Algorithm     | 1-ply | 2-ply | 3-ply | 4-ply  | 5-ply    |
| :----------------: |:-----:|:-----:|:-----:|:------:|:--------:|
|       Minimax      |1344   |32448  |865984 |22283392|1006326912|
|  Parallel Minimax  |1344   |32488  |865984 |22283392|1006326912|
|      Alphabeta     |1344   |5504   |117248 |2478528 |77190720  |
|      Jamboree      |1344   |5504   |157440 |2632128 |104680827 |

In the below graphs, the Y-axis is nodes explored, except for the last three when the X-axis is nodes explored.
![Exp1](http://i.imgur.com/VeK7fDC.png)

#### Conclusions ####
How close were your estimates to the actual values?  Did you find any
entry in the table surprising?  Based ONLY on this table, do you feel
like there is a substantial difference between the four algorithms?
<pre>
We underestimated by around factor of 10^2. The Minimax explored a little under
the order of 10^6 nodes, while Alphabeta explored around 10^5. Nothing in the table is too surprising, we expected
minimax and parallel minimax to explore the most nodes, because they don't do any pruning, and also to explore
the same number of nodes as each other, as parallel just explores the same nodes in parallel. Jamboree, while smaller than
minmax, is slightly larger than alphabeta because we explore part of the algorithm in parallel and part sequentially.
Based on this table, we can see there is a substantial difference between alphabeta/jamboree and minimax in terms of nodes explored
because of pruning.
</pre>

### Optimizing Experiments ###
THE EXPERIMENTS IN THIS SECTION WILL TAKE A LONG TIME TO RUN. 
To make this better, you should use Google Compute Engine:
* Run multiple experiments at the same time, but **NOT ON THE SAME MACHINE**.
* Google Compute Engine lets you spin up as many instances as you want.

#### Generating A Sample Of Games ####
Because chess games are very different at the beginning, middle,
and end, you should choose the starting board, a board around the middle
of a game, and a board about 5 moves from the end of the game.  The exact boards
you choose don't matter (although, you shouldn't choose a board already in
checkmate), but they should be different.

#### Sequential Cut-Offs ####
Experimentally determine the best sequential cut-off for both of your
parallel searchers.  You should test this at depth 5.  If you want it
to go more quickly, now is a good time to figure out Google Compute
Engine.   Plot your results and discuss which cut-offs work the best on each of
your three boards.

|      Algorithm     | Early Game | Mid Game | End Game |
| :----------------: |:----------:|:--------:|:--------:|
|  Parallel Minimax  |      4     |    5     |     2    |
|      Jamboree      |      2     |    2     |     2    |

![exp2tab](https://i.gyazo.com/b66ba80787336e13115e7e42af5669d7.png)

For the below graphs, the X-axis is the sequential cutoff, and the Y-axis is the time taken in ms.
![exp2](http://i.imgur.com/OxEU6xj.png)

<pre>
For each searcher, we took the average of 20 trials at depth 5 at sequential cutoffs of 0-5 for each board state. 
We found that for Jamboree, a cutoff of 2 for all board states gave us the fastest move times. We couldn't say the same for parallel minimax, however. 
For parallel, it seemed that the difference in the time taken for all cutoffs was very small, and there didn't seem to be a pattern going from a lower 
cutoff to a higher or the other way around. For Jamboree, however, cutoff 0 was the worst by far, with cutoff 5 being the second worst times.
The times followed a reverse bell curve: times toward the middle were the lowest, with the best times being at cutoff 2.
</pre>

#### Number Of Processors ####
Now that you have found an optimal cut-off, you should find the optimal
number of processors. You MUST use Google Compute Engine for this
experiment. For the same three boards that you used in the previous 
experiment, at the same depth 5, using your optimal cut-offs, test your
algorithm on a varying number of processors.  You shouldn't need to test all 32
options; instead, do a binary search to find the best number. You can tell the 
ForkJoin framework to only use k processors by giving an argument when
constructing the pool, e.g.,
```java
ForkJoinPool POOL = new ForkJoinPool(k);
```
Plot your results and discuss which number of processors works the best on each
of the three boards.
<pre>TODO: Do the experiment; discuss the results (possibly with pretty graphs!)</pre>

![exp3tab](https://i.gyazo.com/fac7cb1a3d714a98a409c45afc7a55a5.png)
For the below graphs, the X-axis is the # of cores, and the Y-axis is how long it took to take a move at that board state at depth 5 in ms.
![exp3](http://i.imgur.com/ZxxSqz3.png)

<pre>
For each board state, for each searcher, we did 20 trials at Cores 4, 8, ...32 and took the average. Our conclusion was that
for early game, 12 cores seemed optimal as it gave us the lowest average time for both searchers. For the mid game, 16 cores 
seemed optimal. For end game, 20 was the choice. It seemed that using 4 cores, for both searchers and all board states gave the worst
time by far, with a big jump in 8 cores, with a smaller jump to 12. Between 12 and 32 cores there did not seem to be a whole lot of difference
in the time taken, except that after 20 the time taken seemed only to get longer.
Overall, we could choose 16 cores for all board states for both searchers, as the difference between the best times
is quite small (in the 10s/100s of ms) and is the best for mid game, which is when the most moves in a game take place.
</pre>

#### Comparing The Algorithms ####
Now that you have found an optimal cut-off and an optimal number of processors, 
you should compare the actual run times of your four implementations. You MUST
use Google Compute Engine for this experiment (Remember: when calculating
runtimes using *timing*, the machine matters).  At depth 5, using your optimal 
cut-offs and the optimal number of processors, time all four of your algorithms
for each of the three boards.

Plot your results and discuss anything surprising about your results here.
<pre>TODO: Do the experiment; discuss the results (possibly with pretty graphs!)</pre>

|      Algorithm     | Early Game | Mid Game | End Game |
| :----------------: |:----------:|:--------:|:--------:|
|       Minimax      |   4426     |  57162   |   6238   |
|  Parallel Minimax  |   1464     |  21852   |   2070   |
|      Alphabeta     |    139     |   2143   |    722   |
|      Jamboree      |     54     |    812   |    251   |

![exp4](http://i.imgur.com/ZEKt8VC.png)
Based on our results from the previous two experiments, we choose a sequential cutoff of 4 and 16 cores. We took the average of 
20 trials taken at depth 5 for each board state for each searcher. Overall, our results show that for all board states, in terms
of minimum time taken, Jamboree > Alphabeta > Parallel Minimax > Minimax. This is expected, as Minimax does not prune and is sequential,
while Jamboree both prunes and runs partially in parallel. 

### Beating Traffic ###
In the last part of the project, you made a very small modification to your bot
to solve a new problem.  We'd like you to think a bit more about the 
formalization of the traffic problem as a graph in this question.  
- To use Minimax to solve this problem, we had to represent it as a game. In
  particular, the "states" of the game were "stretches of road" and the valid
  moves were choices of other adjacent "stretches of road".  The traffic and
  distance were factored in using the evaluation function.  If you wanted to use
  Dijkstra's Algorithm to solve this problem instead of Minimax, how would you
  formulate it as a graph?
  <pre>
  If we wanted to turn the traffic problem into a graph problem, we would have to turn the problem into that of vertices and edges.
  To do so, we could use the where the "stretches of road" meet, or the "road intersections" as the vertices. Edges could be whether 
  one "road intersection" is connected to another by a "stretch of road." 
  Because Dijkstra's needs to use edge weights, we would use the time it takes to travel from one stretch to another (a positive value
  in seconds, with the time it takes + the time lost to traffic to get the total time it takes). 
  </pre>

- These two algorithms DO NOT optimize for the same thing.  (If they did,
  Dijkstra's is always faster; so, there would be no reason to ever use
  Minimax.)  Describe the difference in what each of the algorithms is
  optimizing for.  When will they output different paths?
  <pre>
  Dijkstra's Algorithm searches for the shortest path between two vertices in a graph. In this case, it would be be the path that gives us the least
  time taken for the journey between destination and source (smallest total edge weight).
  In the case of minimax, we are looking to minimize time spent in traffic. This does not necessarily mean the same thing as Dijkstra's. 
  The end goal of the two algorithms is different. Dijkstra wants to find the path that takes the least time. Minimax wants to take the path
  that minimizes time lost to traffic. 
  The two algorithms will output different paths when the path that minimizes the traffic is not the path that takes the least time to travel.
  Minimax will choose the path described, while Dijkstra will choose a different path that does take the least time.
  </pre>
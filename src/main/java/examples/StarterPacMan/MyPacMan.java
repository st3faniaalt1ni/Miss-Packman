package examples.StarterPacMan;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Random;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController {
    private static final int MIN_DISTANCE = 30;
    private Random random = new Random();

    @Override
    public MOVE getMove(Game game, long timeDue) {
        //Initialize score and time
        int score = 0;
        int totalTime = 0;
        //Ms.PacMan current position
        int current = game.getPacmanCurrentNodeIndex();
        int ghostPosition =  0;



        //1st strategy: Run away from ghosts/Evade ghosts
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            //Query information about the edible nature of a ghost.
            if(game.getGhostLairTime(ghost)==0) {
                if (!game.isGhostEdible(ghost)) {
                    //Find ghost's current position
                    ghostPosition = game.getGhostCurrentNodeIndex(ghost);
                    if (ghostPosition != -1) {
                        if (game.getShortestPathDistance(current, ghostPosition) < MIN_DISTANCE) {

                            System.out.println("Run away from Ghost");
                            return game.getNextMoveAwayFromTarget(current, ghostPosition, Constants.DM.PATH);

                        }


                    }
                }

            }

        }

        //2nd strategy: Chase ghosts
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            //Query information about the edible nature of a ghost.
            if (game.getGhostEdibleTime(ghost) != 0) {
                //Find ghost's current position
                ghostPosition = game.getGhostCurrentNodeIndex(ghost);
                if (ghostPosition != -1) {
                    if (game.getShortestPathDistance(current, ghostPosition) < 40) {
                        System.out.println("Chase Ghosts");
                        return  game.getNextMoveTowardsTarget(current ,ghostPosition,Constants. DM.PATH);
                    }
                }
            }
        }
        ghostPosition = 0;

        //3rd Strategy: find pills

        //get all the available (observable) pills
        int[] pills = game.getActivePillsIndices();
        //if there is observable pills
        if(pills.length > 0){
            //go to the nearest
            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, pills, Constants.DM.PATH), Constants.DM.PATH);
        }
        //if there is not an observable pill
        else if(pills.length == 0){
            //take all the possible moves, except of the opposite of the last move
            MOVE[] possibleMoves = game.getPossibleMoves(current,game.getPacmanLastMoveMade());
            //return one of these possible moves
            return possibleMoves[random.nextInt(possibleMoves.length)];
        }



        //4th Strategy: Find a new way to move
        //Choose a random action
        MOVE[] possibleMoves1 = game.getPossibleMoves(current,game.getPacmanLastMoveMade());
        //choose a random action
        if (ghostPosition == 0){
            return possibleMoves1[random.nextInt(possibleMoves1.length)];

        }
        return MOVE.NEUTRAL;



    }
}
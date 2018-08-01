package com.geneostr.ibm.codech.tennis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TennisTournament {

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    public static void main(String[] args) {
        TennisTournament tournament = new TennisTournament();
        // Get number of players and seed players
        int numOfPlayers = tournament.getIntegerArgument(args, 0, 100);
        int numOfSeeds = tournament.getIntegerArgument(args, 1, 30);
        // Generate players
        final List<Player> players = tournament.generatePlayers(numOfPlayers, numOfSeeds);
        // while we don't have a winner, continue rounds
        while (players.size() > 1) {
            // match up players for tournament
            List<Match> matchUp = tournament.matchUp(players);
            // clear previous list of players
            players.clear();
            // set random winners and set them to players list for next round
            matchUp.forEach(m -> {
                m.setWinner(tournament.random.nextInt(3));
                players.add(m.getMatchWinner());
            });
        }
        // Print winner
        System.out.println("The winner is " + players.get(0));
    }

    /**
     * Create matches by number of players and randomly assign players to matches.
     * In the beginning seed players assign to empty matches and then all other players
     * randomly assigned to matches.
     * If there is an odd number of players then 'Match' for one player created.
     * This player is not playing and just progress to the next round.
     *
     * @param players
     *         list of Player(s)
     *
     * @return list of matches with assigned players.
     */
    List<Match> matchUp(List<Player> players) {
        final List<Match> matches = generateMatches(players.size());
        final AtomicInteger index = new AtomicInteger(0);
        players.forEach(p -> {
            // first try to assign seed players to empty matches. 'Left' is a default seed player position.
            if (p.isSeed() && index.intValue() < matches.size() && matches.get(index.intValue()).getLeft() == null) {
                matches.get(index.getAndIncrement()).setLeft(p);
            } else {
                // Randomly assign rest of players to matches
                // Look for a not assigned match (one of positions is empty)
                int idx = random.nextInt(matches.size());
                while (matches.get(idx).isAssigned()) {
                    idx = random.nextInt(matches.size());
                }
                // assign player to a match
                if (matches.get(idx).getRight() != null) {
                    matches.get(idx).setLeft(p);
                } else {
                    matches.get(idx).setRight(p);
                }
            }
        });
        return matches;
    }

    /**
     * Generate random list of players. Set number of seed players by <b>seed</b> parameter.
     *
     * @param cnt
     *         total number of players
     * @param seeds
     *         number of seed players, always less or equal to number of players
     *
     * @return generated players
     */
    List<Player> generatePlayers(int cnt, int seeds) {
        if (seeds > cnt) {
            throw new AssertionError("Number of seed players should be less than total number of players");
        }
        int numOfSeeds = 0;
        List<Player> lst = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            int rank = numOfSeeds++ <= seeds ? random.nextInt(seeds) + 1 : 0;
            lst.add(new Player(rank, "Player_" + i, (rank > 0)));
        }
        // Sort players by rank
        lst.sort((x, y) -> ((x.getRank() > y.getRank()) ? -1 : ((x == y) ? 0 : 1)));
        return lst;
    }

    /**
     * Generate Matches for players. If there is an odd number of players then additional match
     * generated for the <i>odd player</i>
     *
     * @param numOfPlayers
     *         number of players in the round
     *
     * @return generated empty matches
     */
    List<Match> generateMatches(int numOfPlayers) {
        int numOfMatches = Math.floorDiv(numOfPlayers, 2) + Math.floorMod(numOfPlayers, 2);
        List<Match> lst = new ArrayList<>(numOfMatches);
        for (int i = 0; i < numOfMatches; i++) {
            lst.add(new Match());
        }
        return lst;
    }

    /**
     * Get integer argument from string array of arguments
     *
     * @param args
     *         array of arguments
     * @param pos
     *         position of integer argument
     * @param defaultVal
     *         default value in case of error or empty array
     *
     * @return integer value
     */
    int getIntegerArgument(String[] args, int pos, int defaultVal) {
        if (args.length > pos) {
            try {
                return Integer.parseInt(args[pos]);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return defaultVal;
    }

    @Data
    @AllArgsConstructor
    static class Player {
        /**
         * Player rank
         */
        private final int rank;
        /**
         * Player name
         */
        private final String name;
        /**
         * True for 'seed' players
         */
        private final boolean seed;
    }

    @Data
    static class Match {
        /**
         * Player left
         */
        private Player left;
        /**
         * Player right
         */
        private Player right;
        /**
         * Match winner. If greater than 1 then 'right' player is winner else 'left' player
         */
        private int winner;

        public Player getMatchWinner() {
            if (winner > 1 && isAssigned()) {
                return right;
            } else {
                return left == null ? right : left;
            }
        }

        public boolean isAssigned() {
            return left != null && right != null;
        }
    }

}

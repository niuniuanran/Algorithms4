import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int teamNumber;
    private final ST<String, Team> teams;
    private int currMaxWin;
    private int[][] gameAgainst;
    private int[] wins;
    private String[] teamNames;
    private int[][] subsetRs;

    private final int S, T;

    public BaseballElimination(
            String filename)     // create a baseball division from given filename in format specified below
    {
        In fileIn = new In(filename);
        teamNumber = fileIn.readInt();
        teams = new ST<String, Team>();
        currMaxWin = 0;
        gameAgainst = new int[teamNumber][teamNumber];
        wins = new int[teamNumber];
        subsetRs = new int[teamNumber][teamNumber];
        teamNames = new String[teamNumber];

        S = 0;
        T = (teamNumber - 1) * teamNumber / 2 + 1;

        int i = 0;
        while (!fileIn.isEmpty()) {

            String teamName = fileIn.readString();
            teamNames[i] = teamName;
            int nWin = fileIn.readInt();
            if (currMaxWin < nWin) currMaxWin = nWin;
            wins[i] = nWin;
            int nLose = fileIn.readInt();
            int nRemain = fileIn.readInt();

            for (int j = 0; j < teamNumber; j++) {
                gameAgainst[i][j] = fileIn.readInt();
            }
            teams.put(teamName, new Team(i, nWin, nLose, nRemain));
            i++;
        }

        for (int j = 0; j < teamNumber; j++) {
            testEliminated(teamNames[j]);
        }

    }

    private class Team {
        private final int index;
        private final int win, lose, remain;
        private boolean eliminated;

        public Team(int i, int nWin, int nLose, int nRemain) {
            index = i;
            win = nWin;
            lose = nLose;
            remain = nRemain;
            eliminated = false;
        }
    }

    private int teamVertexIndex(int i, int targetTeam) {
        assert i != targetTeam;
        if (i > targetTeam)
            return ((teamNumber - 2) * (teamNumber - 1) / 2 + i);
        else return ((teamNumber - 2) * (teamNumber - 1) / 2 + i + 1);
    }

    private void testEliminated(String teamName) {
        Team currTeam = teams.get(teamName);

        if (currTeam.win + currTeam.remain < currMaxWin) {
            currTeam.eliminated = true;
            int t = 0;
            for (int i = 0; i < teamNumber; i++) {
                if (teams.get(teamNames[i]).win > (currTeam.win + currTeam.remain))
                    subsetRs[currTeam.index][t++] = i;

            }
            subsetRs[currTeam.index][t] = -1;
            return;
        }

        FlowNetwork teamFlow = new FlowNetwork(T + 1);

        int targetMaxWin = currTeam.win + currTeam.remain;
        int targetTeamIndex = currTeam.index;

        // process the team part.
        // teamVertexIndex[i] = vertex number of team i.
        for (int i = 0; i < teamNumber; i++) {
            if (i != targetTeamIndex) teamFlow.addEdge(
                    new FlowEdge(teamVertexIndex(i, targetTeamIndex), T, targetMaxWin - wins[i]));
        }


        // process the game part.
        // gameVertexCounter counts the vertex number of the current game being discussed.
        int gameVertexCounter = 0;
        int totalGames = 0;
        for (int i = 0; i < teamNumber; i++) {
            if (i == targetTeamIndex) continue;
            for (int j = i + 1; j < teamNumber; j++) {
                // the capacity of the edge from source to game vertex = number of games to be played between team i and j.
                if (j == targetTeamIndex) continue;
                gameVertexCounter++;
                teamFlow.addEdge(new FlowEdge(S, gameVertexCounter, gameAgainst[i][j]));
                totalGames += gameAgainst[i][j];
                // the capacity of the two edges from the game to the two teams are not limited.
                teamFlow.addEdge(
                        new FlowEdge(gameVertexCounter, teamVertexIndex(i, targetTeamIndex),
                                     Double.POSITIVE_INFINITY));
                teamFlow.addEdge(
                        new FlowEdge(gameVertexCounter, teamVertexIndex(j, targetTeamIndex),
                                     Double.POSITIVE_INFINITY));

            }
        }

        FordFulkerson fordFulkerson = new FordFulkerson(teamFlow, S, T);
        if (fordFulkerson.value() == totalGames) currTeam.eliminated = false;
        else {
            currTeam.eliminated = true;
            int t = 0;
            for (int i = 0; i < teamNumber; i++) {
                if (i != targetTeamIndex && fordFulkerson
                        .inCut(teamVertexIndex(i, targetTeamIndex)))
                    subsetRs[targetTeamIndex][t++] = i;

            }
            subsetRs[targetTeamIndex][t] = -1;

        }


    }

    public int numberOfTeams()                        // number of teams
    {
        return teamNumber;
    }

    public Iterable<String> teams()                                // all teams
    {
        Queue<String> teamNameQueue = new Queue<String>();
        for (int i = 0; i < teamNumber; i++)
            teamNameQueue.enqueue(teamNames[i]);

        return teamNameQueue;

    }

    public int wins(String team)                      // number of wins for given team
    {
        validate(team);

        if (!teams.contains(team)) throw new IllegalArgumentException("wins");
        return teams.get(team).win;
    }

    public int losses(String team)                    // number of losses for given team
    {
        validate(team);

        if (!teams.contains(team)) throw new IllegalArgumentException("loses");
        return teams.get(team).lose;
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        validate(team);

        if (!teams.contains(team)) throw new IllegalArgumentException("remaining");
        return teams.get(team).remain;
    }

    public int against(String team1,
                       String team2)    // number of remaining games between team1 and team2
    {
        validate(team1);
        validate(team2);

        int i = teams.get(team1).index;
        int j = teams.get(team2).index;
        return gameAgainst[i][j];
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        validate(team);
        return teams.get(team).eliminated;
    }

    public Iterable<String> certificateOfElimination(
            String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        validate(team);

        if (!teams.get(team).eliminated) return null;

        int targetTeamIndex = teams.get(team).index;
        Queue<String> subsetRNames = new Queue<String>();

        int[] currSubSet = subsetRs[targetTeamIndex];

        int t = 0;
        while (currSubSet[t] != -1)
            subsetRNames.enqueue(teamNames[currSubSet[t++]]);
        
        return subsetRNames;
    }

    private void validate(String team) {
        if (team == null) throw new IllegalArgumentException("Invalid team name!");
        if (!teams.contains(team)) throw new IllegalArgumentException("Invalid team name!");
    }


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF percolationUF;
    private final WeightedQuickUnionUF filledUF;
    private final int gridSize;
    private final boolean[] siteIsOpen;
    private int numOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Illegal grid size!");
        }
        percolationUF = new WeightedQuickUnionUF(n * n + 2);
        filledUF = new WeightedQuickUnionUF(n * n + 1);
        gridSize = n;
        numOfOpenSites = 0;
        siteIsOpen = new boolean[gridSize * gridSize + 2];
        // nodes from 1 to n*n represents the sites in the grid;
        // node 0 and node n*n+1 are conceptual sites to facilitate the test of the system.
        if (n > 1) {
            for (int i = 1; i <= n; i++) {
                percolationUF.union(0, i);
                filledUF.union(0, i);
                percolationUF.union(n * n + 1, n * n + 1 - i);
                for (int j = 0; j < n; j++) {
                    siteIsOpen[j * gridSize + i] = false;
                    // to initialize, all the sites are full.
                    // when the value turns to 1, it is open.
                }
            }
        }
        else {
            siteIsOpen[1] = false;
            percolationUF.union(1, 2);
        }
        siteIsOpen[0] = true;
        siteIsOpen[gridSize * gridSize + 1] = true;
    }

    private int calLocation(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IllegalArgumentException("Illegal position!");
        }
        return (row - 1) * gridSize + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        int location = calLocation(row, col);
        if (siteIsOpen[location]) {
            return;
        }
        siteIsOpen[location] = true;
        numOfOpenSites += 1;
        if (gridSize == 1 && location == 1) {
            percolationUF.union(0, 1);
            filledUF.union(0, 1);
            return;
        }
        if (row > 1 && siteIsOpen[location - gridSize]) {
            percolationUF.union(location, location - gridSize);
            filledUF.union(location, location - gridSize);
        }
        if (row < gridSize && siteIsOpen[location + gridSize]) {
            percolationUF.union(location, location + gridSize);
            filledUF.union(location, location + gridSize);
        }
        if (col > 1 && siteIsOpen[location - 1]) {
            percolationUF.union(location, location - 1);
            filledUF.union(location, location - 1);
        }
        if (col < gridSize && siteIsOpen[location + 1]) {
            percolationUF.union(location, location + 1);
            filledUF.union(location, location + 1);
        }


    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (siteIsOpen[calLocation(row, col)]) {
            return true;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int location = calLocation(row, col);
        if (!siteIsOpen[location]) {
            return false;
        }
        return filledUF.connected(0, location);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolationUF.connected(0, gridSize * gridSize + 1);
    }

}

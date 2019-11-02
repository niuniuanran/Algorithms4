import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture currPicture;
    private double[][] energyCache;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture myPicture) {
        if (myPicture == null) throw new IllegalArgumentException("null picture!");
        currPicture = new Picture(myPicture);
        energyCache = null;
    }

    // current picture
    public Picture picture() {
        return new Picture(currPicture);
    }

    // width of current picture
    public int width() {
        return currPicture.width();

    }

    // height of current picture
    public int height() {
        return currPicture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new IllegalArgumentException("Illegal coordinates for energy calculation!");
        if (energyCache == null) {
            energyCache = new double[width()][height()];
            for (int i = 0; i < width(); i++)
                for (int j = 0; j < height(); j++)
                    energyCache[i][j] = Double.POSITIVE_INFINITY;
        }
        if (energyCache[x][y] != Double.POSITIVE_INFINITY) return energyCache[x][y];

        if (x == 0) return 1000;
        if (y == 0) return 1000;
        if (x == width() - 1) return 1000;
        if (y == height() - 1) return 1000;
        int rgbLeft = currPicture.getRGB(x - 1, y);
        int rgbRight = currPicture.getRGB(x + 1, y);
        int rgbAbove = currPicture.getRGB(x, y - 1);
        int rgbBelow = currPicture.getRGB(x, y + 1);
        double energy = computeEnergy(rgbLeft, rgbRight, rgbAbove, rgbBelow);
        energyCache[x][y] = energy;
        return energy;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (height() < 3 || width() < 3) {
            int[] path = new int[width()];
            for (int i = 0; i < width(); i++) path[i] = 0;
            return path;
        }

        // source: V, sink: V+1
        int V = width() * height();
        double[] distTo = new double[V + 1];
        int[] edgeTo = new int[V + 1];

        for (int i = 0; i < V + 1; i++) distTo[i] = Double.POSITIVE_INFINITY;
        for (int y = 0; y < height(); y++) {
            distTo[y] = 1000;
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                relaxHorizontal(x, y, distTo, edgeTo);
            }
        }

        return horizontalPath(edgeTo);
    }

    private void relaxHorizontal(int x, int y, double[] distTo, int[] edgeTo) {
        if (x == width() - 1) {
            if (distTo[width() * height()] > distTo[x * height() + y]) {
                distTo[width() * height()] = distTo[x * height() + y];
                edgeTo[width() * height()] = y;
            }
            return;
        }
        int newX = x + 1;
        for (int i = -1; i <= 1; i++) {
            int newY = y + i;
            if (newY >= 0 && newY <= height() - 1) update(x, y, newX, newY, distTo, edgeTo, false);
        }
    }


    private int[] horizontalPath(int[] edgeTo) {
        int[] path = new int[width()];
        int currV = width() * height();
        for (int x = width() - 1; x >= 0; x--) {
            path[x] = edgeTo[currV];
            currV = path[x] + height() * x;
        }
        return path;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (width() < 3 || height() < 3) {
            int[] path = new int[height()];
            for (int i = 0; i < height(); i++) path[i] = 0;
            return path;
        }

        // source: V, sink: V+1
        int V = width() * height();
        double[] distTo = new double[V + 1];
        int[] edgeTo = new int[V + 1];
        for (int i = 0; i < V + 1; i++) distTo[i] = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width(); i++) {
            distTo[i] = 1000;
        }

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                relaxVertical(x, y, distTo, edgeTo);
            }
        }

        return verticalPath(edgeTo);
    }

    private void relaxVertical(int x, int y, double[] distTo, int[] edgeTo) {
        if (y == height() - 1) {
            if (distTo[width() * height()] > distTo[y * width() + x]) {
                distTo[width() * height()] = distTo[y * width() + x];
                edgeTo[width() * height()] = x;
            }
            return;
        }
        int newY = y + 1;
        for (int i = -1; i <= 1; i++) {
            int newX = x + i;
            if (newX >= 0 && newX <= width() - 1) update(x, y, newX, newY, distTo, edgeTo, true);
        }
    }

    private void update(int x, int y, int newX, int newY, double[] distTo, int[] edgeTo,
                        boolean vertical) {
        int newV = vertical ? newY * width() + newX : newX * height() + newY;
        double oldDist = distTo[newV];
        double newDist = distTo[vertical ? y * width() + x : x * height() + y] + energy(newX, newY);
        if (oldDist > newDist) {
            distTo[newV] = newDist;
            edgeTo[newV] = vertical ? x : y;
        }
    }

    private int[] verticalPath(int[] edgeTo) {

        int[] path = new int[height()];
        int currV = width() * height();
        for (int i = height() - 1; i >= 0; i--) {
            path[i] = edgeTo[currV];
            currV = path[i] + width() * i;
        }
        return path;

    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!validSeam(seam, true))
            throw new IllegalArgumentException("Illeagal seam for removing horizontal seam!");
        Picture newPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height() - 1; y++) {
                if (y < seam[x]) newPicture.set(x, y, currPicture.get(x, y));
                else newPicture.set(x, y, currPicture.get(x, y + 1));
            }
        }
        energyCache = null;
        currPicture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!validSeam(seam, false))
            throw new IllegalArgumentException("Illeagal seam for removing vertical seam!");
        Picture newPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width() - 1; x++) {
                if (x < seam[y]) newPicture.set(x, y, currPicture.get(x, y));
                else newPicture.set(x, y, currPicture.get(x + 1, y));
            }
        }
        energyCache = null;
        currPicture = newPicture;

    }

    private boolean validSeam(int[] seam, boolean horizontal) {
        if (seam == null) return false;
        if (horizontal) {
            if (height() <= 1) return false;
            if (seam.length != width()) return false;
            for (int i = 0; i < width(); i++) {
                if (seam[i] < 0 || seam[i] > height() - 1) return false;
                if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) return false;
            }
        }
        else {
            if (width() <= 1) return false;
            if (seam.length != height()) return false;
            for (int i = 0; i < height(); i++) {
                if (seam[i] < 0 || seam[i] > width() - 1) return false;
                if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) return false;
            }
        }
        return true;
    }

    private double computeEnergy(int left, int right, int above, int below) {
        double deltaSquareX = Math.pow(red(left) - red(right), 2) + Math
                .pow(green(left) - green(right), 2) + Math.pow(blue(left) - blue(right), 2);
        double deltaSquareY = Math.pow(red(above) - red(below), 2) + Math
                .pow(green(above) - green(below), 2) + Math.pow(blue(above) - blue(below), 2);
        return Math.sqrt(deltaSquareX + deltaSquareY);
    }

    private int red(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int green(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int blue(int rgb) {
        return rgb & 0xFF;
    }

}

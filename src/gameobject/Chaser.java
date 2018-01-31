package gameobject;

import java.util.ArrayList;
import processing.core.PVector;
import grid.Grid;
import grid.Coordinates;
import texture.Texture;
import config.Config;


/// Enemy which pursues the player's ship while using path finding to avoid asteroids.
public class Chaser extends PathEnemy {

    // Constants.
    public static final int ID = 2;
    private static final int SCORE_VALUE = 10;
    private static final float PATH_INTERVAL = 1f;
    private static final float MAX_SPEED = 200f;
    private static final float ACCELERATION = 400f;
    private static final float ROTATE_RATE = 120f;

    /// Ship to chase.
    private Ship mShip;

    /// Coordinates of path leading to ship.
    private ArrayList<Coordinates> mPath;

    /// Initialise chaser.
    public Chaser(Texture texture, Grid grid, Ship ship) {

        super(texture, 
              grid, 
              ID,
              SCORE_VALUE, 
              PATH_INTERVAL, 
              MAX_SPEED,
              ACCELERATION,
              ROTATE_RATE);
        mShip = ship;
        mPath = null;

    }

    /// Update the target to pursue the ship.
    protected PVector updateTarget() {

        // Pop first element if the point in the path has been reached.
        if (mPath != null && !mPath.isEmpty() && mPath.get(0).equals(mCoords)) {

            mPath.remove(0); 

        }
        PVector target = null;

        // If not in same cell as ship, use next cell in path as target (as long as there is a
        // path).
        if (!mCoords.equals(mShip.getCoords()) && mPath != null && !mPath.isEmpty()) {
            
            target = mGrid.getTranslation(mPath.get(0));

        // Otherwise, move directly towards the ship.
        } else {

            target = mShip.getTranslation();

        }
        return  target;

    }

    /// Update path leading towards ship.
    protected void updatePath() {

        mPath = mGrid.getShortestPath(mCoords, mShip.getCoords());

    }

}

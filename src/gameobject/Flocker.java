package gameobject;

import java.util.ArrayList;
import processing.core.PVector;
import grid.Grid;
import grid.Coordinates;
import texture.Texture;
import random.Randomiser;
import scene.EnemyManager;
import config.Config;


/// Enemy that finds another  enemy then flocks around it.
public class Flocker extends PathEnemy {

    // Constants.
    public static final int ID = 3;
    private static final int SCORE_VALUE = 5;
    private static final float PATH_INTERVAL = 1f;
    private static final float MAX_SPEED = 300f;
    private static final float ACCELERATION = 1000f;
    private static final float ROTATE_RATE = 180f;

    /// Enemies to flock around.
    private EnemyManager mEnemies;

    /// Enemy the flocker is currently pursuing to flock around.
    private Enemy mTargetEnemy;

    /// Grid cells making up the path to the target enemy.
    private ArrayList<Coordinates> mPath;

    /// Whether the flocker is currently seeking for an enemy to flock around.
    private boolean mSeeking;

    /// Initialise the flocker.
    public Flocker(Texture texture, Grid grid, EnemyManager enemies) {

        // Move config usage to scene??
        super(texture, 
              grid, 
              ID, 
              SCORE_VALUE, 
              PATH_INTERVAL, 
              MAX_SPEED,
              ACCELERATION,
              ROTATE_RATE);
        mEnemies = enemies;
        mTargetEnemy = null;
        mPath = null;
        mSeeking = true;

    }

    /// Update the flocker's target.
    protected PVector updateTarget() {

        // If there is no target enemy or it has been destroyed, there is no target 
        // for the flocker.
        if (mTargetEnemy == null || mTargetEnemy.isDestroyed()) {

            mTargetEnemy = null;
            mPath = null;
            mSeeking = true;
            return null;

        }

        // Attempt to follow path to enemy if seeking.
        if (mSeeking) {

            // Remove the next path cell if it has been reached.
            if (mPath != null && !mPath.isEmpty() && mCoords.equals(mPath.get(0)) ) {

                mPath.remove(0);

            }

            // Cease seeking if reached enemy position.
            if (mCoords.equals(mTargetEnemy.getCoords())) {

                mSeeking = false;

            // If no paths in cell to choose from there is no target.
            } else if (mPath == null || mPath.isEmpty()) {

                return null; 

            // Otherwise move towards the next cell in the path.
            } else {

                return mGrid.getTranslation(mPath.get(0));

            }

        }

        // If not seeking move towards target enemy to flock around it.
        return mTargetEnemy.getTranslation();

    }

    // Updates the enemy's current pathing.
    protected void updatePath() {

        /// Choose a new target enemy if there is currently one or it has been destroyed.
        if (mTargetEnemy == null || mTargetEnemy.isDestroyed()) {

            mTargetEnemy = getRandomEnemy();
            mPath = null;
            mSeeking = true;

        }

        /// If there is a target enemy and the flocker is seeking, update the path
        /// to that enemy.
        if (mTargetEnemy != null && 
            !mTargetEnemy.isDestroyed() && 
            mSeeking) {

            Coordinates targetCoords = mTargetEnemy.getCoords();

            if (targetCoords != null) {

                mPath = mGrid.getShortestPath(mCoords, mTargetEnemy.getCoords());

            }

        }

    }

    /// Choose a random enemy from the target manage if possible.
    /// \return the chosen enemy or null if no enemy was chosen.
    private Enemy getRandomEnemy() {

        if (mEnemies.size() == 0) {

            return null;

        }
        return mEnemies.get(Randomiser.randomInt(0, mEnemies.size() - 1));
    }

}

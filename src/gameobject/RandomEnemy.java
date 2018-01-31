package gameobject;

import processing.core.PVector;
import grid.Grid;
import grid.Coordinates;
import texture.Texture;
import random.Randomiser;
import config.Config;


/// Random enemy that chooses a random point to move towards.
public class RandomEnemy extends PathEnemy {

    // Constants.
    public static final int ID = 1;
    private static final int SCORE_VALUE = 5;
    private static final float PATH_INTERVAL = 3f;
    private static final float MAX_SPEED = 300f;
    private static final float ACCELERATION = 300f;
    private static final float ROTATE_RATE = 90f;

    /// Current target.
    private PVector mTarget;

    /// Initialise enemy.
    public RandomEnemy(Texture texture, Grid grid) {

        super(texture, 
              grid, 
              ID, 
              SCORE_VALUE, 
              PATH_INTERVAL, 
              MAX_SPEED,
              ACCELERATION,
              ROTATE_RATE);

        PVector target = new PVector(0f, 0f);

    }

    /// Get target position.
    protected PVector updateTarget() {

        return mTarget.copy();

    }

    /// Choose a new random position to move towards on path update.
    protected void updatePath() {

        float x = Randomiser.randomFloat(0f, Config.AREA_WIDTH);
        float y = Randomiser.randomFloat(0f, Config.AREA_HEIGHT);
        mTarget = new PVector(x, y);

    }

}

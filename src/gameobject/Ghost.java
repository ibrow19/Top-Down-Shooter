package gameobject;

import processing.core.PVector;
import grid.Grid;
import grid.Coordinates;
import texture.Texture;


/// Enemy that moves in a straight line across the play area 
/// that is not effected by collisions.
public class Ghost extends Enemy {

    // Constants.
    public static final int ID = 4;
    private static final int SCORE_VALUE = 25;
    private static final float SPEED = 300f;

    /// Initialise ghost.
    public Ghost(Texture texture, Grid grid) {

        super(texture, 
              grid, 
              ID,
              SCORE_VALUE);

        mCanCollide = false;

    }

    
    /// Update the ghost by moving it in the direction it is facing.
    /// \param time passed since the last update.
    public void updateCurrent(float delta) {

        PVector velocity = new PVector(0f, -SPEED * delta);
        velocity.rotate(getRadianRotation());
        translate(velocity);

    }

}

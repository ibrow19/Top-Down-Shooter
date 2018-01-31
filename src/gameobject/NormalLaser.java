package gameobject;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PVector;
import grid.Grid;
import grid.Cell;
import texture.Texture;


/// Normal laser that is destroyed when it hits enemies or asteroids.
public class NormalLaser extends Laser {

    /// Constant laser type ID.
    public static final int ID = 1;

    /// Initialise the laser.
    public NormalLaser(Texture texture, Grid grid, PVector velocity) {

        super(texture, grid, ID, velocity);

    }

    /// Destroy laser on collision.
    protected void onCollision(CollidableObject collidable) {
        
        destroy();
        
    }

}

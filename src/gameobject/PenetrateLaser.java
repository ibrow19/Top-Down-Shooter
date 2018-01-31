package gameobject;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PVector;
import grid.Grid;
import grid.Cell;
import texture.Texture;


/// Laser that penetrates enemies and asteroids.
public class PenetrateLaser extends Laser {

    /// Constant laser type ID.
    public static final int ID = 2;

    /// Initialise the laser.
    public PenetrateLaser(Texture texture, Grid grid, PVector velocity) {

        super(texture, grid, ID, velocity);

    }

    /// Do nothing on collision.
    protected void onCollision(CollidableObject collidable) {}

}

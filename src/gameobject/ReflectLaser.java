package gameobject;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PVector;
import grid.Grid;
import grid.Cell;
import texture.Texture;


/// Laser that reflects off enemies and asteroids.
public class ReflectLaser extends Laser {

    /// Constant laser type ID.
    public static final int ID = 3;

    /// Initialise the laser.
    public ReflectLaser(Texture texture, Grid grid, PVector velocity) {

        super(texture, grid, ID, velocity);

    }

    /// Reflect laser off a collidable object.
    /// \param collidable object to reflect off.
    protected void onCollision(CollidableObject collidable) {

        PVector collidePos = collidable.getTranslation();
        PVector distance = PVector.sub(collidePos, getTranslation());
        distance.normalize();
        mVelocity = PVector.mult(distance, -mVelocity.mag());
        distance.mult(collidable.getRadius() + getRadius());
        setTranslation(collidePos.sub(distance));

    }

}

package camera;

import processing.core.PVector;
import transform.Transformable;
import config.Config;

/// Camera that can zoom in and out and be centred on a position.
public class Camera extends Transformable {

    // Constants.
    private static final int BASE_SCALE = 5;
    private static final int MIN_SCALE = 5;
    private static final int MAX_SCALE = 10;
    private static final float SCALE_FACTOR = 0.1f;

    /// Scaling multiplier.
    private int mScale;

    /// Initialise scaling.
    public Camera() {

        mScale = BASE_SCALE;
        updateScale();

    }

    /// Increase camera zoom.
    public void increaseScale() {

        if (mScale < MAX_SCALE) {

            ++mScale;
            updateScale();

        }

    }

    /// Decrease camera zoom.
    public void decreaseScale() {

        if (mScale > MIN_SCALE) {

            --mScale;
            updateScale();

        }

    }

    /// Centre camera on a position.
    /// \param pos the position to centre the camera on.
    public void centre(PVector pos) {

        float xScale = getXScale();
        float yScale = getYScale();

        float windowWidth = Config.WINDOW_WIDTH;
        float windowHeight = Config.WINDOW_HEIGHT;
        float areaWidth = Config.AREA_WIDTH * xScale;
        float areaHeight = Config.AREA_HEIGHT * yScale;

        float newX = -pos.x * xScale + windowWidth / 2f;
        float newY = -pos.y * yScale + windowHeight / 2f;

        // Move camera back if it would go out of bounds.
        if (newX > 0f) {

            newX = 0f;

        } else if (newX < (windowWidth - areaWidth)) {

            newX = windowWidth - areaWidth;

        }
        if (newY > 0f) {

            newY = 0f;

        } else if (newY < (windowHeight - areaHeight)) {

            newY = windowHeight - areaHeight;

        }
        setTranslation(newX, newY);

    }

    /// Update camera scaling.
    private void updateScale() {

        float scaling = mScale * SCALE_FACTOR;
        setScale(scaling, scaling);

    }

}

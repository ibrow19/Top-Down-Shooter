package rect;

/// A rectangle.
public class Rect {

    /// x coordinate of rectangle's top left corner.
    public float x;    

    /// y coordinate of rectangle's top left corner.
    public float y;    

    /// Width of the rectangle.
    public float width;    

    /// Height of the rectangle.
    public float height;    

    /// Initialise properties of rectangle.
    public Rect(float x, float y, float width, float height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    /// Copy the rectangle.
    /// \return a copy of the rectangle.
    public Rect copy() {

        return new Rect(x, y, width, height);
    
    }

    /// Check equality of rectangles.
    /// \param other rectangle to compare equality with.
    /// \return whether the rectangle are equal.
    public boolean equals(Object other) {

        boolean result = false;
        if (other instanceof Rect) {
            
            Rect rect = (Rect)other;
            result = (x == rect.x) &&
                     (y == rect.y) &&
                     (width == rect.width) &&
                     (height == rect.height);

        }
        return result;

    }

}

package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import gameobject.TextObject;


/// State displaying score and health.
public class StatusState extends SceneState {

    /// Text showing score and health.
    private final TextObject mStatus;

    /// Initialise status text.
    public StatusState(Context context) {

        super(context);
        int textSize = 25;
        boolean centred = false;
        mStatus = new TextObject(textSize, centred);
        updateStatus();

    }

    /// Update status text.
    /// \param delta time since last update.
    public SceneState update(float delta) { 

        super.update(delta);
        updateStatus();
        return this;

    }

    /// Render the game and the status.
    /// \param core processing core to render game with.
    public void render(PApplet core) {

        super.render(core);
        mStatus.render(core);

    }

    /// Update the status with the current score and health.
    private void updateStatus() {

        mStatus.setText("Score: " + mContext.score + "\nHealth: " + mContext.ship.getHealth());

    }

}

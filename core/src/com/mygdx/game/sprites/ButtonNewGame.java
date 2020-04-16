package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.base.ScaledButton;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;

import com.mygdx.game.screen.GameScreen;

public class ButtonNewGame extends ScaledButton {

    private static final float MAX_SCALE = 1.05f;
    private static final float MIN_SCALE = 1f;
    private static final float ANIMATE_INTERVAL = 0.02f;

    private GameScreen gameScreen;
    private MainShip shipMain;

    private boolean isGrow;

    private float animateTimer;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen, MainShip shipMain) throws GameException {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
        this.shipMain = shipMain;
        this.isGrow = true;
    }


    @Override
    public void update(float delta) {

        animateTimer += delta;
        if (animateTimer < ANIMATE_INTERVAL) {
            return;
        }
        animateTimer = 0f;
        if (isGrow) {
            scale += delta;
            if (scale >= MAX_SCALE) {
                scale = MAX_SCALE;
                isGrow = false;
            }
        } else {
            scale -= delta;
            if (scale <= MIN_SCALE) {
                scale = MIN_SCALE;
                isGrow = true;
            }
        }
    }

    @Override
    public void action() {
        shipMain.newGame();
        gameScreen.startNewGAme();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(-0.1f);
    }
}

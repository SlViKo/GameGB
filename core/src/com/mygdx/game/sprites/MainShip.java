package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;

public class ShipMain extends Sprite {

    private final int LEFT_KEY = 21;
    private final int RIGHT_KEY = 22;
    private final float STEP_SHIP = 0.04f;
    private final float HIGHT_SHIP = 0.1f;
    
    
    
    private Rect worldBounds;
    private Vector2 v;


    public ShipMain(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("main_ship"));
        this.regions[0] = new TextureRegion(regions[0], 0, 0, regions[0].getRegionWidth() / 2, regions[0].getRegionHeight());
        v = new Vector2(STEP_SHIP, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HIGHT_SHIP);
        setBottom(worldBounds.getBottom() + 0.02f);
        super.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x > 0) {
            moveShipRight();
        } else {
            moveShipLeft();
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case LEFT_KEY:
                moveShipLeft();
                break;
            case RIGHT_KEY:
                moveShipRight();
                break;
        }
        return false;
    }

    private void moveShipRight() {
        if ((worldBounds.getRight() - getRight()) < v.x) {
            setRight(worldBounds.getRight());
        } else {
            pos.add(v);
        }
    }



    private void moveShipLeft() {
        if ((getLeft() - worldBounds.getLeft()) < v.x) {
            setLeft(worldBounds.getLeft());
        } else {
            pos.sub(v);
        }
    }
}

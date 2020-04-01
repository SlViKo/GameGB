package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;

public class Logo extends Sprite {


    private static final float V_LEN = 0.02f;

    private Vector2 tmp;
    private Vector2 v;
    private Vector2 touch;

    public Logo(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        touch = new Vector2();
        v = new Vector2();
        tmp = new Vector2();
        angle = 50;

    }

    @Override
    public void update(float delta) {
       tmp.set(touch);
       float remainingDistance = (touch.cpy().sub(pos)).len();
       if (remainingDistance > V_LEN) {
           pos.add(v);
       } else {
           v.setZero();
           pos.set(touch);
       }
    }


    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.3f);
    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch = touch;
        v.set(touch.cpy().sub(pos)).setLength(V_LEN);
        return false;
    }


}

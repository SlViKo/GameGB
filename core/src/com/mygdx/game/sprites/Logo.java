package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;

public class Logo extends Sprite {


    private static final float V_LEN = 0.01f;

    private Vector2 pos;
    private Vector2 tmp;
    private Vector2 v;
    private Vector2 touch;

    public Logo(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        pos = new Vector2(0, 0);
        touch = new Vector2(0, 0);
        v = new Vector2();
        tmp = new Vector2();

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
    public void draw(SpriteBatch batch) {
        batch.draw(regions[0], pos.x, pos.y, 0.2f, 0.2f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        v.set(touch.cpy().sub(pos)).setLength(V_LEN);
        return false;
    }


}

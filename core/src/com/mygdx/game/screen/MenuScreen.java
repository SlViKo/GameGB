package com.mygdx.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;


public class MenuScreen extends BaseScreen {

    private Texture img;
    private Vector2 posStart;
    private Vector2 posEnd;
    private Vector2 v;
    private int countStep;
    private float distanceX;
    private float distanceY;
//    private float rotate;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        posStart = new Vector2(0, 0);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        posEnd = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        distanceX = posEnd.x-posStart.x;
        distanceY = posEnd.y-posStart.y;
        calcStep();
        countStep/=4; // изменение скоросит (чем больше цифра, тем больше скорость) для тестирования.
        v = new Vector2((distanceX)/countStep, (distanceY)/countStep);
        // pos.set(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }



    private void update(float delta) {
        if(v!=null && countStep > 0) {
            posStart.add(v);
        }
        countStep--;
//        rotate += 1;
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
//        batch.draw(img, 0, 0);
        batch.draw(img, posStart.x, posStart.y);
//        batch.draw(new TextureRegion(img), pos.x, pos.y, pos.x, pos.y, 250, 250, 1, 1, rotate);
        batch.end();
    }

    private void calcStep() {
        float x = Math.abs(distanceX);
        float y = Math.abs(distanceY);
        if(x > y) {
            countStep = (int) x;
        } else {
            countStep = (int) y;
        }

    }

}


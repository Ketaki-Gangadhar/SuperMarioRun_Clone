package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzyMan;
	Random random;
	int manState =0, pause=0 ;
	float gravity = 0.2f;
	float velocity = 0;
	int many;
	Rectangle manRectangle;
	ArrayList<Integer> coinXs= new ArrayList<Integer>();
	ArrayList<Integer> coinYs= new ArrayList<Integer>();
	ArrayList<Integer> coinXss= new ArrayList<Integer>();
	ArrayList<Integer> coinYss= new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<Rectangle>();
    int CoinCount ;
    Texture coin;
    int BombCount;
    Texture bomb;
   int score;
   BitmapFont font;
   int gameState = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
          background = new Texture("bg.png");
          man = new Texture[4];
          man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		many = Gdx.graphics.getHeight() / 2;
		dizzyMan = new Texture("dizzy-1.png");
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();
         font = new BitmapFont();
         font.setColor(Color.BLACK);
         font.getData().setScale(10);
	}

	public void makeCoin ()
	{
      float height = random.nextFloat() * Gdx.graphics.getHeight();
         coinYs.add((int)height);
         coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb ()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYss.add((int)height);
		coinXss.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameState==1)
		{
			//game is live
			//bomb
			bombRectangle.clear();
			if(BombCount<120)
				BombCount++;
			else
			{
				BombCount=0;
				makeBomb();
			}
			for (int i = 0; i<coinXss.size(); i++)
			{
				batch.draw(bomb,coinXss.get(i),coinYss.get(i));
				coinXss.set(i,coinXss.get(i)-8);
				bombRectangle.add(new Rectangle(coinXss.get(i),coinYss.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			coinRectangle.clear();
			//coin
			if(CoinCount<50)
				CoinCount++;
			else
			{
				CoinCount=0;
				makeCoin();
			}
			for (int i = 0; i<coinXs.size(); i++)
			{
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-4);
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}

			if(Gdx.input.justTouched())
			{
				velocity=-10;
			}

			if(pause<8)
			{
				pause++;
			}
			else {
				pause = 0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity+=gravity;

			many -=velocity;

			if(many<=0)
				many=0;
		}
		else if(gameState==0)
		{
			//waiting to start
			if(Gdx.input.justTouched())
			{
				gameState=1;
			}
		}
		else if(gameState==2)
		{
			//game is over
			if(Gdx.input.justTouched())
			{
				gameState=1;

				many = Gdx.graphics.getHeight() / 2;
				score=0;
				velocity=0;
				coinYs.clear();
				coinXs.clear();
				coinRectangle.clear();
				CoinCount=0;
				coinYss.clear();
				coinXss.clear();
				bombRectangle.clear();
				BombCount=0;
			}
		}

    if(gameState==2) {

		batch.draw(dizzyMan,Gdx.graphics.getWidth()/2 - man[0].getWidth()/2, many);

	}
    else{
        batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[0].getWidth()/2, many);}


        manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[0].getWidth()/2, many,man[manState].getWidth(),man[manState].getHeight());
        for(int i =0; i<coinRectangle.size(); i++)
		{
			if(Intersector.overlaps(manRectangle,coinRectangle.get(i))) {
				score++;

				coinRectangle.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for(int i =0; i<bombRectangle.size(); i++) {
			if (Intersector.overlaps(manRectangle, bombRectangle.get(i))) {
                 gameState=2;

			}
		}
           font.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}

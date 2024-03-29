package ipsum.games.pong;
// Pong.java by Paul Falstad, www.falstad.com
// Copyright (C) 1996 or something

// I had all kinds of problems getting sleep() to work for values less
// than 50 under Windows, so the frame rate isn't as good as it could be...

import ipsum.gifunctions.GIPongFunction;
import ipsum.gofunctions.GOPongFunction;

import java.awt.*;
import java.applet.Applet;

@SuppressWarnings("serial")
public class Pong extends Applet implements Runnable {
    
    Thread engine = null;

    Paddle paddles[];
    Ball ball;
    Dimension winSize;
    Font scoreFont, smallBannerFont, largeBannerFont;
    Image dbimage;
    
    public static final int defaultPause = 100;
    int pause;
    
    public String getAppletInfo() {
	return "Pong by Paul Falstad";
    }

    public void init() {
	setBackground(Color.white);
        @SuppressWarnings("deprecation")
		Dimension d = winSize = size();
        paddles = new Paddle[2];
    	paddles[0] = new Paddle(10, 40, 120, 50);
    	paddles[1] = new Paddle(d.width/2, d.height-40, d.height-120, 40);
		paddles[0].setRange(0, d.width-1);
		paddles[1].setRange(0, d.width-1);
		paddles[0].setColorBase(1, 0, 0);
		paddles[1].setColorBase(0, 0, 1);
		ball = new Ball(new Point(d.width/2, d.height/2), 9, this);
		ball.setRange(0, d.width-1, 0, d.height-1);
		pause = defaultPause;
		scoreFont = new Font("TimesRoman", Font.BOLD, 36);
		largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);
		smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);
		dbimage = createImage(d.width, d.height);
		try {
		    String param = getParameter("PAUSE");
		    if (param != null)
			pause = Integer.parseInt(param);
		} catch (Exception e) { }
		ball.startPlay();
    }

    public void updateScore(int which) {
    	paddles[1-which].score++;
    	ball.startPlay();
    }

    public void run() {
		while (true) {
		    try {
			for (int i = 0; i != 3; i++)
			    step();
			repaint();
	    	Thread.currentThread();
			Thread.sleep(pause);
		    } catch (Exception e) {}
		}
    }

    public void step() {
    	paddles[0].setTarget((int)(paddles[0].getVarPos()+(GOPongFunction.paddle2-GOPongFunction.paddle1)*400));
    	GIPongFunction.playerPaddle = paddles[0].getVarPos();
		paddles[1].setTarget(ball.getPaddlePos());
		GIPongFunction.enemyPaddle = (double)ball.getPaddlePos()/400;
		paddles[0].move();
		if (ball.inPlay)
		    paddles[1].move();
		if (ball.bounce(paddles[0]))
		    paddles[0].bounceIt();
		if (ball.bounce(paddles[1]))
		    paddles[1].bounceIt();
		ball.move();
		//System.out.println((double)ball.pos.x/400);
		GIPongFunction.ballx = (double)ball.pos.x/400;
		GIPongFunction.bally = (double)ball.pos.y/400;
    }

    public void centerString(Graphics g, FontMetrics fm, String str, int ypos) {
	g.drawString(str, (winSize.width-fm.stringWidth(str))/2, ypos);
    }

    public void drawBanner(Graphics g) {
	g.setFont(largeBannerFont);
	FontMetrics fm = g.getFontMetrics();
	g.setColor(Color.red);
	centerString(g, fm, "PONG", 100);
	g.setColor(Color.blue);
	g.setFont(scoreFont);
	fm = g.getFontMetrics();
	centerString(g, fm, "by Paul Falstad", 160);
	g.setFont(smallBannerFont);
	fm = g.getFontMetrics();
	centerString(g, fm, "www.falstad.com", 190);
	g.setColor(Color.black);
	centerString(g, fm, "Press mouse button to start", 300);
    }

    public void update(Graphics realg) {
	Graphics g = dbimage.getGraphics();
	g.setColor(getBackground());
	g.fillRect(0, 0, winSize.width, winSize.height);
	g.setColor(getForeground());
	if (!ball.inPlay) {
	    g.setFont(scoreFont);
	    FontMetrics fm = g.getFontMetrics();
	    if (paddles[0].score == 0 && paddles[1].score == 0)
		drawBanner(g);
	    else
		for (int i = 0; i != 2; i++) {
        	    String score = Integer.toString(paddles[i].score);
		    g.setColor(paddles[i].scoreColor);
		    centerString(g, fm, score, paddles[i].scorePos);
		}
	}
	for (int i = 0; i != 2; i++)
    	    paddles[i].draw(g);
	ball.draw(g);
	realg.drawImage(dbimage, 0, 0, this);
    }

    public void start() {
		if (engine == null) {
		    engine = new Thread(this);
		    engine.start();
		}
    }

    @SuppressWarnings("deprecation")
	public void stop() {
		if (engine != null && engine.isAlive()) {
		    engine.stop();
		}
		engine = null;
    }

    @SuppressWarnings("deprecation")
	public boolean handleEvent(Event evt) {
	if (evt.id == Event.MOUSE_MOVE) {
	    //paddles[0].setTarget(evt.x);
		//System.out.println(evt.x);
	    return true;
	} else if (evt.id == Event.MOUSE_DOWN) {
	    ball.startPlay();
	    return true;
	} else {	    
	    return super.handleEvent(evt);
	}
    }
    
}


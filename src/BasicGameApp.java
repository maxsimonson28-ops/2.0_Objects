//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section
//step 1: implement keylistener
public class BasicGameApp implements Runnable, KeyListener, MouseListener {

   //Variable Definition Section
   //Declare the variables used in the program 
   //You can set their initial values too
   
   //Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

   //Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
   public JPanel panel;
   
	public BufferStrategy bufferStrategy;
	public Image astroPic;
    public Image asteroidPic;
    public Image backgroundPic;

   //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	private Astronaut astro;
    private Astronaut astro2;
    private Asteroid asteroid1;
    private Asteroid asteroid2;
    public Asteroid[] asteroids;


   // Main method definition
   // This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method  
	}


   // Constructor Method
   // This has the same name as the class
   // This section is the setup portion of the program
   // Initialize your variables and construct your program objects here.
	public BasicGameApp() {
      
      setUpGraphics();
       //range is 1-10
      int randx = (int)(Math.random()*10)+1;
      int randy = (int)(Math.random()*10)+1;

      randx = (int)(Math.random()*999)+1;
      randy = (int)(Math.random()*699)+1;

      //variable and objects
      //create (construct) the objects needed for the game and load up 
		astroPic = Toolkit.getDefaultToolkit().getImage("astronaut.png");
        asteroidPic = Toolkit.getDefaultToolkit().getImage("asteroid.jpeg");//load the picture
        backgroundPic = Toolkit.getDefaultToolkit().getImage("starrysky.jpg");
        astro = new Astronaut(500,350);
        astro2 = new Astronaut(randx,randy);
        asteroid1 = new Asteroid(43,255);
        asteroid1.dx=-asteroid1.dx;
        asteroid2 = new Asteroid(43,249);

        asteroids = new Asteroid[5];
        for(int x = 0; x < asteroids.length; x++){
            asteroids[x] = new Asteroid((int)(Math.random()*1000),(int)(Math.random()*700));


        }



	}// BasicGameApp()

   
//*******************************************************************************
//User Method Section
//
// put your code to do things here.

   // main thread
   // this is the code that plays the game after you set things up
	public void run() {

      //for the moment we will loop things forever.
		while (true) {

         moveThings();  //move all the game objects
         render();  // paint the graphics
         pause(20); // sleep for 10 ms
		}
	}






	public void moveThings()
	{
      //calls the move( ) code in the objects
		astro.move();
        astro2.move();
        asteroid1.move();
        asteroid2.move();
        crashing();
        for(int i = 0; i < asteroids.length; i++){
            asteroids[i].move();
        }

	}

    public void crashing() {
        if (astro.hitbox.intersects(astro2.hitbox)) {
            System.out.println("CRASH!!!!");
            astro.dy = -astro.dy;
            astro2.dy = -astro2.dy;
            astro2.isAlive = false;
        }
        if(asteroid1.hitbox.intersects(asteroid2.hitbox)&& asteroid1.isCrashing == false) {
            System.out.println("Explode!!");
            asteroid1.height +=50;
            asteroid1.isCrashing = true;
            asteroid1.isAlive = false;

        }
        if(!asteroid1.hitbox.intersects(asteroid2.hitbox)){
           // System.out.println("Nothing,");
            asteroid1.isCrashing = false;
        }
    }


   //Pauses or sleeps the computer for the amount specified in milliseconds
   public void pause(int time ){
   		//sleep
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {

			}
   }

   //Graphics setup method
   private void setUpGraphics() {
      frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.
   
      panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
      panel.setLayout(null);   //set the layout
   
      // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
      // and trap input events (Mouse and Keyboard events)
      canvas = new Canvas();

      //step 2: set canvas as the key listener
       canvas.addKeyListener(this);
       canvas.addMouseListener(this);

      canvas.setBounds(0, 0, WIDTH, HEIGHT);
      canvas.setIgnoreRepaint(true);
   
      panel.add(canvas);  // adds the canvas to the panel.
   
      // frame operations
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
      frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
      frame.setResizable(false);   //makes it so the frame cannot be resized
      frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!
      
      // sets up things so the screen displays images nicely.
      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      canvas.requestFocus();
      System.out.println("DONE graphic setup");
   
   }


	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);


        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
      //draw the image of the astronaut
		g.drawImage(astroPic, astro.xpos, astro.ypos, astro.width, astro.height, null);
        if(astro2.isAlive == true){
        g.drawImage(astroPic,astro2.xpos,astro2.ypos,astro2.width,astro2.height,null);}
        g.drawImage(asteroidPic, asteroid1.xpos, asteroid1.ypos, asteroid1.width, asteroid1.height, null);
        if(asteroid1.isAlive == true){
        g.drawImage(asteroidPic,asteroid2.xpos,asteroid2.ypos,asteroid2.width,asteroid2.height,null);}




        for(int z = 0; z < asteroids.length; z++){
            g.drawImage(asteroidPic,asteroids[z].xpos,asteroids[z].ypos,asteroids[z].width,asteroids[z].height,null);
        }
        g.dispose();
        bufferStrategy.show();

	}

    //step 3: add key listener methods

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("key typed" + e.getKeyCode());

        if(e.getKeyCode()== 38){
            System.out.println("pressed up arrow");
            //astro.ypos = astro.ypos - 25;
            astro.dy = -Math.abs(astro.dy);
            astro.dy = -2;
        }
        if(e.getKeyCode()==40){
            System.out.println("pressed down arrow");
            astro.dy = Math.abs(astro.dy);
            astro.dy = 2;
        }
        if(e.getKeyCode()==37){
            System.out.println("pressed left arrow");
            astro.dx = -Math.abs(astro.dx);
        }
        if(e.getKeyCode()==39){
            System.out.println("pressed right arrow");
            astro.dx = Math.abs(astro.dx);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()== 38){
            System.out.println("pressed up arrow");
            //astro.ypos = astro.ypos - 25;
            astro.dy = -Math.abs(astro.dy);
            astro.dy =0;
        }
        if(e.getKeyCode()==40){
            System.out.println("pressed down arrow");
            astro.dy = Math.abs(astro.dy);
            astro.dy = 0;
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getPoint());
        astro2.xpos = e.getX();
        astro2.ypos = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("entered!!");
        astro.dx = 10;
        astro.dy = 10;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("exited!!");
        astro.dx = 0;
        astro.dy = 0;


    }
}
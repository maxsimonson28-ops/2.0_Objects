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
    public Image trashPic;
    public Image backgroundPic;
    public Image startPic;
    public Image losePic;
    public Image winPic;                       //these are all my images
    public boolean startGame;           //this allows me to have a start screen
    public Rectangle startHitbox;
    public Rectangle endHitbox;
    public Rectangle mouseHitbox;       //these are all my hitboxes


   //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	private Astronaut astro;
    private Asteroid asteroid1;
    private Asteroid asteroid2;
    public Asteroid[] asteroids;
    public Trash trash1;
    public Trash[] bunchOfTrash;


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


      //variable and objects
      //create (construct) the objects needed for the game and load up
        //load the pictures
		astroPic = Toolkit.getDefaultToolkit().getImage("astronaut.png");
        asteroidPic = Toolkit.getDefaultToolkit().getImage("asteroid.jpeg");
        trashPic = Toolkit.getDefaultToolkit().getImage("trash.jpeg");
        backgroundPic = Toolkit.getDefaultToolkit().getImage("starrysky.jpg");
        startPic = Toolkit.getDefaultToolkit().getImage("startScreen.png");
        startGame = false;  //makes it so the start screen shows
        startHitbox = new Rectangle(318,404,361,40);
        endHitbox = new Rectangle(304,620,395,40);
        losePic = Toolkit.getDefaultToolkit().getImage("missionFailed.png");
        winPic = Toolkit.getDefaultToolkit().getImage("astroWin.png");
        astro = new Astronaut(500,630);//starting position for astronaut


        asteroid1 = new Asteroid(43,255);
        asteroid1.dx=-asteroid1.dx;
        asteroid2 = new Asteroid(43,249);
        //create 2 asteroids

        trash1 = new Trash(34,323); //creates the trash

        asteroids = new Asteroid[8]; //makes a bunch of asteroids
        for(int x = 0; x < asteroids.length; x++){
            asteroids[x] = new Asteroid((int)(Math.random()*1000),(int)(Math.random()*600)); //creates asteroids at random points

        }

        bunchOfTrash = new Trash[4];
        for(int x = 0; x < bunchOfTrash.length; x++) {
            bunchOfTrash[x] = new Trash((int) (Math.random() * 500)-400, (int) (Math.random() * 600));
        } //creates trash at random points




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
        asteroid1.move();
        asteroid2.move();
        trash1.move();
        crashing();
        for(int i = 0; i < asteroids.length; i++){
            asteroids[i].move(); //allows all my asteroids in array to move
        }
        for(int i = 0; i < bunchOfTrash.length; i++){
            bunchOfTrash[i].move(); //allows all my trash in array to move
        }

	}

    public void crashing() {
        for(int x = 0; x < asteroids.length; x++){
            if(asteroids[x].hitbox.intersects(astro.hitbox)){
                astro.isAlive = false;

                System.out.println("Crashing");
                //this if statement makes the astronaut die if it hits an asteroid
            }
        }
        if(trash1.hitbox.intersects(astro.hitbox)){
            trash1.isCrashing = true;
            trash1.isAlive = false;

            if(astro.dx < 0){
                astro.dx = astro.dx + 7;
            }
            else
            {astro.dx = astro.dx - 7;}
            if(astro.dy < 0){
                astro.dy = astro.dy + 7;
            }
            else
            {astro.dy = astro.dy - 7;}
            //this if statement makes the astronaut go out of control if it eats the trash
        }
        for(int x = 0; x < bunchOfTrash.length; x++){
            if(bunchOfTrash[x].hitbox.intersects(astro.hitbox)){
                bunchOfTrash[x].isCrashing = true;
                bunchOfTrash[x].isAlive = false;

                if(astro.dx < 0){
                    astro.dx = astro.dx + 7;
                }
                else
                {astro.dx = astro.dx - 7;}
                if(astro.dy < 0){
                    astro.dy = astro.dy + 7;
                }
                else
                {astro.dy = astro.dy - 7;}
            }
            //this if statement makes the astronaut go out of control if it eats the trash
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


        if (startGame == false) {
            g.drawImage(startPic, 0, 0, WIDTH, HEIGHT, null);
            g.drawRect(startHitbox.x,startHitbox.y,startHitbox.width,startHitbox.height);
            //draws the start screen and allows player to hit the start game button
        }
        else {

            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
            //draw the image of the background
            if (astro.isAlive == true) {
                g.drawImage(astroPic, astro.xpos, astro.ypos, astro.width, astro.height, null);
                //draw the image of the astronaut
            }

             //draws asteroid
            if (asteroid1.isAlive == true) {
                g.drawImage(asteroidPic, asteroid2.xpos, asteroid2.ypos, asteroid2.width, asteroid2.height, null);
            }
            //draws trash
            if (trash1.isAlive == true) {
                g.drawImage(trashPic, trash1.xpos, trash1.ypos, trash1.width, trash1.height, null);
            }

            //draws asteroids in array
            for (int z = 0; z < asteroids.length; z++) {
                g.drawImage(asteroidPic, asteroids[z].xpos, asteroids[z].ypos, asteroids[z].width, asteroids[z].height, null);
            }
            //draws trash in array
            for (int z = 0; z < bunchOfTrash.length; z++) {
                g.drawImage(trashPic, bunchOfTrash[z].xpos, bunchOfTrash[z].ypos, bunchOfTrash[z].width, bunchOfTrash[z].height, null);
            }
            if (astro.isAlive == false) {
                g.drawImage(losePic,0,0,WIDTH,HEIGHT,null);
                g.drawRect(endHitbox.x, endHitbox.y, endHitbox.width, endHitbox.height);
                //if the astronaut dies the losing screen is shown and this allows players to hit try again button
            }
            if (astro.ypos < 60) {
                g.drawImage(winPic,0,0,WIDTH,HEIGHT,null);
                //if the astronaut gets past the asteroids the win screen is shown
            }

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
            astro.dy = -7;
            //code for up arrow movement
        }
        if(e.getKeyCode()==40){
            System.out.println("pressed down arrow");
            astro.dy = Math.abs(astro.dy);
            astro.dy = 7;
            //code for down arrow movement
        }
        if(e.getKeyCode()==37){
            System.out.println("pressed left arrow");
            astro.dx = -10;
            //code for left arrow movement
        }
        if(e.getKeyCode()==39){
            System.out.println("pressed right arrow");
            astro.dx = 10;
            //code for right arrow movement
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()== 38){
            System.out.println("pressed up arrow");
            //astro.ypos = astro.ypos - 25;
            astro.dy = -Math.abs(astro.dy);
            astro.dy =0;
            //code for up arrow movement
        }
        if(e.getKeyCode()==40){
            System.out.println("pressed down arrow");
            astro.dy = Math.abs(astro.dy);
            astro.dy = 0;
            //code for down arrow movement
        }
        if(e.getKeyCode()==37){
            System.out.println("pressed left arrow");
            astro.dx = Math.abs(astro.dx);
            astro.dx = 0;
            //code for left arrow movement
        }
        if(e.getKeyCode()==39){
            System.out.println("pressed right arrow");
            astro.dx = -Math.abs(astro.dx);
            astro.dx = 0;
            //code for right arrow movement
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = MouseInfo.getPointerInfo().getLocation();

        int x = (int) p.getX();
        int y = (int) p.getY();

        System.out.println("Global Mouse Position: " + x + ", " + y); //prints where the mouse is when pressed

        mouseHitbox = new Rectangle(x,y);
        //all the code above creates a mouse hitbox

        if(mouseHitbox.intersects(startHitbox)){
            startGame = true; //if button is pressed start the game
        }
        if(mouseHitbox.intersects(endHitbox)){
            startGame = true;
            astro.isAlive = true;
            System.out.println("start again");
            astro.xpos = 526;
            astro.ypos = 600;
            astro.dx = 0;
            astro.dy = 0;
            astro.hitbox = new Rectangle(astro.xpos,astro.ypos,astro.width,astro.height);
            //resetting the game after the user presses "try again"
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("entered!!");

    }

    @Override
    public void mouseExited(MouseEvent e) {


    }
}
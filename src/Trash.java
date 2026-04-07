import java.awt.*;

public class Trash {
    public String name;                //holds the name of the hero
    public int xpos;                //the x position
    public int ypos;                //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;
    public Rectangle hitbox;
    public boolean isCrashing;


    public Trash(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx = 10;
        dy = 0;
        width = 35;
        height = 35;
        isAlive = true;
        hitbox = new Rectangle(xpos, ypos, width, height);
    }

    public void move() {

        if(xpos>= 1000-width){
            xpos = 50;
        }

        if(xpos<= 0){
            xpos = 859-width;
        }

        if(ypos <= 0){
            ypos = 700-height;
        }
        if (ypos >= 750-height){
            ypos=1;
        }


        xpos = xpos + dx;
        ypos = ypos + dy;

        hitbox = new Rectangle(xpos,ypos,width,height);



    }
}

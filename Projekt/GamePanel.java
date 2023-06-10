import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

class GamePanel extends JPanel implements ActionListener
{
    static final int width=600;
    static final int height=600;
    static final int field_size=25;
    static final int game_fields=(width*height)/field_size;
    static final int delay=100;
    SnakeHead snakeHead;
    ArrayList<SnakePart> snakeParts;
    Food food;
    int bodyparts=6;
    int food_eaten=0;
    char direction='R';
    boolean running=false;
    Timer timer;
    Random random;
    GamePanel()
    {
        random=new Random();
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start_game();
    }
    public void start_game()
    {
        snakeHead=new SnakeHead(width/2,height/2);
        snakeParts=new ArrayList<>();
        for(int i=0; i<bodyparts; i++)
        {
            snakeParts.add(new SnakePart(snakeHead.getX()-i*field_size,snakeHead.getY()));
        }
        new_food();
        running=true;
        timer=new Timer(delay,this);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        if(running)
        {
            g.setColor(Color.red);
            g.fillOval(food.x,food.y,field_size,field_size);
            for(int i=0; i<bodyparts; i++)
            {
                if(i==0)
                {
                    g.setColor(Color.green);
                    g.fillRect(snakeHead.getX(),snakeHead.getY(),field_size,field_size);
                }
                else
                {
                    g.setColor(new Color(45,180,0));
                    SnakePart part=snakeParts.get(i);
                    g.fillRect(part.getX(),part.getY(),field_size,field_size);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("Score: "+food_eaten, (width-metrics.stringWidth("Score: "+food_eaten))/2,g.getFont().getSize());
        }
        else
        {
            game_over(g);
        }
    }
    public void new_food()
    {
        int food_x=random.nextInt((int)(width/field_size))*field_size;
        int food_y=random.nextInt((int)(height/field_size))*field_size;
        food=new Food(food_x,food_y);
    }
    public void move()
    {
        int prevX=snakeHead.getX();
        int prevY=snakeHead.getY();
        switch(direction)
        {
            case 'U':
                snakeHead.y-=field_size;
                break;
            case 'D':
                snakeHead.y+=field_size;
                break;
            case 'L':
                snakeHead.x-=field_size;
                break;
            case 'R':
                snakeHead.x+=field_size;
                break;
        }
        if(bodyparts>0)
        {
            snakeParts.get(0).x=prevX;
            snakeParts.get(0).y=prevY;
        }
        for (int i=bodyparts-1; i>0; i--)
        {
            snakeParts.get(i).x=snakeParts.get(i-1).x;
            snakeParts.get(i).y=snakeParts.get(i-1).y;
        }
    }
    public void check_food()
    {
        if((snakeHead.getX()==food.getX()) && (snakeHead.getY()==food.getY()))
        {
            bodyparts++;
            food_eaten++;
            new_food();
            SnakePart tail=snakeParts.get(snakeParts.size()-1);
            int tailX=tail.getX();
            int tailY=tail.getY();
            snakeParts.add(new SnakePart(tailX, tailY));
        }
    }
    public void check_collisions()
    {
        //zderzenie sam z soba
        for(int i=bodyparts-1; i>0; i--)
        {
            SnakePart part=snakeParts.get(i);
            if((snakeHead.getX()==part.getX()) && (snakeHead.getY()==part.getY()))
            {
                running=false;
            }
        }
        //zderzenie z ktorakolwiek granica planszy
        if(snakeHead.getX()<0 || snakeHead.getX()>width || snakeHead.getY()<0 || snakeHead.getY()>height)
        {
            running=false;
        }
        if(!running)
        {
            timer.stop();
        }
    }
    public void game_over(Graphics g)
    {
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Score: "+food_eaten, (width-metrics1.stringWidth("Score: "+food_eaten))/2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans",Font.BOLD,75));
        FontMetrics metrics2=getFontMetrics(g.getFont());
        g.drawString("Game Over", (width-metrics2.stringWidth("Game Over"))/2, height/2);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            move();
            check_food();
            check_collisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    if(direction!='R')
                    {
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L')
                    {
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D')
                    {
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U')
                    {
                        direction='D';
                    }
                    break;
            }
        }
    }
}

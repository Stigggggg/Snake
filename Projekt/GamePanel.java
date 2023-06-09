import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

class GamePanel extends JPanel implements ActionListener
{
    static final int width=600;
    static final int height=600;
    static final int field_size=25;
    static final int game_fields=(width*height)/field_size;
    static final int delay=75;
    final int x[]=new int[game_fields];
    final int y[]=new int[game_fields];
    int bodyparts=6;
    int food_eaten=0;
    int food_x;
    int food_y;
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
            for(int i=0; i<height/field_size; i++)
            {
                g.drawLine(i*field_size,0,i*field_size,height);
                g.drawLine(0,i*field_size,width,i*field_size);
            }
            g.setColor(Color.red);
            g.fillOval(food_x,food_y,field_size,field_size);
            for(int i=0; i<bodyparts; i++)
            {
                if(i==0)
                {
                    g.setColor((Color.green));
                    g.fillRect(x[i],y[i],field_size,field_size);
                }
                else
                {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i],y[i],field_size,field_size);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("Score: "+food_eaten, (width-metrics.stringWidth("Score: "+food_eaten))/2, g.getFont().getSize());
        }
        else
        {
            game_over(g);
        }
    }
    public void new_food()
    {
        food_x=random.nextInt((int)(width/field_size))*field_size;
        food_y=random.nextInt((int)(height/field_size))*field_size;
    }
    public void move()
    {
        for(int i=bodyparts; i>0; i--)
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction)
        {
            case 'U':
                y[0]=y[0]-field_size;
                break;
            case 'D':
                y[0]=y[0]+field_size;
                break;
            case 'L':
                x[0]=x[0]-field_size;
                break;
            case 'R':
                x[0]=x[0]+field_size;
                break;
        }
    }
    public void check_food()
    {
        if((x[0]==food_x) && (y[0]==food_y))
        {
            bodyparts++;
            food_eaten++;
            new_food();
        }
    }
    public void check_collisions()
    {
        //zderzenie sam z soba
        for(int i=bodyparts; i>0; i--)
        {
            if((x[0]==x[i]) && (y[0]==y[i]))
            {
                running=false;
            }
        }
        //zderzenie z ktorakolwiek granica planszy
        if(x[0]<0 || x[0]>width || y[0]<0 || y[0]>height)
        {
            running=false;
        }
        if(running==false)
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

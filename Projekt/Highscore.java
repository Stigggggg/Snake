import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Highscore
{
    static final String file="highscores.txt";
    static final int max_highscores=3;
    ArrayList<Integer> highScores;
    Highscore()
    {
        highScores=new ArrayList<>();
        load_highscores();
    }
    void load_highscores()
    {
        try(BufferedReader reader=new BufferedReader(new FileReader(file)))
        {
            String line;
            while((line=reader.readLine())!=null)
            {
                int score=Integer.parseInt(line);
                highScores.add(score);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error while loading scores");
        }
    }
    void save_highscores()
    {
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(file)))
        {
            for(int score: highScores)
            {
                writer.write(Integer.toString(score));
                writer.newLine();
            }
        }
        catch(IOException e)
        {
            System.out.println("Error while saving scores");
        }
    }
    public void addHighScore(int score)
    {
        highScores.add(score);
        highScores.sort((a,b)->b.compareTo(a));
        if(highScores.size()>max_highscores)
        {
            highScores.remove(highScores.size()-1);
        }
        save_highscores();
    }
    public ArrayList<Integer> get_highscores()
    {
        return highScores;
    }
}

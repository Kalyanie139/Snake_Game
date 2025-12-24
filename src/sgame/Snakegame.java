package sgame;
//The class JFrame is inside the Swing package. And we write javax as it comes from extended package of Java
import javax.swing.JFrame;

public class Snakegame extends JFrame {   

    //default constructor of class.
    Snakegame(){

        //TO give heading use super keyboard
        super("Snake Game");
        //Whatever we will write in Board will be displayed over frame
        add(new Board());  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setLocation(500, 500);     But doesnt allign it to centre hence to align it to centre as per screen size we can use
        //First set window then move to centre orelse it 1st goes to centre and then creates disproportion
        
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    public static void main(String[] args) {
        new Snakegame();  //Object of class
    }
}
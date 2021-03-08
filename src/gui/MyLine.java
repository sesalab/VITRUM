package gui;


import javax.swing.*;
import java.awt.*;

public class MyLine extends JPanel
{

    @Override public void paint(Graphics g)
    {
        //Get the current size of this component
        Dimension d = this.getSize();

        //draw in black
        g.setColor(Color.WHITE);
        //draw a centered horizontal line
        g.drawLine(0,d.height/2,d.width,d.height/2);
    }
}

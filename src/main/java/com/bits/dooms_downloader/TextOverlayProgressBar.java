package com.bits.dooms_downloader;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author DoomDevil
 */
public class TextOverlayProgressBar extends JProgressBar{
    
    public TextOverlayProgressBar(){
        super();
        setStringPainted(false);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        String textString = getString();
        if(textString != null && !textString.isEmpty()){
            Graphics2D g2d = (Graphics2D) g;
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(textString);
            int stringHeight = fontMetrics.getAscent();
            
            int x = (getWidth() - stringWidth) / 2;
            int y = (getHeight() + stringHeight) / 2 - fontMetrics.getDescent();
            
            g2d.setColor(Color.BLACK);
            g2d.drawString(textString, x, y);
        }
    }
}

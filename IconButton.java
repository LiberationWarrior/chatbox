package chatbox;

import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

public class IconButton extends JButton {

    public IconButton(String pathToIcon) {
        try {
            setIcon(new ImageIcon(ImageIO.read(new File(pathToIcon))));
        } catch (IOException e) {
            System.err.println("Följande fil kunde inte hittas: " + pathToIcon);
            e.printStackTrace();
        }
    }
}
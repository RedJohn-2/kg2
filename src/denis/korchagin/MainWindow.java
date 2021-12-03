package denis.korchagin;

import denis.korchagin.utils.MapUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final DrawPanel dp;
    private String nameFile = null;

    public MainWindow() throws HeadlessException {
        dp = new DrawPanel();

        JButton button = new JButton("Открыть файл");
        button.addActionListener(e -> {
            JFileChooser fileOpen = new JFileChooser();
            int ret = fileOpen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpen.getSelectedFile();
                nameFile = file.getAbsolutePath();
                try {
                    dp.setData(MapUtil.getMapFromFile(nameFile));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dp.add(button);
        this.add(dp);

    }

    /*public File getReadingFile() {
        return readingFile;
    }*/
}

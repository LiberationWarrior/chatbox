package chatbox;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class Controller {

    private View view;
    private ArrayList<Client> clients;

    public Controller(View view) {
        this.view = view;
        clients = new ArrayList<Client>();
        view.clientButton.addActionListener(new ClientButtonListener());
        view.serverButton.addActionListener(new ServerButtonListener());
        view.connectButton.addActionListener(new ConnectButtonListener());
        view.sendButton.addActionListener(new SendButtonListener());
        view.receiveButton.addActionListener(new ReceiveButtonListener());
        view.fileButton.addActionListener(new FileButtonListener());
        view.colorButton.addActionListener(new ColorButtonListener());
        view.closeButton.addActionListener(new CloseButtonListener());
        view.serverOptions.addActionListener(new ServerOptionsListener());
        view.tabbedPane.addChangeListener(new TabbedPaneListener());
        //view.tabbedPane.setTabComponentAt(0, createTabPanel(1));

        //Skapa nya knappar och fält för varje tabb!
        view.tabbedPane.addTab("+", null, view.pane, "Create a new chat");
    }

    public void updateTabButtonIndex(int index) {
        for (TabButton button : view.getTabButtons()) {
            if (button.getIndex() > index) {
                button.setIndex(button.getIndex() - 1);
            }
        }
    }

    public void disableConnection() {
        /*
        view.clientButton.setEnabled(false);
        view.serverButton.setEnabled(false);
        view.IPField.setEnabled(false);
        view.portField.setEnabled(false);
        view.serverOptions.setEnabled(false);
        view.passField.setEnabled(false);
         * 
         */
        view.connectButton.setEnabled(true);
        view.sendMsgButton.setEnabled(true);
    }

    //Replace by state boolean!
    public void enableConnection() {
        view.clientButton.setEnabled(true);
        view.serverButton.setEnabled(true);
        view.IPField.setEnabled(true);
        view.portField.setEnabled(true);
        view.serverOptions.setEnabled(true);
        view.passField.setEnabled(true);
        /*
        view.connectButton.setEnabled(false);
        view.sendMsgButton.setEnabled(false);
         * 
         */

        view.clientButton.setBackground(null);
        view.serverButton.setBackground(null);
    }

    public final JPanel createTabPanel() {
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(view.tabField.getText() + " ");
        TabButton btnClose = new TabButton(view.getIcon(),
                view.tabbedPane.getTabCount() - 1);
        view.getTabButtons().add(btnClose);
        btnClose.setPreferredSize(new Dimension(12, 12));
        btnClose.addActionListener(new TabButtonListener());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);
        return pnlTab;
    }

    public class TabbedPaneListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            int i = view.getTabbedPane().getSelectedIndex();
            int j = view.tabbedPane.getTabCount() - 1;
            if (i == j && !view.tabLock) {
                view.tabLock = true;
                if (j >= 0) {
                    view.tabbedPane.remove(j);
                }
                view.tabbedPane.addTab(null, view.createChatBox());
                view.tabbedPane.setTabComponentAt(i, createTabPanel());
                view.tabbedPane.setSelectedIndex(i);
                view.tabbedPane.addTab("+", null, view.pane,
                        "Create a new chat");
                view.tabCount += 1;
                view.tabField.setText("Chat " + String.valueOf(view.tabCount));
                view.tabLock = false;
            }
        }
    }

    public class ServerButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                disableConnection();
                view.serverButton.setBackground(Color.RED);
                Server thr = new Server(Integer.parseInt(
                        view.getPortField().getText()),
                        view);
                thr.start();
            } catch (Exception ex) {
                System.err.println("Ett fel inträffade1: " + ex);
            }
        }
    }

    // Skicka fil med klient
    public class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FileSender thr = new FileSender(view.getIPField().getText(),
                        Integer.parseInt(view.getPortField().getText()),
                        view.getFileField().getText());
                thr.start();
            } catch (Exception ex) {
                System.err.println("Ett fel inträffade2: " + ex);
            }
        }
    }

    // Starta klient
    public class ClientButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                disableConnection();
                view.clientButton.setBackground(Color.GREEN);
                Client thr = new Client(view.getIPField().getText(),
                        Integer.parseInt(view.getPortField().getText()),
                        view);
                thr.start();
                clients.add(thr);
            } catch (Exception ex) {
                System.err.println("Ett fel inträffade3: " + ex);
            }
        }
    }
    // Mottag fil med server

    public class ReceiveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FileReceiver thr = new FileReceiver(Integer.parseInt(
                        view.getPortField().getText()),
                        view.getFileField().getText());
                thr.start();
            } catch (Exception ex) {
                System.err.println("Ett fel inträffade4: " + ex);
            }
        }
    }

    // Stäng av hela programmet
    public class CloseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you "
                    + "want to quit?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "Good choice. "
                        + "Everyone's finger can slip!");
            }
        }
    }

    public class ConnectButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int reply = JOptionPane.showConfirmDialog(null,
                    String.format("File name: %s\nFile description: %s\n"
                    + "File size: unknown\nAccept file and kill?",
                    view.getFileField().getText(),
                    view.descriptionField.getText()),
                    "Kill", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Hello killer!");
            } else {
                JOptionPane.showMessageDialog(null, "Goodbye!");
            }
        }
    }

    // Välj bakgrundsfärg
    public class ColorButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Color newColor = JColorChooser.showDialog(view.chatBoxPanel,
                    "Choose text color", view.chatBoxPanel.getBackground());
            if (newColor != null) {
                view.nameField.setForeground(newColor);
                view.messageField.setForeground(newColor);
                view.color = Integer.toHexString(
                        newColor.getRGB()).substring(2);
            }
        }
    }

    public class FileButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();

            int returnVal = chooser.showOpenDialog(view.chatBoxPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                view.filePath = file.getAbsolutePath();
                view.getFileField().setText(file.getName());
            }
        }
    }

    //Gör så att den tomma platsen ockuperas!
    public class ServerOptionsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String chosen = String.valueOf(
                    view.serverOptions.getSelectedItem());
            if ("Protected".equals(chosen) || "Secret".equals(chosen)) {
                view.getPassLabel().setVisible(true);
                view.passField.setVisible(true);
            } else {
                view.getPassLabel().setVisible(false);
                view.passField.setVisible(false);
            }
        }
    }

    public class TabButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            TabButton button = (TabButton) e.getSource();
            int index = button.getIndex();

            if (view.tabbedPane.getTabCount() > 1) {
                if (index == view.tabbedPane.getTabCount() - 2) {
                    view.tabbedPane.setSelectedIndex(index - 1);
                }
                view.tabbedPane.remove(index);
                view.getTabButtons().remove(index);  //=>ButtonIndex=TabIndex
                view.chatBoxes.remove(index);
                if (clients.size() > 0) {
                    clients.get(index).kill();
                    clients.remove(index);
                }
                updateTabButtonIndex(index);
            }
        }
    }
}
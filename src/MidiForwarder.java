import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: benedikt
 * Date: 12.12.13
 * Time: 21:00
 */
public class MidiForwarder {
    private JComboBox<String> selectDeviceBox;
    private JLabel selectDeviceLabel;
    private JPanel deviceSelectorPanel;
    private JButton confirmSelectButton;
    private JPanel midiForwarderPanel;
    private JTabbedPane learnPanel;
    private JPanel globalTab;
    private JPanel deckATab;
    private JPanel deckBTab;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton nextButton;
    private JButton playPauseButtonA;
    private JButton stopButtonA;
    private JButton nextButtonA;
    private JButton fadeToNextButtonA;
    private JButton ejectButtonA;
    private JButton playPauseButtonB;
    private JButton stopButtonB;
    private JButton nextButtonB;
    private JButton fadeToNextButtonB;
    private JButton ejectButtonB;
    private JTextPane descriptionPane;
    private JPanel imexportTab;
    private JButton saveAsButton;
    private JButton openButton;
    private JLabel saveAsLabel;
    private JLabel openLabel;
    private JFrame window;
    private MidiDevice activeDevice;
    public static Boolean learning;
    public static HashMap<String, JButton> buttonMap;
    public static HashMap<String, String> commandMap;

    public MidiForwarder(JFrame frame) {
        window = frame;

        confirmSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDevice = (String) selectDeviceBox.getSelectedItem();
                try {
                    activeDevice = getMidiDeviceByName(selectedDevice);
                    activeDevice.open();
                    switchPanels();
                } catch (MidiUnavailableException e1) {
                    JOptionPane.showMessageDialog(window,
                            e1.getMessage(),
                            "MIDI Device unavailable",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        playPauseButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        stopButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        nextButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        fadeToNextButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        ejectButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        playPauseButtonB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        stopButtonB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        nextButtonB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        fadeToNextButtonB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        ejectButtonB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapKey((JButton) e.getSource());
            }
        });
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();

                fc.setFileFilter(new FileNameExtensionFilter("JSON Config Files", "json"));
                int save = fc.showSaveDialog(window);
                if (save == JFileChooser.APPROVE_OPTION)
                {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(commandMap);
                    try {
                        PrintWriter printWriter = new PrintWriter(fc.getSelectedFile(), "UTF-8");
                        printWriter.println(json);
                        printWriter.close();
                    } catch (FileNotFoundException e1) {
                        // This should not happen, the file was chosen via the JFileChooser
                        JOptionPane.showMessageDialog(window,
                                e1.getMessage(),
                                "File not found",
                                JOptionPane.ERROR_MESSAGE);
                        // Exit with an error status
                        System.exit(1);
                    } catch (UnsupportedEncodingException e1) {
                        JOptionPane.showMessageDialog(window,
                                e1.getMessage(),
                                "UTF-8 Encoding not supported",
                                JOptionPane.ERROR_MESSAGE);
                        // Exit with an error status
                        System.exit(1);
                    }
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openJSON();
            }
        });
    }

    private void switchPanels()
    {
        deviceSelectorPanel.setVisible(false);
        learnPanel.setVisible(true);
        window.pack();
    }

    private void openJSON()
    {
        final JFileChooser fc = new JFileChooser();

        fc.setFileFilter(new FileNameExtensionFilter("JSON Config Files", "json"));
        int open = fc.showOpenDialog(window);
        if (open == JFileChooser.APPROVE_OPTION)
        {
            Gson gson = new Gson();
            try {
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                commandMap = gson.fromJson(br, new TypeToken<HashMap<String, String>>(){}.getType());
            } catch (FileNotFoundException e1) {
                // This should not happen, the file was chosen via the JFileChooser
                JOptionPane.showMessageDialog(window,
                        e1.getMessage(),
                        "File not found",
                        JOptionPane.ERROR_MESSAGE);
                // Exit with an error status
                System.exit(1);
            }
        }
        if (activeDevice.isOpen())
        {
            activeDevice.close();
        }

        try {
            activeDevice.open();
        } catch (MidiUnavailableException e) {
            JOptionPane.showMessageDialog(window,
                    e.getMessage(),
                    "MIDI Device unavailable",
                    JOptionPane.ERROR_MESSAGE);
        }
        Receiver mappingReceiver = new MappingReceiver();
        try {
            Transmitter deviceTransmitter = activeDevice.getTransmitter();
            deviceTransmitter.setReceiver(mappingReceiver);
        } catch (MidiUnavailableException e) {
            JOptionPane.showMessageDialog(window,
                    e.getMessage(),
                    "MIDI Device unavailable",
                    JOptionPane.ERROR_MESSAGE);
        }

        buttonMap = new HashMap<String, JButton>();

        for(Map.Entry<String, String> command : commandMap.entrySet())
        {
            String message = command.getKey();
            switch(command.getValue())
            {
                case "Play":
                    buttonMap.put(message, playButton);
                    break;
                case "Pause":
                    buttonMap.put(message, pauseButton);
                    break;
                case "playPauseButtonA":
                    buttonMap.put(message, playPauseButtonA);
                    break;
                case "playPauseButtonB":
                    buttonMap.put(message, playPauseButtonB);
                    break;
                case "Stop":
                    buttonMap.put(message, stopButton);
                    break;
                case "stopButtonA":
                    buttonMap.put(message, stopButtonA);
                    break;
                case "stopButtonB":
                    buttonMap.put(message, stopButtonA);
                    break;
                case "Next":
                    buttonMap.put(message, nextButton);
                    break;
                case "nextButtonA":
                    buttonMap.put(message, nextButtonA);
                    break;
                case "nextButtonB":
                    buttonMap.put(message, nextButtonB);
                    break;
                case "ejectButtonA":
                    buttonMap.put(message, ejectButtonA);
                    break;
                case "ejectButtonB":
                    buttonMap.put(message, ejectButtonB);
                    break;
                case "fadeToNextButtonA":
                    buttonMap.put(message, fadeToNextButtonA);
                    break;
                case "fadeToNextButtonB":
                    buttonMap.put(message, fadeToNextButtonB);
                    break;
            }
        }
    }

    private MidiDevice getMidiDeviceByName(String name) throws MidiUnavailableException {
        MidiDevice device = null;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos)
        {
            if (info.getName().equals(name))
            {
                device = MidiSystem.getMidiDevice(info);
            }
        }

        return device;
    }

    private void mapKey(JButton button) {
        if (activeDevice.isOpen())
        {
            activeDevice.close();
            try {
                activeDevice.open();
            } catch (MidiUnavailableException e) {
                JOptionPane.showMessageDialog(window,
                        e.getMessage(),
                        "MIDI Device unavailable",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        Receiver mappingReceiver = new MappingReceiver(button);
        try {
            Transmitter deviceTransmitter = activeDevice.getTransmitter();

            deviceTransmitter.setReceiver(mappingReceiver);
            learning = true;
            button.setBackground(Color.orange);
        } catch (MidiUnavailableException e) {
            JOptionPane.showMessageDialog(window,
                    e.getMessage(),
                    "MIDI Device unavailable",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void trigger(String message)
    {
        JButton button = buttonMap.get(message);
        if (button != null)
        {
            button.setBackground(Color.green);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            button.setBackground(null);
        }
    }

    public static void trigger(String message, JButton button)
    {
        if (learning) {
            learning = false;
            if (buttonMap == null)
            {
                buttonMap = new HashMap<String, JButton>(14);
            }
            if (commandMap == null)
            {
                commandMap = new HashMap<String, String>(14);
            }
            buttonMap.put(message, button);
            commandMap.put(message, button.getActionCommand());
            button.setBackground(null);
        }
        else {
            JButton triggered = buttonMap.get(message);
            if (triggered != null) {
                triggered.setBackground(Color.green);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                triggered.setBackground(null);
            }
        }
    }

    public void getMidiOutDevices() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice device = null;
            try {
                device = MidiSystem.getMidiDevice(info);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }

            if (device.getMaxTransmitters() != 0) {
                selectDeviceBox.addItem(info.getName());
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MidiForwarder");
        MidiForwarder midiForwarder = new MidiForwarder(frame);
        frame.setContentPane(midiForwarder.midiForwarderPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        midiForwarder.getMidiOutDevices();
    }
}

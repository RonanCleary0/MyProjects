import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Modified {

    // Declaration of GUI components
    private JFrame frame;
    private JTextField patternTextField;
    private JTextArea fileNameTextArea;
    private JPanel resultPanel;
    private List<JTextArea> resultTextAreas;
    private JToggleButton searchTypeToggleButton;
    private JButton chooseFilesButton;
    private JButton clearButton;
    private JButton searchButton;
    private JButton clearResultsButton;
    private JPanel inputPanel; // Declare inputPanel as a class member
    private JPanel resultContainerPanel; // Declare resultContainerPanel as a class member
    private boolean searchWholeLine = true; // Flag to determine the type of search
    private boolean isDarkMode = true; // Flag to determine the current mode



    //Search Engine GUI
    public Modified() {
        // Declaration of GUI components
        frame = new JFrame("Search Engine GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);  // Adjusted the initial size
        frame.setLayout(new BorderLayout());
        setAppTheme(); // Set initial theme,



         // Panel for input and buttons
        inputPanel = new JPanel(); // Initialize inputPanel
        inputPanel.setLayout(new GridBagLayout());
        setPanelTheme(inputPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Description label with HTML for styling
        JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; color: #ecf0f1;'>Search Engine GUI<br>Enter a pattern and select files to search</div></html>");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputPanel.add(descriptionLabel, gbc);
        gbc.gridy++;

        // Pattern label and text field setup
        JLabel patternLabel = new JLabel("Pattern:");
        setLabelTheme(patternLabel);
        patternTextField = new JTextField(20);
        patternTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        patternTextField.setBackground(new Color(67, 122, 180));  // Medium gray background
        patternTextField.setForeground(Color.WHITE);  // Set text color to white
        patternTextField.setCaretColor(Color.WHITE); // Set caret color to white

        inputPanel.add(patternLabel, gbc);
        gbc.gridx++;
        inputPanel.add(patternTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // File name label and text area setup
        JLabel fileNameLabel = new JLabel("Selected Files:");
        setLabelTheme(fileNameLabel);
        fileNameTextArea = new JTextArea(5, 20);
        fileNameTextArea.setLineWrap(true);
        fileNameTextArea.setEditable(false);
        fileNameTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        fileNameTextArea.setBackground(new Color(56, 136, 224));  // Medium gray background
        fileNameTextArea.setForeground(Color.WHITE);  // Set text color to white
        fileNameTextArea.setCaretColor(Color.WHITE); // Set caret color to white

        inputPanel.add(fileNameLabel, gbc);
        gbc.gridx++;
        inputPanel.add(new JScrollPane(fileNameTextArea), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Choose files button setup
        chooseFilesButton = new JButton("Choose Files");
        setButtonTheme(chooseFilesButton);
        inputPanel.add(chooseFilesButton, gbc);
        gbc.gridy++;

        // Clear selected files button setup
        clearButton = new JButton("Clear Selected Files");
        setButtonTheme(clearButton);
        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridy++;
        inputPanel.add(clearButton, gbc); // placing clear button
        gbc.gridy++;

        // Search button setup
        searchButton = new JButton("Search");
        setButtonTheme(searchButton);
        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridx++;
        inputPanel.add(searchButton, gbc);

        // Search type toggle button setup
        searchTypeToggleButton = new JToggleButton("Search Whole Line");
        setSearchTypeButtonTheme(searchTypeToggleButton);
        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridy++;
        inputPanel.add(searchTypeToggleButton, gbc);
        gbc.gridy++;

        // Clear results button
        clearResultsButton = new JButton("Clear Results");
        setButtonTheme(clearResultsButton);
        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridy++;
        inputPanel.add(clearResultsButton, gbc); // placing clear button
        gbc.gridy++;

        // Result panel setup
        resultContainerPanel = new JPanel(new BorderLayout());
        resultContainerPanel.setBackground(Color.darkGray); // Dark background for result container
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.white);  // Dark gray background
        resultContainerPanel.add(new JScrollPane(resultPanel), BorderLayout.CENTER);
        frame.add(resultContainerPanel, BorderLayout.CENTER);

        resultTextAreas = new ArrayList<>();
        frame.add(inputPanel, BorderLayout.WEST);
        frame.setVisible(true);

        chooseFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFiles();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        searchTypeToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSearchType();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSelectedFiles();
            }
        });

        clearResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearResults();
                frame.revalidate(); // Revalidate the frame
                frame.repaint();
            }
        });


        // Menu bar setup
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);

        JMenuItem darkModeItem = new JMenuItem("Dark Mode");
        darkModeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDarkMode();
            }
        });
        settingsMenu.add(darkModeItem);

        JMenuItem lightModeItem = new JMenuItem("Light Mode");
        lightModeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLightMode();
            }
        });
        settingsMenu.add(lightModeItem);
    }

    private void setPanelTheme(JPanel panel) {
        if (isDarkMode) {
            panel.setBackground(Color.darkGray);
        } else {
            panel.setBackground(Color.lightGray);
        }
    }

    private void setButtonTheme(JButton button) {
        if (isDarkMode) {
            button.setBackground(Color.darkGray);
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.lightGray);
            button.setForeground(Color.black);
        }
    }

    private void setSearchTypeButtonTheme(JToggleButton button) {
        if (isDarkMode) {
            button.setBackground(Color.darkGray);
            button.setForeground(Color.white);
        } else {
            button.setBackground(Color.lightGray);
            button.setForeground(Color.black);
        }
    }

    private void setLabelTheme(JLabel label) {
        if (isDarkMode) {
            label.setForeground(Color.lightGray);
        } else {
            label.setForeground(Color.black);
        }
    }

    private void setAppTheme() {
        if (isDarkMode) {
            UIManager.put("OptionPane.background", Color.darkGray);
            UIManager.put("Panel.background", Color.darkGray);
            UIManager.put("Button.background", Color.darkGray);
            UIManager.put("Button.foreground", Color.white);
            UIManager.put("Label.foreground", Color.lightGray);
        } else {
            UIManager.put("OptionPane.background", Color.lightGray);
            UIManager.put("Panel.background", Color.lightGray);
            UIManager.put("Button.background", Color.lightGray);
            UIManager.put("Button.foreground", Color.black);
            UIManager.put("Label.foreground", Color.black);
        }
    }

    private void chooseFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();

            for (File file : selectedFiles) {
                fileNameTextArea.append(file.getAbsolutePath() + ", ");
            }
        }
    }

    private void performSearch() {
        String pattern = patternTextField.getText();
        String[] fileNames = fileNameTextArea.getText().split(",\\s*");

        if (pattern.isEmpty() || fileNames.length == 0) {
            JOptionPane.showMessageDialog(frame, "Please enter both pattern and select at least one file.");
            return;
        }

        clearResults(); // Clear previous results

        for (String fileName : fileNames) {
            JTextArea resultTextArea = new JTextArea(10, 40);
            resultTextArea.setEditable(false);
            resultTextArea.setFont(new Font("Arial", Font.BOLD, 14));
            resultTextAreas.add(resultTextArea);
            int lines = 0;
            int matches = 0;

            try {
                File file = new File(fileName);
                Scanner scanner = new Scanner(file);
                Pattern pat;

                if (searchWholeLine) {
                    pat = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                } else {
                    pat = Pattern.compile("\\b" + Pattern.quote(pattern) + "\\b", Pattern.CASE_INSENSITIVE);
                }

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lines++;

                    Matcher match = pat.matcher(line);
                    while (match.find()) {
                        if (searchWholeLine) {
                            resultTextArea.append(fileName + ": " + lines + ": " + line + "\n");
                        } else {
                            resultTextArea.append(fileName + ": " + lines + ": " + match.group() + "\n");
                        }
                        matches++;
                    }
                }

                resultTextArea.append(fileName + ": " + lines + " lines, " + matches + " matches\n");
                resultPanel.add(new JScrollPane(resultTextArea));
                setResultPanelTheme(resultTextArea);
            } catch (IOException e) {
                resultTextArea.append("Error reading the file " + fileName + ": " + e.getMessage() + "\n");
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    private void clearResults() {
        resultPanel.removeAll();
        resultTextAreas.clear();
    }

    private void clearSelectedFiles() {
        fileNameTextArea.setText(""); // Clear selected files area
        clearResults(); // Clear results
    }

    private void toggleSearchType() {
        searchWholeLine = !searchWholeLine;
        if (searchWholeLine) {
            searchTypeToggleButton.setText("Search Whole Line");
        } else {
            searchTypeToggleButton.setText("Search Word Only");
        }
    }

    private void setDarkMode() {
        isDarkMode = true;
        setAppTheme();
        setPanelTheme(inputPanel);
        setButtonTheme(chooseFilesButton);
        setButtonTheme(clearButton);
        setButtonTheme(searchButton);
        setSearchTypeButtonTheme(searchTypeToggleButton);
        setButtonTheme(clearResultsButton);
        for (JTextArea textArea : resultTextAreas) {
            setResultPanelTheme(textArea);
        }
        // Update text colors
        patternTextField.setForeground(Color.WHITE);
        fileNameTextArea.setForeground(Color.WHITE);
    }

    private void setLightMode() {
        isDarkMode = false;
        setAppTheme();
        setPanelTheme(inputPanel);
        setButtonTheme(chooseFilesButton);
        setButtonTheme(clearButton);
        setButtonTheme(searchButton);
        setSearchTypeButtonTheme(searchTypeToggleButton);
        setButtonTheme(clearResultsButton);
        for (JTextArea textArea : resultTextAreas) {
            setResultPanelTheme(textArea);
        }
        // Update text colors
        patternTextField.setForeground(Color.BLACK);
        fileNameTextArea.setForeground(Color.BLACK);
    }

    private void setResultPanelTheme(JTextArea textArea) {
        if (isDarkMode) {
            textArea.setBackground(Color.darkGray);
            textArea.setForeground(Color.lightGray);
        } else {
            textArea.setBackground(Color.white);
            textArea.setForeground(Color.black);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SearchEngineGUI();
            }
        });
    }
}
























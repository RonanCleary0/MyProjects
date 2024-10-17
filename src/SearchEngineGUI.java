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

public class SearchEngineGUI {

    //Declaration of GUI components
    private JFrame frame;
    private JTextField patternTextField;
    private JTextArea fileNameTextArea;
    private JPanel resultPanel;
    private List<JTextArea> resultTextAreas;
    private JToggleButton searchTypeToggleButton;

    private boolean searchWholeLine = true; // Flag to determine the type of search

    public SearchEngineGUI() {
        //Declaration of GUI components
        frame = new JFrame("Search Engine GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);  // Adjusted the initial size
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(46, 49, 49));  // Dark gray


        //Panel for input and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.lightGray);  // Dark blue background

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
        patternLabel.setForeground(new Color(236, 240, 241));  // Light gray color
        patternTextField = new JTextField(20);
        patternTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        patternTextField.setBackground(new Color(108, 122, 137));  // Medium gray background
        patternTextField.setForeground(new Color(228, 241, 254));  // Light blue text color

        inputPanel.add(patternLabel, gbc);
        gbc.gridx++;
        inputPanel.add(patternTextField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;


        // File name label and text area setup
        JLabel fileNameLabel = new JLabel("Selected Files:");
        fileNameLabel.setForeground(new Color(236, 240, 241));  // Light gray color
        fileNameTextArea = new JTextArea(5, 20);
        fileNameTextArea.setLineWrap(true);
        fileNameTextArea.setEditable(false);
        fileNameTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        fileNameTextArea.setBackground(new Color(108, 122, 137));  // Medium gray background
        fileNameTextArea.setForeground(new Color(228, 241, 254));  // Light blue text color

        inputPanel.add(fileNameLabel, gbc);
        gbc.gridx++;
        inputPanel.add(new JScrollPane(fileNameTextArea), gbc);
        gbc.gridx = 0;
        gbc.gridy++;


        // Choose files button setup
        JButton chooseFilesButton = new JButton("Choose Files");
        chooseFilesButton.setFont(new Font("Arial", Font.BOLD, 14));
        chooseFilesButton.setBackground( Color.BLUE);  // Bright blue color
        chooseFilesButton.setForeground( Color.BLACK);  // Light gray text color

        inputPanel.add(chooseFilesButton, gbc);
        gbc.gridy++;


        // Clear selected files button setup
        JButton clearButton = new JButton("Clear Selected Files");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(255, 87, 34));  // Orange color
        clearButton.setForeground(Color.BLACK);  // Light gray text color

        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridy++;
        inputPanel.add(clearButton, gbc); //placing clear button
        gbc.gridy++;




        //Search button setup
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(39, 174, 96));  // Green color
        searchButton.setForeground(Color.BLACK);  // Light gray text color

        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridx++;
        inputPanel.add(searchButton, gbc);




        //Search type toggle button setup
        searchTypeToggleButton = new JToggleButton("Search Whole Line");
        searchTypeToggleButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchTypeToggleButton.setBackground(new Color(34, 167, 240));  // Bright blue color
        searchTypeToggleButton.setForeground(Color.BLACK);  // Light gray text color

        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridy++;
        inputPanel.add(searchTypeToggleButton, gbc);
        gbc.gridy++;



        //clear results button
        JButton clearResultsButton = new JButton("Clear Results");
        clearResultsButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearResultsButton.setBackground(new Color(255, 87, 34));  // Orange color
        clearResultsButton.setForeground(Color.BLACK);  // Light gray text color

        inputPanel.add(new JLabel()); // Empty label for spacing
        gbc.gridy++;
        inputPanel.add(clearResultsButton, gbc); //placing clear button
        gbc.gridy++;








        // Result panel setup
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.white);  // Dark gray background
        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(new JScrollPane(resultPanel), BorderLayout.CENTER);

        resultTextAreas = new ArrayList<>();
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
                    pat = Pattern.compile(pattern);
                } else {
                    pat = Pattern.compile("\\b" + Pattern.quote(pattern) + "\\b");
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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SearchEngineGUI();
            }
        });
    }
}
package kotlin.jasper;

import java.awt.BorderLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

public class DroppingFormGenerator {
    private static final String DEFAULT_JASPER_TEMPLATE = "src/main/resources/jasperTemplates/templates/DroppingForm_Final.jasper";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> createUI(DEFAULT_JASPER_TEMPLATE));
    }

    private static void createUI(String jasperTemplate) {
        JFrame frame = new JFrame("Dropping Form Generator");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 10", "[right]10[grow,fill]", "[][][][][][][][][]"));

        // Input Fields matching the DroppingForm parameters
        JTextField dateField = new JTextField(20);
        JTextField nameOfStudentField = new JTextField(20);
        JTextField adviserField = new JTextField(20);
        JTextField trackStrandField = new JTextField(20);
        JTextField gradeSectionField = new JTextField(20);
        JTextArea inclusiveDatesField = new JTextArea(3, 30);
        JTextArea actionTakenField = new JTextArea(3, 30);
        JTextArea reasonForDroppingField = new JTextArea(3, 30);
        JTextField effectiveDateField = new JTextField(20);
        JTextField outputNameField = new JTextField("DroppingForm", 20);

        // Add scroll panes for text areas
        JScrollPane inclusiveDatesScrollPane = new JScrollPane(inclusiveDatesField);
        JScrollPane actionTakenScrollPane = new JScrollPane(actionTakenField);
        JScrollPane reasonScrollPane = new JScrollPane(reasonForDroppingField);

        // Report Action Buttons
        JButton printButton = new JButton("Print Report");
        JButton exportDocxButton = new JButton("Export to DOCX");
        JButton exportPdfButton = new JButton("Export to PDF");

        // Action Listeners
        printButton.addActionListener(e -> generateReport(jasperTemplate, dateField, nameOfStudentField, adviserField,
            trackStrandField, gradeSectionField, inclusiveDatesField, actionTakenField, reasonForDroppingField,
            effectiveDateField, outputNameField, "print"));
        exportDocxButton.addActionListener(e -> generateReport(jasperTemplate, dateField, nameOfStudentField, adviserField,
            trackStrandField, gradeSectionField, inclusiveDatesField, actionTakenField, reasonForDroppingField,
            effectiveDateField, outputNameField, "docx"));
        exportPdfButton.addActionListener(e -> generateReport(jasperTemplate, dateField, nameOfStudentField, adviserField,
            trackStrandField, gradeSectionField, inclusiveDatesField, actionTakenField, reasonForDroppingField,
            effectiveDateField, outputNameField, "pdf"));

        // Layout Configuration
        mainPanel.add(new JLabel("Date:"));
        mainPanel.add(dateField, "wrap");

        mainPanel.add(new JLabel("Name of Student:"));
        mainPanel.add(nameOfStudentField, "wrap");

        mainPanel.add(new JLabel("Adviser:"));
        mainPanel.add(adviserField, "wrap");

        mainPanel.add(new JLabel("Track/Strand Specialization:"));
        mainPanel.add(trackStrandField, "wrap");

        mainPanel.add(new JLabel("Grade/Section:"));
        mainPanel.add(gradeSectionField, "wrap");

        mainPanel.add(new JLabel("Inclusive Dates of Absences:"));
        mainPanel.add(inclusiveDatesScrollPane, "wrap, growx");

        mainPanel.add(new JLabel("Action Taken:"));
        mainPanel.add(actionTakenScrollPane, "wrap, growx");

        mainPanel.add(new JLabel("Reason for Dropping:"));
        mainPanel.add(reasonScrollPane, "wrap, growx");

        mainPanel.add(new JLabel("Effective Date:"));
        mainPanel.add(effectiveDateField, "wrap");

        mainPanel.add(new JLabel("Output File Name:"));
        mainPanel.add(outputNameField, "wrap");

        // Buttons
        mainPanel.add(printButton, "span 2, growx, wrap");
        mainPanel.add(exportDocxButton, "span 2, growx, wrap");
        mainPanel.add(exportPdfButton, "span 2, growx, wrap");

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void generateReport(String jasperTemplate, JTextField dateField, JTextField nameOfStudentField,
                                     JTextField adviserField, JTextField trackStrandField, 
                                     JTextField gradeSectionField, JTextArea inclusiveDatesField,
                                     JTextArea actionTakenField, JTextArea reasonForDroppingField,
                                     JTextField effectiveDateField, JTextField outputNameField, 
                                     String action) {
        String outputName = outputNameField.getText().trim();
        if (outputName.isEmpty()) {
            outputName = "DroppingForm";
        }

        // Prepare parameters matching the DroppingForm template
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Date", dateField.getText().trim());
        parameters.put("NameofStudent", nameOfStudentField.getText().trim());
        parameters.put("Adviser", adviserField.getText().trim());
        parameters.put("Trank/Strand Specialization", trackStrandField.getText().trim());
        parameters.put("Grade/Section", gradeSectionField.getText().trim());
        parameters.put("InclusiveDateofAbsences", inclusiveDatesField.getText().trim());
        parameters.put("ActionTaken", actionTakenField.getText().trim());
        parameters.put("ReasonforDropping", reasonForDroppingField.getText().trim());
        parameters.put("EffectiveDate", effectiveDateField.getText().trim());

        ReportGenerator reportGenerator = new ReportGenerator(jasperTemplate);
        
        if (action.equals("print")) {
            reportGenerator.processReport(parameters, outputName, action);
        } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(outputName + (action.equals("docx") ? ".docx" : ".pdf")));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String baseName = fileChooser.getSelectedFile().getAbsolutePath()
                    .replace(action.equals("docx") ? ".docx" : ".pdf", "");
                reportGenerator.processReport(parameters, baseName, action);
            }
        }
    }
}
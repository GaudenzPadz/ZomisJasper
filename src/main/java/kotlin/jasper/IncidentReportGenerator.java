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

public class IncidentReportGenerator {
    private static final String DEFAULT_JASPER_TEMPLATE = "src/main/resources/jasperTemplates/templates/IncidentReport_Final.jasper";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> createUI(DEFAULT_JASPER_TEMPLATE));
    }

    private static void createUI(String jasperTemplate) {
        JFrame frame = new JFrame("Incident Report Generator");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 10", "[right]10[grow,fill]", "[][][][][][][]"));

        // Input Fields matching the JasperReport parameters
        JTextField nameField = new JTextField(20);
        JTextField gradeSectionField = new JTextField(20);
        JTextField dateField = new JTextField(20);
        JTextField timeVisitField = new JTextField(20);
        JTextArea incidentReportField = new JTextArea(10, 30);
        JTextField outputNameField = new JTextField("IncidentReport", 20);

        // Add scroll pane for the incident report text area
        JScrollPane incidentScrollPane = new JScrollPane(incidentReportField);

        // Report Action Buttons
        JButton printButton = new JButton("Print Report");
        JButton exportDocxButton = new JButton("Export to DOCX");
        JButton exportPdfButton = new JButton("Export to PDF");

        // Action Listeners
        printButton.addActionListener(e -> generateReport(jasperTemplate, nameField, gradeSectionField, 
            dateField, timeVisitField, incidentReportField, outputNameField, "print"));
        exportDocxButton.addActionListener(e -> generateReport(jasperTemplate, nameField, gradeSectionField,
            dateField, timeVisitField, incidentReportField, outputNameField, "docx"));
        exportPdfButton.addActionListener(e -> generateReport(jasperTemplate, nameField, gradeSectionField,
            dateField, timeVisitField, incidentReportField, outputNameField, "pdf"));

        // Layout Configuration
        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(nameField, "wrap");

        mainPanel.add(new JLabel("Grade & Section:"));
        mainPanel.add(gradeSectionField, "wrap");

        mainPanel.add(new JLabel("Date of Visit:"));
        mainPanel.add(dateField, "wrap");

        mainPanel.add(new JLabel("Time of Visit:"));
        mainPanel.add(timeVisitField, "wrap");

        mainPanel.add(new JLabel("Initial Incident Report:"));
        mainPanel.add(incidentScrollPane, "wrap, growx");

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

 // In IncidentReportUI, modify the generateReport method:
    private static void generateReport(String jasperTemplate, JTextField nameField, 
                                     JTextField gradeSectionField, JTextField dateField,
                                     JTextField timeVisitField, JTextArea incidentReportField,
                                     JTextField outputNameField, String action) {
        String outputName = outputNameField.getText().trim();
        if (outputName.isEmpty()) {
            outputName = "IncidentReport";
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("VName", nameField.getText().trim());
        parameters.put("VGrSec", gradeSectionField.getText().trim());
        parameters.put("Date", dateField.getText().trim());
        parameters.put("Tvisit", timeVisitField.getText().trim());
        parameters.put("IniReport", incidentReportField.getText().trim());

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
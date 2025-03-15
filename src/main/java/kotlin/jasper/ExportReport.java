package kotlin.jasper;

import java.awt.BorderLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

/**
 * A GUI-based application to generate reports using JasperReports. Users can
 * enter details and generate reports dynamically.
 */
public class ExportReport {
	private static final String DEFAULT_JASPER_TEMPLATE = "src/main/resources/jasperTemplates/templates/GoodMoral_Final.jasper";

	public static void main(String[] args) {
		// Set FlatLaf look and feel
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> createUI(DEFAULT_JASPER_TEMPLATE));
	}

	/**
	 * Creates the user interface for report generation.
	 *
	 * @param jasperTemplate Path to the compiled `.jasper` template file.
	 */
	private static void createUI(String jasperTemplate) {
		JFrame frame = new JFrame("Jasper Report Generator");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 10", "[right]10[grow,fill]", "[][][][][][][][][]"));

		// Input Fields
		JTextField nameField = new JTextField(20);
		JTextField schoolYearField = new JTextField(20);
		JTextField strandField = new JTextField(20);
		JTextField trackField = new JTextField(20);
		JTextField purposeField = new JTextField(20);
		JTextField dateGivenField = new JTextField(20);
		JTextField outputNameField = new JTextField("GeneratedReport", 20);

		// Signer ComboBox
		JComboBox<String> signerComboBox = new JComboBox<>(
				new String[] { "SALLY P. GENUINO, Principal II", "RACQUEL D. COMANDANTE, Guidance Designate" });

		// Report Action Buttons
		JButton printButton = new JButton("Print Report");
		JButton exportDocxButton = new JButton("Export to DOCX");
		JButton exportPdfButton = new JButton("Export to PDF");

		// Action Listeners for Report Buttons
		printButton.addActionListener(e -> generateReport(jasperTemplate, nameField, schoolYearField, strandField,
				trackField, purposeField, dateGivenField, signerComboBox, outputNameField, "print"));
		exportDocxButton.addActionListener(e -> generateReport(jasperTemplate, nameField, schoolYearField, strandField,
				trackField, purposeField, dateGivenField, signerComboBox, outputNameField, "docx"));
		exportPdfButton.addActionListener(e -> generateReport(jasperTemplate, nameField, schoolYearField, strandField,
				trackField, purposeField, dateGivenField, signerComboBox, outputNameField, "pdf"));

		// Layout Configuration
		mainPanel.add(new JLabel("Name:"));
		mainPanel.add(nameField, "wrap");

		mainPanel.add(new JLabel("School Year:"));
		mainPanel.add(schoolYearField, "wrap");

		mainPanel.add(new JLabel("Strand:"));
		mainPanel.add(strandField, "wrap");

		mainPanel.add(new JLabel("Track:"));
		mainPanel.add(trackField, "wrap");

		mainPanel.add(new JLabel("Purpose:"));
		mainPanel.add(purposeField, "wrap");

		mainPanel.add(new JLabel("Date Given:"));
		mainPanel.add(dateGivenField, "wrap");

		mainPanel.add(new JLabel("Signer and Position:"));
		mainPanel.add(signerComboBox, "wrap");

		mainPanel.add(new JLabel("Output File Name:"));
		mainPanel.add(outputNameField, "wrap");

		// Report Generation Buttons
		mainPanel.add(printButton, "span 2, growx, wrap");
		mainPanel.add(exportDocxButton, "span 2, growx, wrap");
		mainPanel.add(exportPdfButton, "span 2, growx, wrap");

		JScrollPane scrollPane = new JScrollPane(mainPanel);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	/**
	 * Generates the Jasper report with collected parameters.
	 */
	private static void generateReport(String jasperTemplate, JTextField nameField, JTextField schoolYearField,
			JTextField strandField, JTextField trackField, JTextField purposeField, JTextField dateGivenField,
			JComboBox<String> signerComboBox, JTextField outputNameField, String action) {
		String outputName = outputNameField.getText().trim();
		if (outputName.isEmpty()) {
			outputName = "GeneratedReport";
		}

// Extract signer information
		String selectedSigner = (String) signerComboBox.getSelectedItem();
		String[] signerParts = selectedSigner.split(", ");
		String nameToSign = signerParts.length > 0 ? signerParts[0] : "Unknown";
		String workPosition = signerParts.length > 1 ? signerParts[1] : "Unknown";

// Prepare parameters
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("Name", nameField.getText().trim());
		parameters.put("SchoolYear", schoolYearField.getText().trim());
		parameters.put("Strand", strandField.getText().trim());
		parameters.put("TrackAndSpecialization", trackField.getText().trim());
		parameters.put("purpose", purposeField.getText().trim());
		parameters.put("DateGiven", dateGivenField.getText().trim());
		parameters.put("nameToSign", nameToSign);
		parameters.put("workPosition", workPosition);

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

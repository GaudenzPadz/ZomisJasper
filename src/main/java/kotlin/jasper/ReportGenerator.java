package kotlin.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.pdf.JRPdfExporter;

import javax.swing.*;
import java.io.File;
import java.util.Map;

/**
 * A reusable report generator for compiling and exporting JasperReports templates.
 * This class allows flexibility in selecting different report templates and parameters dynamically.
 */
public class ReportGenerator {
    private final String jasperTemplate;

    /**
     * Constructor for ReportGenerator.
     * @param jasperTemplate Path to the compiled `.jasper` template file.
     */
    public ReportGenerator(String jasperTemplate) {
        this.jasperTemplate = jasperTemplate;
    }

    /**
     * Generates a report based on the provided parameters.
     * @param parameters A map of parameter names and values required by the Jasper template.
     * @return JasperPrint The filled JasperPrint object.
     */
    public JasperPrint generateReport(Map<String, Object> parameters) {
        try {
            return JasperFillManager.fillReport(jasperTemplate, parameters, new JREmptyDataSource());
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage(),
                    "Report Generation Failed", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates and processes the report based on user selection.
     * @param parameters A map of parameters.
     * @param outputName The desired output file name (without extension).
     * @param action The action to perform: "print", "docx", or "pdf".
     */
    public void generateReport(Map<String, Object> parameters, String outputName, String action) {
        JasperPrint jasperPrint = generateReport(parameters);

        if (jasperPrint == null) {
            JOptionPane.showMessageDialog(null, "Report generation failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (action.toLowerCase()) {
            case "print":
                printReport(jasperPrint);
                break;
            case "docx":
                File docxFile = new File(outputName + ".docx");
                exportToDocx(jasperPrint, docxFile);
                break;
            case "pdf":
                File pdfFile = new File(outputName + ".pdf");
                exportToPdf(jasperPrint, pdfFile);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid action specified!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Prints the generated report.
     * @param jasperPrint The JasperPrint object to be printed.
     */
    public void printReport(JasperPrint jasperPrint) {
        try {
            JasperPrintManager.printReport(jasperPrint, true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error printing report: " + e.getMessage(),
                    "Print Failed", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Exports the report as a DOCX file.
     * @param jasperPrint The JasperPrint object to be exported.
     * @param outputFile The output DOCX file.
     */
    public void exportToDocx(JasperPrint jasperPrint, File outputFile) {
        try {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));

            SimpleDocxReportConfiguration configuration = new SimpleDocxReportConfiguration();
            configuration.setFramesAsNestedTables(false);
            configuration.setFlexibleRowHeight(true);
            configuration.setIgnoreHyperlink(true);
            configuration.setNewLineAsParagraph(true);
            configuration.setBackgroundAsHeader(false);
            configuration.setOverrideHints(true);

            exporter.setConfiguration(configuration);
            exporter.exportReport();

            JOptionPane.showMessageDialog(null, "DOCX export successful!\nSaved to: " + outputFile.getAbsolutePath(),
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error exporting to DOCX: " + e.getMessage(),
                    "Export Failed", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Exports the report as a PDF file.
     * @param jasperPrint The JasperPrint object to be exported.
     * @param outputFile The output PDF file.
     */
    public void exportToPdf(JasperPrint jasperPrint, File outputFile) {
        try {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
            exporter.exportReport();

            JOptionPane.showMessageDialog(null, "PDF export successful!\nSaved to: " + outputFile.getAbsolutePath(),
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error exporting to PDF: " + e.getMessage(),
                    "Export Failed", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

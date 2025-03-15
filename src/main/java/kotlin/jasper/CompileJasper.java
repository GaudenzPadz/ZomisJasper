package kotlin.jasper;

import net.sf.jasperreports.engine.*;

/**
 * The {@code CompileJasper} class is responsible for compiling a JRXML template file
 * into a Jasper template (.jasper) file. This compiled file is used for generating reports
 * in JasperReports.
 * 
 * <p>Usage:
 * <ul>
 *   <li>Ensure the JRXML template exists in the specified path.</li>
 *   <li>Run this class to generate the compiled Jasper file.</li>
 *   <li>The compiled file will be stored in the specified output location.</li>
 * </ul>
 * </p>
 *
 * <p>Example:
 * <pre>
 * java -cp target/classes kotlin.jasper.CompileJasper
 * </pre>
 * </p>
 *
 * <p>Dependencies:
 * <ul>
 *   <li>JasperReports Library</li>
 *   <li>Maven setup with JasperReports dependencies</li>
 * </ul>
 * </p>
 *
 * @author ChatGPT na may Kunting Gaudenz 
 * @version 1.0
 * Paki basa nalang please 
 */
public class CompileJasper {

    /**
     * The main method compiles a JRXML file into a Jasper (.jasper) file.
     * 
     * <p>Steps performed:
     * <ol>
     *   <li>Loads the JRXML template.</li>
     *   <li>Compiles the template using {@code JasperCompileManager}.</li>
     *   <li>Saves the compiled template in the specified location.</li>
     * </ol>
     * </p>
     *
     * @param args Command-line arguments (not used).
     */
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        try {
            // Define the source JRXML file (input)
            String jrxmlFile = "src/main/resources/jasperTemplates/templates/GoodMoral.jrxml";
            
            // Compile the JRXML file into a JasperReport object
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile);

            // Define the output location for the compiled Jasper file
            String jasperFile = "src/main/resources/jasperTemplates/templates/GoodMoral_Final.jasper";

            // Save the compiled Jasper template to a file
            JasperCompileManager.compileReportToFile(jrxmlFile, jasperFile);

            // Print success message
            System.out.println("Compilation successful: " + jasperFile);
        } catch (JRException e) {
            // Print the stack trace in case of an error
            e.printStackTrace();
        }
    }
}

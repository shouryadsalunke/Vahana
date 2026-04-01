import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Uses the JVM's built-in Java compiler (javax.tools) to compile
 * all sources in src/ — works even without a separate javac binary.
 */
public class Compile {
    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("ERROR: No system Java compiler found. Need JDK, not just JRE.");
            System.exit(1);
        }

        // Collect all .java files under src/
        List<File> sourceFiles = new ArrayList<>();
        Files.walk(Paths.get("src"))
             .filter(p -> p.toString().endsWith(".java"))
             .forEach(p -> sourceFiles.add(p.toFile()));

        System.out.println("Compiling " + sourceFiles.size() + " source files...");

        StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> units = fm.getJavaFileObjectsFromFiles(sourceFiles);

        // Output to out/ directory
        new File("out").mkdirs();
        List<String> options = Arrays.asList("-d", "out");

        DiagnosticCollector<JavaFileObject> diag = new DiagnosticCollector<>();
        boolean success = compiler.getTask(null, fm, diag, options, null, units).call();

        for (Diagnostic<?> d : diag.getDiagnostics()) {
            System.out.println(d.getKind() + ": " + d.getMessage(null));
            if (d.getSource() != null)
                System.out.println("  at " + d.getSource() + ":" + d.getLineNumber());
        }

        fm.close();

        if (success) {
            System.out.println("\n✓ Compilation successful! Run with:");
            System.out.println("  java -cp out com.rental.Main");
        } else {
            System.err.println("\n✗ Compilation failed.");
            System.exit(1);
        }
    }
}

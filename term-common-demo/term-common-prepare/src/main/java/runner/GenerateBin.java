package runner;

import xyz.cofe.io.fn.IOFun;
import xyz.cofe.io.fs.File;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class GenerateBin {
    private String outputFileName;
    private Charset outputCharset = Charset.defaultCharset();
    private final Template template;
    {
        var resourceName = "/batch-template.bat";
        Template t;
        try {
            //noinspection ConstantConditions
            t = new Template(IOFun.readText(GenerateBin.class.getResource(resourceName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            t = new Template("can't read resource "+resourceName);
        }
        template = t;
    }

    public static void main(String[] cmdLineArgs){
        var bin = new GenerateBin();
        var args = new ArrayList<String>(Arrays.asList(cmdLineArgs));
        while (!args.isEmpty()) {
            var arg = args.remove(0);
            switch (arg){
                case "-mainClass":
                    bin.template.setMainClass(args.remove(0));
                    break;
                case "-appName":
                    bin.template.setApplicationName(args.remove(0));
                    break;
                case "-repoName":
                    bin.template.setRepoName(args.remove(0));
                    break;
                case "-javaExe":
                    bin.template.setJavaExe(args.remove(0));
                    break;
                case "-charset":
                    bin.outputCharset = Charset.forName(args.remove(0));
                    break;
                case "-javaOpts":
                    bin.template.setJavaOptions(args.remove(0));
                    break;
                case "-out":
                    bin.outputFileName = args.remove(0);
                    break;
                default:
                    System.err.println("undefined command line args "+arg);
                    break;
            }
        }
        bin.generate();
    }

    private void generate(){
        if( outputFileName==null )throw new IllegalStateException("output file not defined, use -out command line arg");

        var outFile = new File(outputFileName);

        var dir = outFile.toAbsolute().normalize().getParent();
        if( dir!=null && !dir.exists() ){
            dir.createDirectories();
        }

        outFile.writeText(template.toString(), outputCharset);
        System.out.println("written "+outFile);
    }
}

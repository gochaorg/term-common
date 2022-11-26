package runner;

import xyz.cofe.io.fn.IOFun;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CopyFile {
    public static void main(String[] args){
        if( args==null )throw new IllegalArgumentException("args==null");

        List<File> from = new ArrayList<>();
        List<File> to = new ArrayList<>();
        var cmdl = new ArrayList<>(Arrays.asList(args));
        var state = "init";
        while (!cmdl.isEmpty()){
            var arg = cmdl.remove(0);
            switch (state){
                case "init":
                    if( "-from".equals(arg) ){
                        state = "from";
                    }else if( "-to".equals(arg) ){
                        state = "to";
                    }
                    break;
                case "from":
                    from.add(new File(arg));
                    state = "init";
                    break;
                case "to":
                    to.add(new File(arg));
                    state = "init";
                    break;
            }
        }

        from.forEach( fromFile -> {
            to.forEach( toFile -> {
                copy(fromFile,toFile);
            });
        });
    }

    private static void copy(File from, File to){
        System.out.println("copy "+from+" to "+to);

        var dir = to.getParentFile();
        if( dir!=null && !dir.exists() ){
            if( !dir.mkdirs() ){
                throw new RuntimeException("can't mkdirs "+dir);
            }
        }

        if( from.isFile() ){
            copyFile(from, to);
        }else if( from.isDirectory() ){
            copyDir(from, to);
        }else {
            System.out.println("not file or dir "+from);
        }
    }

    private static void copyFile(File from, File to){
        try( var src = new FileInputStream(from); var dest = new FileOutputStream(to)){
            IOFun.copy(src, dest);
            dest.flush();
            System.out.println("file copied "+from+" to "+to);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyDir(File from, File to){
        if( !to.exists() ){
            if( !to.mkdirs() ){
                throw new RuntimeException("can't mkdir "+to);
            }
        }

        for( var subFile : from.listFiles() ){
            copy(subFile, new File(to, subFile.getName()));
        }
        System.out.println("dir copied "+from+" to "+to);
    }
}

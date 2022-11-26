package runner;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Heredoc {
    private final String heredoc;

    public Heredoc(String heredoc){
        if( heredoc ==null )throw new IllegalArgumentException("heredoc==null");
        if( heredoc.trim().length()<1 )throw new IllegalArgumentException("heredoc.trim().length()<1");
        this.heredoc = heredoc;
    }

    public Heredoc(){
        this("<<");
    }

    public String toString(String source, Function<String,String> plainTextEval, Function<String,String> codeEval){
        if( source==null )throw new IllegalArgumentException("source==null");
        if( plainTextEval==null )throw new IllegalArgumentException("plainTextEval==null");
        if( codeEval==null )throw new IllegalArgumentException("codeEval==null");
        StringBuilder sb = new StringBuilder();
        toString(sb,source,heredoc,plainTextEval,codeEval);
        return sb.toString();
    }

    public void toString(StringBuilder out, String source, Function<String,String> plainTextEval, Function<String,String> codeEval){
        if( out==null )throw new IllegalArgumentException("out==null");
        if( source==null )throw new IllegalArgumentException("source==null");
        if( plainTextEval==null )throw new IllegalArgumentException("plainTextEval==null");
        if( codeEval==null )throw new IllegalArgumentException("codeEval==null");
        toString(out,source,heredoc,plainTextEval,codeEval);
    }

    private static String lookup(String source,int from,int len){
        int rest = source.length() - from;
        if( rest<=0 )return "";
        return source.substring(from, from + Math.min(rest,len));
    }

    private static class HerdocBegin {
        public final int begin;
        public final int end;
        public final String marker;
        public final boolean tabSuppresion;

        public HerdocBegin(int begin, int end, String marker, boolean tabSuppresion) {
            this.begin = begin;
            this.end = end;
            this.marker = marker;
            this.tabSuppresion = tabSuppresion;
        }
    }

    private static Optional<HerdocBegin> parseHerdocBegin(String source, int from, String herdoc){
        if(! source.startsWith(herdoc,from) )return Optional.empty();
        var ptr = from + herdoc.length();
        var state = 0;
        var tabSuppresion = false;
        var marker = new StringBuilder();

        Predicate<Character> markerChar = c -> false;
        while (ptr < source.length() && state>=0){
            switch (state){
                case 0:
                    if( source.charAt(ptr)=='-' ){
                        tabSuppresion = true;
                        ptr++;
                    }
                    state = 1;
                    break;
                case 1: {
                    var c0 = source.charAt(ptr);
                    if (Character.isWhitespace(c0)) {
                        ptr++;
                    } else if (Character.isLetter(c0)) {
                        state = 2;
                        ptr++;
                        marker.append(c0);
                        markerChar = Character::isLetter;
                    } else {
                        marker.append(c0);
                        ptr++;
                        state = 2;
                        markerChar = c -> !Character.isLetter(c);
                    }
                } break;
                case 2: {
                    var c0 = source.charAt(ptr);
                    if (markerChar.test(c0)) {
                        marker.append(c0);
                        ptr++;
                    } else {
                        state = -1;
                    }
                } break;
            }
        }

        if( state!=-1 )return Optional.empty();
        return Optional.of(new HerdocBegin(from,ptr,marker.toString(),tabSuppresion));
    }

    private static void toString(StringBuilder sb, String source, String heredoc, Function<String,String> plainTextEval, Function<String,String> codeEval){
        var plainText = new StringBuilder();
        var code = new StringBuilder();

        var state = new String[]{ "plain" };
        var prevState = new String[]{ state[0] };
        var herdocBeginLen = heredoc.length();
        HerdocBegin herdocBegin = null;

        Runnable flush = ()->{
            if( prevState[0].equals("plain") ){
                sb.append(plainTextEval.apply(plainText.toString()));
                plainText.setLength(0);
            }else //noinspection ConstantConditions
                if( prevState[0].equals("code") ){
                    sb.append(codeEval.apply(code.toString()));
                    code.setLength(0);
                }
            prevState[0] = state[0];
        };

        Supplier<String> stateGet = ()->state[0];
        Consumer<String> stateSet = str -> {
            state[0] = str;
            flush.run();
        };

        var ptr = 0;
        try {
            while (ptr < source.length()) {
                switch (stateGet.get()) {
                    case "plain":
                        var c0 = source.charAt(ptr);
                        if (c0 == heredoc.charAt(0) && lookup(source, ptr, herdocBeginLen).equals(heredoc)) {
                            var hdOpt = parseHerdocBegin(source, ptr, heredoc);
                            if (hdOpt.isPresent()) {
                                herdocBegin = hdOpt.get();
                                ptr = herdocBegin.end;
                                stateSet.accept("code");
                            }
                        } else {
                            plainText.append(c0);
                            ptr++;
                        }
                        break;
                    case "code":
                        //noinspection ConstantConditions
                        var endCode = source.indexOf(herdocBegin.marker, ptr);
                        if (endCode >= ptr) {
                            code.append(source.substring(ptr, endCode));
                            stateSet.accept("plain");
                            ptr = endCode + herdocBegin.marker.length();
                        } else {
                            code.append(source.substring(ptr));
                            stateSet.accept("plain");
                            return;
                        }
                        break;
                }
            }
        } finally {
            flush.run();
        }
    }
}

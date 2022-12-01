package xyz.cofe.term.common;

import com.googlecode.lanterna.terminal.MouseCaptureMode;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;
import xyz.cofe.term.common.err.ConsoleError;
import xyz.cofe.term.common.nix.NixAsyncConsole;
import xyz.cofe.term.common.nix.NixConsole;
import xyz.cofe.term.common.win.WinConsole;
import xyz.cofe.term.win.ConnectToConsole;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Создание консоли под конкретную ОС.
 *
 * <br>
 * В зависимости от настроек которые располагается в ресурсах создает ту или иную консоль.
 *
 * <h2>Настройки</h2>
 * При создании консоли ищет настройки в ресурсах программы.
 *
 * <br>
 * Ищет файл <code>term-common.properties</code>
 *
 * <br>
 * Создайте файл <code>term-common.properties</code> так
 * <pre>
 * src/
 *   main/
 *     resources/
 *       <b>term-common.properties</b>
 * </pre>
 *
 * Файл может содержать следующие свойства
 *
 * <ul>
 *     <li>
 *         <b>default</b> = auto
 *         <ul>
 *             <li>Возможные значения: auto | nix | win</li>
 *             <li>По умолчанию auto</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>nix.async.reader</b> = true
 *         <ul>
 *             <li>Возможные значения: true | false</li>
 *             <li>По умолчанию true</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>win.connect</b> = tryAttach
 *         <ul>
 *             <li>Возможные значения: alloc | attach | tryAttach</li>
 *             <li>По умолчанию tryAttach</li>
 *         </ul>
 *     </li>
 * </ul>
 */
public class ConsoleBuilder {
    //region init config values
    private static final String confResourceName = "term-common.properties";
    private static final Map<String,String> config;

    // initialize
    static {
        var src = new ArrayList<URL>();
        Map<String,String> conf = null;
        try {
            var propsUrls = ConsoleBuilder.class.getClassLoader().getResources(confResourceName);
            while (propsUrls.hasMoreElements()){
                var propsUrl = propsUrls.nextElement();
                src.add(propsUrl);
            }

            conf = merge(sort(load(src)));
        } catch (IOException e) {
            System.err.println("fail read Console config "+e);
            conf = new HashMap<>();
        }
        config = conf;
    }

    /**
     * read url content
     * @param source urls of properties files
     * @return properties
     */
    private static List<Properties> load(List<URL> source) throws IOException {
        var result = new ArrayList<Properties>();
        for( var url : source ){
            try( var reader = url.openStream() ){
                Properties props = new Properties();
                props.load(reader);
                result.add(props);
            }
        }
        return result;
    }

    private static final String ORDER_FIELD = "order";

    /**
     * sort config files before merge by ORDER_FIELD in lexical order
     * @param config config files
     * @return sorted config files
     */
    private static List<Properties> sort(List<Properties> config){
        Map<String, List<Properties>> ordered = new TreeMap<>();
        config.forEach( conf -> {
            ordered.computeIfAbsent(
                conf.getProperty(ORDER_FIELD, "0000"),
                x -> new ArrayList<>()
            ).add(conf);
        });
        return ordered.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
    private static Map<String,String> merge(List<Properties> config){
        var res = new HashMap<String,String>();
        config.forEach( conf -> {
            conf.stringPropertyNames().forEach( name -> {
                res.put(name, conf.getProperty(name));
            });
        });
        return res;
    }

    /**
     * read config value with validate
     *
     * @param key key of value
     * @param defaultValue default value if value with key not exists or
     *                     read value not valid
      */
    @SuppressWarnings("SameParameterValue")
    private static String read(String key, String defaultValue, Predicate<String> validate){
        if( !config.containsKey(key) )return defaultValue;
        var value = config.get(key);
        if( validate!=null && !validate.test(value) )return defaultValue;
        return value;
    }
    //endregion

    private static String osName(){ return System.getProperty("os.name","nix"); }
    private static boolean isWinOs(){ return osName().toLowerCase().contains("window"); }

    /**
     * Создает консоль в зависимости от настроек (файл в ресурсах term-common.properties)
     * @return консоль
     * @see #autoConsole()
     */
    public static Console defaultConsole(){
        switch ( read("default","auto", str -> List.of("auto", "win", "nix").contains(str)) ){
            case "win":
                return windowsConsole();
            case "nix":
                return nixConsole();
            case "auto":
            default:
                return autoConsole();
        }
    }

    /**
     * Создает консоль основываясь на системном свойстве os.name
     * @return консоль
     * @see #windowsConsole()
     * @see #nixConsole()
     */
    public static Console autoConsole(){
        return isWinOs() ? windowsConsole() : nixConsole();
    }

    /**
     * Создает консоль пригодную для OS Windows.
     *
     * <br>
     * В зависимости от настроек win.connect определяет способ присоединения к  консоли
     * @return консоль
     */
    public static Console windowsConsole(){
        ConnectToConsole connect = new ConnectToConsole.TryAttachParent();
        switch ( read("win.connect", "tryAttach", str -> List.of("alloc", "attach", "tryAttach").contains(str)) ) {
            case "alloc":
                connect = new ConnectToConsole.AllocConsole();
                break;
            case "attach":
                connect = new ConnectToConsole.AttachParent();
                break;
            case "tryAttch":
                connect = new ConnectToConsole.TryAttachParent();
            default:
                break;
        }
        var winTerm = new xyz.cofe.term.win.WinConsole(connect);
        return new WinConsole(winTerm);
    }

    /**
     * Создает консоль пригодную для OS Linux/Mac
     * @return консоль
     */
    public static Console nixConsole(){
        try {
            UnixTerminal terminal = new UnixTerminal();
            terminal.setMouseCaptureMode(MouseCaptureMode.CLICK_RELEASE_DRAG_MOVE);

            var async_str = read("nix.async.reader", "true", str -> List.of("true","false").contains(str));
            var async = "true".equals(async_str);

            return async ? new NixAsyncConsole(terminal) : new NixConsole(terminal);
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }
}

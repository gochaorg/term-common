package xyz.cofe.term.common.demo;

import xyz.cofe.term.common.*;
import xyz.cofe.term.common.win.WConsole;
import xyz.cofe.term.common.win.WinInputMouseButtonEvent;
import xyz.cofe.term.win.ConnectToConsole;
import xyz.cofe.term.win.WinConsole;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args){
        var main = new Main();

        var delay_for_debug = System.getenv("DELAY_FOR_DEBUG");
        if( delay_for_debug!=null ){
            if( delay_for_debug.matches("\\d+") ){
                System.out.println("delay for attach debugger");
                try {
                    Thread.sleep(Integer.parseInt(delay_for_debug) * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        main.run();
    }

    private void run(){
        var winConsole = new WinConsole(new ConnectToConsole.AllocConsole());
        winConsole.setInputMode(
            winConsole.getInputMode().quickEdit(false).mouse(true)
        );
        try (Console console = new WConsole(winConsole)) {
            run(console);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void run(Console console){
        console.setCursorPosition(new Position(0,0));
        console.write("press q for exit\n");

        var stop = false;
        while (!stop){
            var input = console.read();
            if( input.isEmpty() ){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                var inputEvent = input.get();
                if( inputEvent instanceof InputCharEvent ) {
                    var ev = (InputCharEvent) input.get();
                    console.write("input char " + ev.getChar() + "\n");
                    if (ev.getChar() == 'q') {
                        stop = true;
                    }
                }else if( inputEvent instanceof InputKeyEvent){
                    var ev = (InputKeyEvent) input.get();
                    console.write("input key "+ev.getKey()+" alt="+ev.isAltDown()+" shift="+ev.isShiftDown()+" ctrl="+ev.isControlDown());
                }else if( inputEvent instanceof InputMouseButtonEvent ){
                    var ev = (InputMouseButtonEvent)inputEvent;
                    console.write("input mouse button="+ev.button()+" pressed="+ev.pressed()+" position="+ ev.position());
                    if( ev instanceof WinInputMouseButtonEvent ){
                        var wev = (WinInputMouseButtonEvent)ev;
                        console.write(" "+wev.getEvent());
                    }
                    console.write("\n");
                }
            }
        }
    }
}

package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.ScreenInfoAction;
import com.googlecode.lanterna.terminal.ExtendedTerminal;
import xyz.cofe.term.common.*;
import xyz.cofe.term.common.err.ConsoleError;
import xyz.cofe.term.common.ev.InputResizeEventImpl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

/**
 * Адоптация для Unix терминалов
 */
public class NixConsole extends NixAbstractConsole {
    protected final ExtendedTerminal terminal;

    public NixConsole( ExtendedTerminal terminal ){
        if( terminal==null )throw new IllegalArgumentException("terminal==null");
        this.terminal = terminal;

        init(terminal);
    }

    @Override
    public void close() throws Exception {
        close(terminal);
    }

    protected final Queue<InputEvent> eventBuffer = createEventQueue();
    protected Queue<InputEvent> createEventQueue(){ return new ConcurrentLinkedQueue<>(); }

    @Override
    protected <R> R terminal(Function<ExtendedTerminal, R> term) {
        return term.apply(terminal);
    }

    @Override
    protected void putToQueue(InputEvent event) {
        eventBuffer.add(event);
    }

    @Override
    protected Optional<InputEvent> pollFromQueue() {
        var ev = eventBuffer.poll();
        return ev!=null ? Optional.of(ev) : Optional.empty();
    }

    protected final List<MouseAction> mouseActions = createHistory();
    protected List<MouseAction> createHistory(){ return new ArrayList<>(); }

    @SuppressWarnings("FieldCanBeLocal")
    protected final int limitMouseActions = 10;

    @Override
    protected <R> R mouseHistory(Function<List<MouseAction>, R> history) {
        var res = history.apply(mouseActions);
        if( mouseActions.size()>limitMouseActions ){
            var diff = mouseActions.size() - limitMouseActions;
            mouseActions.subList(0,diff).clear();
        }
        return res;
    }
}

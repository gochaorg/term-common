package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.terminal.ExtendedTerminal;
import xyz.cofe.term.common.Color;
import xyz.cofe.term.common.InputEvent;
import xyz.cofe.term.common.Position;
import xyz.cofe.term.common.Size;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class NixAsyncConsole extends NixConsole {
    public NixAsyncConsole(ExtendedTerminal terminal) {
        super(terminal);
        readerThread = new InputReader();
        readerThread.reading(this::readSync);
        readerThread.start();
    }

    protected final InputReader readerThread;

    protected class InputReader extends Thread {
        public InputReader(){
            setDaemon(true);
            setName("console reader");
        }

        public volatile boolean stopReading = false;
        private volatile Supplier<Optional<InputEvent>> read = Optional::empty;
        public void reading(Supplier<Optional<InputEvent>> reading ){
            if( reading==null )throw new IllegalArgumentException("reading==null");
            this.read = reading;
        }

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            var read = this.read;
            while (!stopReading) {
                read.get().ifPresent(NixAsyncConsole.this::putToQueue);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    protected final ReadWriteLock closeLock  = new ReentrantReadWriteLock();
    protected <R> R lock( boolean write, Supplier<R> code ){
        var l = write ? closeLock.writeLock() : closeLock.readLock();
        try {
            l.lock();
            return code.get();
        } finally {
            l.unlock();
        }
    }
    protected void lock( boolean write, Runnable code ){
        lock(write, () -> {
            code.run();
            return null;
        });
    }
    protected <R> R lockRead( Supplier<R> code ){
        return lock(false,code);
    }
    protected void lockRead( Runnable code ){
        lock(false,code);
    }

    @Override
    protected Queue<InputEvent> createEventQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Override
    protected List<MouseAction> createHistory() {
        return new CopyOnWriteArrayList<>();
    }

    @Override
    public void close() throws Exception {
        lock(true,()->{
            while (readerThread.isAlive()){
                readerThread.interrupt();
                readerThread.stopReading = true;
            }

            closeLock.writeLock().lock();
            close(terminal);
        });
    }

    @Override
    public void setTitle(String title) {
        lockRead(()-> super.setTitle(title) );
    }

    @Override
    public Position getCursorPosition() {
        return lockRead(super::getCursorPosition);
    }

    @Override
    public void setCursorPosition(Position position) {
        lockRead( ()-> super.setCursorPosition(position) );
    }

    @Override
    public void setCursorPosition(int x, int y) {
        lockRead( ()-> super.setCursorPosition(x, y) );
    }

    @Override
    public Size getSize() {
        return lockRead(super::getSize);
    }

    @Override
    public void setSize(Size size) {
        lockRead(()->super.setSize(size));
    }

    @Override
    public void setForeground(Color color) {
        lockRead(()->super.setForeground(color));
    }

    @Override
    public void setBackground(Color color) {
        lockRead(()->super.setBackground(color));
    }

    @Override
    public void setCursorVisible(boolean visible) {
        lockRead(()->super.setCursorVisible(visible));
    }

    @Override
    public Optional<InputEvent> read() {
        return lockRead(this::pollFromQueue);
    }

    @Override
    public Optional<InputEvent> readSync() {
        return lockRead(super::readSync);
    }

    @Override
    public void write(String text) {
        lockRead(()->super.write(text));
    }

    @Override
    protected Optional<InputEvent> read(KeyStroke ks) {
        return lockRead(()->super.read(ks));
    }
}

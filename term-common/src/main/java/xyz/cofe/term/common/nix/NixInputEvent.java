package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.input.KeyStroke;

/**
 * Возвращает исходное событие
 */
public interface NixInputEvent {

    /**
     * Возвращает исходное событие
     * @return исходное событие
     */
    KeyStroke getNixInputEvent();
}

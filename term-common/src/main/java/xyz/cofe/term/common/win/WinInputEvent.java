package xyz.cofe.term.common.win;

import xyz.cofe.term.common.InputEvent;
import xyz.cofe.term.common.KeyName;

import java.util.Optional;

public interface WinInputEvent {
    public static Optional<InputEvent> parse( xyz.cofe.term.win.InputEvent event ){
        if( event instanceof xyz.cofe.term.win.InputKeyEvent ){
            return parse((xyz.cofe.term.win.InputKeyEvent)event);
        }else if( event instanceof xyz.cofe.term.win.InputMouseEvent ){
            return parse((xyz.cofe.term.win.InputMouseEvent)event);
        }else if( event instanceof xyz.cofe.term.win.InputWindowEvent ){
            return parse((xyz.cofe.term.win.InputWindowEvent)event);
        }else{
            return Optional.empty();
        }
    }

    /**
     * <table>
     *     <caption>codes of keys</caption>
     *     <thead style="font-weight:bold">
     *         <tr>
     *            <td>KeyName</td>
     *            <td>keyCode</td>
     *            <td>scanCode</td>
     *         </tr>
     *     </thead>
     *     <tr><td> F1         </td><td> 112  </td><td> 59 </td></tr>
     *     <tr><td> F2         </td><td> 113  </td><td> 60 </td></tr>
     *     <tr><td> F3         </td><td> 114  </td><td> 61 </td></tr>
     *     <tr><td> F4         </td><td> 115  </td><td> 62 </td></tr>
     *     <tr><td> F5         </td><td> 116  </td><td> 63 </td></tr>
     *     <tr><td> F6         </td><td> 117  </td><td> 64 </td></tr>
     *     <tr><td> F7         </td><td> 118  </td><td> 65 </td></tr>
     *     <tr><td> F8         </td><td> 119  </td><td> 66 </td></tr>
     *     <tr><td> F9         </td><td> 120  </td><td> 67 </td></tr>
     *     <tr><td> F10        </td><td> 121  </td><td> 68 </td></tr>
     *     <tr><td> F11        </td><td> 122  </td><td> 87 </td></tr>
     *     <tr><td> F12        </td><td> 123  </td><td> 88 </td></tr>
     *     <tr><td> Escape     </td><td> 27   </td><td> 1  </td></tr>
     *     <tr><td> Backspace  </td><td> 8    </td><td> 14 </td></tr>
     *     <tr><td> ArrowLeft  </td><td> 37   </td><td> 75 </td></tr>
     *     <tr><td> ArrowRight </td><td> 39   </td><td> 77 </td></tr>
     *     <tr><td> ArrowUp    </td><td> 38   </td><td> 72 </td></tr>
     *     <tr><td> ArrowDown  </td><td> 40   </td><td> 80 </td></tr>
     *     <tr><td> Insert     </td><td> 45   </td><td> 82 </td></tr>
     *     <tr><td> Delete     </td><td> 46   </td><td> 83 </td></tr>
     *     <tr><td> Home       </td><td> 36   </td><td> 71 </td></tr>
     *     <tr><td> End        </td><td> 35   </td><td> 79 </td></tr>
     *     <tr><td> PageUp     </td><td> 33   </td><td> 73 </td></tr>
     *     <tr><td> PageDown   </td><td> 34   </td><td> 81 </td></tr>
     *     <tr><td> Tab        </td><td> 9    </td><td> 15 </td></tr>
     *     <tr><td> ReverseTab </td><td>      </td><td>    </td></tr>
     *     <tr><td> Enter      </td><td> 13   </td><td> 28 </td></tr>
     * </table>
     * @param event event
     * @return event
     */
    public static Optional<InputEvent> parse( xyz.cofe.term.win.InputKeyEvent event ){
        if(event.isKeyDown()) {
            if (event.isEnhanced()) {
                return keyName(event.getKeyCode()).map(keyName -> new WinInputKeyEvent(keyName,
                    event.isLeftAlt() || event.isRightAlt(),
                    event.isShift(),
                    event.isLeftCtrl() || event.isRightCtrl(),
                    event));
            }else{
                if( ((int)event.getCharacter())<32 ){
                    return keyName(event.getKeyCode()).map(keyName -> new WinInputKeyEvent(keyName,
                        event.isLeftAlt() || event.isRightAlt(),
                        event.isShift(),
                        event.isLeftCtrl() || event.isRightCtrl(),
                        event));
                }else{
                    return Optional.of(
                        new WinInputCharEvent(
                            event.getCharacter(),
                            event.isLeftAlt() || event.isRightAlt(),
                            event.isShift(),
                            event.isLeftCtrl() || event.isRightCtrl(),
                            event
                        )
                    );
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<KeyName> keyName(int keyCode){
        switch (keyCode){
            case 112: return Optional.of(KeyName.F1);
            case 113: return Optional.of(KeyName.F2);
            case 114: return Optional.of(KeyName.F3);
            case 115: return Optional.of(KeyName.F4);
            case 116: return Optional.of(KeyName.F5);
            case 117: return Optional.of(KeyName.F6);
            case 118: return Optional.of(KeyName.F7);
            case 119: return Optional.of(KeyName.F8);
            case 120: return Optional.of(KeyName.F9);
            case 121: return Optional.of(KeyName.F10);
            case 122: return Optional.of(KeyName.F11);
            case 123: return Optional.of(KeyName.F12);
            case 27:  return Optional.of(KeyName.Escape);
            case 8:   return Optional.of(KeyName.Backspace);
            case 37:  return Optional.of(KeyName.Left);
            case 39:  return Optional.of(KeyName.Right);
            case 38:  return Optional.of(KeyName.Up);
            case 40:  return Optional.of(KeyName.Down);
            case 45:  return Optional.of(KeyName.Insert);
            case 46:  return Optional.of(KeyName.Delete);
            case 36:  return Optional.of(KeyName.Home);
            case 35:  return Optional.of(KeyName.End);
            case 33:  return Optional.of(KeyName.PageUp);
            case 34:  return Optional.of(KeyName.PageDown);
            case 9:   return Optional.of(KeyName.Tab);
            case 13:   return Optional.of(KeyName.Enter);
            default: return Optional.empty();
        }
    }

    private static Optional<InputEvent> parse( xyz.cofe.term.win.InputMouseEvent event ){
        return Optional.empty();
    }

    private static Optional<InputEvent> parse( xyz.cofe.term.win.InputWindowEvent event ){
        return Optional.empty();
    }
}

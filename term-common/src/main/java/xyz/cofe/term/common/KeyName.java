package xyz.cofe.term.common;

// OS Win:
// F1         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=112, scanCode=59, character=state={}}
// F2         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=113, scanCode=60, character=state={}}
// F3         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=114, scanCode=61, character=state={}}
// F4         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=115, scanCode=62, character=state={}}
// F5         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=116, scanCode=63, character=state={}}
// F6         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=117, scanCode=64, character=state={}}
// F7         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=118, scanCode=65, character=state={}}
// F8         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=119, scanCode=66, character=state={}}
// F9         InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=120, scanCode=67, character=state={}}
// F10        InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=121, scanCode=68, character=state={}}
// F11        InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=122, scanCode=87, character=state={}}
// F12        InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=123, scanCode=88, character=state={}}
// Escape     InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=27 , scanCode=1  , character=state={}}
// Backspace  InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=8  , scanCode=14 , character=state={}}
// ArrowLeft  InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=37 , scanCode=75 , character=state={Enhanced}}
// ArrowRight InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=39 , scanCode=77 , character=state={Enhanced}}
// ArrowUp    InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=38 , scanCode=72 , character=state={Enhanced}}
// ArrowDown  InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=40 , scanCode=80 , character=state={Enhanced}}
// Insert     InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=45 , scanCode=82 , character=state={Enhanced}}
// Delete     InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=46 , scanCode=83 , character=state={Enhanced}}
// Home       InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=36 , scanCode=71 , character=state={Enhanced}}
// End        InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=35 , scanCode=79 , character=state={Enhanced}}
// PageUp     InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=33 , scanCode=73 , character=state={Enhanced}}
// PageDown   InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=34 , scanCode=81 , character=state={Enhanced}}
// Tab        InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=9  , scanCode=15 , character=state={}}
// ReverseTab InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=   , scanCode=   , character=state={}} ?
// Enter      InputKeyEvent{keyDown=true, repeatCount=1,  keyCode=13 , scanCode=28 , character=state={}}

/**
 * Специальная клавиша клавиатуры
 */
public enum KeyName {
    F1, F2, F3, F4, F5, F6,
    F7, F8, F9, F10, F11, F12,
    Escape, Enter,
    Left, Right, Up, Down,
    Insert, Delete, Backspace,
    Home, End,
    PageUp, PageDown,
    Tab, ReverseTab
}

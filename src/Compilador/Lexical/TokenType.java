package Compilador.Lexical;

/**
 *
 * @author pedro
 */
public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,

    // symbols
    COMMA,      // ,
    DOT_COMMA,  // ;
    PAR_OPEN,   // (
    PAR_CLOSE,  // )
    CBRA_OPEN,  // {
    CBRA_CLOSE, // }

    // keywords
    PRINT,     // print
    IF,        // if
    ELSE,      // else
    WHILE,     // while
    START,     // start
    EXIT,      // exit
    INT,       // int
    FLOAT,     // float
    STRING,    // string
    DO,        // do
    END,       // end
    SCAN,      //scan
    THEN,      // then

    // operators
    EQUAL,     // = 
    AND,       // and
    OR,        // or
    EQUAL_COMP,     // ==
    DIFF,      // !=
    LOWER,     // <
    HIGHER,    // >
    LOWER_EQ,  // <=
    HIGHER_EQ, // >=
    PLUS,      // +
    MINUS,     // -
    MUL,       // *
    DIV,       // /

    // others
    VAR,             // var
    LITERAL,         // string literal
    CONST_INT,       // const int
    CONST_FLOAT      // const float
};

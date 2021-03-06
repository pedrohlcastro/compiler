package Compilador.Lexical;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 *
 * @author pedro
 */
public class LexicalAnalysis {
    private int line;
    private PushbackInputStream input;
    private SymbolTable st = new SymbolTable();
    private TokenType lastType;
    private int identLevel = 0;
    public LexicalAnalysis(String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }

        line = 1;
    }

    public void close() throws Exception {
        input.close();
    }

    public int line() {
        return this.line;
    }


    public Lexeme nextToken() throws IOException {
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);
        int c, e = 1;
        while (e != 9 && e != 10){
            c = input.read();
            //System.out.printf("[LOG] - %d\n", (int)c);
            switch (e){
                case 1:
                    if (c == -1)
                        e = 10;
                    else if (c == ' ' || c == '\t' || c == '\n' || c == '\r'){
                        e = 1;
                        if (c == '\n')
                            this.line++;
                    }
                    else if (c == '{')
                        e = 2;
                    else if (Character.isDigit(c)){
                        lex.token += (char) c;
                        e = 3;
                    }
                    else if (c == '!' || c == '='){
                        lex.token += (char) c;
                        e = 4;
                    }
                    else if(c == '<' || c == '>'){
                        lex.token += (char) c;
                        e = 5;
                    }
                    else if(Character.isLetter(c)){
                        lex.token += (char) c;
                        e = 7;
                    }
                    else if (c == '\"'){
                        e = 8;
                    }
                    else if(c == ';' || c == ',' || c == '(' || c == ')' || c == '+' || c == '*' || c == '/' || c =='-'){
                        lex.token += (char)c;
                        e = 9;
                    }
                    else {
                        lex.token += (char) c;
                        lex.type = TokenType.INVALID_TOKEN;
                        e = 10;
                    }
                    break;
                case 2:
                    if (c == '}'){
                        e = 1;
                        this.line++; 
                    }
                    if(c == -1){
                        e = 10;
                    }
                    break;
                case 3:
                    if (Character.isDigit(c)){
                        lex.token += (char) c;
                    } else {
                        if (c != -1 && c != '.')
                            input.unread(c);
                        if(c == '.'){
                            lex.token += (char) c;
                            e = 6;
                        }
                        else {                            
                            lex.type = TokenType.CONST_INT;
                            e = 10;
                        }
                    }
                    break;
                case 4:
                    if (c == '='){
                        lex.token += (char) c;
                        e = 9;
                    } else {
                        if (c == -1)
                            lex.type = TokenType.UNEXPECTED_EOF;
                        else
                            e = 9;
                    }
                    break;
                case 5: //eu que fiz
                    if(c == '='){
                        lex.token += (char) c;
                    }
                    else{
                        if(c == -1){
                            lex.type = TokenType.UNEXPECTED_EOF;
                        }
                        else{
                            input.unread(c);
                        }                       
                    }
                    e = 9;
                    break;
                case 6://eu que fiz
                    if (Character.isDigit(c)){
                        lex.token += (char) c;
                    }
                    else {
                        if (c != -1){
                            input.unread(c);
                        }
                        lex.type = TokenType.CONST_FLOAT;
                        e = 10;
                    }
                    break;
                case 7:
                    if (Character.isDigit(c) || Character.isLetter(c)){
                        lex.token += (char) c;
                    } else {
                        if (c != -1)
                            input.unread(c);
                        e = 9;
                    }
                    break;
                case 8:
                    if (c == '\"'){
                        e = 10;
                        lex.type = TokenType.LITERAL;
                    } else {
                        if (c == -1){
                            e = 10;
                            lex.type = TokenType.UNEXPECTED_EOF;
                        } else
                            lex.token += (char) c;
                    }
                    break;
             }
         }
         if (e == 9){
            if (st.contains(lex.token)){
                lex.type = st.find(lex.token).getTokenType();
                if(lex.type != TokenType.VAR_FLT ||lex.type != TokenType.VAR_INT ||lex.type != TokenType.VAR_STR){
                    if(lex.type == TokenType.THEN || lex.type == TokenType.DO){
                        this.identLevel++;
                    } 
                    else if(lex.type == TokenType.END){
                        this.identLevel--;
                    }
                    else if(lex.type == TokenType.INT || lex.type == TokenType.FLOAT || lex.type == TokenType.STRING ){
                        this.lastType = lex.type;
                    } else if(lex.type == TokenType.DOT_COMMA){
                        this.lastType = null;
                    }
                } 
                /*EXPLODE ERRO DE VARIAVEL JA DECLARADA*/
                if(lex.type == TokenType.VAR_FLT ||lex.type == TokenType.VAR_INT ||lex.type == TokenType.VAR_STR) {
                    if(this.lastType != null){
                        System.err.printf ("[PREVIUS DECLARATION ERROR] LINE  - %d: %s\n", this.line, lex.token);
                        System.exit(0);
                    }
                }
            }
            else{
                lex.type = st.createVar(lex.token, this.identLevel, this.lastType);
            }
         }

        return lex;
    }
}

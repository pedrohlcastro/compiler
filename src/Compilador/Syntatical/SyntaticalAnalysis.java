package Compilador.Syntatical;

import Compilador.Lexical.Lexeme;
import Compilador.Lexical.LexicalAnalysis;
import Compilador.Lexical.TokenType;
import java.io.IOException;

/**
 *
 * @author pedro
 */
public class SyntaticalAnalysis {
    private Lexeme current;
    private LexicalAnalysis lex;

    public SyntaticalAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.current = lex.nextToken();
    }
    
    private void showError() {
        if (this.current.type == TokenType.UNEXPECTED_EOF || this.current.type == TokenType.END_OF_FILE)
            this.errorUnexpectedEOF ();
        else
            this.errorUnexpectedToken (this.current.token);
    }
    
    private void errorUnexpectedToken (String token){
        System.err.printf ("[UNEXPECTED TOKEN] LINE - %d : %s\n", this.lex.line(), token);
        System.exit(0);
    }
    
    private void errorUnexpectedEOF (){
        System.err.printf ("[UNEXPECTED EOF] LINE  - %d: %s\n", this.lex.line(), TokenType.UNEXPECTED_EOF);
        System.exit(0);
    }
    
    private void eat (TokenType type) throws IOException{
        if (type == this.current.type){
            this.current = lex.nextToken();
        }
        else {
            this.showError();
        }
    }
    
    // program ::= start [decl-list] stmt-list exit
    private void program() throws IOException{
        if(this.current.type == TokenType.START){
            this.eat(TokenType.START);
            // [decl-list]
            if(this.current.type == TokenType.INT || this.current.type == TokenType.FLOAT || this.current.type == TokenType.STRING){
                this.decl_list();
            } else {
                this.stmt_list();
                this.eat(TokenType.EXIT);
            }
        } else {
            this.showError();
        }
    }
    
    // decl-list ::= decl {decl}
    private void decl_list() throws IOException{
         if(this.current.type == TokenType.INT || this.current.type == TokenType.FLOAT || this.current.type == TokenType.STRING){
             this.decl();
             while(this.current.type == TokenType.INT || this.current.type == TokenType.FLOAT || this.current.type == TokenType.STRING){
                 this.decl();
             }
         } else {
            this.showError();
        }
    }

    
    //decl ::= type ident-list ";"
    private void decl() throws IOException{
        if(this.current.type == TokenType.INT || this.current.type == TokenType.FLOAT || this.current.type == TokenType.STRING){
            this.type();
            this.ident_list();
            this.eat(TokenType.DOT_COMMA);
        } else {
            this.showError();
        }
    }
    
    //ident-list ::= identifier {"," identifier}
    private void ident_list() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR){
            this.identifier();
            while(this.current.type == TokenType.COMMA){
                this.eat(TokenType.COMMA);
                this.identifier();
            }
        } else {
            this.showError();
        }
    }
    
    // type ::= int | float | string
    private void type() throws IOException{
        switch(this.current.type){
            case INT: this.eat(TokenType.INT); break;
            case FLOAT: this.eat(TokenType.FLOAT); break;
            case STRING: this.eat(TokenType.STRING); break;
            default: this.showError();
        }
    }
    
    //stmt-list ::= stmt {stmt}
    private void stmt_list() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR
           || this.current.type == TokenType.IF || this.current.type == TokenType.DO || this.current.type == TokenType.SCAN || this.current.type == TokenType.PRINT){
            
            this.stmt();
            
            while(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR
                || this.current.type == TokenType.IF || this.current.type == TokenType.DO || this.current.type == TokenType.SCAN || this.current.type == TokenType.PRINT){
                this.stmt();
            }
        } else {
            this.showError();
        }
    }
    
    //stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt ";"
    private void stmt() throws IOException{
        switch(this.current.type){
            case VAR_FLT:
            case VAR_STR:
            case VAR_INT: this.assign_stmt(); this.eat(TokenType.DOT_COMMA); break;
            case IF: this.if_stmt(); this.eat(TokenType.DOT_COMMA); break;
            case DO: this.while_stmt(); break;
            case SCAN: this.read_stmt(); this.eat(TokenType.DOT_COMMA); break;
            case PRINT: this.write_stmt(); this.eat(TokenType.DOT_COMMA); break;
            default: this.showError(); break;
        }
            

    }
    
    // assign-stmt ::= identifier "=" simple_expr
    private void assign_stmt() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR){
            this.identifier();
            this.eat(TokenType.EQUAL);
            this.simple_expr();
        } else {
            this.showError();
        }
    }
    // if-stmt ::= if condition then stmt-list end | if condition then stmt-list else stmt-list end
    private void if_stmt() throws IOException{
        if(this.current.type == TokenType.IF){
            this.condition();
            this.eat(TokenType.THEN);
            this.stmt_list();
            if(this.current.type == TokenType.ELSE){
                this.eat(TokenType.ELSE);
                this.stmt_list();
                this.eat(TokenType.END);
            } else {
                this.eat(TokenType.END);
            }
        } else {
            this.showError();
        }
    }
    
    // condition ::= expression
    private void condition(){
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.CBRA_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
        
            // TODO
            // simple-expr ::= term | simple-expr addop term
            // term ::= factor-a | term mulop factor-a
            // RECURSAO A ESQUERDA OQ FAZ????
            
        }
    }
    
    private void while_stmt(){
        
    }
    
    private void read_stmt(){
        
    }
    
    private void write_stmt(){
        
    }
    
    private void identifier(){
        
    }
    
    private void simple_expr(){
        
    }
}

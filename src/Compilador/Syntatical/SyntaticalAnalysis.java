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
    
    public void init () throws IOException{
        this.program();
        this.eat(TokenType.END_OF_FILE);
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
//            System.out.println(this.current);
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
            }
            this.stmt_list();
            this.eat(TokenType.EXIT);
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
//        System.out.println("STMT" + this.current);
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
//        System.out.println("AQUI" + this.current);
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
    private void condition() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            this.expression();
        } else {
            this.showError();
        }
    }
    
    // expression ::= simple-expr | simple-expr relop simple-expr
    private void expression() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            this.simple_expr();
            if(this.current.type == TokenType.EQUAL_COMP || this.current.type == TokenType.HIGHER || this.current.type == TokenType.HIGHER_EQ ||
                this.current.type == TokenType.LOWER || this.current.type == TokenType.LOWER_EQ || this.current.type == TokenType.DIFF){
                this.relop();
                this.simple_expr();
            }
        } else {
            this.showError();
        }
    }
    
    // while-stmt ::= do stmt-list stmt-sufix
    private void while_stmt() throws IOException{
        if(this.current.type == TokenType.DO) {
            this.eat(TokenType.DO);
            this.stmt_list();
            this.stmt_sufix();
        } else {
            this.showError();
        }
    }
    
    // stmt-sufix ::= while condition end
    private void stmt_sufix() throws IOException{
        if(this.current.type == TokenType.WHILE) {
            this.eat(TokenType.WHILE);
            this.condition();
            this.eat(TokenType.END);
        } else {
            this.showError();
        }
    }
    
    // read-stmt ::= scan "(" identifier ")"
    private void read_stmt() throws IOException{
        if(this.current.type == TokenType.SCAN) {
            this.eat(TokenType.SCAN);
            this.eat(TokenType.PAR_OPEN);
            this.identifier();
            this.eat(TokenType.PAR_CLOSE);
        } else {
            this.showError();
        }
    }
    
    // write-stmt ::= print "(" writable ")"
    private void write_stmt() throws IOException{
        if(this.current.type == TokenType.PRINT) {
            this.eat(TokenType.PRINT);
            this.eat(TokenType.PAR_OPEN);
            this.writable();
            this.eat(TokenType.PAR_CLOSE);
        } else {
            this.showError();
        }
    }
    
    // writable ::= simple-expr | literal
    private void writable() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            this.simple_expr();
        } else if(this.current.type == TokenType.LITERAL){
            this.eat(TokenType.LITERAL);
        } else {
            this.showError();
        }
    }
    
    private void identifier() throws IOException{
        if(this.current.type == TokenType.VAR_FLT){
            this.eat(TokenType.VAR_FLT);
        }else if(this.current.type == TokenType.VAR_INT){
            this.eat(TokenType.VAR_INT);
        } else {
            this.eat(TokenType.VAR_STR);
        }
    }
    
    // simple_expr := term simple_expr_aux
    private void simple_expr() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            this.term();
            this.simple_expr_aux();
        } else {
            this.showError();
        }
    }
    
    // simple_expr_aux := addOp term simple_expr_aux | lambda
    private void simple_expr_aux() throws IOException{
        if(this.current.type == TokenType.PLUS ||this.current.type == TokenType.MINUS || this.current.type == TokenType.OR){
            this.addOp();
            this.term();
            this.simple_expr_aux();
        }
    }
    
    // term := factor_a term_aux
    private void term() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            this.factor_a();
            this.term_aux();
        } else {
            this.showError();
        }
    }
    
    // term_aux := mulOp factor_a termAux | lambda
    private void term_aux() throws IOException{
        if(this.current.type == TokenType.MUL ||this.current.type == TokenType.DIV || this.current.type == TokenType.AND){
            this.mulOp();
            this.factor_a();
            this.term_aux();
        }
    }
    
    // fator-a ::= factor | not factor | "-" factor
    private void factor_a() throws IOException{
         if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN ){
             this.factor();
         } else if(this.current.type == TokenType.NOT){
             this.eat(TokenType.NOT);
             this.factor();
         } else if(this.current.type == TokenType.MINUS){
             this.eat(TokenType.MINUS);
             this.factor();
         } else {
             this.showError();
         }
    }
    
    // factor ::= identifier | constant | "(" expression ")"
    private void factor() throws IOException{
        if(this.current.type == TokenType.PAR_OPEN){
            this.eat(TokenType.PAR_OPEN);
            this.expression();
            this.eat(TokenType.PAR_CLOSE);
        } else if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR){
            this.identifier();
        } else if(this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL){
            this.constant();
        } else {
            this.showError();
        }
    }
    
    // addop ::= "+" | "-" | or
    private void addOp() throws IOException{
        switch(this.current.type){
            case PLUS:
                this.eat(TokenType.PLUS);
                break;
            case MINUS:
                this.eat(TokenType.MINUS);
                break;
            case OR:
                this.eat(TokenType.OR);
                break;
            default:
                this.showError();
                break;
        }
    }
    
    private void relop() throws IOException{
        switch(this.current.type){
            case HIGHER:
                this.eat(TokenType.HIGHER);
                break;
            case HIGHER_EQ:
                this.eat(TokenType.HIGHER_EQ);
                break;
            case LOWER:
                this.eat(TokenType.LOWER);
                break;
            case LOWER_EQ:
                this.eat(TokenType.LOWER_EQ);
                break;
            case DIFF:
                this.eat(TokenType.DIFF);
                break;
            case EQUAL:
                this.eat(TokenType.DIFF);
                break;
            default:
                this.showError();
                break;
        }
    }
    
    // mulop ::= "*" | "/" | and
    private void mulOp() throws IOException{
        switch(this.current.type){
            case MUL:
                this.eat(TokenType.MUL);
                break;
            case DIV:
                this.eat(TokenType.DIV);
                break;
            case AND:
                this.eat(TokenType.AND);
                break;
            default:
                this.showError();
                break;
        }
    }
    
    // constant ::= integer_const | float_const | literal
    private void constant() throws IOException{
         switch(this.current.type){
            case CONST_FLOAT:
                this.eat(TokenType.CONST_FLOAT);
                break;
            case CONST_INT:
                this.eat(TokenType.CONST_INT);
                break;
            case LITERAL:
                this.eat(TokenType.LITERAL);
                break;
            default:
                this.showError();
                break;
        }
    }
}

package Compilador.Syntatical;

import Compilador.Lexical.Lexeme;
import Compilador.Lexical.LexicalAnalysis;
import Compilador.Lexical.SymbolTable;
import Compilador.Lexical.TokenType;
import Compilador.Semantical.AddOp;
import Compilador.Semantical.AssignCommand;
import Compilador.Semantical.BoolValue;
import Compilador.Semantical.Command;
import Compilador.Semantical.CommandBlock;
import Compilador.Semantical.CompareBoolValue;
import Compilador.Semantical.ConstFloatValue;
import Compilador.Semantical.ConstIntValue;
import Compilador.Semantical.ConstStringValue;
import Compilador.Semantical.DualBoolExp;
import Compilador.Semantical.DualNumberExp;
import Compilador.Semantical.IfCommand;
import Compilador.Semantical.LoadCommand;
import Compilador.Semantical.MulOp;
import Compilador.Semantical.PrintCommand;
import Compilador.Semantical.RelOp;
import Compilador.Semantical.StringValue;
import Compilador.Semantical.Value;
import Compilador.Semantical.Variable;
import Compilador.Semantical.WhileCommand;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pedro
 */
public class SyntaticalAnalysis {
    private Lexeme current;
    private LexicalAnalysis lex;
    private SymbolTable st;

    public SyntaticalAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.current = lex.nextToken();
        this.st = new SymbolTable();
    }
    
    public Command init () throws IOException{
        Command c = this.program();
        this.eat(TokenType.END_OF_FILE);
        return c;
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
        System.out.println("Expected = " + type);
        if (type == this.current.type){
            this.current = lex.nextToken();
//            System.out.println(this.current);
        }
        else {
            this.showError();
        }
    }
    
    // program ::= start [decl-list] stmt-list exit
    private CommandBlock program() throws IOException{
        CommandBlock program = null;
        if(this.current.type == TokenType.START){
            this.eat(TokenType.START);
            // [decl-list]
            if(this.current.type == TokenType.INT || this.current.type == TokenType.FLOAT || this.current.type == TokenType.STRING){
                this.decl_list();
            }
            program = this.stmt_list();
            this.eat(TokenType.EXIT);
        } else {
            this.showError();
        }
        return program;
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
    private CommandBlock stmt_list() throws IOException{
        CommandBlock commandBlock = new CommandBlock();
        Command cmd = null;
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR
           || this.current.type == TokenType.IF || this.current.type == TokenType.DO || this.current.type == TokenType.SCAN || this.current.type == TokenType.PRINT){
            cmd = this.stmt();
            commandBlock.addCommand(cmd);
            while(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR
                || this.current.type == TokenType.IF || this.current.type == TokenType.DO || this.current.type == TokenType.SCAN || this.current.type == TokenType.PRINT){
                cmd = this.stmt();
                commandBlock.addCommand(cmd);
            }
        } else {
            this.showError();
        }
        return commandBlock;
    }
    
    //stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt ";"
    private Command stmt() throws IOException{
        Command cmd = null;
        switch(this.current.type){
            case VAR_FLT:
            case VAR_STR:
            case VAR_INT: cmd = this.assign_stmt(); this.eat(TokenType.DOT_COMMA); break;
            case IF: cmd = this.if_stmt(); break;
            case DO: cmd = this.while_stmt(); break;
            case SCAN: cmd = this.read_stmt(); this.eat(TokenType.DOT_COMMA); break;
            case PRINT: cmd = this.write_stmt(); this.eat(TokenType.DOT_COMMA); break;
            default: this.showError(); break;
        }
        return cmd;

    }
    
    //FIXME
    // assign-stmt ::= identifier "=" simple_expr
    private AssignCommand assign_stmt() throws IOException{
        AssignCommand as;
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR){
            Variable v;
            Value<?> vl;
            v = this.identifier();
            
            this.eat(TokenType.EQUAL);
            vl = this.simple_expr();
            as = new AssignCommand(vl, this.lex.line());
            as.addVariable(v);
        } else {
            this.showError();
        }
        return null;
    }
    
    // if-stmt ::= if condition then stmt-list end | if condition then stmt-list else stmt-list end
    private IfCommand if_stmt() throws IOException{
        IfCommand ic = null;
        Command c,c1;
        if(this.current.type == TokenType.IF){
            this.eat(TokenType.IF);
            Value<?> bv = this.condition();
            this.eat(TokenType.THEN);
            c = this.stmt_list();
            if(this.current.type == TokenType.ELSE){
                this.eat(TokenType.ELSE);
                c1 = this.stmt_list();
                ic = new IfCommand(bv, c, c1, this.lex.line());
                this.eat(TokenType.END);
                return ic;
            }
            this.eat(TokenType.END);
            ic = new IfCommand(bv, c, this.lex.line());
            return ic;
        } else {
            this.showError();
        }
        return ic;
    }
    
    // condition ::= expression
    private Value<?> condition() throws IOException{
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            return this.expression();
        } else {
            this.showError();
        }
        return null;
    }
    
    // expression ::= simple-expr | simple-expr relop simple-expr
    private Value<?> expression() throws IOException{
        Value<?> v1 = null;
        Value<?> v2 = null;
        RelOp rl = null;
        // FAZER MEIO OQ EU FIZ COM DUALEXP
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            v1 = this.simple_expr();
            if(this.current.type == TokenType.EQUAL_COMP || this.current.type == TokenType.HIGHER || this.current.type == TokenType.HIGHER_EQ ||
                this.current.type == TokenType.LOWER || this.current.type == TokenType.LOWER_EQ || this.current.type == TokenType.DIFF){
                rl = this.relop();
                v2 = this.simple_expr();
                CompareBoolValue cbv = new CompareBoolValue(rl, v1, v2, this.lex.line());
                return cbv;
            }
            return v1;
        } else {
            this.showError();
        }
        return null;
    }
    
    // while-stmt ::= do stmt-list stmt-sufix
    private Command while_stmt() throws IOException{
        Value<?> cond;
        CommandBlock cb;
        if(this.current.type == TokenType.DO) {
            this.eat(TokenType.DO);
            cb = this.stmt_list();
            cond = this.stmt_sufix();
            return new WhileCommand(cond, cb, this.lex.line());
        } else {
            this.showError();
        }
        return null;
    }
    
    // stmt-sufix ::= while condition end
    private Value<?> stmt_sufix() throws IOException{
        if(this.current.type == TokenType.WHILE) {
            this.eat(TokenType.WHILE);
            Value v =  this.condition();
            this.eat(TokenType.END);
            return v;
        } else {
            this.showError();
        }
        return null;
    }
    
    // read-stmt ::= scan "(" identifier ")"
    private Command read_stmt() throws IOException{
        Variable v = null;
        if(this.current.type == TokenType.SCAN) {
            this.eat(TokenType.SCAN);
            this.eat(TokenType.PAR_OPEN);
            v = this.identifier();
            this.eat(TokenType.PAR_CLOSE);
            LoadCommand ld = new LoadCommand(this.lex.line(), v);
            return ld;
        } else {
            this.showError();
        }
        return null;
    }
    
    // write-stmt ::= print "(" writable ")"
    private Command write_stmt() throws IOException{
        Value v = null;
        if(this.current.type == TokenType.PRINT) {
            this.eat(TokenType.PRINT);
            this.eat(TokenType.PAR_OPEN);
            v = this.writable();
            this.eat(TokenType.PAR_CLOSE);
            PrintCommand pc = new PrintCommand(v, this.lex.line());
            return pc;
        } else {
            this.showError();
        }
        return null;
    }
    
    // writable ::= simple-expr | literal
    private Value<?> writable() throws IOException{
        Value<?> v = null;
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            v = this.simple_expr();
        } else if(this.current.type == TokenType.LITERAL){
            ConstStringValue st = new ConstStringValue(this.current.token,this.lex.line());
            this.eat(TokenType.LITERAL);
            v = st;
        } else {
            this.showError();
        }
        return v;
    }
    
    // Acho que esta OK
    private Map <String, Variable> vars = new HashMap <String, Variable>();
    private Variable identifier() throws IOException{
        String n = this.current.token;
        Variable v;
        if (vars.containsKey(n))
            v = vars.get(n);
        else {
            if(this.current.type == TokenType.VAR_FLT){
                this.eat(TokenType.VAR_FLT);
                v = new Variable(this.current.token, "float");
            }else if(this.current.type == TokenType.VAR_INT){
                this.eat(TokenType.VAR_INT);
                v = new Variable(this.current.token, "int");
            } else {
                this.eat(TokenType.VAR_STR);
                v = new Variable(this.current.token, "string");
            }
            vars.put(n, v);
        }
        
        return v;
    }
    
    // simple_expr := term simple_expr_aux
    private Value<?> simple_expr() throws IOException{
        Value<?> v;
        Value<?> v2;
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            v = this.term();
            v2 = this.simple_expr_aux(v);
            if(v2 == null){
               return v;
            } else {
                return v2;
            }
        } else {
            this.showError();
        }
        return null;
    }
    
    // simple_expr_aux := addOp term simple_expr_aux | lambda
    private Value<?> simple_expr_aux(Value<?> meu_v) throws IOException{
        Value<?> v1 = null;
        Value<?> v2 = null;
        AddOp ao = null;
        if(this.current.type == TokenType.PLUS ||this.current.type == TokenType.MINUS || this.current.type == TokenType.OR){
            ao = this.addOp();
            v1 = this.term();
            v2 = this.simple_expr_aux(null);
            if(v2 == null){
                if(ao != AddOp.Or)
                    return new DualNumberExp(ao, meu_v, v1, this.lex.line());
                else 
                    return new DualBoolExp(ao, meu_v, v1, this.lex.line());
            }
            if(ao != AddOp.Or)
                return new DualNumberExp(ao, v1, v2, this.lex.line());
            else 
                return new DualBoolExp(ao, v1, v2, this.lex.line());
        }
        return null;
    }
    
    // term := factor_a term_aux
    private Value<?> term() throws IOException{
        Value<?> v;
        Value<?> v2;
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
            || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
            || this.current.type == TokenType.PAR_OPEN || this.current.type == TokenType.NOT || this.current.type == TokenType.MINUS ){
            v = this.factor_a();
            v2 = this.term_aux(v);
            if(v2 == null){
               return v;
            } else {
                return v2;
            }
        } else {
            this.showError();
        }
        return null;
    }
    
    // term_aux := mulOp factor_a termAux | lambda
    private Value<?> term_aux(Value<?> meu_v) throws IOException{
        Value<?> v1 = null;
        Value<?> v2 = null;
        MulOp ml = null;
        if(this.current.type == TokenType.MUL ||this.current.type == TokenType.DIV || this.current.type == TokenType.AND){
            ml = this.mulOp();
            v1 = this.factor_a();
            v2 = this.term_aux(null);
            if(v2 == null){
                if(ml != MulOp.And)
                    return new DualNumberExp(ml, meu_v, v1, this.lex.line());
                else 
                    return new DualBoolExp(ml, meu_v, v1, this.lex.line());
            }
            if(ml != MulOp.And)
                return new DualNumberExp(ml, v1, v2, this.lex.line());
            else 
                return new DualBoolExp(ml, v1, v2, this.lex.line());
        }
        return null;
    }
    
    // fator-a ::= factor | not factor | "-" factor
    private Value<?> factor_a() throws IOException{
        Value<?> v1 = null;
        if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR 
           || this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL
           || this.current.type == TokenType.PAR_OPEN ){
            v1 = this.factor();
        } else if(this.current.type == TokenType.NOT){
            // FIXME
            this.eat(TokenType.NOT);
            v1 = this.factor();
        } else if(this.current.type == TokenType.MINUS){
            // FIXME
            this.eat(TokenType.MINUS);
            v1 = this.factor();
        } else {
            this.showError();
        }
        return v1;
    }
    
    // Acho que ta OK
    // factor ::= identifier | constant | "(" expression ")"
    private Value<?> factor() throws IOException{
        Value<?> v = null;
        if(this.current.type == TokenType.PAR_OPEN){
            this.eat(TokenType.PAR_OPEN);
            v = this.expression();
            this.eat(TokenType.PAR_CLOSE);
        } else if(this.current.type == TokenType.VAR_FLT || this.current.type == TokenType.VAR_INT || this.current.type == TokenType.VAR_STR){
            v = this.identifier();
        } else if(this.current.type == TokenType.CONST_FLOAT || this.current.type == TokenType.CONST_INT || this.current.type == TokenType.LITERAL){
            v = this.constant();
        } else {
            this.showError();
        }
        return v;
    }
    
    // OK
    // addop ::= "+" | "-" | or
    private AddOp addOp() throws IOException{
        switch(this.current.type){
            case PLUS:
                this.eat(TokenType.PLUS);
                return AddOp.Plus;
            case MINUS:
                this.eat(TokenType.MINUS);
                return AddOp.Minus;
            case OR:
                this.eat(TokenType.OR);
                return AddOp.Or;
            default:
                this.showError();
                return null;
        }
    }
    
    //Ok
    private RelOp relop() throws IOException{
        switch(this.current.type){
            case HIGHER:
                this.eat(TokenType.HIGHER);
                return RelOp.GreaterThan;
            case HIGHER_EQ:
                this.eat(TokenType.HIGHER_EQ);
                return RelOp.GreaterEqual;
            case LOWER:
                this.eat(TokenType.LOWER);
                return RelOp.LowerThan;
            case LOWER_EQ:
                this.eat(TokenType.LOWER_EQ);
                return RelOp.LowerEqual;
            case DIFF:
                this.eat(TokenType.DIFF);
                return RelOp.NotEqual;
            case EQUAL_COMP:
                this.eat(TokenType.EQUAL_COMP);
                return RelOp.Equal;
            default:
                this.showError();
                return null;
        }
    }
    
    // Ok
    // mulop ::= "*" | "/" | and
    private MulOp mulOp() throws IOException{
        switch(this.current.type){
            case MUL:
                this.eat(TokenType.MUL);
                return MulOp.Mul;
            case DIV:
                this.eat(TokenType.DIV);
                return MulOp.Div;
            case AND:
                this.eat(TokenType.AND);
                return MulOp.And;
            default:
                this.showError();
                return null;
        }
    }
    
    // OK
    // constant ::= integer_const | float_const | literal
    private Value<?> constant() throws IOException{
        Value v = null;
        switch(this.current.type){
            case CONST_FLOAT: 
                ConstFloatValue cv;
                cv = new ConstFloatValue(Float.parseFloat(this.current.token), this.lex.line());
                this.eat(TokenType.CONST_FLOAT);
                return cv;
            case CONST_INT:
                ConstIntValue iv;
                iv = new ConstIntValue(Integer.parseInt(this.current.token), this.lex.line());
                this.eat(TokenType.CONST_INT);
                return iv;
            case LITERAL:
                ConstStringValue sv = new ConstStringValue(this.current.token, this.lex.line());
                this.eat(TokenType.LITERAL);
                return sv;
            default:
                this.showError();
                break;
        }
        return v;
    }
}

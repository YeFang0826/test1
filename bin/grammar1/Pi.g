grammar Pi;

options {
  language = Java;
}

@header {
  package grammar1;
  import java.util.*;
  import org.antlr.runtime.BitSet;
  import expression.*;
  import a_expression.*;
  import term.*;
  import a_term.*;
  import statement.*;
  import annotation.*;
  import parameter.*;
  import functionCall.*;
}
@lexer::header {
  package grammar1;
  import java.util.*;
  
  
}
@members {
}

program returns [defFunStatement program]: 
        mainFunction { $program = $mainFunction.mainFunction; }
        ;
mainFunction returns [defFunStatement mainFunction]:
        defFunStatement { $mainFunction = $defFunStatement.defFunStatement; }
        ;
statement returns [statement statement]: 
        defFunStatement { $statement = $defFunStatement.defFunStatement; }
        |
        { boolean hasAnnotation = false; annotation a = null;}
        (inContextAnnotation { hasAnnotation = true; a = $inContextAnnotation.inContextAnnotation;  })?
        ( defVarStatement 
          { 
            if(hasAnnotation)
              $defVarStatement.defVarStatement.a = a;
            $statement = $defVarStatement.defVarStatement; 
          }
          | assignStatement 
          { 
            if(hasAnnotation)
              $assignStatement.assignStatement.a = a;
            $statement = $assignStatement.assignStatement; 
          }
          | ifStatement 
          { 
            if(hasAnnotation)
              $ifStatement.ifStatement.a = a;
            $statement = $ifStatement.ifStatement; 
          }
        )
        | whileStatement
          { $statement = $whileStatement.whileStatement; }
        | forStatement 
          { $statement = $forStatement.forStatement; }
        | returnStatement 
          { $statement = $returnStatement.returnStatement; }
        ;
        
defFunStatement returns [defFunStatement defFunStatement]:
         { $defFunStatement = new defFunStatement(); }
         (preFunAnnotation { $defFunStatement.pre = $preFunAnnotation.preFunAnnotation; })?
         (postFunAnnotation { $defFunStatement.post = $postFunAnnotation.postFunAnnotation; } )?
         type ID
         '(' inputs 
         { $defFunStatement.name = $ID.text; 
           $defFunStatement.inputs = $inputs.inputs; 
           // return type is not added. needed for type checking
         }
         ')'
        '{'
          (statement { $defFunStatement.body.add($statement.statement); })+
        '}'
         
        ;
defVarStatement returns [defVarStatement defVarStatement]:
        { $defVarStatement = new defVarStatement(); }
        type ID { $defVarStatement.vtype = $type.type; $defVarStatement.name = $ID.text;  }
        ('=' expr { $defVarStatement.assignment = $expr.expr; })? 
        ';'
        ;
assignStatement returns [assignStatement assignStatement]:
        { $assignStatement = new assignStatement(); String name = ""; boolean isArrayElement = false; ArrayList<expression> index = null;}
        ID  { name = $ID.text; } 
        ('[' op1 = expr ']'
          { if(!isArrayElement){
              isArrayElement = true;
              index = new ArrayList<expression>();
             }
             index.add($op1.expr);
          }
        )* 
        { 
          if(isArrayElement){
            arrayElement lhs = new arrayElement(name, index);
            $assignStatement.lhs = lhs;
          }
          else
            $assignStatement.name = name;
       }
        '=' 
        op2 = expr { $assignStatement.assignment = $op2.expr; }
        ';'
        ;
whileStatement returns [whileStatement whileStatement]:
        { $whileStatement = new whileStatement(); ArrayList<statement> body = new ArrayList<statement>(); }
        WHILE (loopInvariant { $whileStatement.invariant = $loopInvariant.loopInvariant; })?
        '(' expr 
            { $whileStatement.guard = $expr.expr; }
        ')' 
        '{' 
          (statement { body.add($statement.statement); })+
        '}' 
        { $whileStatement.body = body; }
        ;
forStatement returns [forStatement forStatement]:
        { $forStatement = new forStatement(); }
        FOR (loopInvariant { $forStatement.invariant = $loopInvariant.loopInvariant; })?
        '(' 
          (INT)? op1=ID '=' op3=expr { $forStatement.iterator = $op1.text; $forStatement.begin = $op3.expr; }
          ';'
          op4=expr { $forStatement.guard = $op4.expr; } 
          ';' 
          op2=ID '=' op5=expr { $forStatement.increment = $op5.expr; }
        ')' 
        '{'
          (statement { $forStatement.body.add($statement.statement); })+
        '}'
        ;
ifStatement returns [ifStatement ifStatement]:
       { $ifStatement = new ifStatement(); }
       IF '(' op1 = expr { $ifStatement.conditions.add($op1.expr); }')' 
       '{' 
       { ArrayList<statement> temp = new ArrayList<statement>(); }
       (op3 = statement  { temp.add($op3.statement); })+ 
       { $ifStatement.exe.add(temp); }
       '}'
       
       (ELIF '(' op2=expr { $ifStatement.conditions.add($op2.expr); } ')' 
       '{' {  temp = new ArrayList<statement>(); }
       (op4=statement { temp.add($op4.statement); } )+
       { $ifStatement.exe.add(temp); }
       '}')*
       (ELSE '{' {  temp = new ArrayList<statement>(); }
       ( op5=statement  { temp.add($op5.statement); } )+ '}'
       { $ifStatement.exe.add(temp); }
       )?
       
       ;
       
returnStatement returns [returnStatement returnStatement]:
        RETURN expr ';'
        { $returnStatement = new returnStatement();
          $returnStatement.toreturn = $expr.expr;
        }
        ;


expr returns [expression expr]:   
        { expression temp= null; expression temp1 =null; boolean noleaf = false; boolean first = true; String operator = "";  }
      op1=negation 
      ( 
        (
        '&&' op2=negation { noleaf = true; operator = "&&";}
        | 
        '||' op2=negation { noleaf = true; operator = "||";}
        )
       {  if(first){
            temp = new expression();
            temp.l = $op1.negation;
            temp.r = $op2.negation;
            temp.op= operator;
            first = false;
          }
          else{
              temp1 = temp;
              temp = new expression();
              temp.l = temp1;
              temp.op = operator;
              temp.r = $op2.negation;
          }
      }
      )*
      { if(first && !noleaf){
          $expr = $op1.negation;
        } 
        else
          $expr = temp;
      }
      ;
    
negation returns [expression negation]: 
      { boolean noleaf = false; String operator = "";  }
      (op1 = relation 
      |
      '~' op2 = relation { noleaf = true; operator = "~";}
      )
      { if(!noleaf)
            $negation = $op1.relation;
          else{
            $negation = new expression();
            $negation.r = $op2.relation;
            $negation.op= operator;
          }
       }
      ;

relation returns [expression relation]:
      { boolean noleaf = false; String operator = "";  }
      op1=add 
      (
      '>' op2=add { noleaf = true; operator = ">";}
      |'<' op2=add { noleaf = true; operator = "<";}
      |'>=' op2=add { noleaf = true; operator = ">=";}
      |'<=' op2=add { noleaf = true; operator = "<=";}
      |'!=' op2=add { noleaf = true; operator = "!=";}
      |'==' op2=add { noleaf = true; operator = "==";}
      )?
      { if(!noleaf)
            $relation = $op1.add;
          else{
            $relation = new expression(); 
            $relation.l = $op1.add;
            $relation.r = $op2.add;
            $relation.op= operator;
          }
       }
      ;

add returns [expression add]:
     { expression temp= null; expression temp1 =null; boolean noleaf = false; boolean first = true; String operator = "";  }
      op1=mult 
      ( 
        (
        '+' op2=mult { noleaf = true; operator = "+";}
        | 
        '-' op2=mult { noleaf = true; operator = "-";}
        )
       {  if(first){
            temp = new expression();
            temp.l = $op1.mult;
            temp.r = $op2.mult;
            temp.op= operator;
            first = false;
          }
          else{
              temp1 = temp;
              temp = new expression();
              temp.l = temp1;
              temp.op = operator;
              temp.r = $op2.mult;
          }
      }
      )*
      { if(first && !noleaf){
          $add = $op1.mult;
        } 
        else
          $add = temp;
      }
      ;

mult returns [expression mult]:
      { expression temp = null; expression temp1 =null; boolean noleaf = false; boolean first = true; String operator = "";  }
      op1=unary 
      ( 
        (
        '*' op2=unary { noleaf = true; operator = "*";}
        | 
        '/' op2=unary { noleaf = true; operator = "/";}
        )
       {  if(first){
            temp = new expression(); 
            temp.l = $op1.unary;
            temp.r = $op2.unary;
            temp.op= operator;
            first = false;
          }
          else{
              temp1 = temp;
              temp = new expression();
              temp.l = temp1;
              temp.op = operator;
              temp.r = $op2.unary;
          }
      }
      )*
      { if(first && !noleaf){
            $mult = $op1.unary;
        } 
        else
          $mult = temp;
      }
      ;

unary returns [expression unary]: 
      { $unary = new expression(); }
      (
        op1 = term { $unary.leaf = $op1.term; }
        | 
        '-' op2 = term { expression temp = new expression(); temp.leaf = $op2.term; $unary.r = temp; $unary.op = "-"; }
      ) 
      ;
      
term returns [term term]:   
       NUMERIC { $term = new num($NUMERIC.text); }
      | TRUE { $term = new bool("true");}
      | FALSE { $term = new bool("false");}
      |'('expr')'{ $term = new expressionT($expr.expr); }
      | 
      ID  { String name = $ID.text; boolean isArrayElement=false; ArrayList<expression> index = null;} 
        ('[' op1 = expr ']'
          { if(!isArrayElement){
              isArrayElement = true;
              index = new ArrayList<expression>();
             }
             index.add($op1.expr);
          }
        )*
        { if(!isArrayElement)
            $term = new string(name);
          else{
            $term = new arrayElement(name, index);
          }
        } 
      | 
      LENGTH 
      '(' ID  
      { String name = $ID.text; boolean isArrayElement=false; ArrayList<expression> index = null;} 
        ('[' op1 = expr ']'
          { if(!isArrayElement){
              isArrayElement = true;
              index = new ArrayList<expression>();
             }
             index.add($op1.expr);
          }
        )*
      ')' 
       {  
         ArrayList<expression> inputs = new ArrayList<expression>();
        { if(!isArrayElement)
            inputs.add(new expression(new string(name)));
          else
            inputs.add(new expression(new arrayElement(name, index)));
        } 
        arrayOperation length = new arrayOperation("length");
        length.inputs = inputs;
        length.a_inputs = null;
        $term = new functionCallT(length);
      }
      | ID { ud_FunctionCall call = new ud_FunctionCall($ID.text); }
      '(' (op1 = expr { ArrayList<expression> inputs = new ArrayList<expression>(); inputs.add($op1.expr); }
       (',' op2 = expr { inputs.add($op2.expr); })* 
       { call.inputs = inputs; }
       )? ')'
       { $term = new functionCallT(call); }
      ;
inputs returns [ArrayList<parameter> inputs]: 
      { $inputs = new ArrayList<parameter>(); parameter toadd = null; }
      op1=type  op2=ID { toadd = new parameter($op1.type, $op1.dimension, $op2.text); $inputs.add(toadd); }
      (',' op3=type op4=ID {  toadd = new parameter($op3.type, $op3.dimension, $op4.text); $inputs.add(toadd); } )*
      ;
type returns [String type, int dimension]: 
      { int dimension = 0; }
      (
      INT { $type = "int"; } ('[' ']' { $type = "array"; dimension = dimension+1; })* 
      |
       DOUBLE { $type = "double"; } ('[' ']' { $type = "array"; dimension = dimension+1; } )*
      |
       BOOLEAN { $type = "boolean"; }
      |
       VOID { $type = "void"; }
      )
      { $dimension = dimension; }
      ;
      
preFunAnnotation returns [preFunAnnotation preFunAnnotation]:
          '@' PRE ':' a_Expr { $preFunAnnotation = new preFunAnnotation($a_Expr.a_Expr);}
          ;
postFunAnnotation returns [postFunAnnotation postFunAnnotation]:
          '@' POST ':' a_Expr { $postFunAnnotation = new postFunAnnotation($a_Expr.a_Expr);}
          ;
loopInvariant returns [loopInvariant loopInvariant]:
          '@' L ':' a_Expr { $loopInvariant = new loopInvariant($a_Expr.a_Expr);}
          ;
inContextAnnotation returns [inContextAnnotation inContextAnnotation]:
          '@' ASSERT 
          ':' a_Expr  { $inContextAnnotation = new inContextAnnotation($a_Expr.a_Expr);}
          ;
          
quantifier returns [quantifier quantifier]:
         (EXIST { $quantifier = new quantifier("exist"); }| FORALL { $quantifier = new quantifier("forall"); } ) 
         '(' op1=type  op2=ID { $quantifier.v.add(new parameter($op1.type, $op1.dimension, $op2.text)); }
         (',' op3=type  op4=ID { $quantifier.v.add(new parameter($op3.type, $op3.dimension, $op4.text)); } )* ')' ','
         ;
         
a_Expr returns [a_expression a_Expr]: 
       { ArrayList<quantifier> qs = new ArrayList<quantifier>(); }
       ( quantifier { qs.add($quantifier.quantifier); System.out.println("add quantifier"); })*
      { a_expression temp= null; a_expression temp1 =null; boolean noleaf = false; boolean first = true; String operator = "";  }
      op1=a_negation 
      ( 
        (
        AND op2=a_negation { noleaf = true; operator = "and";}
        | 
        OR op2=a_negation { noleaf = true; operator = "or";}
        | 
        IMPLIES op2=a_negation { noleaf = true; operator = "=>";}
        | 
        EQUIV op2=a_negation { noleaf = true; operator = "<=>";}
        )
       {  if(first){
            temp = new a_expression();
            temp.l = $op1.a_negation;
            temp.r = $op2.a_negation;
            temp.op= operator;
            first = false;
          }
          else{
              temp1 = temp;
              temp = new a_expression();
              temp.l = temp1;
              temp.op = operator;
              temp.r = $op2.a_negation;
          }
      }
      )*
      { if(first && !noleaf){
          $a_Expr = $op1.a_negation;
        } 
        else
          $a_Expr = temp;
        
        if(qs.size()>0)
          $a_Expr.q = qs;
      }
      ;
    
a_negation returns [a_expression a_negation]: 
      a_relation { $a_negation = $a_relation.a_relation; }
      |
      NOT a_relation 
      { $a_negation = new a_expression();
        $a_negation.op = "not";
        $a_negation.r = $a_relation.a_relation;
      }
      ;

a_relation returns [a_expression a_relation]:
      { boolean noleaf = false; String operator = "";  }
      op1=a_add 
      ('>' op2=a_add  { noleaf = true; operator = ">";  }
      |'<' op2=a_add  { noleaf = true; operator = "<";  }
      |'>=' op2=a_add  { noleaf = true; operator = ">=";  }
      |'<=' op2=a_add  { noleaf = true; operator = "<=";  }
      |'!=' op2=a_add  { noleaf = true; operator = "!=";  }
      |'==' op2=a_add { noleaf = true; operator = "==";  }
      )?
      { if(!noleaf){
            $a_relation = $op1.a_add;
            }
          else{
            $a_relation = new a_expression(); 
            $a_relation.l = $op1.a_add;
            $a_relation.r = $op2.a_add;
            $a_relation.op= operator;
          }
       }
      ;

a_add returns [a_expression a_add]:
     { a_expression temp= null; a_expression temp1 =null; boolean noleaf = false; boolean first = true; String operator = "";  }
      op1=a_mult 
      ( 
        (
        '+' op2=a_mult { noleaf = true; operator = "+";}
        | 
        '-' op2=a_mult { noleaf = true; operator = "-";}
        )
       {  if(first){
            temp = new a_expression();
            temp.l = $op1.a_mult;
            temp.r = $op2.a_mult;
            temp.op= operator;
            first = false;
          }
          else{
              temp1 = temp;
              temp = new a_expression();
              temp.l = temp1;
              temp.op = operator;
              temp.r = $op2.a_mult;
          }
      }
      )*
      { if(first && !noleaf){
          $a_add = $op1.a_mult;
        } 
        else
          $a_add = temp;
      }
      ;

a_mult returns [a_expression a_mult]:
      { a_expression temp= null; a_expression temp1 =null; boolean noleaf = false; boolean first = true; String operator = "";  }
      op1=a_unary 
      (
      '*' op2=a_unary { noleaf = true; operator = "*";}
      | 
      '/' op2=a_unary { noleaf = true; operator = "/";}
      {  if(first){
            temp = new a_expression();
            temp.l = $op1.a_unary;
            temp.r = $op2.a_unary;
            temp.op= operator;
            first = false;
          }
          else{
              temp1 = temp;
              temp = new a_expression();
              temp.l = temp1;
              temp.op = operator;
              temp.r = $op2.a_unary;
          }
      }
      )*
      { if(first && !noleaf){
          $a_mult = $op1.a_unary;
        } 
        else
          $a_mult = temp;
      }
      ;

a_unary returns[a_expression a_unary]: 
      a_term 
      { 
        $a_unary = new a_expression();
        $a_unary.leaf = $a_term.a_term;
      }
      |  
      '-' a_term 
      
      { a_expression temp = new a_expression();
        temp.leaf = $a_term.a_term;
        $a_unary = new a_expression();
        $a_unary.r = temp;
        $a_unary.op = "-";
      }
      
      ;
      
a_term returns [a_term a_term]:   
        NUMERIC { $a_term = new a_num($NUMERIC.text); }
      | 
      TRUE { $a_term = new a_bool("true");}
      | 
      FALSE { $a_term = new a_bool("false");}
      |
      '('a_Expr')'{ $a_term = new a_expressionT($a_Expr.a_Expr); }
      | 
      ID  
      { String name = $ID.text; boolean isArrayElement=false; ArrayList<a_expression> index = null;} 
        ('[' op1 = a_Expr ']'
          { if(!isArrayElement){
              isArrayElement = true;
              index = new ArrayList<a_expression>();
              System.out.println("has index");
             }
             index.add($op1.a_Expr);
          }
        )*
        { if(!isArrayElement)
            $a_term = new a_string(name);
          else
            $a_term = new a_arrayElement(name, index);
        }
      |  
      '|' ID  
      { ArrayList<a_expression> inputs = new ArrayList<a_expression>();  String name = $ID.text; boolean isArrayElement=false; ArrayList<a_expression> index = null;} 
        (
          '[' op1 = a_Expr ']'
          { if(!isArrayElement){
              isArrayElement = true;
              index = new ArrayList<a_expression>();
             }
             index.add($op1.a_Expr);
          }
        )*
      '|'
       { 
        { if(!isArrayElement)
            inputs.add(new a_expression(new a_string(name)));
          else
            inputs.add(new a_expression(new a_arrayElement(name, index)));
        } 
        arrayOperation length = new arrayOperation("length");
        length.a_inputs = inputs;
        length.inputs = null;
        $a_term = new a_functionCallT(length);
      }
      |
      { String method =""; } 
      ( SORTED   { method = "sorted"; } | PARTITIONED { method = "partitioned"; } )
      { ArrayList<a_expression> inputs = new ArrayList<a_expression>(); }
      '(' 
        ID  { String name = $ID.text; boolean isArrayElement=false; ArrayList<a_expression> index = null;} 
        ('[' op1 = a_Expr ']'
          { 
             if(!isArrayElement){
              isArrayElement = true;
              index = new ArrayList<a_expression>();
             }
             index.add($op1.a_Expr);
          }
        )*
        {
          if(!isArrayElement)
            inputs.add(new a_expression(new a_string(name)));
          else
            inputs.add(new a_expression(new a_arrayElement(name, index)));
        } 
        ',' op2 = a_Expr { inputs.add($op2.a_Expr); }  
        ',' op3 = a_Expr { inputs.add($op3.a_Expr); } 
        (',' op4 = a_Expr { inputs.add($op4.a_Expr); } ',' op5 = a_Expr { inputs.add($op5.a_Expr); } )?
      ')'
      { 
        arrayOperation sortedarray= new arrayOperation(method); 
        sortedarray.inputs = null;
        sortedarray.a_inputs = inputs;
        $a_term = new a_functionCallT(sortedarray);
      }
      ;
      
RETURN: 'return';       
INT: 'int';
DOUBLE: 'double';
VOID: 'void';
IF: 'if';
ELIF: 'elif';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
DIV: '/'; 
BOOLEAN: 'boolean';  
TRUE: 'true';
FALSE: 'false'; 
LENGTH: 'length';
SORTED: 'sorted';
PARTITIONED: 'partitioned';

PRE: 'pre';
POST: 'post';
ASSERT: 'assert';
L: 'L';
AND : 'and';
OR : 'or';
NOT: 'not';
IMPLIES: '=>';
EQUIV: '<=>';
ASSUME: 'assume';
EXIST: 'exist';
FORALL: 'forall';


MULTI_COMMENTS: '/*' .* '*/'  { $channel = HIDDEN; };     
COMMENTS: '//' .* ('\n'|'\r')  { $channel = HIDDEN; };
NUMERIC: ('0'..'9')+ (|('.'(('0'..'9')+))); 
ID    : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
WS : ( ' ' | '\t' | '\n' | '\r' | '\u000C')+  { $channel = HIDDEN; };

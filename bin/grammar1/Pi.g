grammar Pi;

options {
  language = Java;
}

@header {
  package grammar1;
  import java.util.*;
  import org.antlr.runtime.BitSet;
}
@lexer::header {
  package grammar1;
  import java.util.*;
}
@members {
}

rule: main
    ;
main: MAIN
    ;

MULTI_COMMENTS: '/*' .* '*/'  { $channel = HIDDEN; };     
COMMENTS: '//' .* ('\n'|'\r')  { $channel = HIDDEN; };
NUMERIC: ('0'..'9')+ (|('.'(('0'..'9')+))); 
ID    : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;
WS : ( ' ' | '\t' | '\n' | '\r' | '\u000C')+  { $channel = HIDDEN; };

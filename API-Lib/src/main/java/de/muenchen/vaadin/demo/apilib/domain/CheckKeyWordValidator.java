package de.muenchen.vaadin.demo.apilib.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by arne.schoentag on 07.01.16.
 * Validator for the NoKeyWord annotation.
 */
public class CheckKeyWordValidator implements ConstraintValidator<NoKeyWord,String> {

    /** Private NoKeyWord object */
    private NoKeyWord noKeyWord;

    /** Constant List of Java Keywords */
    private final List<String> JAVA_KEYWORDS = Arrays.asList(new String[]{"abstract","assert","boolean","break","byte","case","catch","char","class","const","continue","default","do","double","else",
            "enum","extends","false","final","finally","float","goto","for","if","implements","import","instanceof","int","interface","long","native","new","null","package","private","protected",
            "public","return","short","static","strictfp"});

    /** Constant List of Barrakuda Keywords */
    private final List<String> BARRAKUDA_KEYWORDS = Arrays.asList(new String[]{"allowedRegex","any","auditAll","auditChanges","auditNone","businessAction","customDateType","customListType",
            "customLogicType","customNumberType","customTextType","entity","extends","external","given","handleLikeAttribute","inTheFuture","inThePast","listOf","long","mainFeature","manyToMany",
            "manyToOne","maxLength=","maxValue=","minLength=","minValue=","normal","oneToMany","oneToOne","optional","package","password","pointNumber","purpose","searchable","serviceModel","then",
            "type=","version"});

    public void initialize(NoKeyWord noKeyWord){
        this.noKeyWord = noKeyWord;
    }

    public boolean isValid(String object, ConstraintValidatorContext context){
        if (object == null)
            return true;
        if (Stream.of(noKeyWord.of()).anyMatch(keyword -> keyword.equals(NoKeyWord.Keywords.JAVA))) {
            if(JAVA_KEYWORDS.contains(object))
                return false;
        }
        if (Stream.of(noKeyWord.of()).anyMatch(keyword -> keyword.equals(NoKeyWord.Keywords.BARRAKUDA))) {
            if(BARRAKUDA_KEYWORDS.contains(object))
                return false;
        }
        return true;
    }
}

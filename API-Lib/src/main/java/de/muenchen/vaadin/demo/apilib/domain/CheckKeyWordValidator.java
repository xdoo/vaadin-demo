package de.muenchen.vaadin.demo.apilib.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by arne.schoentag on 07.01.16.
 */
public class CheckKeyWordValidator implements ConstraintValidator<NoKeyWord,String> {

    private NoKeyWord noKeyWord;

    private final List<String> JAVA_KEYWORDS = Arrays.asList(new String[]{"abstract","assert","boolean","break","byte","case","catch","char","class","const","continue","default","do","double","else",
            "enum","extends","false","final","finally","float","goto","for","if","implements","import","instanceof","int","interface","long","native","new","null","package","private","protected",
            "public","return","short","static","strictfp"});

    public void initialize(NoKeyWord noKeyWord){
        this.noKeyWord = noKeyWord;
    }

    public boolean isValid(String object, ConstraintValidatorContext context){
        if (object == null)
            return true;
        if (Stream.of(noKeyWord.of()).anyMatch(new Predicate<NoKeyWord.Keywords>() {
            @Override
            public boolean test(NoKeyWord.Keywords keywords) {
                return keywords.equals(NoKeyWord.Keywords.JAVA);
            }
        })) {
            return JAVA_KEYWORDS.contains(object);
        }
        return true;
    }
}

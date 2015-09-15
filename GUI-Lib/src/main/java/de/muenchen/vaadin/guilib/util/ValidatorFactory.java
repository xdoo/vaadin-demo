package de.muenchen.vaadin.guilib.util;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.datefield.Resolution;

import java.util.Date;

/**
 * Created by arne.schoentag on 17.08.15.
 */
public class ValidatorFactory {

    public enum Type {STRING_LENGTH, DATE_RANGE, INTEGER_RANGE, NULL, REGEXP, DIAKRITISCH}

    /**
     * Factory method to create a vaadin validator using the given arguments.
     * @param type of which validator will be created.
     * @param args the arguments needed for the constructor.
     * @return new validator.
     */
    public static Validator getValidator(Type type,String... args){
        if(type == null)
            return null;
        switch(type){
            case STRING_LENGTH:
                if(args.length<4)
                       return null;
                return new StringLengthValidator(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),Boolean.parseBoolean(args[3]));
            case DATE_RANGE:
                if(args.length<3)
                    return null;
                return new DateRangeValidator(args[0],((args[1]!=null||!"".equals(args[1])) ? (args[1].equals("start"))? new Date(Long.MIN_VALUE):new Date(Integer.parseInt(args[1])) :new Date() ),((args[2]==null||"".equals(args[2])) ? new Date(): new Date(Integer.parseInt(args[2]))),Resolution.YEAR);
            case INTEGER_RANGE:
                if(args.length<3)
                    return null;
                return new IntegerRangeValidator(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]));
            case NULL:
                if(args.length<2)
                    return null;
                return new NullValidator(args[0],Boolean.parseBoolean(args[1]));
            case REGEXP:
                if(args.length<3)
                    return null;                
                return new RegexpValidator(args[2],Boolean.parseBoolean(args[1]),args[0]);
            case DIAKRITISCH:
                if(args.length<2)
                    return null;
                String abc = "";
                for(char c = 'a';c <= 'z'; c++)
                    abc+=c;
                for(char c = 'A';c <= 'Z'; c++)
                    abc+=c;
                for(int i =192;i<=382;i++)
                    abc+=Character.toString((char)i);
                for(int i =7682;i<=7807;i++)
                    abc+=Character.toString((char)i);
                abc+="-";
                return new RegexpValidator("["+abc+"]*",Boolean.parseBoolean(args[1]),args[0]);
            default:
                return null;
        }
    }
}

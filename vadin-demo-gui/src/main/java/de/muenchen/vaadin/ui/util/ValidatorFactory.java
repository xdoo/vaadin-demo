package de.muenchen.vaadin.ui.util;

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
    
    
    /**
     * Factory method to create a vaadin validator using the given arguments.
     * @param kind of which validator will be created.
     * @param args the arguments needed for the constructor.
     * @return new validator.
     */
    public static Validator getValidator(String kind,String... args){
        if(kind == null)
            return null;
        switch(kind){
            case "StringLength":
                if(args.length<4)
                       return null;
                return new StringLengthValidator(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),Boolean.parseBoolean(args[3]));
            case "DateRange"    :
                if(args.length<3)
                    return null;
                return new DateRangeValidator(args[0],((args[1]!=null||!"".equals(args[1])) ? (args[1].equals("start"))? new Date(Long.MIN_VALUE):new Date(Integer.parseInt(args[1])) :new Date() ),((args[2]==null||"".equals(args[2])) ? new Date(): new Date(Integer.parseInt(args[2]))),Resolution.YEAR);
            case "IntegerRange" :
                if(args.length<3)
                    return null;
                return new IntegerRangeValidator(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]));
            case "Null":
                if(args.length<2)
                    return null;
                return new NullValidator(args[0],Boolean.parseBoolean(args[1]));
            case "Regexp":
                if(args.length<3)
                    return null;                
                return new RegexpValidator(args[2],Boolean.parseBoolean(args[1]),args[0]);
            default:
                return null;
        }
    }
}
